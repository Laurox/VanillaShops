package de.laurox.mc.files;

import de.laurox.mc.VanillaShops;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileManager {
    private static FileConfiguration mainConfig;
    private static FileConfiguration language;

    public static void setup() {
        mainConfig = loadConfig("config.yml", "/config.yml");
        language = loadConfig(mainConfig.getString("language") + ".yml", "/lang/" + mainConfig.get("language") + ".yml");
    }

    private static FileConfiguration loadConfig(String filePath, String resourcePath) {
        File file = new File(VanillaShops.getPlugin().getDataFolder(), filePath);
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                InputStream in = FileManager.class.getResourceAsStream(resourcePath);
                assert in != null;
                Files.copy(in, file.toPath());
            } catch (IOException | NullPointerException e) {
                VanillaShops.getPlugin().getLogger().severe("Could not load file with path: " + resourcePath);
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    // @Deprecated
   //  public static String getMessage(String key) {
   //     return (String) language.get(key.replace("&", "§"));
    //}

    public static FileConfiguration getConfig() {
        return mainConfig;
    }

    public static FileConfiguration getLanguageConfig() {
        return language;
    }
}
