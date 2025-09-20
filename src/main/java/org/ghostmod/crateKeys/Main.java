package org.ghostmod.crateKeys;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static String prefix = "§b§lBlockEngine §8» §7";

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        Bukkit.getPluginManager().registerEvents(new GUI(config), this);

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new Command(manager));
        manager.registerCommand(new cratecommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return getPlugin(Main.class);
    }
}
