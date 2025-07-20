package org.ghostmod.crateKeys;

import co.aikar.commands.PaperCommandManager;
import com.trynocs.tryLibs.TryLibs;
import com.trynocs.tryLibs.utils.database.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrateKeys extends JavaPlugin {
    private TryLibs tryLibs;
    private DatabaseHandler databaseHandler;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new GUI(), this);
        tryLibs = TryLibs.getPlugin();
        databaseHandler = tryLibs.getDatabaseHandler();

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new Command(manager));
        manager.registerCommand(new cratecommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static CrateKeys getInstance() {
        return getPlugin(CrateKeys.class);
    }
}
