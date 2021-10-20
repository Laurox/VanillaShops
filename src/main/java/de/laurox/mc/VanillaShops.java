package de.laurox.mc;

import de.laurox.mc.beta.PlayerJoin;
import de.laurox.mc.commands.ListCommand;
import de.laurox.mc.commands.SummonCommand;
import de.laurox.mc.files.FileManager;
import de.laurox.mc.shops.*;
import de.laurox.mc.shopsrewrite.ChatListener;
import de.laurox.mc.shopsrewrite.InventoryHandler;
import de.laurox.mc.shopsrewrite.ShopHandler;
import de.laurox.mc.files.ShopConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VanillaShops extends JavaPlugin {

    private static Plugin plugin;
    private static ShopConfig shops;

    @Override
    public void onEnable() {
        greet(this);

        plugin = this;

        FileManager.setup();

        shops = new ShopConfig(this, "shopkeeper");

        registerEvents();

        getCommand("summonShop").setExecutor(new SummonCommand());
        getCommand("listShops").setExecutor(new ListCommand());

        Recipe.addShopmerald();
    }

    private static void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new InteractListener(), plugin);
        pluginManager.registerEvents(new InventoryListener(), plugin);
        pluginManager.registerEvents(new RemoveListener(), plugin);

        pluginManager.registerEvents(new ShopHandler(), plugin);
        pluginManager.registerEvents(new InventoryHandler(), plugin);

        pluginManager.registerEvents(new ChatListener(), plugin);

        pluginManager.registerEvents(new PlayerJoin(), plugin);
    }

    public static void greet(Plugin plugin) {
        plugin.getLogger().info("     __          ");
        plugin.getLogger().info("\\  /(_ |_  _  _  ");
        plugin.getLogger().info(" \\/ __)| )(_)|_) ");
        plugin.getLogger().info("             |    ");
    }

    public static ShopConfig getShopsConfig() {
        return shops;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

}
