package de.laurox.mc;

import de.laurox.mc.VanillaShops;
import de.laurox.mc.files.FileManager;
import de.laurox.mc.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.*;
import java.util.stream.Collectors;

public class Recipe {

    public static void addShopmerald() {

        // DO NOT REGISTER THE RECIPE IF SET TO FALSE
        if (!FileManager.getConfig().getBoolean("customCrafting")) {
            return;
        }

        ItemStack item = InventoryUtil.createItem(Material.EMERALD, "§aShopmerald", new String[]{"", "§7A magical emerald holding the ", "§7power of a eager villager", "", "§eRight-Click to summon"});

        // create a NamespacedKey for your recipe
        NamespacedKey key = new NamespacedKey(VanillaShops.getPlugin(), "shopmerald");
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        // Transform Input into Chars
        String recipeString = FileManager.getConfig().getString("recipe.top") + " " + FileManager.getConfig().getString("recipe.middle") + " " + FileManager.getConfig().getString("recipe.bottom");
        Set<String> keys = Arrays.stream(recipeString.split("\\s+")).filter(s -> !s.equalsIgnoreCase("EMPTY")).collect(Collectors.toSet());

        // Save conversion map for later
        Map<Character, String> recipeMap = new HashMap<>();
        char baseChar = 'A';

        for (String materialKey : keys) {
            recipeMap.put(baseChar, materialKey);
            baseChar++;
        }

        // transform config input to correct format
        recipe.shape(
                prepareRow(FileManager.getConfig().getString("recipe.top"), recipeMap),
                prepareRow(FileManager.getConfig().getString("recipe.middle"), recipeMap),
                prepareRow(FileManager.getConfig().getString("recipe.bottom"), recipeMap)
        );

        // Set what the letters represent.
        for (Map.Entry<Character, String> entry : recipeMap.entrySet()) {
            recipe.setIngredient(entry.getKey(), Material.getMaterial(entry.getValue()));
        }

        // Add the recipe to the Bukkit recipes
        Bukkit.addRecipe(recipe);
    }

    private static String prepareRow(String row, Map<Character, String> keys) {
        row = row.replaceAll("\\s+", "").replaceAll("EMPTY", " ");
        for (Map.Entry<Character, String> entry : keys.entrySet()) {
            row = row.replaceAll(entry.getValue(), String.valueOf(entry.getKey()));
        }

        return row;
    }
}
