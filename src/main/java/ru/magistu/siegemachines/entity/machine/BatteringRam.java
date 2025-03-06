package ru.magistu.siegemachines.entity.machine;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.client.SoundTypes;
import ru.magistu.siegemachines.entity.Breakdown;
import ru.magistu.siegemachines.item.ModItems;
import ru.magistu.siegemachines.network.PacketHandler;
import ru.magistu.siegemachines.network.PacketMachineUse;
import ru.magistu.siegemachines.network.PacketMachineUseRelease;
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
import ru.magistu.siegemachines.util.CartesianGeometry;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;



public class BatteringRam extends Machine implements GeoAnimatable
{
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    static RawAnimation MOVING_ANIM = RawAnimation.begin().thenLoop("Moving");
    static RawAnimation HITTING_ANIM = RawAnimation.begin().thenLoop("Hitting");
    static RawAnimation RELOADING_ANIM = RawAnimation.begin().thenLoop("Reloading");

    public int hittingticks = 0;
    private int wheelssoundticks = 10;

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

    private PlayState wheels_predicate(AnimationState<BatteringRam> event)
    {
        event.getController().setAnimation(MOVING_ANIM);

        return PlayState.CONTINUE;
	}

    private PlayState reloading_predicate(AnimationState<BatteringRam> event)
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
	public void registerControllers(AnimatableManager.ControllerRegistrar data)
    {
        AnimationController<?> wheels_controller = new AnimationController<>(this, "wheels_controller",  1, this::wheels_predicate);
		wheels_controller.setOverrideEasingType((dbl)->(t) -> {
            double d = this.getWheelsSpeed();
            this.wheelsspeed = d > 0 ? Math.min(d, 1.0) : Math.max(d, -1.0);
            return wheelspitch += 0.015 * this.wheelsspeed;
        });
        data.add(wheels_controller);

        AnimationController<?> reloading_controller = new AnimationController<>(this, "controller", 1, this::reloading_predicate);
        reloading_controller.setOverrideEasingType((dbl)->(t) ->
        {
            if (this.state.equals(State.RELOADING))
            {
                return (double) (this.type.specs.delaytime.get() - this.delayticks) / this.type.specs.delaytime.get();
            }
            return t;
        });
		data.add(reloading_controller);
	}

    @Override
    public double getTick(Object entity) {
        return tickCount;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand)
    {
        if (!this.level().isClientSide() && !this.isVehicle())
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
            this.delayticks = this.type.specs.delaytime.get();
        }

        if (this.hittingticks != 0 && --this.hittingticks <= 0)
        {
            this.useRelease();
            this.hittingticks = 0;
        }

        if (!level().isClientSide() && this.onGround())
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
        
        if (this.level().isClientSide() && this.hasControllingPassenger() && this.getWheelsSpeed() > 0.0081 && this.wheelssoundticks-- <= 0)
        {
            Entity passenger = this.getControllingPassenger();
            if (Minecraft.getInstance().player == passenger)
            {
                Vec3 pos = this.position();
                this.level().playLocalSound(pos.x, pos.y, pos.z, SoundTypes.RAM_WHEELS.get(), this.getSoundSource(), 1.5f, 0.85f + this.level().random.nextFloat() * 0.3f, false);
                this.wheelssoundticks = 20;
            }
        }

        super.tick();
    }

    @Override
    public void use(LivingEntity entity)
    {
        if (this.deploymentticks > 0 && entity instanceof Player player)
        {
            player.sendSystemMessage(Component.translatable(SiegeMachines.ID + ".wait", this.deploymentticks / 20.0f).withStyle(ChatFormatting.RED));
            return;
        }
        
        if (!this.level().isClientSide())
            PacketHandler.sendPacketToAllInArea(new PacketMachineUse(this.getId()), this.blockPosition(), SiegeMachines.RENDER_UPDATE_RANGE_SQR);

        if (this.delayticks <= 0 && this.useticks <= 0 && this.hittingticks <= 0)
        {
            this.state = State.HITTING;
            this.useticks = this.type.usetime;
            this.hittingticks = this.type.usereleasetime;

            Vec3 pos = this.position();
            this.level().playLocalSound(pos.x, pos.y, pos.z, SoundTypes.RAM_HITTING.get(), this.getSoundSource(), 0.5f, 0.9f, false);
        }
    }

    @Override
    public UsageType getUsage() {
        return UsageType.RAM;
    }

    public void ramHit(BlockPos blockpos)
    {
        if (!this.level().isClientSide())
        {
            Breakdown breakdown = new Breakdown(this.level(), this, this.getControllingPassenger(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), 2, false, 3.0f, Explosion.BlockInteraction.DESTROY);
            breakdown.explode();
            breakdown.finalizeExplosion(true);
        }
    }

    @Override
    public void useRelease()
    {
        if (this.deploymentticks > 0)
            return;
        
        if (!this.level().isClientSide())
        {
            PacketHandler.sendPacketToAllInArea(new PacketMachineUseRelease(this.getId()), this.blockPosition(), SiegeMachines.RENDER_UPDATE_RANGE_SQR);

            BlockPos blockpos = new BlockPos((int) this.getHitPos().x, (int) this.getHitPos().y, (int) this.getHitPos().z);
            this.ramHit(blockpos);
        }
    }

    public double getWheelsSpeed()
    {
        if (this.onGround())
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

        return this.position().add(CartesianGeometry.applyRotations(this.type.turretpivot, 0.0, yaw).add(CartesianGeometry.applyRotations(this.type.turretvector, pitch, yaw)));
    }
}
