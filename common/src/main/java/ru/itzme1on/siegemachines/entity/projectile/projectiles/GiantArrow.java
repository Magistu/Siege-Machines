package ru.itzme1on.siegemachines.entity.projectile.projectiles;

import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.itzme1on.siegemachines.registry.ItemRegistry;

public class GiantArrow extends PersistentProjectileEntity {
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
        return new ItemStack(ItemRegistry.GIANT_ARROW.get());
    }
}
