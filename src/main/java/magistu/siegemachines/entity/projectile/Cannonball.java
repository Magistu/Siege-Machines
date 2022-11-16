package magistu.siegemachines.entity.projectile;

import magistu.siegemachines.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class Cannonball extends Missile
{
    public Cannonball(EntityType<Cannonball> type, World level)
    {
        super(type, level);
        this.item = ModItems.CANNONBALL.get();
    }

	public Cannonball(EntityType<Stone> entitytype, World level, Vector3d pos, LivingEntity entity, Item item)
    {
		super(entitytype, level, pos, entity, MissileType.CANNONBALL, item);
	}
}
