package ru.magistu.siegemachines.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public interface IProjectileFactory<T extends Projectile>
{
      T create(EntityType<T> entitytype, Level level, org.joml.Vector3d pos, LivingEntity entity, Item item);
}
