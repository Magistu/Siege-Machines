package ru.magistu.siegemachines.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;

public class Cannonball extends Missile
{
    public Cannonball(EntityType<Cannonball> type, Level level)
    {
        super(type, level);
    }

	public Cannonball(EntityType<Cannonball> entitytype, Level level, Vector3d pos, LivingEntity entity)
    {
		super(entitytype, level, pos, entity, MissileType.CANNONBALL);
	}

    @Override
    protected Item getDefaultItem() {
        return ModItems.CANNONBALL.get();
    }
}
