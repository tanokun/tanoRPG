package com.github.tanokun.tanorpg.game.mob;

import com.github.tanokun.tanorpg.game.item.CustomItem;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CustomEntityDropItems {
    private ArrayList<CustomItem> dropItems = new ArrayList<>();
    private ArrayList<Double> dropPercentages = new ArrayList<>();

    public void addItem(CustomItem drop, double percentage){
        dropItems.add(drop);
        dropPercentages.add(percentage);
    }
    public void giveDropItems(Player player){
        for (int i = 0; i < dropItems.size(); i++) {
            if (CustomEntity.chance(dropPercentages.get(i))){
                player.getInventory().addItem(dropItems.get(i).getItem());
            }
        }
    }

}
