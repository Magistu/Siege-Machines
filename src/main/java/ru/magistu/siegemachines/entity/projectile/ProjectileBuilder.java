package ru.magistu.siegemachines.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;
import ru.magistu.siegemachines.entity.ModEntityTypes;
import ru.magistu.siegemachines.item.ModItems;


public class ProjectileBuilder<T extends Projectile> {
    public final static ProjectileBuilder<Stone> NONE = new ProjectileBuilder<>(Items.AIR, ModEntityTypes.STONE.get(), (entitytype1, level, pos, shooter, engine, item1) -> new Stone(entitytype1, level, pos, shooter, engine));

    public final static ProjectileBuilder<?>[] NO_AMMO = new ProjectileBuilder[]{};
    public final static ProjectileBuilder<?>[] GIANT_THROWING_AMMO = new ProjectileBuilder[]{
            new ProjectileBuilder<>(Items.COBBLESTONE, ModItems.GIANT_STONE.get(), ModEntityTypes.GIANT_STONE.get(), (entitytype1, level, pos, shooter, engine, stack) -> new GiantStone(entitytype1, level, pos, shooter, engine))};
    public final static ProjectileBuilder<?>[] CANNON_AMMO = new ProjectileBuilder[]{
            new ProjectileBuilder<>(ModItems.CANNONBALL.get(), ModEntityTypes.CANNONBALL.get(), (entitytype1, level, pos, shooter, engine, stack) -> new Cannonball(entitytype1, level, pos, shooter, engine))};
    public final static ProjectileBuilder<?>[] THROWING_AMMO = new ProjectileBuilder[]{
            new ProjectileBuilder<>(Items.COBBLESTONE, ModItems.STONE.get(), ModEntityTypes.STONE.get(), (entitytype1, level, pos, shooter, engine, item1) -> new Stone(entitytype1, level, pos, shooter, engine))};
    public final static ProjectileBuilder<?>[] BALLISTA_AMMO = new ProjectileBuilder[]{
            new ProjectileBuilder<>(ModItems.GIANT_ARROW.get(), ModEntityTypes.GIANT_ARROW.get(), GiantArrow::new),
            new ProjectileBuilder<>(Items.ARROW, EntityType.ARROW, (entitytype, level, pos, shooter, entity, stack) -> {
                if (shooter.getControllingPassenger() != null) {
                    shooter = shooter.getControllingPassenger();
                }
                Arrow arrow = new Arrow(level, shooter);
                arrow.setPos(pos.x, pos.y, pos.z);
                return arrow;
            })};

    public final Item item;
    public final Item projectilitem;
    public final EntityType<T> entitytype;
    public final ProjectileFactory<T> factory;

    public ProjectileBuilder(Item item, EntityType<T> entitytype, ProjectileFactory<T> factory) {
        this(item, item, entitytype, factory);
    }

    public ProjectileBuilder(Item item, Item projectilitem, EntityType<T> entitytype, ProjectileFactory<T> factory) {
        this.item = item;
        this.projectilitem = projectilitem;
        this.entitytype = entitytype;
        this.factory = factory;
    }

    public T build(Level level, Vector3d pos, LivingEntity shooter, Entity engine) {
        return this.factory.create(this.entitytype, level, pos, shooter, engine, new ItemStack(item));
    }
}
