package de.laurox.mc.commands;

import com.google.common.collect.ImmutableMap;
import de.laurox.mc.MessageParser;
import de.laurox.mc.VanillaShops;
import de.laurox.mc.files.FileManager;
import de.laurox.mc.shopsrewrite.BaseShop;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                int cap = FileManager.getConfig().getInt("limit");

                player.sendMessage(
                        MessageParser.send(
                                "Commands.list.self",
                                ImmutableMap.of(
                                        "$playerShopCount$", "" + BaseShop.countShops(player),
                                        "$playerShopCap$", "" + cap
                                )
                        )
                );
                return true;
            }

            if (!player.isOp() && !player.hasPermission("vs.list"))
                return false;


            if (args.length == 1 && player.hasPermission("vs.list.others")) {
                String possiblePlayer = args[0];
                OfflinePlayer[] offlinePlayers = VanillaShops.getPlugin().getServer().getOfflinePlayers();
                for (OfflinePlayer oP : offlinePlayers) {
                    if (oP != null && oP.getName() != null && oP.getName().equalsIgnoreCase(possiblePlayer)) {
                        player.sendMessage(
                                MessageParser.send(
                                        "Commands.list.others",
                                        ImmutableMap.of(
                                                "$playerName$", "" + oP.getName(),
                                                "$playerShopCap$", "" + BaseShop.countShops(player)
                                        )
                                )
                        );
                        return true;
                    }
                }

                player.sendMessage(
                        MessageParser.send(
                                "Commands.list.error",
                                ImmutableMap.of(
                                        "$playerName$", possiblePlayer
                                )
                        )
                );
                return true;
            }
        }
        return false;
    }
}
