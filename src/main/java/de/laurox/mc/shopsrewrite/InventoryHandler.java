package de.laurox.mc.shopsrewrite;

import de.laurox.mc.VanillaShops;
import de.laurox.mc.files.FileManager;
import de.laurox.mc.util.Config;
import de.laurox.mc.util.InventoryUtil;
import de.laurox.mc.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryHandler implements Listener {

    private static final HashMap<Player, Integer> validateMap = new HashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String inventoryTitle = event.getView().getTitle();
        Inventory inventory = event.getInventory();

        if (!ShopHandler.interactionMap.containsKey(player))
            return;

        BaseShop baseShop = ShopHandler.interactionMap.get(player);

        ItemStack current = event.getCurrentItem();

        if (inventoryTitle.equalsIgnoreCase("§6Edit")) {
            event.setCancelled(true);

            if (current == null || current.getType().equals(Material.AIR))
                return;

            // Can not be null because all items are custom created in that inventory!
            String displayName = current.getItemMeta().getDisplayName();

            switch (displayName) {
                case "§cConfig":
                    player.openInventory(baseShop.config());
                    return;
                case "§eStorage":
                    player.openInventory(baseShop.storage());
                    return;
                case "§aPayment":
                    player.openInventory(baseShop.payment());
                    return;
                case "§aOrigin":
                    player.openInventory(BaseShop.getOriginInventory());
                    return;
                case "§eProfession":
                    player.openInventory(BaseShop.getProfessionInventory());
                    return;
            }
        }

        if (inventoryTitle.equalsIgnoreCase("§cConfig")) {
            InventoryAction action = event.getAction();
            int rawSlot = event.getRawSlot();

            if (event.getHotbarButton() != -1) {
                event.setCancelled(true);
                return;
            }

            if ((rawSlot > -1 && rawSlot < 9) || (rawSlot > 17 && rawSlot < 27)) {
                if ((action.equals(InventoryAction.PLACE_ALL) || action.equals(InventoryAction.PLACE_ONE) || action.equals(InventoryAction.PLACE_SOME)) || (action.equals(InventoryAction.PICKUP_ALL) || action.equals(InventoryAction.PICKUP_HALF) || action.equals(InventoryAction.PICKUP_ONE) || action.equals(InventoryAction.PICKUP_SOME))) {
                    if(!validateMap.containsKey(player)) {
                        int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(VanillaShops.getPlugin(), () -> validateAll(event.getInventory()), 2, 2);
                        validateMap.put(player, taskID);
                    }
                }
            }

            if (rawSlot > 8 && rawSlot < 18) {
                event.setCancelled(true);
                return;
            }

        }

        if (inventoryTitle.equalsIgnoreCase("§eOrigin")) {
            event.setCancelled(true);

            if (current == null || current.getType().equals(Material.AIR))
                return;

            String displayName = current.getItemMeta().getDisplayName();

            switch (displayName) {
                case "§eDesert":
                    baseShop.getVillager().setVillagerType(Villager.Type.DESERT);
                    return;
                case "§aJungle":
                    baseShop.getVillager().setVillagerType(Villager.Type.JUNGLE);
                    return;
                case "§2Plains":
                    baseShop.getVillager().setVillagerType(Villager.Type.PLAINS);
                    return;
                case "§6Savanna":
                    baseShop.getVillager().setVillagerType(Villager.Type.SAVANNA);
                    return;
                case "§9Snow":
                    baseShop.getVillager().setVillagerType(Villager.Type.SNOW);
                    return;
                case "§aSwamp":
                    baseShop.getVillager().setVillagerType(Villager.Type.SWAMP);
                    return;
                case "§7Taiga":
                    baseShop.getVillager().setVillagerType(Villager.Type.TAIGA);
                    return;
            }
        }

        if (inventoryTitle.equalsIgnoreCase("§eProfession")) {
            event.setCancelled(true);

            if (current == null || current.getType().equals(Material.AIR))
                return;

            String displayName = current.getItemMeta().getDisplayName();

            switch (displayName) {
                case "§4None":
                    baseShop.getVillager().setProfession(Villager.Profession.NONE);
                    return;
                case "§cButcher":
                    baseShop.getVillager().setProfession(Villager.Profession.BUTCHER);
                    return;
                case "§8Armorer":
                    baseShop.getVillager().setProfession(Villager.Profession.ARMORER);
                    return;
                case "§7Cartographer":
                    baseShop.getVillager().setProfession(Villager.Profession.CARTOGRAPHER);
                    return;
                case "§bCleric":
                    baseShop.getVillager().setProfession(Villager.Profession.CLERIC);
                    return;
                case "§eFarmer":
                    baseShop.getVillager().setProfession(Villager.Profession.FARMER);
                    return;
                case "§9Fisherman":
                    baseShop.getVillager().setProfession(Villager.Profession.FISHERMAN);
                    return;
                case "§7Fletcher":
                    baseShop.getVillager().setProfession(Villager.Profession.FLETCHER);
                    return;
                case "§6Leatherworker":
                    baseShop.getVillager().setProfession(Villager.Profession.LEATHERWORKER);
                    return;
                case "§eLibrarian":
                    baseShop.getVillager().setProfession(Villager.Profession.LIBRARIAN);
                    return;
                case "§7Mason":
                    baseShop.getVillager().setProfession(Villager.Profession.MASON);
                    return;
                case "§7Nitwit":
                    baseShop.getVillager().setProfession(Villager.Profession.NITWIT);
                    return;
                case "§7Shepperd":
                    baseShop.getVillager().setProfession(Villager.Profession.SHEPHERD);
                    return;
                case "§8Smith":
                    baseShop.getVillager().setProfession(Villager.Profession.TOOLSMITH);
                    return;
            }
        }

        if (inventoryTitle.equalsIgnoreCase("§eOffers")) {
            event.setCancelled(true);

            if (current == null || current.getType().equals(Material.AIR))
                return;

            // check for the correct button
            if (!current.hasItemMeta() || !current.getItemMeta().hasDisplayName() || !current.getItemMeta().getDisplayName().startsWith("§aPurchase Offer #"))
                return;

            // Rule 1: Buyers Inventory is full
            if (isFull(player.getInventory().getStorageContents())) {
                player.sendMessage(FileManager.getMessage("Trading.errors.invFull"));
                return;
            }

            // helpful variables
            String itemName = current.getItemMeta().getDisplayName();

            int currentOffer = Integer.parseInt(itemName.substring(itemName.length() - 1));
            ItemStack purchasedItem = inventory.getItem(currentOffer - 1);
            ItemStack priceItem = inventory.getItem(currentOffer - 1 + 18);

            Inventory storageInventory = baseShop.storage();
            Inventory paymentInventory = baseShop.payment();

            // Rule 2: Shop must be stocked
            if (!storageInventory.containsAtLeast(purchasedItem, purchasedItem.getAmount())) {
                player.sendMessage(FileManager.getMessage("Trading.errors.notStocked")
                        .replace("%owner", baseShop.getOwner())
                );
                return;
            }

            // Rule 3: Payment Inventory have enough space
            if (isFull(paymentInventory)) {
                player.sendMessage(FileManager.getMessage("Trading.errors.fullCashbox")
                        .replace("%owner", baseShop.getOwner())
                );
                return;
            }

            // Rule 4: Player must have enough cash to pay
            if (!player.getInventory().containsAtLeast(priceItem, priceItem.getAmount())) {
                player.sendMessage(FileManager.getMessage("Trading.errors.noItems"));
                return;
            }

            // Perform Trade!
            removeItems(storageInventory, purchasedItem.getType(), purchasedItem.getAmount());
            paymentInventory.addItem(priceItem.clone());

            Config shops = VanillaShops.getShopsConfig();
            shops.set(baseShop.getVillager().getUniqueId().toString() + ".storage", storageInventory.getContents());
            shops.set(baseShop.getVillager().getUniqueId().toString() + ".payment", paymentInventory.getContents());
            shops.reload();

            removeItems(player.getInventory(), priceItem.getType(), priceItem.getAmount());
            player.getInventory().addItem(purchasedItem.clone());

            player.sendMessage(FileManager.getMessage("Trading.purchase")
                    .replaceFirst("%amount", purchasedItem.getAmount() + "")
                    .replace("%purchased", purchasedItem.getType().toString().toLowerCase())
                    .replaceFirst("%amount", priceItem.getAmount() + "")
                    .replace("%price", priceItem.getType().toString().toLowerCase())
            );
        }
    }

    // INVENTORY UTILS -> NEW CLASS LATER // TODO
    public static void validateAll(Inventory inventory) {
        for (int i = 0; i < 9; i++) {
            Pair<Integer, ItemStack> result = validateOffer(inventory, i);
            inventory.setItem(result.getK(), result.getV());
        }
    }

    public static Pair<Integer, ItemStack> validateOffer(Inventory inventory, int rawSlot) {
        int direction = 1;
        int offer = (rawSlot % 9) + 1;
        if (rawSlot > 17) {
            direction = -1;
        }

        boolean emptyTop = inventory.getItem(rawSlot) == null || inventory.getItem(rawSlot).getType().equals(Material.AIR);
        boolean emptyBottom = inventory.getItem(rawSlot + (18 * direction)) == null || inventory.getItem(rawSlot + (18 * direction)).getType().equals(Material.AIR);

        if (emptyBottom && emptyTop) {
            return new Pair<>(rawSlot + (9 * direction), InventoryUtil.createItem(Material.GRAY_STAINED_GLASS_PANE, "§eEmpty | Offer #" + offer));
        } else if (!emptyBottom && !emptyTop) {
            return new Pair<>(rawSlot + (9 * direction), InventoryUtil.createItem(Material.LIME_STAINED_GLASS_PANE, "§aValid | Offer #" + offer));
        } else {
            return new Pair<>(rawSlot + (9 * direction), InventoryUtil.createItem(Material.RED_STAINED_GLASS_PANE, "§cInvalid | Offer #" + offer));
        }
    }

    /**
     * Checks if a given inventory is full
     *
     * @param inventory the given inventory
     * @return if its full
     */
    private static boolean isFull(Inventory inventory) {
        return isFull(inventory.getStorageContents());
    }

    private static boolean isFull(ItemStack[] items) {
        for (ItemStack current : items) {
            if (current == null || current.getType().equals(Material.AIR)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Removes the items of type from an inventory.
     *
     * @param inventory Inventory to modify
     * @param type      The type of Material to remove
     * @param amount    The amount to remove, or Integer.MAX_VALUE to remove all
     * @return The amount of items that could not be removed, 0 for success, or -1 for failures
     */
    public static int removeItems(Inventory inventory, Material type, int amount) {

        if (type == null || inventory == null)
            return -1;
        if (amount <= 0)
            return -1;

        if (amount == Integer.MAX_VALUE) {
            inventory.remove(type);
            return 0;
        }

        HashMap<Integer, ItemStack> retVal = inventory.removeItem(new ItemStack(type, amount));

        int notRemoved = 0;
        for (ItemStack item : retVal.values()) {
            notRemoved += item.getAmount();
        }
        return notRemoved;
    }

    public static HashMap<Player, Integer> getValidateMap() {
        return validateMap;
    }
}
