package com.github.tanokun.tanorpg.game;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.EntityData;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.game.player.GamePlayer;
import com.github.tanokun.tanorpg.game.player.GamePlayerManager;
import com.github.tanokun.tanorpg.game.player.status.buff.BuffType;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static com.github.tanokun.tanorpg.game.entity.EntityManager.getEntity;
import static com.github.tanokun.tanorpg.game.player.status.buff.Buff.getBuffPercent;

public class DamageManager {
    public static long getCompDamage(double atk, double def, int attackerLv, int victimLv, Entity entity){
        long damage = Math.round(atk * (10 + attackerLv) / (9.8 + def + victimLv));
        double percent = getBuffPercent(entity, BuffType.ATK_UP_S) +
                getBuffPercent(entity, BuffType.ATK_UP_M) +
                getBuffPercent(entity, BuffType.ATK_UP_L);
        percent = percent / 100;
        damage = damage + Math.round(percent * damage);
        return damage;
    }
    public static double getDamage(double atk, double ing, double agi){
        return atk + ing/2 + agi/3;
    }

    public static void createDamage(long damage, Entity attacker, Entity target){
        new BukkitRunnable(){
            @Override
            public void run() {
                ((Creature)target).setTarget((LivingEntity) attacker);
                String[] name = target.getName().split(" ");
                if (getEntity((Creature) target) == null) return;
                EntityData customEntity;
                try {
                    customEntity = getEntity((Creature) target).getEntityData();
                }catch (NullPointerException e){return;}
                GamePlayer gamePlayer = GamePlayerManager.getPlayer(attacker.getUniqueId());
                if (((Creature) target).getHealth() - damage <= 0) {
                    customEntity.getDropItems().giveDropItems((Player) attacker);
                    gamePlayer.setHAS_EXP(gamePlayer.getHAS_EXP() + EntityManager.getEntityData(name[0]).getEXP());
                }
                ((Creature) target).damage(damage);
                gamePlayer.getPlayer().sendMessage(TanoRPG.PX + damage + "ダメージ！");
            }
        }.runTask(TanoRPG.getPlugin());
    }
}
