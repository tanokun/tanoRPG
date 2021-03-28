package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseMagmaCube extends ObjectEntity {
    private int size;

    public BaseMagmaCube(Config config) {
        super(config);
        size = config.getConfig().getInt("Options.size", - 1);
    }

    @Override
    public Entity spawn(Location location) {
        MagmaCube entity = (MagmaCube) location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE);
        setOptions(entity);
        ActiveEntity activeEntity = new ActiveEntity(this, entity);
        EntityManager.registerActiveEntity(activeEntity);
        return entity;
    }

    @Override
    public Entity setOptions(Entity entity) {
        MagmaCube target = (MagmaCube) entity;
        target.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7]");
        target.setCustomNameVisible(true);
        target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        if (this.size > 0)
            target.setSize(this.size);
        target.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        return entity;
    }
}
