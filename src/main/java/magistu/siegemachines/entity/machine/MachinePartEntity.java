package magistu.siegemachines.entity.machine;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import org.jetbrains.annotations.NotNull;

public class MachinePartEntity extends net.minecraftforge.entity.PartEntity<Machine>
{
    public final Machine parentmob;
    public final String name;
    private final EntitySize size;

    public MachinePartEntity(Machine parent, String name, float width, float height)
    {
        super(parent);
        this.size = EntitySize.scalable(width, height);
        this.refreshDimensions();
        this.parentmob = parent;
        this.name = name;
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundNBT p_70037_1_) {}

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundNBT p_213281_1_) {}

    public boolean hurt(@NotNull DamageSource source, float value)
    {
        return !this.isInvulnerableTo(source) && this.parentmob.hurt(source, value);
    }

    public boolean is(@NotNull Entity entity)
    {
        return this == entity || this.parentmob == entity;
    }

    public IPacket<?> getAddEntityPacket()
    {
        throw new UnsupportedOperationException();
    }

    public @NotNull EntitySize getDimensions(@NotNull Pose p_213305_1_)
    {
        return this.size;
    }
}
