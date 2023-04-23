package ru.itzme1on.siegemachines.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import ru.itzme1on.siegemachines.entity.projectile.projectiles.Cannonball;
import ru.itzme1on.siegemachines.entity.projectile.projectiles.GiantArrow;
import ru.itzme1on.siegemachines.entity.projectile.projectiles.Stone;
import ru.itzme1on.siegemachines.registry.EntityRegistry;
import ru.itzme1on.siegemachines.registry.ItemRegistry;

public class ProjectileBuilder<T extends ProjectileEntity> {
    public final static ProjectileBuilder<Stone> NONE = new ProjectileBuilder<>(Items.AIR, EntityRegistry.STONE.get(), Stone::new);

    public final static ProjectileBuilder<?>[] NO_AMMO = new ProjectileBuilder[] {};
    public final static ProjectileBuilder<?>[] CANNON_AMMO = new ProjectileBuilder[] {
            new ProjectileBuilder<>(ItemRegistry.CANNONBALL.get(), EntityRegistry.CANNONBALL.get(), Cannonball::new)
    };
    public final static ProjectileBuilder<?>[] THROWING_AMMO = new ProjectileBuilder[] {
            new ProjectileBuilder<>(Items.COBBLESTONE, ItemRegistry.STONE.get(), EntityRegistry.STONE.get(), Stone::new)
    };

    public final static ProjectileBuilder<?>[] BALLISTA_AMMO = new ProjectileBuilder[] {
            new ProjectileBuilder<>(ItemRegistry.GIANT_ARROW.get(), EntityRegistry.GIANT_ARROW.get(), GiantArrow::new),
            new ProjectileBuilder<>(Items.ARROW, EntityType.ARROW, (entityType, world, pos, entity, item) -> {
                ArrowEntity arrow = new ArrowEntity(world, entity);
                arrow.setPos(pos.x, pos.y, pos.z);
                return arrow;
            })
    };

    public final Item item;
    public final Item projectileItem;
    public final EntityType<T> entityType;
    public final IProjectileFactory<T> factory;

    public ProjectileBuilder(Item item, EntityType<T> entityType, IProjectileFactory<T> factory) {
        this(item, item, entityType, factory);
    }

    public ProjectileBuilder(Item item, Item projectileItem, EntityType<T> entityType, IProjectileFactory<T> factory) {
        this.item = item;
        this.projectileItem = projectileItem;
        this.entityType = entityType;
        this.factory = factory;
    }
}
