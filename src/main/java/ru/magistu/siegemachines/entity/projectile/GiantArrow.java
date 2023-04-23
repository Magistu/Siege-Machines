package ru.magistu.siegemachines.entity.projectile;

import com.mojang.math.Vector3d;
import ru.magistu.siegemachines.item.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class GiantArrow extends AbstractArrow
{
    private final Packet<?> spawningpacket = NetworkHooks.getEntitySpawningPacket(this);

    public GiantArrow(EntityType<GiantArrow> type, Level level)
    {
        super(type, level);
    }

	public GiantArrow(EntityType<GiantArrow> entitytype, Level level, Vector3d pos, LivingEntity entity, Item item)
    {
		super(entitytype, entity, level);
        this.setPos(pos.x, pos.y, pos.z);
        this.setBaseDamage(5.0F);
	}

    @Override
    protected @NotNull ItemStack getPickupItem()
    {
        return new ItemStack(ModItems.GIANT_ARROW.get());
    }

    @Override
	public @NotNull Packet<?> getAddEntityPacket()
    {
		return spawningpacket;
	}
}