package ru.magistu.siegemachines.entity.machine;

import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import ru.magistu.siegemachines.ModSoundTypes;
import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import ru.magistu.siegemachines.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import ru.magistu.siegemachines.network.C2SPacketMachineUse;
import ru.magistu.siegemachines.network.PacketMachineControl;
import ru.magistu.siegemachines.platform.Services;
import ru.magistu.siegemachines.util.BaseAnimations;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Mortar extends ShootingMachine implements GeoEntity {
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public int shootingticks = 0;

    public double wheelspitch = 0.0;
    public double lastwheelpitch;
    private int wheelssoundticks = 10;

    public Mortar(EntityType<? extends Mob> entitytype, Level level)
    {
        super(entitytype, level, MachineType.MORTAR);
    }

    private <E extends GeoAnimatable> PlayState wheels_predicate(AnimationState<E> event)
    {
      //  if (event.isMoving()) {
     //       event.getController().setAnimation(BaseAnimations.MOVING_ANIM);
    //        ((CustomAnimationController<Mortar>) event.getController()).setAnimationState(AnimationController.State.RUNNING);
    //    } else {
    //        ((CustomAnimationController<Mortar>) event.getController()).setAnimationState(AnimationController.State.PAUSED);
   //     }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data)
    {
        AnimationController<Mortar> wheels_controller = new CustomAnimationController<>(this, "wheels_controller", 1, this::wheels_predicate);
        data.add(wheels_controller);
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache()
    {
        return this.factory;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem().equals(Items.FLINT_AND_STEEL))
        {
            if (getUseTicks() <= 0 && this.shootingticks <= 0)
            {
                stack.hurtAndBreak(1, player, getSlotForHand(hand));
                this.startShooting(player);
            }
            return InteractionResult.SUCCESS;
        }
        if (stack.getItem().equals(Items.GUNPOWDER))
        {
            if (!this.inventory.containsItem(Items.GUNPOWDER))
            {
                if (!player.isCreative())
                {
                    stack.shrink(1);
                }
                this.inventory.putItem(Items.GUNPOWDER);
            }
            return InteractionResult.SUCCESS;
        }
        if (super.mobInteract(player, hand) == InteractionResult.SUCCESS)
        {
            return InteractionResult.SUCCESS;
        }
        if (!this.level().isClientSide() && !this.isVehicle())
        {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }


    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive())
        {
            if (this.isVehicle())
            {
                LivingEntity livingentity = this.getControllingPassenger();

                this.setTurretRotationsDest(livingentity.getXRot(), livingentity.getYRot() - this.getYaw());
                this.setYawDest(livingentity.getYRot());

                this.updateYaw();
                this.updateTurretRotations();
            }
        }
    }

    @Override
    public void tick()
    {

        lastwheelpitch = wheelspitch;
        wheelspitch += this.getWheelsSpeed();

        int useticks = getUseTicks();
        if (useticks >0) {
            setUseTicks(--useticks);
            if (useticks == 0) {
                setDelayTicks(this.type.specs.delaytime.get());
            }
        }

        if (this.shootingticks != 0 && --this.shootingticks <= 0)
        {
            if (this.inventory.containsItem(Items.GUNPOWDER))
            {
                this.useRealise();
            }
            else if (!this.level().isClientSide())
            {
                Entity passenger = this.getControllingPassenger();
                if (passenger instanceof Player)
                {
                    passenger.sendSystemMessage(Component.translatable(SiegeMachines.ID + ".no_gunpowder").withStyle(ChatFormatting.RED));
                }
            }
            this.shootingticks = 0;
        }

        if (!level().isClientSide() && this.onGround())
        {
            this.setDeltaMovement(this.getWheelsDeltaMovement());
        }

        if (getDelayTicks() > 0 && this.isVehicle())
        {
            setDelayTicks(getDelayTicks() -1);
        }

        if (this.renderupdateticks-- <= 0)
        {
            this.updateMachineRender();
            this.renderupdateticks = SiegeMachines.RENDER_UPDATE_TIME;
        }

        if (this.level().isClientSide() && this.hasControllingPassenger() && this.getWheelsSpeed() > 0.0081 && this.wheelssoundticks-- <= 0)
        {
            Entity passenger = this.getControllingPassenger();
            if (Minecraft.getInstance().player == passenger)
            {
                Vec3 pos = this.position();
                this.level().playLocalSound(pos.x, pos.y, pos.z, ModSoundTypes.CANNON_WHEELS.get(), this.getSoundSource(), 1.5f, 0.85f + this.level().random.nextFloat() * 0.3f, false);
                this.wheelssoundticks = 20;
            }
        }

        super.tick();
    }

    public double getLerpedWheelPitch(float partialTick) {
        return Mth.lerp(partialTick,lastwheelpitch,wheelspitch);
    }

    @Override
    public void startShooting(Player player)
    {
        if (getDelayTicks() <= 0 && getUseTicks() <= 0 && this.shootingticks <= 0)
        {
            if (!this.level().isClientSide())
            {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSoundTypes.FUSE.get(), this.getSoundSource(), this.getVolumeFromDist(this.distanceTo(player)), 0.8f);
            }
            setUseTicks(this.type.usetime);
            this.shootingticks = this.type.userealisetime;
        }
    }

    @Override
    public void shoot()
    {
        if (!level().isClientSide())
        {
            super.shoot();
            this.setDeltaMovement(this.getDeltaMovement().subtract(this.getShotView().scale(0.25)));
            this.hasImpulse = true;
            this.inventory.shrinkItem(Items.GUNPOWDER);
        }
        else
        {
            this.blowParticles(ParticleTypes.FLAME, 0.035, 25);
            this.blowParticles(ParticleTypes.CLOUD, 0.2, 60);
            Vec3 pos = this.position();
            this.level().playLocalSound(pos.x, pos.y, pos.z, ModSoundTypes.MORTAR_SHOOTING.get(), this.getSoundSource(), 0.3f, 1.0f, false);
        }
    }

    public double getWheelsSpeed()
    {
        if (this.onGround())
        {
            return this.getViewVector(.5f).multiply(1, 0, 1).dot(this.getDeltaMovement());
        }

        return 0.0;
    }

    public Vec3 getWheelsDeltaMovement()
    {
        if (this.onGround())
        {
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

    @Override
    public Item getMachineItem()
    {
        return ModItems.MORTAR.get();
    }
}