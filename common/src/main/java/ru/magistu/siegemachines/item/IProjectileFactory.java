package ru.magistu.siegemachines.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;

public interface IProjectileFactory<T extends Projectile>
{
      T create(EntityType<T> entitytype, Level level, Vector3d pos, LivingEntity entity, ItemStack stack);
}
