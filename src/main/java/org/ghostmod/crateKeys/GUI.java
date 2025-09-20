package org.ghostmod.crateKeys;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GUI implements Listener {
    public static Inventory inv = Bukkit.createInventory(null, 27, "§bCrate Keys");
    private FileConfiguration config;

    public GUI(FileConfiguration config) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Bukkit.getLogger().warning("PlaceholderAPI ist nicht installiert!");
        }
        this.config = config;
    }


    public  void createGUI(Player player, Inventory inv) {
        ItemStack glass = new ItemStack(Material.PAPER);
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
        List<String> lore1 = new ArrayList<>();
        lore1.add("§a" +config.getString("firstChest.Price"));
        chestMeta.setLore(lore1);
        chest.setItemMeta(chestMeta);
        inv.setItem(11, chest);

        Material slot2 = Material.valueOf(config.getString("secondChestSlotMaterial").toUpperCase()) ;
        ItemStack BetaChest = new ItemStack(slot2);
        ItemMeta BetaChestMeta = BetaChest.getItemMeta();
        BetaChestMeta.setDisplayName((String) config.get("secondChestName"));
        List<String> lore2 = new ArrayList<>();
        lore2.add("§a" +config.getString("secondChest.Price"));
        BetaChestMeta.setLore(lore2);
        BetaChest.setItemMeta(BetaChestMeta);
        inv.setItem(13, BetaChest);


        Material slot3 = Material.valueOf(config.getString("thirdChestSlotMaterial").toUpperCase()) ;
        ItemStack eventChest = new ItemStack(slot3);
        ItemMeta eventChestMeta = eventChest.getItemMeta();
        eventChestMeta.setDisplayName(config.getString("thirdChestName"));
        List<String> lore3 = new ArrayList<>();
        lore3.add("§a" + config.getString("thirdChest.Price"));
        eventChestMeta.setLore(lore3);
        eventChest.setItemMeta(eventChestMeta);
        inv.setItem(15, eventChest);


        ItemStack gems = new ItemStack(Material.valueOf(config.getString("gemsMaterial").toUpperCase()));
        ItemMeta gemsMeta = gems.getItemMeta();
        String title = (String) config.get("gemsName");
        title = PlaceholderAPI.setPlaceholders(player, title);
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
                if (Material.valueOf(config.getString("firstChestSlotMaterial")) == Material.BARRIER) {
                    return;
                }
                int price = config.getInt("firstChest.Price");
                if (gemsCount < price) {
                    player.sendMessage(Main.prefix + "§cDu hast nicht genug Gems für die " + config.get("firstChestName"));
                    return;
                }
                player.closeInventory();
                player.sendMessage("§aDu hast dir die " + config.get("firstChestName") + " gekauft!");
                executeConsoleCommand("syseco gems remove " + player.getName() + " " + price);
                executeConsoleCommand(config.getString("firstChest.command").replace("%player%", player.getName()));
            }
            case 13 -> {
                if (Material.valueOf(config.getString("secondChestSlotMaterial")) == Material.BARRIER) {
                    return;
                }
                int price = config.getInt("secondChest.Price");
                if (gemsCount < price) {
                    player.sendMessage(Main.prefix + "§cDu hast nicht genug Gems für die " + config.get("secondChestName"));
                    return;
                }
                player.closeInventory();
                player.sendMessage("§aDu hast dir die " + config.get("secondChestName") + " gekauft!");
                executeConsoleCommand("syseco gems remove " + player.getName() + " " + price);
                executeConsoleCommand(config.getString("secondChest.command").replace("%player%", player.getName()));
            }
            case 15 -> {
                if (Material.valueOf(config.getString("thirdChestSlotMaterial")) == Material.BARRIER) {
                    return;
                }
                int price = config.getInt("thirdChest.Price");
                if (gemsCount < price) {
                    player.sendMessage(Main.prefix + "§cDu hast nicht genug Gems für die " + config.get("thirdChestName"));
                    return;
                }
                player.closeInventory();
                player.sendMessage("§aDu hast dir die " + config.get("thirdChestName") + " gekauft!");
                executeConsoleCommand("syseco gems remove " + player.getName() + " " + price);
                executeConsoleCommand(config.getString("thirdChest.command").replace("%player%", player.getName()));
            }
        }
    }

    private void executeConsoleCommand(String command) {
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    private double getPlayerGems(Player player) {
        String raw = PlaceholderAPI.setPlaceholders(player, "%bpeco_gems%");
        if (raw == null) return 0.0;
        raw = raw.replaceAll("[^0-9.,]", "").replace(",", ".");
        if (raw.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }




}
