package de.laurox.mc.shops;

import de.laurox.mc.VanillaShops;
import de.laurox.mc.files.FileManager;
import de.laurox.mc.shopsrewrite.BaseShop;
import de.laurox.mc.util.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Set;

public class RemoveListener implements Listener {

    private static HashMap<Player, Pair<BaseShop, Long>> removeMap = new HashMap<>();

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();

        if (!(damaged instanceof Villager))
            return;

        if (!(damager instanceof Player))
            return;

        Player player = (Player) damager;
        Villager villager = (Villager) damaged;

        Set<String> villagerKeys = VanillaShops.getShopsConfig().getKeys();
        if(!villagerKeys.contains(villager.getUniqueId().toString())) {
            return;
        }

        BaseShop baseShop = new BaseShop(villager);
        event.setCancelled(true);

        if (!player.isOp() && !player.hasPermission("vs.remove") && !baseShop.isOwner(player)) {
            player.sendMessage(FileManager.getMessage("Removing.missingPerms"));
            return;
        }

        if (player.getInventory().getItemInMainHand().getType().equals(Material.LAVA_BUCKET)) {

            if (removeMap.containsKey(player) && (System.currentTimeMillis() - removeMap.get(player).getV()) < 10000 && removeMap.get(player).getK().getVillager().getUniqueId().equals(baseShop.getVillager().getUniqueId())) {
                baseShop.remove();
                player.sendMessage(FileManager.getMessage("Removing.delete"));
            } else {
                long cM = System.currentTimeMillis();
                removeMap.put(player, new Pair<>(baseShop, cM));
                player.sendMessage("§8>> §cWARNING§8: §7This will clear all the remaining items in the Shop!");
                player.sendMessage("§8>> §7Hit the Shopkeeper once again within 10 seconds to proceed.");
            }


        }
    }

}
