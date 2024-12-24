package ru.magistu.siegemachines.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public class GiantArrow extends AbstractArrow {
    public GiantArrow(EntityType<GiantArrow> type, Level level)
    {
        super(type, level);
    }

	public GiantArrow(EntityType<GiantArrow> entitytype, Level level, Vector3d pos, LivingEntity entity, ItemStack item)
    {
		super(entitytype, entity, level,item,null);
        this.setPos(pos.x, pos.y, pos.z);
        this.setBaseDamage(5.0F);
	}

    @Override
    protected @NotNull ItemStack getDefaultPickupItem()
    {
        return new ItemStack(ModItems.GIANT_ARROW.get());
    }

}