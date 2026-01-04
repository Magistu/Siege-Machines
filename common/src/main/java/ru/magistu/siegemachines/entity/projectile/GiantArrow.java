package ru.magistu.siegemachines.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.phys.Vec3;
import ru.magistu.siegemachines.item.ModItems;
import ru.magistu.siegemachines.util.CombatUtil;

public class GiantArrow extends AbstractArrow {
    public GiantArrow(EntityType<GiantArrow> type, Level level) {
        super(type, level);
    }

    public GiantArrow(EntityType<GiantArrow> entitytype, Level level, Vec3 pos, LivingEntity shooter, Entity engine, ItemStack item) {
        super(entitytype, shooter.getControllingPassenger() != null ? shooter.getControllingPassenger() : shooter, level, item, null);
        this.setPos(pos.x, pos.y, pos.z);
        this.setBaseDamage(5.0F);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.GIANT_ARROW.get());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (CombatUtil.canHurt(this.getOwner(), result.getEntity())) {
            super.onHitEntity(result);
        }
    }
}