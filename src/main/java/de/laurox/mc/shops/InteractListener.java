package de.laurox.mc.shops;

import de.laurox.mc.files.FileManager;
import de.laurox.mc.shopsrewrite.BaseShop;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InteractListener implements Listener {

    private static final String name = "§aShopmerald";

    @EventHandler
    public void onItemInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getBlockFace() != BlockFace.UP) return;

        if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType().equals(Material.AIR))
            return;
        ItemStack itemStack = event.getItem();
        if (itemStack == null) return;
        if (!itemStack.hasItemMeta()) return;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;
        if (!itemMeta.hasDisplayName()) return;
        String displayName = itemMeta.getDisplayName();

        if (!displayName.equals(name)) return;

        Location location = event.getClickedBlock().getLocation().add(0.5, 1.0, 0.5);

        boolean allowed = BaseShop.spawn(location, player);

        if (!allowed) {
            return;
        }

        if (itemStack.getAmount() > 1) {
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }
    }


}
