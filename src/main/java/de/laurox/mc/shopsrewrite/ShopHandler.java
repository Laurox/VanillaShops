package de.laurox.mc.shopsrewrite;

import de.laurox.mc.VanillaShops;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.HashMap;
import java.util.Set;

public class ShopHandler implements Listener {

    public static HashMap<Player, BaseShop> interactionMap = new HashMap<>();

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity clickedEntity = event.getRightClicked();

        // Only Villagers can be Shops
        if (!(clickedEntity instanceof Villager))
            return;

        Villager villager = (Villager) clickedEntity;

        Set<String> villagerKeys = VanillaShops.getShopsConfig().getKeys();
        if (!villagerKeys.contains(villager.getUniqueId().toString())) {
            return;
        }

        BaseShop baseShop = new BaseShop(villager);
        interactionMap.put(player, baseShop);

        event.setCancelled(true);

        if (baseShop.isOwner(player)) {
            player.openInventory(BaseShop.getEditInventory());
        } else {
            player.openInventory(baseShop.offers());
        }
    }

}