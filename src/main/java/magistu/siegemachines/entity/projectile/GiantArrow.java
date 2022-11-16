package magistu.siegemachines.entity.projectile;

import magistu.siegemachines.item.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class GiantArrow extends AbstractArrowEntity
{
    private final IPacket<?> spawningpacket = NetworkHooks.getEntitySpawningPacket(this);

    public GiantArrow(EntityType<GiantArrow> type, World level)
    {
        super(type, level);
    }

	public GiantArrow(EntityType<GiantArrow> entitytype, World level, Vector3d pos, LivingEntity entity, Item item)
    {
		super(entitytype, entity, level);
        this.setPos(pos.x, pos.y, pos.z);
        this.setBaseDamage(15.0f);
	}

    @Override
    protected @NotNull ItemStack getPickupItem()
    {
        return new ItemStack(ModItems.GIANT_ARROW.get());
    }

    @Override
	public @NotNull IPacket<?> getAddEntityPacket()
    {
		return spawningpacket;
	}
}