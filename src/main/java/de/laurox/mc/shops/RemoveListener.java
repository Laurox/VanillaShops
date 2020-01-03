package de.laurox.mc.shops;

import de.laurox.mc.VanillaShops;
import de.laurox.mc.files.FileManager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class RemoveListener implements Listener {

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();

        if(damaged.getCustomName() == null) return;

        if (damaged.getCustomName().equalsIgnoreCase("§6Shopkeeper")) {
            event.setCancelled(true);
            if (!(damager instanceof Player)) return;
            Villager villager = (Villager) damaged;
            Player player = (Player) damager;

            if (!player.isOp() || !player.hasPermission("vs.remove") || !InventoryListener.isOwner(player, villager)) return;

            if (player.getInventory().getItemInMainHand().getType().equals(Material.LAVA_BUCKET)) {
                VanillaShops.getShopsConfig().set(villager.getUniqueId().toString(), null);
                villager.remove();
                player.sendMessage(FileManager.getMessage("Removing.delete"));
            }
        }
    }

}
