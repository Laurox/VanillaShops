package de.laurox.mc.shops;

import de.laurox.mc.VanillaShops;
import de.laurox.mc.shopsrewrite.InventoryHandler;
import de.laurox.mc.shopsrewrite.ShopHandler;
import de.laurox.mc.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

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
            Integer taskID = InventoryHandler.getValidateMap().remove(player);

            if (taskID != null) {
                Bukkit.getScheduler().cancelTask(taskID);
            }

            shops.set(villager.getUniqueId().toString() + ".config", event.getInventory().getContents());
        } else if (inventoryTitle.equalsIgnoreCase("§aPayment")) {
            shops.set(villager.getUniqueId().toString() + ".payment", event.getInventory().getContents());
        }

    }
}
