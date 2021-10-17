package de.laurox.mc.shops;

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

                player.sendMessage("§8>> §7You own " + BaseShop.countShops(player) + " shopkeepers" + (cap > 0 ? " §8(limit: " + cap + ")" :""));
                return true;
            }

            if (!player.isOp() && !player.hasPermission("vs.list"))
                return false;


            if(args.length == 1 && player.hasPermission("vs.list.others")) {
                String possiblePlayer = args[0];
                OfflinePlayer[] offlinePlayers = VanillaShops.getPlugin().getServer().getOfflinePlayers();
                for(OfflinePlayer oP : offlinePlayers) {
                    if(oP != null && oP.getName() != null && oP.getName().equalsIgnoreCase(possiblePlayer)) {
                        player.sendMessage("§8>> §7" + oP.getName() + " owns " + BaseShop.countShops(player) + " shopkeepers");
                        return true;
                    }
                }
                player.sendMessage("§8>> §7" + possiblePlayer + " has not played on this server yet");
                return true;
            }
        }
        return false;
    }
}
