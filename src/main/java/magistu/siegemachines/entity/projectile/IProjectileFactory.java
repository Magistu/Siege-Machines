package magistu.siegemachines.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public interface IProjectileFactory<T extends ProjectileEntity>
{
      T create(EntityType<T> entitytype, World level, Vector3d pos, LivingEntity entity, Item item);
}
