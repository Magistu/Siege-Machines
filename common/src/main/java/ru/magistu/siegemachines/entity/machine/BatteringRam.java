package ru.magistu.siegemachines.entity.machine;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

import ru.magistu.siegemachines.ModSoundTypes;
import ru.magistu.siegemachines.item.ModItems;
import ru.magistu.siegemachines.network.PacketHandler;
import ru.magistu.siegemachines.network.S2CPacketMachineUse;
import net.minecraft.core.BlockPos;
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
import ru.magistu.siegemachines.util.BaseAnimations;
import ru.magistu.siegemachines.util.CartesianGeometry;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;


public class BatteringRam extends Machine implements GeoEntity
{
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);



    public int hittingticks = 0;
    private int wheelssoundticks = 10;
    public double lastwheelpitch;

    public enum State
    {
        HITTING,
        RELOADING
    }
    public State state = State.RELOADING;

    private double wheelspitch = 0.0;

    public BatteringRam(EntityType<? extends Mob> entitytype, Level level)
    {
        super(entitytype, level, MachineType.BATTERING_RAM);
    }

    private <E extends GeoAnimatable> PlayState reloading_predicate(AnimationState<E> event)
    {
        return switch (state) {
            case HITTING -> {
                event.getController().setAnimation(BaseAnimations.HITTING_ANIM);
                yield PlayState.CONTINUE;
            }
            case RELOADING -> {
                event.getController().setAnimation(BaseAnimations.RELOADING_ANIM);
                yield PlayState.CONTINUE;
            }
        };
    }

    @Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data)
    {
        AnimationController<?> reloading_controller = new AnimationController<>(this, "controller", 1,/* (t) ->
        {
            if (this.state.equals(State.RELOADING))
            {
                return (double) (this.type.specs.delaytime.get() - this.delayticks) / this.type.specs.delaytime.get();
            }
            return t;
        },*/ this::reloading_predicate);
		data.add(reloading_controller);
	}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache()
    {
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
			    LivingEntity livingentity = this.getControllingPassenger();

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
        lastwheelpitch = wheelspitch;
        wheelspitch += this.getWheelsSpeed();

        int useticks = getUseTicks();
        if (useticks > 0)
        {
            useticks--;
            if (useticks <=0) {
                this.state = State.RELOADING;
                setDelayTicks(this.type.specs.delaytime.get());
            }
            setUseTicks(useticks);
        }

        if (this.hittingticks != 0 && --this.hittingticks <= 0)
        {
            this.useRealise();
            this.hittingticks = 0;
        }

        if (!level().isClientSide() && this.onGround())
        {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.0, 1.0, 0.0));
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
                this.level().playLocalSound(pos.x, pos.y, pos.z, ModSoundTypes.RAM_WHEELS.get(), this.getSoundSource(), 1.5f, 0.85f + this.level().random.nextFloat() * 0.3f, false);
                this.wheelssoundticks = 20;
            }
        }

        super.tick();
    }

    public double getLerpedWheelPitch(float partialTick) {
        return Mth.lerp(partialTick,lastwheelpitch,wheelspitch);
    }

    @Override
    public void use(Player player)
    {
        if (this.deploymentticks > 0)
        {
            player.sendSystemMessage(Component.translatable(SiegeMachines.ID + ".wait", this.deploymentticks / 20.0f).withStyle(ChatFormatting.RED));
            return;
        }
        
        if (!this.level().isClientSide())
            PacketHandler.sendPacketToAllInArea((ServerLevel) level(),new S2CPacketMachineUse(this.getId()), this.blockPosition(), SiegeMachines.RENDER_UPDATE_RANGE_SQR);

        if (getDelayTicks() <= 0 && getUseTicks() <= 0 && this.hittingticks <= 0)
        {
            this.state = State.HITTING;
            setUseTicks(type.usetime);
            this.hittingticks = this.type.userealisetime;

            Vec3 pos = this.position();
            this.level().playLocalSound(pos.x, pos.y, pos.z, ModSoundTypes.RAM_HITTING.get(), this.getSoundSource(), 0.5f, 0.9f, false);
        }
    }

    public void ramHit(BlockPos blockpos)
    {
        if (!this.level().isClientSide())
        {
            //    public Explosion(
            //        Level level,
            //        @Nullable Entity source,
            //        double x,
            //        double y,
            //        double z,
            //        float radius,
            //        boolean fire,
            //        Explosion.BlockInteraction blockInteraction
            //    )
            Explosion breakdown = new Explosion(this.level(), this,
                    blockpos.getX(), blockpos.getY(), blockpos.getZ(), 2, false, Explosion.BlockInteraction.DESTROY);
            breakdown.explode();
            breakdown.finalizeExplosion(true);
        }
    }

    @Override
    public void useRealise()
    {
        if (this.deploymentticks > 0)
            return;
        
        if (!this.level().isClientSide())
        {
            level().broadcastEntityEvent(this,(byte) USE_REALISE);
            BlockPos blockpos = BlockPos.containing(this.getHitPos());
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
