package org.ghostmod.crateKeys;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.trynocs.tryLibs.TryLibs;
import org.bukkit.entity.Player;

@CommandAlias("kisten")
@CommandPermission("be.kisten")
public class cratecommand extends BaseCommand {


    @Default
    public void onCrateCommand(Player player) {
        player.openInventory(GUI.inv);
        GUI gui = new GUI(Main.getInstance().getConfig());
        gui.createGUI(player, GUI.inv);
    }


    @Subcommand("reloadConfig")
    public void onReloadConfig(Player player){

        Main.getInstance().reloadConfig();

        player.sendMessage(Main.prefix + "§7Die Config wurde erfolgreich neugeladen");

        GUI gui = new GUI(Main.getInstance().getConfig());
        gui.createGUI(player, GUI.inv);
    }


}
