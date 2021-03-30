package com.github.tanokun.tanorpg.game.shop.sell;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.item.itemtype.base.Item;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Sell implements Listener {
    private static String INV_NAME = "§d§l売却";
    private ArrayList<Integer> nulls;
    private static HashMap<String, BukkitRunnable> check = new HashMap<>();

    public Sell(){
        nulls = new ArrayList<>();
        for (int i = 1; i <= 18; i++){
            if (i <= 9){
                nulls.add(i - 1);
            } else {
                nulls.add(i + 35);
            }
        }
        for (int i = 1; i <= 8; i++){
            if (i <= 4){
                nulls.add(i * 9);
            } else {
                nulls.add(8 + (i - 4) * 9);
            }
        }
    }

    public static void openSell(Player player){
        Inventory sell = Bukkit.createInventory(null, 54, INV_NAME);
        ItemStack BSG = MenuManager.createItem(Material.PURPLE_STAINED_GLASS_PANE, "    ", 1, false);
        ItemStack sell_item = MenuManager.createItem(Material.EMERALD, "§d§l合計値段: 0",
                Arrays.asList("§bクリックで売却する", "§7※ 合計値段がおかしくても売却時", "§7  に合計値は治ります"), 1, true);
        ItemStack side = MenuManager.createItem(Material.PURPLE_GLAZED_TERRACOTTA, "  ", 1, false);

        for (int i = 1; i <= 18; i++){
            if (i <= 9){
                sell.setItem(i - 1, BSG);
            } else {
                sell.setItem(i + 35, BSG);
            }
        }
        for (int i = 1; i <= 8; i++){
            if (i <= 4){
                sell.setItem(i * 9, BSG);
            } else {
                sell.setItem(8 + (i - 4) * 9, BSG);
            }
        }
        sell.setItem(0, side);
        sell.setItem(8, side);
        sell.setItem(45, side);
        sell.setItem(53, side);
        sell.setItem(49, sell_item);
        new CheckItems(player);
        player.openInventory(sell);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (e.getCurrentItem() == null) return;
        if (e.getClickedInventory().equals(e.getWhoClicked().getOpenInventory().getTopInventory()) && e.getView().getTitle().equals(INV_NAME)){
            if (nulls.contains(e.getSlot())){
                e.setCancelled(true);
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§d§l合計値段: ")){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute at " + e.getWhoClicked().getName() + " run playsound minecraft:shop.buy master @p ~ ~ ~ 3 1");
                    long price = check(e.getClickedInventory().getContents());
                    e.getWhoClicked().sendMessage(TanoRPG.PX + "§d売却しました！ (合計価格: " + price + ")");
                    GamePlayerManager.getPlayer(e.getWhoClicked().getUniqueId()).addMoney(price);
                    e.getWhoClicked().setMetadata("sell", new FixedMetadataValue(TanoRPG.getPlugin(), true));
                    e.getWhoClicked().closeInventory();
                }
                return;
            }
        }
    }
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if (e.getView().getTitle().equals(INV_NAME)){
            check.get(e.getPlayer().getUniqueId().toString()).cancel();
            check.remove(e.getPlayer().getUniqueId().toString());
            if (e.getPlayer().hasMetadata("sell")){
                e.getPlayer().removeMetadata("sell", TanoRPG.getPlugin());
                return;
            }
            for (int i = 0; i < 53; i++) {
                if (nulls.contains(i)) continue;
                if (e.getView().getItem(i) == null) continue;
                if (e.getView().getItem(i).getType().equals(Material.AIR)) continue;
                e.getPlayer().getInventory().addItem(e.getView().getItem(i));
            }
        }
    }
    private static long check(ItemStack[] items){
        long price = 0;
        for (ItemStack item : items){
            if (!ItemManager.isExists(ItemManager.getID(item))) continue;
            Item sell_item = ItemManager.getItem(item);
            price += sell_item.getPrice() * item.getAmount();
        }
        return price;
    }
    static class CheckItems extends BukkitRunnable {
        Player player;
        InventoryView inv;
        public CheckItems(Player player){
            this.player = player;
            check.put(player.getUniqueId().toString(), this);
            this.runTaskTimerAsynchronously(TanoRPG.getPlugin(), 0, 5);
        }
        public void run() {
            long money = check(player.getOpenInventory().getTopInventory().getContents());
            ItemStack item = player.getOpenInventory().getItem(49);
            ItemMeta itemM = item.getItemMeta();
            itemM.setDisplayName("§d§l合計値段: " + money + " " + TanoRPG.MONEY);
            item.setItemMeta(itemM);
            player.getOpenInventory().setItem(49, item);
        }
    }
}
