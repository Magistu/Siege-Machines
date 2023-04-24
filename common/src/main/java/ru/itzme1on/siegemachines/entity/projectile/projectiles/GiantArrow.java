package ru.itzme1on.siegemachines.entity.projectile.projectiles;

import com.google.common.base.Suppliers;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.itzme1on.siegemachines.item.ModItems;

import java.util.function.Supplier;

public class GiantArrow extends PersistentProjectileEntity {
    public static final Supplier<EntityType<GiantArrow>> TYPE = Suppliers.memoize(() -> EntityType.Builder.create((EntityType.EntityFactory<GiantArrow>) GiantArrow::new, SpawnGroup.MISC)
            .maxTrackingRange(4)
            .trackingTickInterval(20)
            .setDimensions(0.5F, 0.5F)
            .build("giant_arrow"));
    
    public GiantArrow(EntityType<GiantArrow> entityType, World world) {
        super(entityType, world);
    }

    public GiantArrow(EntityType<GiantArrow> entityType, World world, Vector3d pos, LivingEntity entity, Item item) {
        super(entityType, entity, world);
        this.setPosition(pos.x, pos.y, pos.z);
        this.setDamage(5.0F);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.GIANT_ARROW.get());
    }
}
