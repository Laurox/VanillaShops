package de.laurox.mc;

import de.laurox.mc.files.FileManager;
import de.laurox.mc.shops.InteractListener;
import de.laurox.mc.shops.InventoryListener;
import de.laurox.mc.shops.RemoveListener;
import de.laurox.mc.shops.SummonCommand;
import de.laurox.mc.shopsrewrite.InventoryHandler;
import de.laurox.mc.shopsrewrite.ShopHandler;
import de.laurox.mc.util.Config;
import de.laurox.mc.util.Recipe;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VanillaShops extends JavaPlugin {

    private static Plugin plugin;
    private static Config shops;

    @Override
    public void onEnable() {
        plugin = this;

        FileManager.setup(this);

        //main = new Config(this, "main");
        shops = new Config(this, "shopkeeper");

        registerEvents();

        getCommand("summonShop").setExecutor(new SummonCommand());
        Recipe.addShopmerald();
    }

    private static void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new InteractListener(shops), plugin);
        pluginManager.registerEvents(new InventoryListener(), plugin);
        pluginManager.registerEvents(new RemoveListener(), plugin);

        pluginManager.registerEvents(new ShopHandler(), plugin);
        pluginManager.registerEvents(new InventoryHandler(), plugin);
    }

    public static Config getShopsConfig() {
        return shops;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

}
