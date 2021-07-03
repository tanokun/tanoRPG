package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityDropItems;
import com.github.tanokun.tanorpg.player.EquipmentMap;
import com.github.tanokun.tanorpg.player.status.StatusMap;
import com.github.tanokun.tanorpg.player.status.StatusType;
import com.github.tanokun.tanorpg.util.io.Config;
import net.minecraft.server.v1_15_R1.EntitySkeleton;
import net.minecraft.server.v1_15_R1.EntitySpider;
import net.minecraft.server.v1_15_R1.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftSkeleton;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseSpider extends ObjectEntity {

    public BaseSpider(Config entityConfig, String name, StatusMap statusMap, EquipmentMap equipMap, EntityDropItems dropItems, int exp, int level) {
        super(entityConfig, name, statusMap, equipMap, dropItems, exp, level);
    }

    @Override
    public Entity spawn(Location location) {
        CraftWorld craftWorld = (CraftWorld) location.getWorld();
        Spider entity = new CraftSpider((CraftServer) Bukkit.getServer(),
                new EntitySpider(EntityTypes.SPIDER, craftWorld.getHandle().getMinecraftWorld()));
        entity = craftWorld.spawn(location, entity.getClass());

        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getStatusMap().getStatus(StatusType.HP));
        entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getDefaultValue() * (1 + (getStatusMap().getStatus(StatusType.SPEED) / 100)));

        entity.setCustomName(getName() + " §7[§dLv:§e" + getHasLevel() + "§7] " + "§a❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘❘");
        entity.setCustomNameVisible(true);
        entity.setHealth(getStatusMap().getStatus(StatusType.HP));
        entity.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), new ActiveEntity(this, entity)));

        entity.getEquipment().setItemInMainHand(getEquipMap().getEquip(EquipmentMap.EquipmentType.MAIN));
        entity.getEquipment().setItemInOffHand(getEquipMap().getEquip(EquipmentMap.EquipmentType.SUB));
        entity.getEquipment().setHelmet(getEquipMap().getEquip(EquipmentMap.EquipmentType.HELMET));
        entity.getEquipment().setChestplate(getEquipMap().getEquip(EquipmentMap.EquipmentType.CHESTPLATE));
        entity.getEquipment().setLeggings(getEquipMap().getEquip(EquipmentMap.EquipmentType.LEGGINGS));
        entity.getEquipment().setBoots(getEquipMap().getEquip(EquipmentMap.EquipmentType.BOOTS));
        return entity;
    }
}
