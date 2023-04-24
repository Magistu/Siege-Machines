package ru.itzme1on.siegemachines.entity.projectile.projectiles;

import com.google.common.base.Suppliers;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import ru.itzme1on.siegemachines.entity.projectile.Missile;
import ru.itzme1on.siegemachines.entity.projectile.MissileType;
import ru.itzme1on.siegemachines.item.ModItems;

import java.util.function.Supplier;

public class Cannonball extends Missile {
    public static final Supplier<EntityType<Cannonball>> TYPE = Suppliers.memoize(() -> EntityType.Builder.create((EntityType.EntityFactory<Cannonball>) Cannonball::new, SpawnGroup.MISC)
            .setDimensions(0.5F, 0.5F)
            .build("cannonball"));

    public Cannonball(EntityType<Cannonball> entityType, World world) {
        super(entityType, world);
        this.item = ModItems.CANNONBALL.get();
    }

    public Cannonball(EntityType<Cannonball> entityType, World world, Vector3d pos, LivingEntity entity, Item item) {
        super(entityType, world, pos, entity, MissileType.CANNONBALL, item);
    }
}
