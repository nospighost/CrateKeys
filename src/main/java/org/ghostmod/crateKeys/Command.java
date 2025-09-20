package org.ghostmod.crateKeys;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandManager;
import co.aikar.commands.annotation.*;
import com.trynocs.tryLibs.TryLibs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("syseco")
@CommandPermission("crates.syseco")
public class Command extends BaseCommand {

    public Command(CommandManager commandManager){
        commandManager.getCommandCompletions().registerAsyncCompletion("players", c ->
                Bukkit.getOnlinePlayers().stream().map(Player::getName).toList()
        );
    }

    @Default
    public void onDefault() {

    }

    @Subcommand("remove")
    @CommandPermission("syseco.admin.remove")
    @CommandCompletion("@players")
    public void onSubcommand(Player player, String name, int amount) {
        if (amount <= 0) {
            player.sendMessage(Main.prefix + "§cDie Anzahl muss größer als 0 sein.");
            return;
        }

        player.sendMessage(Main.prefix + "§aDu hast §e" + amount + "§a Games von §e" + name + "§a entfernt.");
        int countBefore = TryLibs.getPlugin().getDatabaseHandler().loadInt("economy", player.getUniqueId(), "gems.balance", 0);
        int newCount = countBefore -= amount;
        TryLibs.getPlugin().getDatabaseHandler().saveInt("economy", player.getUniqueId(), "gems.balance", newCount);

    }

}


