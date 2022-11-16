package magistu.siegemachines.entity.projectile;

import magistu.siegemachines.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class GiantStone extends Missile
{
	public GiantStone(EntityType<GiantStone> entitytype, World level)
    {
		super(entitytype, level);
		this.item = ModItems.GIANT_STONE.get();
	}

	public GiantStone(EntityType<GiantStone> entitytype, World level, Vector3d pos, LivingEntity entity, Item item)
    {
		super(entitytype, level, pos, entity, MissileType.GIANT_STONE, item);
	}
}
