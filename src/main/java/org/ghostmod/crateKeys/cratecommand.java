package org.ghostmod.crateKeys;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import org.bukkit.entity.Player;

public class cratecommand extends BaseCommand {

    @CommandAlias("crate")
    public void onCrateCommand(Player player) {
        player.openInventory(GUI.inv);
        GUI.createGUI(player, GUI.inv);
    }

}
