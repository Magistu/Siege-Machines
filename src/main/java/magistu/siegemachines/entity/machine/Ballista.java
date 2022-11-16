package magistu.siegemachines.entity.machine;

import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.client.SoundTypes;
import magistu.siegemachines.gui.Crosshair;
import magistu.siegemachines.gui.ReloadingCrosshair;
import magistu.siegemachines.item.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class Ballista extends ShootingMachine implements IAnimatable
{
    private final AnimationFactory factory = new AnimationFactory(this);

    static AnimationBuilder SHOOTING_ANIM = new AnimationBuilder().addAnimation("Shooting", true);
    static AnimationBuilder RELOADING_ANIM = new AnimationBuilder().addAnimation("Reloading", true);

    public enum State
    {
        SHOOTING,
        RELOADING
    }
    public State state = State.RELOADING;

    public Ballista(EntityType<? extends MobEntity> entitytype, World level)
    {
        super(entitytype, level, MachineType.BALLISTA);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        switch (state)
        {
            case SHOOTING:
                event.getController().setAnimation(SHOOTING_ANIM);
                return PlayState.CONTINUE;
            case RELOADING:
                event.getController().setAnimation(RELOADING_ANIM);
                return PlayState.CONTINUE;
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
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand)
    {
        if (super.mobInteract(player, hand) == ActionResultType.SUCCESS)
        {
            return ActionResultType.SUCCESS;
        }
        if (!this.level.isClientSide() && !this.isVehicle())
        {
			player.startRiding(this);
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
    }

    public void startShooting(PlayerEntity player)
    {
        if (this.delayticks <= 0 && this.useticks <= 0 && this.shootingticks <= 0)
        {
            this.state = State.SHOOTING;
            this.useticks = this.type.usetime;
            this.shootingticks = this.type.userealisetime;

            Vector3d pos = this.position();
            this.level.playLocalSound(pos.x, pos.y, pos.z, SoundTypes.BALLISTA_SHOOTING.get(), SoundCategory.BLOCKS, 1.4f, 1.0f, false);
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

    @Override
	public void travel(Vector3d pos)
    {
		if (this.isAlive())
        {
            if (this.isVehicle())
            {
			    LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();

                this.setTurretRotationsDest(livingentity.xRot, livingentity.yRot - this.getYaw());
                this.updateTurretRotations();
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

        if (!level.isClientSide() && (this.isOnGround() || this.isInWater()))
        {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.0, 1.0, 0.0));
        }

        if (this.delayticks > 0 && this.isVehicle())
        {
            if (this.delayticks % 21 == 0)
            {
                Vector3d pos = this.position();
                this.level.playLocalSound(pos.x, pos.y, pos.z, SoundTypes.BALLISTA_RELOADING.get(), SoundCategory.BLOCKS, 1.0f, 1.0f, false);
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