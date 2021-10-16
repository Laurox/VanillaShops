package de.laurox.mc.shops;

import de.laurox.mc.VanillaShops;
import de.laurox.mc.util.Config;
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
            if (!player.isOp() && !player.hasPermission("vs.list"))
                return false;

            if (args.length == 0) {
                player.sendMessage("§8>> §7You own " + countOwnedShops(player.getUniqueId().toString()) + " shopkeepers");
                return true;
            }

            if(args.length == 1 && player.hasPermission("vs.list.others")) {
                String possiblePlayer = args[0];
                OfflinePlayer[] offlinePlayers = VanillaShops.getPlugin().getServer().getOfflinePlayers();
                for(OfflinePlayer oP : offlinePlayers) {
                    if(oP.getName().equalsIgnoreCase(possiblePlayer)) {
                        player.sendMessage("§8>> §7" + oP.getName() + " owns " + countOwnedShops(oP.getUniqueId().toString()) + " shopkeepers");
                        return true;
                    }
                }
                player.sendMessage("§8>> §7" + possiblePlayer + " has not played on this server yet");
                return true;
            }
        }
        return false;
    }

    private int countOwnedShops(String playerUUID) {
        Config shops = VanillaShops.getShopsConfig();

        int shopCount = 0;

        for (String key : shops.getKeys()) {
            String owner = VanillaShops.getShopsConfig().get(key + ".owner");
            if(owner.equalsIgnoreCase(playerUUID)) {
                shopCount++;
            }
        }

        return shopCount;
    }
}
