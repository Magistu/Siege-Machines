package ru.magistu.siegemachines.entity.machine;

import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.client.SoundTypes;
import ru.magistu.siegemachines.client.gui.machine.crosshair.Crosshair;
import ru.magistu.siegemachines.client.gui.machine.crosshair.ReloadingCrosshair;
import ru.magistu.siegemachines.item.ModItems;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Ballista extends ShootingMachine implements IAnimatable
{
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    static AnimationBuilder SHOOTING_ANIM = new AnimationBuilder().addAnimation("Shooting", ILoopType.EDefaultLoopTypes.LOOP);
    static AnimationBuilder RELOADING_ANIM = new AnimationBuilder().addAnimation("Reloading", ILoopType.EDefaultLoopTypes.LOOP);

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

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        switch (state) {
            case SHOOTING -> {
                event.getController().setAnimation(SHOOTING_ANIM);
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
    public void registerControllers(AnimationData data)
    {
        AnimationController<?> controller = new AnimationController<>(this, "controller", 1, (t) ->
        {
            if (this.state.equals(State.RELOADING))
            {
                return (double) (this.type.specs.delaytime.get() - this.delayticks) / this.type.specs.delaytime.get();
            }
            return t;
        }, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory()
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
        if (!this.level.isClientSide() && !this.isVehicle())
        {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public void startShooting(Player player)
    {
        if (this.delayticks <= 0 && this.useticks <= 0 && this.shootingticks <= 0)
        {
            this.state = State.SHOOTING;
            this.useticks = this.type.usetime;
            this.shootingticks = this.type.userealisetime;

            Vec3 pos = this.position();
            this.level.playLocalSound(pos.x, pos.y, pos.z, SoundTypes.BALLISTA_SHOOTING.get(), this.getSoundSource(), 1.4f, 1.0f, false);
        }
    }

    @Override
    public void shoot()
    {
        if (!level.isClientSide())
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
            if (this.isVehicle() && this.useticks <= 0 && this.delayticks <= 0)
            {
                LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();

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
        if (this.useticks != 0 && --this.useticks <= 0)
        {
            this.state = State.RELOADING;
            this.useticks = 0;
            this.delayticks = this.type.specs.delaytime.get();
        }

        if (this.shootingticks != 0 && --this.shootingticks <= 0)
        {
            this.useRealise();
            this.shootingticks = 0;
        }

        if (!level.isClientSide() && this.isOnGround())
        {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.0, 1.0, 0.0));
        }

        if (this.delayticks > 0 && this.isVehicle())
        {
            if (this.delayticks % 21 == 0)
            {
                Vec3 pos = this.position();
                this.level.playLocalSound(pos.x, pos.y, pos.z, SoundTypes.BALLISTA_RELOADING.get(), this.getSoundSource(), 1.0f, 1.0f, false);
            }
            --this.delayticks;
        }

        if (this.renderupdateticks-- <= 0)
        {
            this.updateMachineRender();
            this.renderupdateticks = SiegeMachines.RENDER_UPDATE_TIME;
        }

        super.tick();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Crosshair createCrosshair()
    {
        return new ReloadingCrosshair();
    }

    @Override
    public Item getMachineItem()
    {
        return ModItems.BALLISTA.get();
    }
}