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


public class GUI implements Listener {
    public static Inventory inv = Bukkit.createInventory(null, 27, "§bCrate Keys");
    private TryLibs tryLibs = TryLibs.getPlugin();

    private final DatabaseHandler databaseHandler = tryLibs.getPlugin().getDatabaseHandler();
    private TryLibsAPI tryLibsAPI;
    private FileConfiguration config;
    public GUI(FileConfiguration config) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Bukkit.getLogger().warning("PlaceholderAPI ist nicht installiert! P");
        }
        this.config = config;
    }


    public  void createGUI(Player player, Inventory inv) {
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName("§7");
        glassMeta.setCustomModelData(config.getInt("paperCustomData")); // Weißes Quadrat
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, glass);
        }

        Material slot1 = Material.valueOf(config.getString("firstChestSlotMaterial").toUpperCase()) ;
        ItemStack chest = new ItemStack(slot1);
        ItemMeta chestMeta = chest.getItemMeta();
        chestMeta.setDisplayName((String) config.get("firstChestName"));
        chest.setItemMeta(chestMeta);
        inv.setItem(11, chest);

        Material slot2 = Material.valueOf(config.getString("secondChestSlotMaterial").toUpperCase()) ;
        ItemStack BetaChest = new ItemStack(slot2);
        ItemMeta BetaChestMeta = BetaChest.getItemMeta();
        BetaChestMeta.setDisplayName((String) config.get("secondChestName"));
        BetaChest.setItemMeta(BetaChestMeta);
        inv.setItem(13, BetaChest);


        Material slot3 = Material.valueOf(config.getString("thirdChestName").toUpperCase()) ;
        ItemStack eventChest = new ItemStack(Material.END_PORTAL_FRAME);
        ItemMeta eventChestMeta = eventChest.getItemMeta();
        eventChestMeta.setDisplayName((String) config.get("thirdChestName"));
        eventChest.setItemMeta(eventChestMeta);
        inv.setItem(15, eventChest);


        Material gemsMaterial = Material.valueOf(config.getString("gemsMaterial").toUpperCase()) ;
        ItemStack gems = new ItemStack(gemsMaterial);
        ItemMeta gemsMeta = gems.getItemMeta();
        String title = (String) config.get("gemsName");
        title.replace("%bpeco_gems%", PlaceholderAPI.setPlaceholders(player, "%bpeco_gems%"));
        gemsMeta.setDisplayName(title);
        gems.setItemMeta(gemsMeta);
        inv.setItem(22, gems);

        player.openInventory(inv);
    }



    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        FileConfiguration config = Main.getInstance().getConfig();
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
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    private double getPlayerGems(Player player) {
        return databaseHandler.loadDouble("economy", player.getUniqueId(), "gems.balance", 0.0);
    }

}
