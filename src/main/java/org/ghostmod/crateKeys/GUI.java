package org.ghostmod.crateKeys;

import com.trynocs.tryLibs.TryLibs;
import com.trynocs.tryLibs.api.TryLibsAPI;
import com.trynocs.tryLibs.utils.database.DatabaseHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;


public class GUI implements Listener {
    public static Inventory inv = Bukkit.createInventory(null, 27, "§bCrate Keys");
    private TryLibsAPI tryLibsAPI;
    public GUI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Bukkit.getLogger().warning("PlaceholderAPI ist nicht installiert! P");
        }
        RegisteredServiceProvider<TryLibsAPI> provider = Bukkit.getServer().getServicesManager().getRegistration(TryLibsAPI.class);
        tryLibsAPI = provider.getProvider();
    }


    public static void createGUI(Player player, Inventory inv) {
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName("§7");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, glass);
        }

        ItemStack chest = new ItemStack(Material.CHEST);
        ItemMeta chestMeta = chest.getItemMeta();
        chestMeta.setDisplayName("§e§lVote Kiste");
        chest.setItemMeta(chestMeta);
        inv.setItem(11, chest);

        ItemStack BetaChest = new ItemStack(Material.ENDER_CHEST);
        ItemMeta BetaChestMeta = BetaChest.getItemMeta();
        BetaChestMeta.setDisplayName("§3§lBeta Kiste");
        BetaChest.setItemMeta(BetaChestMeta);
        inv.setItem(13, BetaChest);

        ItemStack eventChest = new ItemStack(Material.END_PORTAL_FRAME);
        ItemMeta eventChestMeta = eventChest.getItemMeta();
        eventChestMeta.setDisplayName("§c§lEvent Kiste");
        eventChest.setItemMeta(eventChestMeta);
        inv.setItem(15, eventChest);

        ItemStack gems = new ItemStack(Material.EMERALD);
        ItemMeta gemsMeta = gems.getItemMeta();
        String placeholderDisplay = PlaceholderAPI.setPlaceholders(player, "§a§lGems: %bpeco_gems%");
        gemsMeta.setDisplayName("Deine Gems: " + placeholderDisplay);
        gems.setItemMeta(gemsMeta);
        inv.setItem(22, gems);

        player.openInventory(inv);
    }



    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        FileConfiguration config = CrateKeys.getInstance().getConfig();
        Inventory inv = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (inv == null || item == null || item.getType() == Material.AIR || !event.getView().getTitle().equals("§bCrate Keys")) {
            return;
        }

        event.setCancelled(true);
        int slot = event.getSlot();
        double gemsCount = getPlayerGems(player);

        switch (slot) {
            case 11 -> {
                int price = config.getInt("VoteCrate.Price");
                if (gemsCount < price) {
                    player.sendMessage("§cDu hast nicht genug Gems für eine VoteKiste!");
                    return;
                }
                player.closeInventory();
                player.sendMessage("§aDu hast eine VoteKiste gekauft!");
                executeConsoleCommand("syseco gems remove " + player.getName() + " " + price);
            }
            case 13 -> {
                int price = config.getInt("Beta Kiste.Price");
                if (gemsCount < price) {
                    player.sendMessage("§cDu hast nicht genug Gems für eine Beta Kiste!");
                    return;
                }
                player.closeInventory();
                player.sendMessage("§aDu hast eine BetaKiste gekauft!");
                executeConsoleCommand("syseco gems remove " + player.getName() + " " + price);
            }
            case 15 -> {
                int price = config.getInt("EventCase.Price");
                if (gemsCount < price) {
                    player.sendMessage("§cDu hast nicht genug Gems für eine EventCase!");
                    return;
                }
                player.closeInventory();
                player.sendMessage("§aDu hast eine EventKiste gekauft!");
                executeConsoleCommand("syseco gems remove " + player.getName() + " " + price);
            }
        }
    }

    private void executeConsoleCommand(String command) {
        Bukkit.getScheduler().runTask(CrateKeys.getInstance(), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    private double getPlayerGems(Player player) {
        return tryLibsAPI.getDatabaseHandler().loadDouble("economy", player.getUniqueId(), "gems.balance", 0.0);
    }

}
