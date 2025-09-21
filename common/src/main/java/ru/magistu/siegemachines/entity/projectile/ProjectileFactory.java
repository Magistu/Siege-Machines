package ru.magistu.siegemachines.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;

public interface ProjectileFactory<T extends Projectile> {
    T create(EntityType<T> entitytype, Level level, Vector3d pos, LivingEntity shooter, Entity engine, ItemStack stack);
}
