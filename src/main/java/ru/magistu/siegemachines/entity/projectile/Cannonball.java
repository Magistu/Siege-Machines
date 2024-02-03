package ru.magistu.siegemachines.entity.projectile;

import com.mojang.math.Vector3d;
import ru.magistu.siegemachines.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class Cannonball extends Missile
{
    public Cannonball(EntityType<Cannonball> type, Level level)
    {
        super(type, level);
        this.item = ModItems.CANNONBALL.get();
    }

	public Cannonball(EntityType<Cannonball> entitytype, Level level, Vector3d pos, LivingEntity entity, Item item)
    {
		super(entitytype, level, pos, entity, MissileType.CANNONBALL, item);
	}
}
