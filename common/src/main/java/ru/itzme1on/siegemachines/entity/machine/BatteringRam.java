package ru.itzme1on.siegemachines.entity.machine;

import com.google.common.base.Suppliers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import ru.itzme1on.siegemachines.SiegeMachines;
import ru.itzme1on.siegemachines.entity.Breakdown;
import ru.itzme1on.siegemachines.network.PacketMachineUse;
import ru.itzme1on.siegemachines.network.PacketMachineUseRealise;
import ru.itzme1on.siegemachines.item.ModItems;
import ru.itzme1on.siegemachines.audio.ModSounds;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Supplier;


public class BatteringRam extends Machine implements IAnimatable {
    public static final Supplier<EntityType<BatteringRam>> TYPE = Suppliers.memoize(() -> EntityType.Builder.create(BatteringRam::new, SpawnGroup.MISC)
            .setDimensions(4.0F, 3.0F)
            .build("battering_ram"));
    
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    static AnimationBuilder MOVING_ANIM = new AnimationBuilder().addAnimation("Moving", ILoopType.EDefaultLoopTypes.LOOP);
    static AnimationBuilder HITTING_ANIM = new AnimationBuilder().addAnimation("Hitting", ILoopType.EDefaultLoopTypes.LOOP);
    static AnimationBuilder RELOADING_ANIM = new AnimationBuilder().addAnimation("Reloading", ILoopType.EDefaultLoopTypes.LOOP);

    public int hittingTicks = 0;
    private int wheelsSoundTicks = 10;

    public enum State {
        HITTING,
        RELOADING
    }
    public State state = State.RELOADING;

    private double wheelsPitch = 0.0;
    private double wheelsSpeed = 0.0;

    public BatteringRam(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world, MachineType.BATTERING_RAM);
    }

    private <E extends IAnimatable> PlayState wheels_predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(MOVING_ANIM);

        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState reloading_predicate(AnimationEvent<E> event) {
        switch (state) {
            case HITTING -> {
                event.getController().setAnimation(HITTING_ANIM);
                return PlayState.CONTINUE;
            }
            case RELOADING -> {
                event.getController().setAnimation(RELOADING_ANIM);
                return PlayState.CONTINUE;
            }
        }
        
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<?> wheels_controller = new AnimationController<>(this, "wheels_controller", 1, (t) -> {
            double d = this.getWheelsSpeed();
            this.wheelsSpeed = d > 0 ? Math.min(d, 1.0) : Math.max(d, -1.0);
            return wheelsPitch += 0.015 * this.wheelsSpeed;
        }, this::wheels_predicate);
        data.addAnimationController(wheels_controller);

        AnimationController<?> reloading_controller = new AnimationController<>(this, "controller", 1, (t) -> {
            if (this.state.equals(State.RELOADING))
                return (double) (this.type.delayTime - this.delayTicks) / this.type.delayTime;
            
            return t;
        }, this::reloading_predicate);
        data.addAnimationController(reloading_controller);
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.world.isClient() && !this.hasPassengers()) {
            player.startRiding(this);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public void travel(Vec3d pos) {
        if (this.isAlive()) {
            if (this.hasPassengers()) {
                LivingEntity livingentity = (LivingEntity) this.getPrimaryPassenger();

                this.setYawDest(livingentity.getYaw());

                this.updateYaw();

                float f1 = livingentity.forwardSpeed;
                if (f1 <= 0.0f) f1 *= 0.25f;
                this.setMovementSpeed(0.04f);

                pos = new Vec3d(0.0f, pos.y, f1);
            }
            super.travel(pos);
        }
    }

    @Override
    public void tick() {
        if (this.useTicks != 0 && --this.useTicks <= 0) {
            this.state = State.RELOADING;
            this.useTicks = 0;
            this.delayTicks = this.type.delayTime;
        }

        if (this.hittingTicks != 0 && --this.hittingTicks <= 0) {
            this.useRealise();
            this.hittingTicks = 0;
        }

        if (!world.isClient() && this.isOnGround())
            this.setVelocity(this.getVelocity().multiply(0.0, 1.0, 0.0));

        if (this.delayTicks > 0 && this.hasPassengers()) --this.delayTicks;

        if (this.renderUpdateTicks-- <= 0) {
            this.updateMachineRender();
            this.renderUpdateTicks = SiegeMachines.RENDER_UPDATE_TIME;
        }

//        if (this.getWheelsSpeed() > 0.0081 && this.wheelsSoundTicks-- <= 0)
//        {
//            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundTypes.RAM_WHEELS.get(), SoundCategory.NEUTRAL, 0.6f, 1.0f, true);
//            this.wheelsSoundTicks = 20;
//        }

        super.tick();
    }

    @Override
    public void use(PlayerEntity player) {
        if (!this.world.isClient())
            PacketMachineUse.sendToAllAround(this);

        if (this.delayTicks <= 0 && this.useTicks <= 0 && this.hittingTicks <= 0) {
            this.state = State.HITTING;
            this.useTicks = this.type.useTime;
            this.hittingTicks = this.type.useRealiseTime;

            Vec3d pos = this.getHitPos();
            this.world.playSound(pos.x, pos.y, pos.z, ModSounds.RAM_HITTING.get(), SoundCategory.BLOCKS, 0.5f, 0.9f, false);
        }
    }

    public void ramHit(BlockPos blockpos) {
        if (!this.world.isClient()) {
            Breakdown breakdown = new Breakdown(this.world, this, this.getPrimaryPassenger(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), 2, false, 3.0f, Explosion.DestructionType.BREAK);
            breakdown.explode();
            breakdown.finalizeExplosion(true);
        }
    }

    @Override
    public void useRealise() {
        if (!this.world.isClient) {
            PacketMachineUseRealise.sendToAllAround(this);

            BlockPos blockpos = new BlockPos(this.getHitPos());
            this.ramHit(blockpos);
        }
    }

    public double getWheelsSpeed()
    {
        if (this.isOnGround())
        {
            return this.getRotationVec(5.0f)
                    .multiply(1, 0, 1).dotProduct(this.getVelocity());
        }

        return 0.0;
    }

    @Override
    public Item getMachineItem()
    {
        return ModItems.BATTERING_RAM.get();
    }

    protected Vec3d getHitPos() {
        double pitch = this.getTurretPitch() * Math.PI / 180.0;
        double yaw = (this.getYaw(0.5f) + this.getTurretYaw()) * Math.PI / 180.0;

        return this.getHitPos()
                        .add(applyRotation(this.type.turretPivot, 0.0, yaw)
                        .add(applyRotation(this.type.turretVector, pitch, yaw))
                );
    }
}
