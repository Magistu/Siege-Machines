package magistu.siegemachines.entity.projectile;

import magistu.siegemachines.entity.EntityTypes;
import magistu.siegemachines.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;


public class ProjectileBuilder<T extends ProjectileEntity>
{
    public final static ProjectileBuilder<Stone> NONE = new ProjectileBuilder<>(Items.AIR, EntityTypes.STONE.get(), Stone::new);

    public final static ProjectileBuilder[] NO_AMMO = new ProjectileBuilder[]{};
    public final static ProjectileBuilder[] CANNON_AMMO = new ProjectileBuilder[]{
            new ProjectileBuilder(ModItems.CANNONBALL.get(), EntityTypes.CANNONBALL.get(), Cannonball::new)};
    public final static ProjectileBuilder[] GIANT_THROWING_AMMO = new ProjectileBuilder[]{
            new ProjectileBuilder(Items.COBBLESTONE, ModItems.GIANT_STONE.get(), EntityTypes.GIANT_STONE.get(), GiantStone::new)};
    public final static ProjectileBuilder[] THROWING_AMMO = new ProjectileBuilder[]{
            new ProjectileBuilder(Items.COBBLESTONE, ModItems.STONE.get(), EntityTypes.STONE.get(), Stone::new)};
    public final static ProjectileBuilder[] BALLISTA_AMMO = new ProjectileBuilder[]{
            new ProjectileBuilder(ModItems.GIANT_ARROW.get(), EntityTypes.GIANT_ARROW.get(), GiantArrow::new),
            new ProjectileBuilder(Items.ARROW, EntityType.ARROW, (entitytype, level, pos, entity, item) ->
            {
                ArrowEntity arrow = new ArrowEntity(level, entity);
                arrow.setPos(pos.x, pos.y, pos.z);
                return arrow;
            })};

    public final Item item;
    public final Item projectileitem;
    public final EntityType<T> entitytype;
    public final IProjectileFactory<T> factory;

    public ProjectileBuilder(Item item, EntityType<T> entitytype, IProjectileFactory<T> factory)
    {
        this(item, item, entitytype, factory);
    }

    public ProjectileBuilder(Item item, Item projectileitem, EntityType<T> entitytype, IProjectileFactory<T> factory)
    {
        this.item = item;
        this.projectileitem = projectileitem;
        this.entitytype = entitytype;
        this.factory = factory;
    }


}
