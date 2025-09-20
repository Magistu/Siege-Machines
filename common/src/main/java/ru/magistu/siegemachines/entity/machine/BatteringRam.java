package ru.magistu.siegemachines.entity.machine;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import ru.magistu.siegemachines.client.ModSoundTypes;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.network.ModNetwork;
import ru.magistu.siegemachines.network.S2CPacketMachineUse;
import ru.magistu.siegemachines.util.BaseAnimations;
import ru.magistu.siegemachines.util.CartesianGeometry;
import ru.magistu.siegemachines.util.HitUtil;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;


public class BatteringRam extends Machine implements MachineGeoEntity {
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public int hittingticks = 0;
    private int wheelssoundticks = 10;
    public double lastwheelpitch;

    public enum State {
        HITTING,
        RELOADING
    }

    public State state = State.RELOADING;

    private double wheelspitch = 0.0;

    public BatteringRam(EntityType<? extends Mob> entitytype, Level level) {
        super(entitytype, level, MachineType.BATTERING_RAM);
    }


    @Override
    public RawAnimation getUsingRawAnimation() {
        return BaseAnimations.HITTING_ANIM;
    }

    @Override
    public RawAnimation getReloadingAnimation() {
        return BaseAnimations.RELOADING_ANIM;
    }

    @Override
    public int getDelayTime() {
        return type.specs.delaytime.get();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level().isClientSide() && !this.isVehicle()) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void travel(Vec3 velocity) {
        if (this.isAlive()) {
            if (this.isVehicle()) {
                LivingEntity livingentity = this.getControllingPassenger();

                float f1 = livingentity.zza;
                if (f1 <= 0.0f) {
                    f1 *= 0.25f;
                }
                this.setSpeed(0.04f);

                velocity = new Vec3(0.0f, velocity.y, f1);
            }
            super.travel(velocity);
        }
    }

    @Override
    public void tick() {
        this.lastwheelpitch = this.wheelspitch;
        this.wheelspitch += this.getWheelsSpeed();

        if (this.hittingticks != 0 && --this.hittingticks <= 0) {
            this.useRelease();
            this.hittingticks = 0;
        }

        if (this.level().isClientSide() && this.hasControllingPassenger() && this.getWheelsSpeed() > 0.0081 && this.wheelssoundticks-- <= 0) {
            Entity passenger = this.getControllingPassenger();
            if (Minecraft.getInstance().player == passenger) {
                Vec3 pos = this.position();
                this.level().playLocalSound(pos.x, pos.y, pos.z, ModSoundTypes.RAM_WHEELS.get(), this.getSoundSource(), 1.5f, 0.85f + this.level().random.nextFloat() * 0.3f, false);
                this.wheelssoundticks = 20;
            }
        }

        super.tick();
    }

    public double getLerpedWheelPitch(float partialTick) {
        return Mth.lerp(partialTick, lastwheelpitch, wheelspitch);
    }

    @Override
    public void use(LivingEntity entity) {
        if (this.deploymentticks > 0 && entity instanceof Player player) {
            player.sendSystemMessage(Component.translatable(SiegeMachines.ID + ".wait", this.deploymentticks / 20.0f).withStyle(ChatFormatting.RED));
            return;
        }

        if (!this.level().isClientSide()) {
            ModNetwork.sendPacketToAllInArea((ServerLevel) level(), new S2CPacketMachineUse(this.getId()), this.blockPosition(), SiegeMachines.RENDER_UPDATE_RANGE_SQR);
        }

        if (getDelayTicks() <= 0 && getUseTicks() <= 0 && this.hittingticks <= 0) {
            this.usesoundplayer.run();
            this.state = State.HITTING;
            setUseTicks(type.usetime);
            this.hittingticks = this.type.usereleasetime;
        }
    }

    public void ramHit(BlockPos blockpos) {
        if (!this.level().isClientSide()) {
            Explosion explosion = new Explosion(this.level(), this,
                    blockpos.getX(), blockpos.getY(), blockpos.getZ(), 2, false, Explosion.BlockInteraction.DESTROY);
            explosion.explode();
            explosion.finalizeExplosion(true);
        }
    }

    @Override
    public void useRelease() {
        if (this.deploymentticks > 0)
            return;

        this.usereleasesoundplayer.run();
        if (!this.level().isClientSide()) {
            level().broadcastEntityEvent(this, (byte) USE_RELEASE);
            BlockPos blockpos = BlockPos.containing(this.getHitPos());
            this.ramHit(blockpos);
        }
    }

    private Vec3 getHitPos() {
        double pitch = this.getTurretPitch() * Math.PI / 180.0;
        double yaw = (this.getViewYRot(0.5f) + this.getTurretYaw()) * Math.PI / 180.0;
        Vec3 pos = this.position().add(CartesianGeometry.applyRotations(this.type.turretpivot, 0.0, yaw));
        Vec3 delta = CartesianGeometry.applyRotations(this.type.turretvector, pitch, yaw);
        HitResult hit = HitUtil.getHitResult(pos, this, e -> e.getVehicle() != this, delta, this.level(), 0.0f, ClipContext.Block.COLLIDER);
        if (hit.getType() != HitResult.Type.MISS) {
            return hit.getLocation();
        }
        return pos.add(delta);
    }

    public double getWheelsSpeed() {
        if (this.onGround()) {
            return this.getViewVector(5.0f).multiply(1, 0, 1).dot(this.getDeltaMovement());
        }

        return 0.0;
    }

    @Override
    public void push(double p_70024_1_, double p_70024_3_, double p_70024_5_) {
    }

    public UsageType getUsage() {
        return UsageType.RAM;
    }
}
