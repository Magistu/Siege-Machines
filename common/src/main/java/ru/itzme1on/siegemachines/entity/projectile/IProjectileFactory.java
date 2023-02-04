package ru.itzme1on.siegemachines.entity.projectile;

import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public interface IProjectileFactory<T extends ProjectileEntity> {
    T create(EntityType<T> entityType, World world, Vector3d pos, LivingEntity entity, Item item);
}
