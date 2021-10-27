package de.laurox.mc.commands;

import de.laurox.mc.MessageParser;
import de.laurox.mc.shopsrewrite.BaseShop;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SummonCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp() && !player.hasPermission("vs.summon")) return false;

            if (args.length != 0) return false;

            Location location = player.getLocation();
            boolean allowed = BaseShop.spawn(location, player);

            if (allowed) {
                player.sendMessage(MessageParser.send("Commands.summon"));
                return true;
            }

        }
        return false;
    }

}
