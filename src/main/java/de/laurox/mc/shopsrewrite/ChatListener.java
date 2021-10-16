package de.laurox.mc.shopsrewrite;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {

        if (InventoryHandler.getRenameMap().containsKey(event.getPlayer())) {
            event.setCancelled(true);
            if (System.currentTimeMillis() - InventoryHandler.getRenameMap().get(event.getPlayer()).getV() < 30000) {
                try {
                    InventoryHandler.getRenameMap().get(event.getPlayer()).getK().getVillager().setCustomName(event.getMessage().replace("&", "§"));
                    event.getPlayer().sendMessage("§8>> §aSuccessfully change name");
                } catch (Exception e) {
                    event.getPlayer().sendMessage("§8>> §7There was an unexpected error! Could it be that your name was too long?");
                }

            } else {
                event.getPlayer().sendMessage("§8>> §cYour rename request timed out! Be faster next time.");
            }
            InventoryHandler.getRenameMap().remove(event.getPlayer());
        }
    }
}
