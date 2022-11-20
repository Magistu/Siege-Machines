package ru.magistu.siegemachines.entity.machine;

import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.client.SoundTypes;
import ru.magistu.siegemachines.entity.Breakdown;
import ru.magistu.siegemachines.item.ModItems;
import ru.magistu.siegemachines.network.PacketHandler;
import ru.magistu.siegemachines.network.PacketMachineUse;
import ru.magistu.siegemachines.network.PacketMachineUseRealise;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;


public class BatteringRam extends Machine implements IAnimatable
{
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    static AnimationBuilder MOVING_ANIM = new AnimationBuilder().addAnimation("Moving", ILoopType.EDefaultLoopTypes.LOOP);
    static AnimationBuilder HITTING_ANIM = new AnimationBuilder().addAnimation("Hitting", ILoopType.EDefaultLoopTypes.LOOP);
    static AnimationBuilder RELOADING_ANIM = new AnimationBuilder().addAnimation("Reloading", ILoopType.EDefaultLoopTypes.LOOP);

    public int hittingTicks = 0;
    private int wheelsSoundTicks = 10;

    public enum State
    {
        HITTING,
        RELOADING
    }
    public State state = State.RELOADING;

    private double wheelspitch = 0.0;
    private double wheelsspeed = 0.0;

    public BatteringRam(EntityType<? extends Mob> entitytype, Level level)
    {
        super(entitytype, level, MachineType.BATTERING_RAM);
    }

    private <E extends IAnimatable> PlayState wheels_predicate(AnimationEvent<E> event)
    {
        event.getController().setAnimation(MOVING_ANIM);

        return PlayState.CONTINUE;
	}

    private <E extends IAnimatable> PlayState reloading_predicate(AnimationEvent<E> event)
    {
        switch (state)
        {
            case HITTING:
                event.getController().setAnimation(HITTING_ANIM);
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
        AnimationController<?> wheels_controller = new AnimationController<>(this, "wheels_controller", 1, (t) -> {
            double d = this.getWheelsSpeed();
            this.wheelsspeed = d > 0 ? Math.min(d, 1.0) : Math.max(d, -1.0);
            return wheelspitch += 0.015 * this.wheelsspeed;
        }, this::wheels_predicate);
		data.addAnimationController(wheels_controller);

        AnimationController<?> reloading_controller = new AnimationController<>(this, "controller", 1, (t) ->
        {
            if (this.state.equals(State.RELOADING))
            {
                return (double) (this.type.delaytime - this.delayticks) / this.type.delaytime;
            }
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
    protected InteractionResult mobInteract(Player player, InteractionHand hand)
    {
        if (!this.level.isClientSide() && !this.isVehicle())
        {
			player.startRiding(this);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
    }

    @Override
	public void travel(Vec3 pos)
    {
		if (this.isAlive())
        {
            if (this.isVehicle())
            {
			    LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();

                this.setYawDest(livingentity.getYRot());

                this.updateYaw();

				float f1 = livingentity.zza;
				if (f1 <= 0.0f)
                {
					f1 *= 0.25f;
				}
				this.setSpeed(0.04f);

                pos = new Vec3(0.0f, pos.y, f1);
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
            this.delayticks = this.type.delaytime;
        }

        if (this.hittingTicks != 0 && --this.hittingTicks <= 0)
        {
            this.useRealise();
            this.hittingTicks = 0;
        }

        if (!level.isClientSide() && this.isOnGround())
        {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.0, 1.0, 0.0));
        }

        if (this.delayticks > 0 && this.isVehicle())
        {
            --this.delayticks;
        }

        if (this.renderupdateticks-- <= 0)
        {
            this.updateMachineRender();
            this.renderupdateticks = SiegeMachines.RENDER_UPDATE_TIME;
        }

//        if (this.getWheelsSpeed() > 0.0081 && this.wheelssoundticks-- <= 0)
//        {
//            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundTypes.RAM_WHEELS.get(), SoundCategory.NEUTRAL, 0.6f, 1.0f, true);
//            this.wheelssoundticks = 20;
//        }

        super.tick();
    }

    @Override
    public void use(Player player)
    {
        if (!this.level.isClientSide())
        {
            PacketHandler.sendPacketToAllInArea(new PacketMachineUse(this.getId()), this.blockPosition(), SiegeMachines.RENDER_UPDATE_RANGE_SQR);
        }

        if (this.delayticks <= 0 && this.useticks <= 0 && this.hittingTicks <= 0)
        {
            this.state = State.HITTING;
            this.useticks = this.type.usetime;
            this.hittingTicks = this.type.userealisetime;

            Vec3 pos = this.position();
            this.level.playLocalSound(pos.x, pos.y, pos.z, SoundTypes.RAM_HITTING.get(), SoundSource.BLOCKS, 0.5f, 0.9f, false);
        }
    }

    public void ramHit(BlockPos blockpos)
    {
        if (!this.level.isClientSide())
        {
            Breakdown breakdown = new Breakdown(this.level, this, this.getControllingPassenger(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), 2, false, 3.0f, Explosion.BlockInteraction.BREAK);
            breakdown.explode();
            breakdown.finalizeExplosion(true);
        }
    }

    @Override
    public void useRealise()
    {
        if (!this.level.isClientSide())
        {
            PacketHandler.sendPacketToAllInArea(new PacketMachineUseRealise(this.getId()), this.blockPosition(), SiegeMachines.RENDER_UPDATE_RANGE_SQR);

            BlockPos blockpos = new BlockPos(this.getHitPos());
            this.ramHit(blockpos);
        }
    }

    public double getWheelsSpeed()
    {
        if (this.isOnGround())
        {
            return this.getViewVector(5.0f).multiply(1, 0, 1).dot(this.getDeltaMovement());
        }

        return 0.0;
    }

    @Override
    public void push(double p_70024_1_, double p_70024_3_, double p_70024_5_) {
//      this.setDeltaMovement(this.getDeltaMovement().add(p_70024_1_, p_70024_3_, p_70024_5_));
//      this.hasImpulse = true;
    }

    @Override
    public Item getMachineItem()
    {
        return ModItems.BATTERING_RAM.get();
    }

    protected Vec3 getHitPos()
    {
        double pitch = this.getTurretPitch() * Math.PI / 180.0;
        double yaw = (this.getViewYRot(0.5f) + this.getTurretYaw()) * Math.PI / 180.0;

        return this.position().add(applyRotations(this.type.turretpivot, 0.0, yaw).add(applyRotations(this.type.turretvector, pitch, yaw)));
    }
}
