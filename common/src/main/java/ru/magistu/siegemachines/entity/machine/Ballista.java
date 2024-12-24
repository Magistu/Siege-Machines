package ru.magistu.siegemachines.entity.machine;

import ru.magistu.siegemachines.ModSoundTypes;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.item.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import ru.magistu.siegemachines.util.BaseAnimations;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Ballista extends ShootingMachine implements GeoEntity
{
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);


    public enum State
    {
        SHOOTING,
        RELOADING
    }
    public State state = State.RELOADING;

    public Ballista(EntityType<? extends Mob> entitytype, Level level)
    {
        super(entitytype, level, MachineType.BALLISTA);
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> event)
    {
        switch (state) {
            case SHOOTING -> {
                event.getController().setAnimation(BaseAnimations.SHOOTING_ANIM);
                return PlayState.CONTINUE;
            }
            case RELOADING -> {
                event.getController().setAnimation(BaseAnimations.RELOADING_ANIM);
                if (!hasControllingPassenger()) {
                    ((CustomAnimationController<Trebuchet>) event.getController()).setAnimationState(AnimationController.State.PAUSED);
                }
                return PlayState.CONTINUE;
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data)
    {
        AnimationController<?> controller = new CustomAnimationController<>(this, "controller", 1, this::predicate);
        data.add(controller);
    }

    //(t) ->
    //        {
    //            if (this.state.equals(State.RELOADING))
    //            {
    //                return (double) (this.type.specs.delaytime.get() - this.delayticks) / this.type.specs.delaytime.get();
    //            }
    //            return t;
    //        }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache()
    {
        return this.factory;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand)
    {
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

    public void startShooting(Player player)
    {
        if (getDelayTicks() <= 0 && getUseTicks() <= 0 && this.shootingticks <= 0)
        {
            this.state = State.SHOOTING;
            setUseTicks(this.type.usetime);
            this.shootingticks = this.type.userealisetime;

            Vec3 pos = this.position();
            this.level().playLocalSound(pos.x, pos.y, pos.z, ModSoundTypes.BALLISTA_SHOOTING.get(), this.getSoundSource(), 1.4f, 1.0f, false);
        }
    }

    @Override
    public void shoot()
    {
        if (!level().isClientSide())
        {
            super.shoot();
        }
    }

//    @Override
//  public void travel(Vec3 pos)
//    {
//      if (this.isAlive())
//        {
//            if (this.isVehicle())
//            {
//              LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();
//
//                this.setTurretRotationsDest(livingentity.getXRot(), livingentity.getYRot() - this.getYaw());
//                this.updateTurretRotations();
//          }
//            super.travel(pos);
//      }
//
//  }

    @Override
    public void travel(Vec3 pos)
    {
        if (this.isAlive())
        {
            if (this.isVehicle() && getUseTicks() <= 0 && getDelayTicks() <= 0)
            {
                LivingEntity livingentity = this.getControllingPassenger();

                this.setTurretRotations(livingentity.getXRot(), this.getTurretYaw());
                this.updateTurretRotations();

                this.setYawDest(livingentity.getYRot());
                this.updateYaw();

            }
            super.travel(pos);
        }
    }

    @Override
    public void tick()
    {
        int useticks = getUseTicks();

        if(useticks > 0) {
            setUseTicks(--useticks);
            if (useticks <= 0) {
                this.state = State.RELOADING;
                setUseTicks(0);
                setDelayTicks(this.type.specs.delaytime.get());
            }
        }

        if (this.shootingticks != 0 && --this.shootingticks <= 0)
        {
            this.useRealise();
            this.shootingticks = 0;
        }

        if (!level().isClientSide() && this.onGround())
        {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.0, 1.0, 0.0));
        }

        int delayticks = getDelayTicks();

        if (delayticks > 0 && this.isVehicle())
        {
            if (delayticks % 21 == 0)
            {
                Vec3 pos = this.position();
                this.level().playLocalSound(pos.x, pos.y, pos.z, ModSoundTypes.BALLISTA_RELOADING.get(), this.getSoundSource(), 1.0f, 1.0f, false);
            }
            setDelayTicks(--delayticks);
        }

        if (this.renderupdateticks-- <= 0)
        {
            this.updateMachineRender();
            this.renderupdateticks = SiegeMachines.RENDER_UPDATE_TIME;
        }

        super.tick();
    }

    public float getReloadProgress() {
        return ((float) this.type.specs.delaytime.get() - getDelayTicks())/type.specs.delaytime.get();
    }

    @Override
    public Item getMachineItem()
    {
        return ModItems.BALLISTA.get();
    }

    @Override
    public double getTick(Object entity) {
        return state == State.RELOADING ? type.specs.delaytime.get()-getDelayTicks() : GeoEntity.super.getTick(entity);
    }
}