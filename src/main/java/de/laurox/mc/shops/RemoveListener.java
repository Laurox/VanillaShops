package de.laurox.mc.shops;

import de.laurox.mc.VanillaShops;
import de.laurox.mc.files.FileManager;
import de.laurox.mc.shopsrewrite.BaseShop;
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

        if(!(damaged instanceof Villager))
            return;

        if (!(damager instanceof Player))
            return;

        Player player = (Player) damager;
        Villager villager = (Villager) damaged;

        // Shops must have the correct Custom Name!
        if (villager.getCustomName() == null || !villager.getCustomName().equalsIgnoreCase("§6Shopkeeper"))
            return;

        BaseShop baseShop = new BaseShop(villager);
        event.setCancelled(true);

        if (!player.isOp() && !player.hasPermission("vs.remove") && !baseShop.isOwner(player)) {
            player.sendMessage(FileManager.getMessage("Removing.missingPerms"));
            return;
        }

        if (player.getInventory().getItemInMainHand().getType().equals(Material.LAVA_BUCKET)) {
            VanillaShops.getShopsConfig().set(villager.getUniqueId().toString(), null);
            villager.remove();
            player.sendMessage(FileManager.getMessage("Removing.delete"));

        }
    }

}
