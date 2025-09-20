package org.ghostmod.crateKeys;

import co.aikar.commands.PaperCommandManager;
import com.trynocs.tryLibs.TryLibs;
import com.trynocs.tryLibs.utils.database.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private DatabaseHandler databaseHandler;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        Bukkit.getPluginManager().registerEvents(new GUI(config), this);

        TryLibs tryLibs = TryLibs.getPluginSafe();
        databaseHandler = tryLibs.getDatabaseHandler();
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
