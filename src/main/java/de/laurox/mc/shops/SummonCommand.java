package de.laurox.mc.shops;

import de.laurox.mc.VanillaShops;
import de.laurox.mc.files.FileManager;
import de.laurox.mc.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SummonCommand implements CommandExecutor {

    private Config shops;

    private Inventory config;
    private Inventory storage;
    private Inventory payment;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.isOp() || !player.hasPermission("vs.summon")) return false;

            if(args.length != 0) return false;

            Location location = player.getLocation();
            spawn(location, player);
            player.sendMessage(FileManager.getMessage("Commands.summon"));
            return true;
        }
        return false;
    }

    private void spawn(Location location, Player player) {
        Villager villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        villager.setAdult();
        villager.setBreed(false);
        villager.setAgeLock(true);
        villager.setCanPickupItems(false);
        villager.setCustomNameVisible(true);
        villager.setCustomName("§6Shopkeeper");
        villager.setInvulnerable(true);
        villager.setCollidable(false);
        villager.setVillagerExperience(1);
        villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255), true);

        config = Bukkit.createInventory(villager, 9 * 3, "§cConfig");
        storage = Bukkit.createInventory(villager, 9 * 3, "§eStorage");
        payment = Bukkit.createInventory(villager, 9 * 3, "§aPayment");

        shops = VanillaShops.getShopsConfig();

        shops.set(villager.getUniqueId().toString() + ".owner", player.getUniqueId().toString());
        shops.set(villager.getUniqueId().toString() + ".configTitle", "§cConfig");
        shops.set(villager.getUniqueId().toString() + ".storageTitle", "§eStorage");
        shops.set(villager.getUniqueId().toString() + ".paymentTitle", "§aPayment");
        for (int i = 9; i < 18; i++) {
            config.setItem(i, createItem(Material.GRAY_STAINED_GLASS_PANE, "§eOffer #" + (i - 8)));
        }
        shops.set(villager.getUniqueId().toString() + ".config", config.getContents());
        shops.set(villager.getUniqueId().toString() + ".storage", storage.getContents());
        shops.set(villager.getUniqueId().toString() + ".payment", payment.getContents());

        shops.reload();
    }

    private ItemStack createItem(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
