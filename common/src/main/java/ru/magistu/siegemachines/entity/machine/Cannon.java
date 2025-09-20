package ru.magistu.siegemachines.entity.machine;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import ru.magistu.siegemachines.ModSoundTypes;
import ru.magistu.siegemachines.SiegeMachines;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;


public class Cannon extends ShootingMachine implements GeoEntity {
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public double wheelspitch = 0.0;
    public double lastwheelpitch;
    private int wheelssoundticks = 10;

    public Cannon(EntityType<? extends Mob> entitytype, Level level, MachineType type) {
        super(entitytype, level, type);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem().equals(Items.FLINT_AND_STEEL)) {
            if (getUseTicks() <= 0 && this.shootingticks <= 0) {
                stack.hurtAndBreak(1, player, getSlotForHand(hand));
                this.startShooting(player);
            }
            return InteractionResult.SUCCESS;
        }
        if (stack.getItem().equals(Items.GUNPOWDER)) {
            if (!this.inventory.containsItem(Items.GUNPOWDER) && this.inventory.canAddItem(stack)) {
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                this.inventory.addItem(stack);
            }
            return InteractionResult.SUCCESS;
        }
        if (super.mobInteract(player, hand) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }
        if (!this.level().isClientSide() && !this.isVehicle()) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void useRelease() {
        if (this.inventory.containsItem(Items.GUNPOWDER)) {
            super.useRelease();
        } else if (!this.level().isClientSide()) {
            Entity passenger = this.getControllingPassenger();
            if (passenger instanceof Player) {
                passenger.sendSystemMessage(Component.translatable(SiegeMachines.ID + ".no_gunpowder").withStyle(ChatFormatting.RED));
            }
        }
    }

    @Override
    public void travel(Vec3 velocity) {
        if (this.isAlive()) {
            if (getDelayTicks() <= 0 && this.isVehicle()) {
                LivingEntity livingentity = this.getControllingPassenger();

                float f0 = livingentity.xxa * 0.2f;
                float f1 = livingentity.zza;
                if (f1 <= 0.0f) {
                    f1 *= 0.5f;
                }
                this.setSpeed(0.04f);

                velocity = new Vec3(f0, velocity.y, f1);
            }
            super.travel(velocity);
        }
    }

    @Override
    public void tick() {
        lastwheelpitch = wheelspitch;
        wheelspitch += this.getWheelsSpeed();

        if (this.level().isClientSide() && this.hasControllingPassenger() && this.getWheelsSpeed() > 0.0081 && this.wheelssoundticks-- <= 0) {
            Entity passenger = this.getControllingPassenger();
            if (Minecraft.getInstance().player == passenger) {
                Vec3 pos = this.position();
                this.level().playLocalSound(pos.x, pos.y, pos.z, ModSoundTypes.CANNON_WHEELS.get(), this.getSoundSource(), 1.5f, 0.85f + this.level().random.nextFloat() * 0.3f, false);
                this.wheelssoundticks = 20;
            }
        }

        if (!this.level().isClientSide() && this.onGround()) {
            this.setDeltaMovement(this.getWheelsDeltaMovement());
        }

        super.tick();
    }

    public double getLerpedWheelPitch(float partialTick) {
        return Mth.lerp(partialTick, lastwheelpitch, wheelspitch);
    }

    @Override
    public void startShooting(LivingEntity entity) {
        if (getDelayTicks() <= 0 && getUseTicks() <= 0 && this.shootingticks <= 0) {
            this.usesoundplayer.run();
            setUseTicks(this.type.usetime);
            this.shootingticks = this.type.usereleasetime;
        }
    }

    @Override
    public void shoot() {
        if (!level().isClientSide()) {
            super.shoot();
            this.setDeltaMovement(this.getDeltaMovement().subtract(this.getShotView().scale(0.25)));
            this.hasImpulse = true;
            this.inventory.removeItemType(Items.GUNPOWDER, 1);
        } else {
            this.blowParticles(ParticleTypes.FLAME, 0.035, 25);
            this.blowParticles(ParticleTypes.CLOUD, 0.2, 60);
        }
    }

    public double getWheelsSpeed() {
        if (this.onGround()) {
            return this.getViewVector(.5f).multiply(1, 0, 1).dot(this.getDeltaMovement());
        }

        return 0.0;
    }

    public Vec3 getWheelsDeltaMovement() {
        if (this.onGround()) {
            Vec3 view = this.getViewVector(1.0f);
            Vec3 movement = this.getDeltaMovement();

            double d0 = movement.x * view.x + movement.z * view.z;

            double d1 = d0 * view.x;
            double d2 = 0.0;
            double d3 = d0 * view.z;

            return new Vec3(d1, d2, d3);
        }

        return Vec3.ZERO;
    }
}
