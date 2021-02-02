package com.github.tanokun.tanorpg.util.task;

import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SidebarTask extends BukkitRunnable {
    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()){
            Sidebar.updateSidebar(player);
        }
    }
}
