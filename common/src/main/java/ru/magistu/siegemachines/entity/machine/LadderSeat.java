package ru.magistu.siegemachines.entity.machine;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import ru.magistu.siegemachines.entity.ModEntityTypes;


public class LadderSeat extends Seat {
    public final float climbspeed = 0.02f;
    public final SiegeLadder parent;
    private static final EntityDataAccessor<Float> DATA_HIGHNESS = SynchedEntityData.defineId(LadderSeat.class, EntityDataSerializers.FLOAT);

    public LadderSeat(EntityType<LadderSeat> type, Level level) {
        super(type, level);
        parent = null;
    }

    public LadderSeat(SiegeLadder parent) {
        super(ModEntityTypes.LADDER_SEAT.get(), parent.level());
        this.parent = parent;
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_HIGHNESS.equals(accessor)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(accessor);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_HIGHNESS, 0.0f);
    }

    public float getHighness() {
        return entityData.get(DATA_HIGHNESS);
    }

    public void setHighness(float highness) {
        entityData.set(DATA_HIGHNESS, highness);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        InteractionResult result = super.interact(player, hand);
        if (result.consumesAction())
            return result;

        if (player.isSecondaryUseActive())
            return InteractionResult.PASS;
        else if (this.isVehicle())
            return InteractionResult.PASS;
        else if (!this.level().isClientSide)
            return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        else
            return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("unused")
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    protected void removePassenger(Entity entity) {
        if (!level().isClientSide){
            this.setHighness(0.0f);
        }
        super.removePassenger(entity);
    }

    public float climb() {
        if (this.getFirstPassenger() instanceof LivingEntity livingentity) {
            if (livingentity.zza < -this.climbspeed && this.getHighness() >= this.climbspeed)
                this.setHighness(this.getHighness() - this.climbspeed);
            if (livingentity.zza > this.climbspeed && this.getHighness() <= 1.0f - this.climbspeed)
                this.setHighness(this.getHighness() + this.climbspeed);
        }

        return this.getHighness();
    }
}
