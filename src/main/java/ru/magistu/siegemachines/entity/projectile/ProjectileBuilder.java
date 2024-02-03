package ru.magistu.siegemachines.entity.projectile;

import com.mojang.math.Vector3d;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import ru.magistu.siegemachines.entity.EntityTypes;
import ru.magistu.siegemachines.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;


public class ProjectileBuilder<T extends Projectile>
{
    public final static ProjectileBuilder<Stone> NONE = new ProjectileBuilder<>(Items.AIR, EntityTypes.STONE.get(), Stone::new);

    public final static ProjectileBuilder<?>[] NO_AMMO = new ProjectileBuilder[]{};
    public final static ProjectileBuilder<?>[] GIANT_THROWING_AMMO = new ProjectileBuilder[]{
            new ProjectileBuilder<>(Items.COBBLESTONE, ModItems.GIANT_STONE.get(), EntityTypes.GIANT_STONE.get(), GiantStone::new)};
    public final static ProjectileBuilder<?>[] CANNON_AMMO = new ProjectileBuilder[]{
            new ProjectileBuilder<>(ModItems.CANNONBALL.get(), EntityTypes.CANNONBALL.get(), Cannonball::new)};
    public final static ProjectileBuilder<?>[] THROWING_AMMO = new ProjectileBuilder[]{
            new ProjectileBuilder<>(Items.COBBLESTONE, ModItems.STONE.get(), EntityTypes.STONE.get(), Stone::new)};
    public final static ProjectileBuilder<?>[] BALLISTA_AMMO = new ProjectileBuilder[]{
            new ProjectileBuilder<>(ModItems.GIANT_ARROW.get(), EntityTypes.GIANT_ARROW.get(), GiantArrow::new),
            new ProjectileBuilder<>(Items.ARROW, EntityType.ARROW, (entitytype, level, pos, entity, item) ->
            {
                Arrow arrow = new Arrow(level, entity);
                arrow.setPos(pos.x, pos.y, pos.z);
                return arrow;
            })};

    public final Item item;
    public final Item projectilitem;
    public final EntityType<T> entitytype;
    public final IProjectileFactory<T> factory;

    public ProjectileBuilder(Item item, EntityType<T> entitytype, IProjectileFactory<T> factory)
    {
        this(item, item, entitytype, factory);
    }

    public ProjectileBuilder(Item item, Item projectilitem, EntityType<T> entitytype, IProjectileFactory<T> factory)
    {
        this.item = item;
        this.projectilitem = projectilitem;
        this.entitytype = entitytype;
        this.factory = factory;
    }
    
    public T build(Level level, Vector3d pos, LivingEntity entity)
    {
        return this.factory.create(this.entitytype, level, pos, entity, this.item);
    }
}
