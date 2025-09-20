package org.ghostmod.crateKeys;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import com.trynocs.tryLibs.TryLibs;
import org.bukkit.entity.Player;

public class cratecommand extends BaseCommand {


    @CommandAlias("kisten")
    public void onCrateCommand(Player player) {
        player.openInventory(GUI.inv);
        GUI gui = new GUI(Main.getInstance().getConfig());
        gui.createGUI(player, GUI.inv);
    }

}
