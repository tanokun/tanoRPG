package com.github.tanokun.tanorpg;

import com.github.tanokun.tanorpg.command.*;
import com.github.tanokun.tanorpg.command.register.Register;
import com.github.tanokun.tanorpg.event.worldguard.WgEvents;
import com.github.tanokun.tanorpg.game.craft.CraftManager;
import com.github.tanokun.tanorpg.game.mission.MissionManager;
import com.github.tanokun.tanorpg.game.mission.listener.CraftEventListener;
import com.github.tanokun.tanorpg.game.mission.listener.EntityKillMissionEventListener;
import com.github.tanokun.tanorpg.game.mission.listener.NpcClickListener;
import com.github.tanokun.tanorpg.game.mission.listener.ShoppingEventListener;
import com.github.tanokun.tanorpg.game.mission.model.robert.Robert_FirstShoppingMission;
import com.github.tanokun.tanorpg.game.mission.model.robert.Robert_GoblinKillMission;
import com.github.tanokun.tanorpg.game.mission.model.Merchant_GuardianKillMission;
import com.github.tanokun.tanorpg.game.mission.model.robert.Robert_firstCraftMission;
import com.github.tanokun.tanorpg.game.player.skill.SkillManager;
import com.github.tanokun.tanorpg.game.player.skill.combo.ComboManager;
import com.github.tanokun.tanorpg.game.player.skill.execute.PlayerSkJump;
import com.github.tanokun.tanorpg.game.player.skill.execute.mage.PlayerSkExplosionAttack;
import com.github.tanokun.tanorpg.game.player.skill.execute.mage.PlayerSkLiningAttack;
import com.github.tanokun.tanorpg.game.player.skill.execute.priest.PlayerSkCircleHeal;
import com.github.tanokun.tanorpg.game.player.skill.execute.priest.PlayerSkCoolTimeUpBuff;
import com.github.tanokun.tanorpg.game.player.skill.execute.priest.PlayerSkHeal;
import com.github.tanokun.tanorpg.game.player.skill.execute.warrior.PlayerSkAtkUp;
import com.github.tanokun.tanorpg.game.player.skill.execute.warrior.PlayerSkJumpAttack;
import com.github.tanokun.tanorpg.game.player.skill.execute.warrior.PlayerSkLineAttack;
import com.github.tanokun.tanorpg.game.shop.ShopManager;
import com.github.tanokun.tanorpg.game.shop.sell.Sell;
import com.github.tanokun.tanorpg.listener.*;
import com.github.tanokun.tanorpg.menu.MenuManager;
import com.github.tanokun.tanorpg.menu.mission.AllMissionMenu;
import com.github.tanokun.tanorpg.menu.mission.MissionCheck;
import com.github.tanokun.tanorpg.menu.player.MissionMenu;
import com.github.tanokun.tanorpg.menu.player.SetJobMenu;
import com.github.tanokun.tanorpg.menu.player.StatusMainMenu;
import com.github.tanokun.tanorpg.menu.player.StatusSkillMenu;
import com.github.tanokun.tanorpg.util.Glowing;
import com.github.tanokun.tanorpg.util.io.Folder;
import com.github.tanokun.tanorpg.util.task.AutoSaveTask;
import com.github.tanokun.tanorpg.util.task.EditStatusTask;
import com.github.tanokun.tanorpg.util.task.PlayerRegenerationTask;
import com.github.tanokun.tanorpg.util.task.SidebarTask;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public class Registration {
    private static Plugin plugin;
    public Registration(Plugin plugin){this.plugin = plugin;}

    public void registerConfigs() {
        plugin.saveDefaultConfig();
        new Folder("mobs", plugin).createExists();

        new Folder("player_database", plugin).createExists();
        new Folder("shop", plugin).createExists();
        new Folder("craft", plugin).createExists();
        new Folder("spawner", plugin).createExists();
    }
    public void registerCommand(){
        Register.register(new TanoRPGCommand());
        Register.register(new GiveItemCommand());
        Register.register(new TestCommand());
        Register.register(new MobSpawnCommand());
        Register.register(new OpenCraftCommand());
        Register.register(new OpenShopCommand());
        Register.register(new OpenSellCommand());
        Register.register(new GiveSkItemCommand());
        Register.register(new OpenStatusCommand());
        Register.register(new HatCommand());
        Register.register(new ReloadCommand());
    }
    public void registerListener(){
        Bukkit.getPluginManager().registerEvents(new BreakBlockEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new DamageEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new MenuManager(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new ComboManager(), plugin);
        Bukkit.getPluginManager().registerEvents(new LeftClickEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new DeathEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new ShopManager(), plugin);
        Bukkit.getPluginManager().registerEvents(new CraftManager(), plugin);
        Bukkit.getPluginManager().registerEvents(new Sell(), plugin);
        Bukkit.getPluginManager().registerEvents(new SkillShortCutListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new NpcClickListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new EntityKillMissionEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new ShoppingEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new CraftEventListener(), plugin);
    }
    public void registerOthers() {
        {
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Glowing glow = new Glowing();
                Enchantment.registerEnchantment(glow);
            } catch (IllegalArgumentException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        WgEvents.setup();
    }

    public void registerTask(){
        new AutoSaveTask().runTaskTimerAsynchronously(plugin, 1, 6000);
        new EditStatusTask().runTaskTimerAsynchronously(plugin, 1, 1);
        new PlayerRegenerationTask().runTaskTimerAsynchronously(plugin, 1, 80);
        new SidebarTask().runTaskTimerAsynchronously(plugin, 1, 100);
        TanoRPG.getEntitySpawnerManager().runTaskTimerAsynchronously(plugin, 1, 20);
    }

    public void registerMenus() {
        MenuManager.registerMenu(new SetJobMenu());
        MenuManager.registerMenu(new StatusMainMenu(null));
        MenuManager.registerMenu(new StatusSkillMenu(null));
        MenuManager.registerMenu(new MissionMenu(null));
        MenuManager.registerMenu(new MissionCheck());
        MenuManager.registerMenu(new AllMissionMenu(null));
    }

    public void registerSkills() {
        //すべて
        SkillManager.addAllSkill(new PlayerSkJump());

        //ウォーリア
        SkillManager.addWarriorSkill(new PlayerSkAtkUp());
        SkillManager.addWarriorSkill(new PlayerSkJumpAttack());
        SkillManager.addWarriorSkill(new PlayerSkLineAttack());

        //メイジ
        SkillManager.addMageSkill(new PlayerSkLiningAttack());
        SkillManager.addMageSkill(new PlayerSkExplosionAttack());

        //プリースト
        SkillManager.addPriestSkill(new PlayerSkHeal());
        SkillManager.addPriestSkill(new PlayerSkCircleHeal());
        SkillManager.addPriestSkill(new PlayerSkCoolTimeUpBuff());
    }

    public void registerMissions(){
        MissionManager.registerMission(new Robert_FirstShoppingMission());
        MissionManager.registerMission(new Robert_firstCraftMission());
        MissionManager.registerMission(new Robert_GoblinKillMission());

        MissionManager.registerMission(new Merchant_GuardianKillMission());
    }
}
