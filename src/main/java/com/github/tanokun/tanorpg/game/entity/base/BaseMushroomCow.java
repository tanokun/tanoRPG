package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.entity.ActiveEntity;
import com.github.tanokun.tanorpg.game.entity.EntityManager;
import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;
import org.bukkit.metadata.FixedMetadataValue;

public class BaseMushroomCow extends ObjectEntity {

    public BaseMushroomCow(Config config) {
        super(config);
    }

    @Override
    public Entity spawn(Location location) {
        MushroomCow entity = (MushroomCow) location.getWorld().spawnEntity(location, EntityType.MUSHROOM_COW);
        setOptions(entity);
        ActiveEntity activeEntity = new ActiveEntity(this, entity);
        EntityManager.registerActiveEntity(activeEntity);
        return entity;
    }

    @Override
    public Entity setOptions(Entity entity) {
        MushroomCow target = (MushroomCow) entity;
        target.setCustomName(getName() + " §7[§dLv:§e" + getLEVEL() + "§7]");
        target.setCustomNameVisible(true);
        target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
        target.setMetadata("TanoRPG_entity", new FixedMetadataValue(TanoRPG.getPlugin(), true));
        return entity;
    }
}
