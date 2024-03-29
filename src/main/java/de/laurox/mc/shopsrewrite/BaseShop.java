package de.laurox.mc.shopsrewrite;

import de.laurox.mc.MessageParser;
import de.laurox.mc.VanillaShops;
import de.laurox.mc.files.FileManager;
import de.laurox.mc.files.ShopConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.UUID;

import static de.laurox.mc.util.InventoryUtil.createItem;

public class BaseShop {

    private static final Inventory editInventory;
    private static final Inventory originInventory;
    private static final Inventory professionInventory;

    static {
        editInventory = Bukkit.createInventory(null, 9, "§6Edit");
        editInventory.setItem(4, createItem(Material.REDSTONE_BLOCK, "§cConfig", new String[]{"", "§7Here you can configure", "§7the trades your shop offers!", "", "§eClick to configure!"}));
        editInventory.setItem(7, createItem(Material.CHEST, "§eStorage", new String[]{"", "§7Store the items you want", "§7to sell here!", "", "§eClick to open!"}));
        editInventory.setItem(8, createItem(Material.DIAMOND_BLOCK, "§aPayment", new String[]{"", "§7Your payment will be", "§7collected here!", "", "§eClick to open!"}));
        editInventory.setItem(0, createItem(Material.GREEN_TERRACOTTA, "§aOrigin", new String[]{"", "§7Lets you can change the", "§7origin of the villager!", "", "§eClick to configure!"}));
        editInventory.setItem(1, createItem(Material.YELLOW_TERRACOTTA, "§eProfession", new String[]{"", "§7Lets you can change the", "§7profession of the villager!", "", "§eClick to configure!"}));
        editInventory.setItem(2, createItem(Material.ANVIL, "§bRename", new String[]{"", "§7Lets you change the", "§7name of the villager!", "", "§eClick to rename!"}));

        originInventory = Bukkit.createInventory(null, 9, "§eOrigin");
        originInventory.setItem(0, createItem(Material.SAND, "§eDesert"));
        originInventory.setItem(1, createItem(Material.JUNGLE_LOG, "§aJungle"));
        originInventory.setItem(4, createItem(Material.GRASS_BLOCK, "§2Plains"));
        originInventory.setItem(2, createItem(Material.ACACIA_LOG, "§6Savanna"));
        originInventory.setItem(6, createItem(Material.SNOW_BLOCK, "§9Snow"));
        originInventory.setItem(7, createItem(Material.SLIME_BALL, "§aSwamp"));
        originInventory.setItem(8, createItem(Material.SPRUCE_LOG, "§7Taiga"));

        professionInventory = Bukkit.createInventory(null, 9 * 2, "§eProfession");
        professionInventory.setItem(1, createItem(Material.BARRIER, "§4None"));
        professionInventory.setItem(2, createItem(Material.BEEF, "§cButcher"));
        professionInventory.setItem(3, createItem(Material.COAL, "§8Armorer"));
        professionInventory.setItem(4, createItem(Material.MAP, "§7Cartographer"));
        professionInventory.setItem(5, createItem(Material.EXPERIENCE_BOTTLE, "§bCleric"));
        professionInventory.setItem(6, createItem(Material.WHEAT, "§eFarmer"));
        professionInventory.setItem(7, createItem(Material.COD, "§9Fisherman"));

        professionInventory.setItem(10, createItem(Material.STRING, "§7Fletcher"));
        professionInventory.setItem(11, createItem(Material.LEATHER, "§6Leatherworker"));
        professionInventory.setItem(12, createItem(Material.BOOK, "§eLibrarian"));
        professionInventory.setItem(13, createItem(Material.BRICK, "§7Mason"));
        professionInventory.setItem(14, createItem(Material.WHITE_STAINED_GLASS, "§7Nitwit"));
        professionInventory.setItem(15, createItem(Material.WHITE_WOOL, "§7Shepperd"));
        professionInventory.setItem(16, createItem(Material.IRON_PICKAXE, "§8Smith"));
    }

    private static Inventory buildInventory(String name, Villager villager) {
        ArrayList<ItemStack> itemStacks = VanillaShops.getShopsConfig().get(villager.getUniqueId() + "." + name);
        ItemStack[] array = itemStacks.toArray(new ItemStack[itemStacks.size()]);
        String title = VanillaShops.getShopsConfig().get(villager.getUniqueId() + "." + name + "Title");
        Inventory inventory = Bukkit.createInventory(villager, 9 * 3, title);
        int count = 0;
        for (ItemStack itemStack : array) {
            if (itemStack == null || itemStack.getType() == Material.AIR)
                inventory.addItem(new ItemStack(Material.AIR));
            else inventory.setItem(count, itemStack);

            count++;
        }
        return inventory;
    }

    private final Villager villager;
    private final String ownerUUID;
    private final String ownerName;

    public BaseShop(Villager villager) {
        this.villager = villager;

        this.ownerUUID = VanillaShops.getShopsConfig().get(villager.getUniqueId() + ".owner");
        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(ownerUUID));
        this.ownerName = player.getName();
    }

    public boolean isOwner(Player player) {
        return ownerUUID.equalsIgnoreCase(player.getUniqueId().toString());
    }

    public Inventory offers() {
        Inventory offers = Bukkit.createInventory(null, 9 * 3, "§eOffers");
        Inventory config = buildInventory("config", this.villager);

        for (int i = 0; i < 9; i++) {
            ItemStack topItem = config.getItem(i);
            ItemStack bottomItem = config.getItem(i + 18);

            if ((topItem == null || topItem.getType().equals(Material.AIR)) || (bottomItem == null || bottomItem.getType().equals(Material.AIR))) {
                offers.setItem(i, new ItemStack(Material.AIR));
                offers.setItem(i + 9, createItem(Material.RED_STAINED_GLASS_PANE, "§cEmpty Offer"));
                offers.setItem(i + 18, new ItemStack(Material.AIR));
            } else if (!topItem.getType().equals(Material.AIR) && !bottomItem.getType().equals(Material.AIR)) {
                offers.setItem(i, topItem);
                offers.setItem(i + 9, createItem(Material.LIME_STAINED_GLASS_PANE, "§aPurchase Offer #" + (i + 1)));
                offers.setItem(i + 18, bottomItem);
            } else {
                // SHOULD NEVER EVER HAPPEN
                offers.setItem(i, new ItemStack(Material.AIR));
                offers.setItem(i + 9, createItem(Material.RED_STAINED_GLASS_PANE, "§cERROR"));
                offers.setItem(i + 18, new ItemStack(Material.AIR));
            }
        }

        return offers;
    }

    public Inventory config() {
        return buildInventory("config", this.villager);
    }

    public Inventory storage() {
        return buildInventory("storage", this.villager);
    }

    public Inventory payment() {
        return buildInventory("payment", this.villager);
    }

    public boolean remove() {
        String armorStandID = VanillaShops.getShopsConfig().get(villager.getUniqueId() + ".armorStand");

        if (armorStandID != null) {
            ArmorStand as = (ArmorStand) VanillaShops.getPlugin().getServer().getEntity(UUID.fromString(armorStandID));

            if (as != null) {
                as.remove();
            }
        }

        VanillaShops.getShopsConfig().set(villager.getUniqueId().toString(), null);
        villager.remove();
        return true;
    }

    // getters

    public Villager getVillager() {
        return villager;
    }

    public String getOwnerName() {
        return ownerName;
    }

    // static getters

    public static Inventory getEditInventory() {
        return editInventory;
    }

    public static Inventory getOriginInventory() {
        return originInventory;
    }

    public static Inventory getProfessionInventory() {
        return professionInventory;
    }

    // static methods

    public static boolean spawn(Location location, Player player) {
        int cap = FileManager.getConfig().getInt("limit");

        if ((cap > 0 && countShops(player) >= cap) && (!player.isOp() && !player.hasPermission("vs.limit"))) {
            player.sendMessage(
                    MessageParser.send(
                            "Commands.limit"
                    )
            );
            return false;
        }

        Villager villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        villager.setAdult();
        villager.setBreed(false);
        villager.setAgeLock(true);
        villager.setCanPickupItems(false);
        villager.setCustomNameVisible(true);
        villager.setCustomName("§6Shopkeeper");
        villager.setInvulnerable(true);
        villager.setCollidable(false);
        villager.setVillagerExperience(1);
        villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255), true);

        ShopConfig shops = VanillaShops.getShopsConfig();

        shops.set(villager.getUniqueId() + ".owner", player.getUniqueId().toString());

        if (FileManager.getConfig().getBoolean("shopPrefixActive")) {
            ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location.add(0, 0.225, 0), EntityType.ARMOR_STAND);
            as.setInvisible(true);
            as.setCollidable(false);
            as.setInvulnerable(true);
            as.setCustomNameVisible(true);
            as.setGravity(false);

            String prefix = FileManager.getConfig().getString("shopPrefix");
            as.setCustomName(prefix);

            shops.set(villager.getUniqueId() + ".armorStand", as.getUniqueId().toString());
        }

        Inventory config = Bukkit.createInventory(villager, 9 * 3, "§cConfig");
        Inventory storage = Bukkit.createInventory(villager, 9 * 3, "§eStorage");
        Inventory payment = Bukkit.createInventory(villager, 9 * 3, "§aPayment");

        shops.set(villager.getUniqueId() + ".configTitle", "§cConfig");
        shops.set(villager.getUniqueId() + ".storageTitle", "§eStorage");
        shops.set(villager.getUniqueId() + ".paymentTitle", "§aPayment");
        for (int i = 9; i < 18; i++) {
            config.setItem(i, createItem(Material.GRAY_STAINED_GLASS_PANE, "§eEmpty | Offer #" + (i - 8)));
        }
        shops.set(villager.getUniqueId() + ".config", config.getContents());
        shops.set(villager.getUniqueId() + ".storage", storage.getContents());
        shops.set(villager.getUniqueId() + ".payment", payment.getContents());

        shops.reload();
        return true;
    }

    public static int countShops(Player player) {
        int shopCount = 0;

        for (String key : VanillaShops.getShopsConfig().getKeys()) {
            String owner = VanillaShops.getShopsConfig().get(key + ".owner");
            if (owner.equalsIgnoreCase(player.getUniqueId().toString())) {
                shopCount++;
            }
        }

        return shopCount;
    }

}
