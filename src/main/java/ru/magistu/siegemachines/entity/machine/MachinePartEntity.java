package ru.magistu.siegemachines.entity.machine;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.NotNull;

public class MachinePartEntity extends PartEntity<Machine>
{
    public final Machine parentmob;
    public final String name;
    private final EntityDimensions size;

    public MachinePartEntity(Machine parent, String name, float width, float height)
    {
        super(parent);
        this.size = EntityDimensions.scalable(width, height);
        this.refreshDimensions();
        this.parentmob = parent;
        this.name = name;
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag p_70037_1_) {}

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag p_213281_1_) {}

    public boolean hurt(@NotNull DamageSource source, float value)
    {
        return !this.isInvulnerableTo(source) && this.parentmob.hurt(source, value);
    }

    public boolean is(@NotNull Entity entity)
    {
        return this == entity || this.parentmob == entity;
    }

    public Packet<?> getAddEntityPacket()
    {
        throw new UnsupportedOperationException();
    }

    public @NotNull EntityDimensions getDimensions(@NotNull Pose p_213305_1_)
    {
        return this.size;
    }
}
