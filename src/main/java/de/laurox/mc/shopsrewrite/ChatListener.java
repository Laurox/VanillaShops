package de.laurox.mc.shopsrewrite;

import de.laurox.mc.MessageParser;
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
                    event.getPlayer().sendMessage(
                            MessageParser.send(
                                    "Renaming.success"
                            )
                    );
                } catch (Exception e) {
                    event.getPlayer().sendMessage(
                            MessageParser.send(
                                    "Renaming.error"
                            )
                    );
                }

            } else {
                event.getPlayer().sendMessage(
                        MessageParser.send(
                                "Renaming.timeout"
                        )
                );
            }
            InventoryHandler.getRenameMap().remove(event.getPlayer());
        }
    }
}
