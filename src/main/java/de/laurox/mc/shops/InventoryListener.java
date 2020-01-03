package de.laurox.mc.shops;

import de.laurox.mc.VanillaShops;
import de.laurox.mc.files.FileManager;
import de.laurox.mc.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class InventoryListener implements Listener {

    private static HashMap<Player, Villager> statusMap = new HashMap<>();

    private static Inventory edit;
    private static Inventory origin;
    private static Inventory profession;

    public InventoryListener() {
        edit = Bukkit.createInventory(null, 9, "§6Edit");
        edit.setItem(4, createItem(Material.REDSTONE_BLOCK, "§cConfig"));
        edit.setItem(7, createItem(Material.CHEST, "§eStorage"));
        edit.setItem(8, createItem(Material.DIAMOND_BLOCK, "§aPayment"));
        edit.setItem(0, createItem(Material.GREEN_TERRACOTTA, "§aOrigin"));
        edit.setItem(1, createItem(Material.YELLOW_TERRACOTTA, "§eProfession"));

        origin = Bukkit.createInventory(null, 9, "§eOrigin");
        origin.setItem(0, createItem(Material.SAND, "§eDesert"));
        origin.setItem(1, createItem(Material.JUNGLE_LOG, "§aJungle"));
        origin.setItem(4, createItem(Material.GRASS_BLOCK, "§2Plains"));
        origin.setItem(2, createItem(Material.ACACIA_LOG, "§6Savanna"));
        origin.setItem(6, createItem(Material.SNOW_BLOCK, "§9Snow"));
        origin.setItem(7, createItem(Material.SLIME_BALL, "§aSwamp"));
        origin.setItem(8, createItem(Material.SPRUCE_LOG, "§7Taiga"));

        profession = Bukkit.createInventory(null, 9 * 2, "§eProfession");
        profession.setItem(1, createItem(Material.BARRIER, "§4None"));
        profession.setItem(2, createItem(Material.BEEF, "§cButcher"));
        profession.setItem(3, createItem(Material.COAL, "§8Armorer"));
        profession.setItem(4, createItem(Material.MAP, "§7Cartographer"));
        profession.setItem(5, createItem(Material.EXPERIENCE_BOTTLE, "§bCleric"));
        profession.setItem(6, createItem(Material.WHEAT, "§eFarmer"));
        profession.setItem(7, createItem(Material.COD, "§9Fisherman"));

        profession.setItem(10, createItem(Material.STRING, "§7Fletcher"));
        profession.setItem(11, createItem(Material.LEATHER, "§6Leatherworker"));
        profession.setItem(12, createItem(Material.BOOK, "§eLibrarian"));
        profession.setItem(13, createItem(Material.BRICK, "§7Mason"));
        profession.setItem(14, createItem(Material.WHITE_STAINED_GLASS, "§7Nitwit"));
        profession.setItem(15, createItem(Material.WHITE_WOOL, "§7Shepperd"));
        profession.setItem(16, createItem(Material.IRON_PICKAXE, "§8Smith"));
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (entity instanceof Villager) {
            Villager villager = (Villager) entity;
            if(villager.getCustomName() == null) return;
            if (villager.getCustomName().equalsIgnoreCase("§6Shopkeeper")) {
                if (statusMap.containsKey(player)) statusMap.replace(player, villager);
                else statusMap.put(player, (Villager) entity);
                event.setCancelled(true);
                if (isOwner(player, villager)) {
                    player.openInventory(edit);
                } else {
                    player.openInventory(generateOffers(buildInventory("config", villager)));
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String inventoryTitle = event.getView().getTitle();
        Inventory inventory = event.getInventory();

        if (!statusMap.containsKey(player)) return;
        Villager villager = statusMap.get(player);
        if (inventoryTitle.equalsIgnoreCase("§6Edit")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cConfig"))
                player.openInventory(buildInventory("config", statusMap.get(player)));
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eStorage"))
                player.openInventory(buildInventory("storage", statusMap.get(player)));
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aPayment"))
                player.openInventory(buildInventory("payment", statusMap.get(player)));
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aOrigin"))
                player.openInventory(origin);
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eProfession"))
                player.openInventory(profession);
        } else if (inventoryTitle.equalsIgnoreCase("§cConfig")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
                event.setCancelled(true);
                return;
            }
        } else if (inventoryTitle.equalsIgnoreCase("§eStorage")) {
            //todo
        } else if (inventoryTitle.equalsIgnoreCase("§aPayment")) {
            //todo
        } else if (inventoryTitle.equalsIgnoreCase("§eOrigin")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
            String clicked = event.getCurrentItem().getItemMeta().getDisplayName();

            switch (clicked) {
                case "§eDesert":
                    villager.setVillagerType(Villager.Type.DESERT);
                    return;
                case "§aJungle":
                    villager.setVillagerType(Villager.Type.JUNGLE);
                    return;
                case "§2Plains":
                    villager.setVillagerType(Villager.Type.PLAINS);
                    return;
                case "§6Savanna":
                    villager.setVillagerType(Villager.Type.SAVANNA);
                    return;
                case "§9Snow":
                    villager.setVillagerType(Villager.Type.SNOW);
                    return;
                case "§aSwamp":
                    villager.setVillagerType(Villager.Type.SWAMP);
                    return;
                case "§7Taiga":
                    villager.setVillagerType(Villager.Type.TAIGA);
                    return;
            }
        } else if (inventoryTitle.equalsIgnoreCase("§eProfession")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
            String clicked = event.getCurrentItem().getItemMeta().getDisplayName();

            switch (clicked) {
                case "§4None":
                    villager.setProfession(Villager.Profession.NONE);
                    return;
                case "§cButcher":
                    villager.setProfession(Villager.Profession.BUTCHER);
                    return;
                case "§8Armorer":
                    villager.setProfession(Villager.Profession.ARMORER);
                    return;
                case "§7Cartographer":
                    villager.setProfession(Villager.Profession.CARTOGRAPHER);
                    return;
                case "§bCleric":
                    villager.setProfession(Villager.Profession.CLERIC);
                    return;
                case "§eFarmer":
                    villager.setProfession(Villager.Profession.FARMER);
                    return;
                case "§9Fisherman":
                    villager.setProfession(Villager.Profession.FISHERMAN);
                    return;
                case "§7Fletcher":
                    villager.setProfession(Villager.Profession.FLETCHER);
                    return;
                case "§6Leatherworker":
                    villager.setProfession(Villager.Profession.LEATHERWORKER);
                    return;
                case "§eLibrarian":
                    villager.setProfession(Villager.Profession.LIBRARIAN);
                    return;
                case "§7Mason":
                    villager.setProfession(Villager.Profession.MASON);
                    return;
                case "§7Nitwit":
                    villager.setProfession(Villager.Profession.NITWIT);
                    return;
                case "§7Shepperd":
                    villager.setProfession(Villager.Profession.SHEPHERD);
                    return;
                case "§8Smith":
                    villager.setProfession(Villager.Profession.TOOLSMITH);
                    return;
            }
        } else if (inventoryTitle.equalsIgnoreCase("§eOffers")) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
            ItemStack itemStack = event.getCurrentItem();
            if (!itemStack.hasItemMeta()) return;
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (!itemMeta.hasDisplayName()) return;
            String displayName = itemMeta.getDisplayName();
            if (!displayName.startsWith("§aPurchase Offer #")) return;

            //check if Buyer inventory is full
            if (isFull(player.getInventory().getStorageContents())) {
                player.sendMessage(FileManager.getMessage("Trading.errors.invFull"));
                return;
            }

            //useful stuff
            String name = event.getCurrentItem().getItemMeta().getDisplayName();
            int current = Integer.parseInt(name.substring(name.length() - 1));
            ItemStack purchasedItem = inventory.getItem(current - 1);
            ItemStack price = inventory.getItem(current - 1 + 18);
            Inventory storage = buildInventory("storage", villager);

            //check if Shop is stocked
            if (!storage.containsAtLeast(purchasedItem, purchasedItem.getAmount())) {
                player.sendMessage(FileManager.getMessage("Trading.errors.notStocked")
                        .replace("%owner", getOwner(villager))
                );
                return;
            }

            //check if Payment is not full
            Inventory payment = buildInventory("payment", villager);
            if (isFull(payment)) {
                player.sendMessage(FileManager.getMessage("Trading.errors.fullCashbox")
                        .replace("%owner", getOwner(villager))
                );
                return;
            }

            //check if Player has Payment
            if (!player.getInventory().containsAtLeast(price, price.getAmount())) {
                player.sendMessage(FileManager.getMessage("Trading.errors.noItems"));
                return;
            }

            removeItems(storage, purchasedItem.getType(), purchasedItem.getAmount());
            payment.addItem(price);
            removeItems(player.getInventory(), price.getType(), price.getAmount());
            player.getInventory().addItem(purchasedItem);
            Config shops = VanillaShops.getShopsConfig();
            shops.set(villager.getUniqueId().toString() + ".storage", storage.getContents());
            shops.set(villager.getUniqueId().toString() + ".payment", payment.getContents());
            shops.reload();

            player.sendMessage(FileManager.getMessage("Trading.purchase")
                    .replace("%amount", purchasedItem.getAmount()+"")
                    .replace("%purchased", purchasedItem.getType().toString().toLowerCase())
                    .replace("%amount", price.getAmount()+"")
                    .replace("%price", price.getType().toString().toLowerCase())
            );
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        String inventoryTitle = event.getView().getTitle();
        Config shops = VanillaShops.getShopsConfig();

        if (!statusMap.containsKey(player)) return;

        Villager villager = statusMap.get(player);

        if (inventoryTitle.equalsIgnoreCase("§eStorage")) {
            shops.set(villager.getUniqueId().toString() + ".storage", event.getInventory().getContents());
        } else if (inventoryTitle.equalsIgnoreCase("§cConfig")) {
            validate(event.getInventory(), player);
            shops.set(villager.getUniqueId().toString() + ".config", event.getInventory().getContents());
        } else if (inventoryTitle.equalsIgnoreCase("§aPayment")) {
            shops.set(villager.getUniqueId().toString() + ".payment", event.getInventory().getContents());
        }

    }

    protected static boolean isOwner(Player player, Villager villager) {
        String owner = VanillaShops.getShopsConfig().get(villager.getUniqueId().toString() + ".owner");
        return owner.equalsIgnoreCase(player.getUniqueId().toString());
    }

    private ItemStack createItem(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private static Inventory buildInventory(String name, Villager villager) {
        ArrayList<ItemStack> itemStacks = (ArrayList) VanillaShops.getShopsConfig().get(villager.getUniqueId().toString() + "." + name);
        ItemStack[] array = itemStacks.toArray(new ItemStack[itemStacks.size()]);
        String title = VanillaShops.getShopsConfig().get(villager.getUniqueId().toString() + "." + name + "Title");
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

    private static void validate(Inventory itemStacks, Player player) {
        if(FileManager.getBool("Trading.offers.show"))
            player.sendMessage(FileManager.getMessage("Trading.offers.header"));
        for (int i = 0; i < 9; i++) {
            ItemStack top = itemStacks.getItem(i);
            ItemStack bottom = itemStacks.getItem(i + 18);

            if ((top == null || top.getType().equals(Material.AIR)) && (bottom == null || bottom.getType().equals(Material.AIR))) {
                player.sendMessage(FileManager.getMessage("Trading.offers.invalid")
                        .replace("%num", ""+(i+1))
                );
            } else if ((top == null || top.getType().equals(Material.AIR)) || (bottom == null || bottom.getType().equals(Material.AIR))) {
                player.sendMessage(FileManager.getMessage("Trading.offers.empty")
                        .replace("%num", ""+(i+1))
                );
            } else if ((top != null || !top.getType().equals(Material.AIR)) && (bottom != null || !bottom.getType().equals(Material.AIR))) {
                player.sendMessage(FileManager.getMessage("Trading.offers.valid")
                        .replace("%num", ""+(i+1))
                );
            }
        }
        if(FileManager.getBool("Trading.offers.show"))
            player.sendMessage(FileManager.getMessage("Trading.offers.footer"));
    }

    private Inventory generateOffers(Inventory config) {
        Inventory offers = Bukkit.createInventory(null, 9 * 3, "§eOffers");

        for (int i = 0; i < 9; i++) {
            ItemStack top = config.getItem(i);
            ItemStack bottom = config.getItem(i + 18);

            if ((top == null || top.getType().equals(Material.AIR)) && (bottom == null || bottom.getType().equals(Material.AIR))) {
                offers.setItem(i, new ItemStack(Material.AIR));
                offers.setItem(i + 9, createItem(Material.RED_STAINED_GLASS_PANE, "§cEmpty Offer"));
                offers.setItem(i + 18, new ItemStack(Material.AIR));
            } else if ((top == null || top.getType().equals(Material.AIR)) || (bottom == null || bottom.getType().equals(Material.AIR))) {
                offers.setItem(i, new ItemStack(Material.AIR));
                offers.setItem(i + 9, createItem(Material.RED_STAINED_GLASS_PANE, "§cEmpty Offer"));
                offers.setItem(i + 18, new ItemStack(Material.AIR));
            } else if ((top != null || !top.getType().equals(Material.AIR)) && (bottom != null || !bottom.getType().equals(Material.AIR))) {
                offers.setItem(i, top);
                offers.setItem(i + 9, createItem(Material.LIME_STAINED_GLASS_PANE, "§aPurchase Offer #" + (i + 1)));
                offers.setItem(i + 18, bottom);
            }
        }

        return offers;
    }

    /**
     * Checks if a given inventory is full
     *
     * @param inventory the given inventory
     * @return if its full
     */
    private static boolean isFull(Inventory inventory) {
        boolean isfull = true;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack current = inventory.getItem(i);
            if (current == null || current.getType().equals(Material.AIR)) {
                isfull = false;
                break;
            }
        }
        return isfull;
    }

    /**
     * Checks if an given ItemStackArray is full
     * @param itemStacks the given array
     * @return if its full
     */
    private static boolean isFull(ItemStack[] itemStacks) {
        boolean isfull = true;
        for (int i = 0; i < itemStacks.length; i++) {
            ItemStack current = itemStacks[i];
            if (current == null || current.getType().equals(Material.AIR)) {
                isfull = false;
                break;
            }
        }
        return isfull;
    }

    /**
    Credit to:
    https://bukkit.org/members/blablubbabc.64583/
    Src found:
    https://bukkit.org/threads/how-do-i-remove-a-specific-amount-of-items-from-inventory.312565/
     */
    public static void removeItems(Inventory inventory, Material type, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (type == is.getType()) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }

    /**
     * returns the owner of a given VillagerShop
     * @param villager the ShopKeeper
     * @return The name of a player in String format
     */
    private static String getOwner(Villager villager) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(VanillaShops.getShopsConfig().get(villager.getUniqueId().toString() + ".owner")));
        return player.getName();
    }

}
