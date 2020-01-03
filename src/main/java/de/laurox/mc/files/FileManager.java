package de.laurox.mc.files;

import de.laurox.mc.VanillaShops;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class FileManager {
    private static FileConfiguration mainConfig;
    private static FileConfiguration language;
    private static FileConfiguration crafting;

    private static File mainFile;
    private static File languageFile;
    private static File craftingFile;

    private static String initLang;

    public static void setup(Plugin plugin) {
        // load main-config
        mainFile = new File(plugin.getDataFolder(), "config.yml");
        mainFile.getParentFile().mkdirs();
        mainConfig = YamlConfiguration.loadConfiguration(mainFile);

        mainConfig.addDefault("language", "en");

        mainConfig.options().copyDefaults(true);
        saveConfig();

        // load language
        languageFile = new File(plugin.getDataFolder(), mainConfig.getString("language") + ".yml");
        if(!languageFile.exists()) {
            try {
                InputStream in = FileManager.class.getResource(mainConfig.get("language") + ".yml").openStream();
                Files.copy(in, languageFile.toPath());
            } catch (IOException e) {
                plugin.getLogger().severe("Could not load file: " + mainConfig.get("language") + ".yml");
                e.printStackTrace();
            }
        }
        language = YamlConfiguration.loadConfiguration(languageFile);
        initLang = mainConfig.getString("language");

        // load crafting
        craftingFile = new File(plugin.getDataFolder(), "recipe.yml");
        if(!craftingFile.exists()) {
            try {
                InputStream in = FileManager.class.getResource("recipe.yml").openStream();
                Files.copy(in, craftingFile.toPath());
            } catch (IOException e) {
                plugin.getLogger().severe("Could not load file: recipe.yml");
                e.printStackTrace();
            }
        }
        crafting = YamlConfiguration.loadConfiguration(craftingFile);

    }


    private static void saveConfig() {
        try {
            mainConfig.save(mainFile);
        } catch (IOException e) {
            VanillaShops.getPlugin().getLogger().severe("Could not save file: config.yml");
            e.printStackTrace();
        }
    }

    /* Get Values */
    public static String getMessage(String key) {
        return (String) language.get(key.replace("&", "§"));
    }

    public static boolean getBool(String key) {
        return language.getBoolean(key);
    }

    public static String getShape(String row) {
        return crafting.getString("shape." + row);
    }

    public static List<String> getCrafting() {
        return crafting.getStringList("crafting.list");
    }

    /* Config Getter */
    public static FileConfiguration getLanguage() {
        return language;
    }

    public static FileConfiguration getConfig() {
        return mainConfig;
    }

    public static FileConfiguration getCraftingConfig() {
        return crafting;
    }
}
