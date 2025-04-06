package ru.magistu.siegemachines.entity.machine;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
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
        this.setHighness(0.0f);
        super.removePassenger(entity);
    }

    public void climb(boolean upwards) {
//        System.out.println(Stream.of(this.getHighness(), upwards, level().isClientSide).map(String::valueOf).collect(Collectors.joining(", ")));
        if (upwards) {
           if (this.getHighness() <= 1.0f - this.climbspeed) {
               this.setHighness(this.getHighness() + this.climbspeed);
           }
        } else if (this.getHighness() >= this.climbspeed) {
            this.setHighness(this.getHighness() - this.climbspeed);
        }
    }
}
