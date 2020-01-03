package de.laurox.mc.util;

import de.laurox.mc.VanillaShops;
import de.laurox.mc.files.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Recipe {

    public static void addShopmerald() {
        // Our custom variable which we will be changing around.
        ItemStack item = new ItemStack(Material.EMERALD);

        // The meta of the diamond sword where we can change the name, and properties of the item.
        ItemMeta meta = item.getItemMeta();

        // We will initialise the next variable after changing the properties of the sword

        // This sets the name of the item.
        meta.setDisplayName("§aShopmerald");

        // Set the meta of the sword to the edited meta.
        item.setItemMeta(meta);

        // create a NamespacedKey for your recipe
        NamespacedKey key = new NamespacedKey(VanillaShops.getPlugin(), "shopmerald");

        // Create our custom recipe variable
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        // Here we will set the places. E and S can represent anything, and the letters can be anything. Beware; this is case sensitive.
        recipe.shape(FileManager.getShape("top"), FileManager.getShape("middle"), FileManager.getShape("bottom"));

        // Set what the letters represent.
        List<String> ingredients = FileManager.getCrafting();
        for (String s : ingredients) {
            String mat = FileManager.getCraftingConfig().getString("crafting" + "." + s + "." + "material");
            recipe.setIngredient(s.charAt(0), Material.getMaterial(mat));
        }

        // Add the recipe to the Bukkit recipes
        Bukkit.addRecipe(recipe);
    }
}
