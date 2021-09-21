package de.laurox.mc.shops;

import de.laurox.mc.VanillaShops;
import de.laurox.mc.files.FileManager;
import de.laurox.mc.shopsrewrite.ShopHandler;
import de.laurox.mc.util.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        String inventoryTitle = event.getView().getTitle();
        Config shops = VanillaShops.getShopsConfig();

        if (!ShopHandler.interactionMap.containsKey(player)) return;

        Villager villager = ShopHandler.interactionMap.get(player).getVillager();

        if (inventoryTitle.equalsIgnoreCase("§eStorage")) {
            shops.set(villager.getUniqueId().toString() + ".storage", event.getInventory().getContents());
        } else if (inventoryTitle.equalsIgnoreCase("§cConfig")) {
            validate(event.getInventory(), player);
            shops.set(villager.getUniqueId().toString() + ".config", event.getInventory().getContents());
        } else if (inventoryTitle.equalsIgnoreCase("§aPayment")) {
            shops.set(villager.getUniqueId().toString() + ".payment", event.getInventory().getContents());
        }

    }

    private static void validate(Inventory itemStacks, Player player) {
        if (FileManager.getBool("Trading.offers.show"))
            player.sendMessage(FileManager.getMessage("Trading.offers.header"));
        for (int i = 0; i < 9; i++) {
            ItemStack top = itemStacks.getItem(i);
            ItemStack bottom = itemStacks.getItem(i + 18);

            if ((top == null || top.getType().equals(Material.AIR)) && (bottom == null || bottom.getType().equals(Material.AIR))) {
                player.sendMessage(FileManager.getMessage("Trading.offers.invalid")
                        .replace("%num", "" + (i + 1))
                );
            } else if ((top == null || top.getType().equals(Material.AIR)) || (bottom == null || bottom.getType().equals(Material.AIR))) {
                player.sendMessage(FileManager.getMessage("Trading.offers.empty")
                        .replace("%num", "" + (i + 1))
                );
            } else if ((top != null || !top.getType().equals(Material.AIR)) && (bottom != null || !bottom.getType().equals(Material.AIR))) {
                player.sendMessage(FileManager.getMessage("Trading.offers.valid")
                        .replace("%num", "" + (i + 1))
                );
            }
        }
        if (FileManager.getBool("Trading.offers.show"))
            player.sendMessage(FileManager.getMessage("Trading.offers.footer"));
    }

}
