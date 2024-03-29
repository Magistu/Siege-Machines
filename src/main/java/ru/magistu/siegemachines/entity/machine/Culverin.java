package ru.magistu.siegemachines.entity.machine;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.client.SoundTypes;
import ru.magistu.siegemachines.entity.IReloading;
import ru.magistu.siegemachines.client.gui.machine.crosshair.Crosshair;
import ru.magistu.siegemachines.client.gui.machine.crosshair.ReloadingCrosshair;
import ru.magistu.siegemachines.item.ModItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class Culverin extends ShootingMachine implements IAnimatable, IReloading
{
    private final AnimationFactory factory = new AnimationFactory(this);

    static AnimationBuilder MOVING_ANIM = new AnimationBuilder().addAnimation("Moving", true);

    private double wheelspitch = 0.0;
    private double wheelsspeed = 0.0;

    public Culverin(EntityType<? extends Mob> entitytype, Level level)
    {
        super(entitytype, level, MachineType.CULVERIN);
        this.turretpitch = -18.5f;
        this.turretpitchprev = this.turretpitch;
        this.turretpitchdest = this.turretpitch;
    }

    private <E extends IAnimatable> PlayState wheels_predicate(AnimationEvent<E> event)
    {
        event.getController().setAnimation(MOVING_ANIM);

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
        wheels_controller.registerSoundListener(this::soundListener);
		data.addAnimationController(wheels_controller);
	}

    private <ENTITY extends IAnimatable> void soundListener(SoundKeyframeEvent<ENTITY> event)
    {
        //ClientPlayer player = Minecraft.getInstance().player;
        if (/*player != null && */this.getWheelsSpeed() > 0.0081)
        {
            Vec3 pos = this.position();
            this.level.playLocalSound(pos.x, pos.y, pos.z, SoundTypes.CANNON_WHEELS.get(), SoundSource.BLOCKS, 0.5f/*this.getVolumeFromDist(0.5f, 16.0f, this.distanceTo(player))*/, 1.0f, false);
        }
	}

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem().equals(Items.FLINT_AND_STEEL))
        {
            if (this.useticks < 0)
            {
                stack.hurtAndBreak(1, player, (p_213833_1_) ->
                {
                    p_213833_1_.broadcastBreakEvent(hand);
                    net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, this.useItem, hand);
                });
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

                this.setTurretRotationsDest(livingentity.getXRot(), livingentity.getYRot() - this.getYaw());
                this.setYawDest(livingentity.getYRot());

                this.updateYaw();
                this.updateTurretRotations();

				float f0 = livingentity.xxa * 0.2f;
				float f1 = livingentity.zza;
				this.setSpeed(0.02f);

                pos = new Vec3(f0, pos.y, f1);
			}
            super.travel(pos);
		}
	}

    @Override
    public void tick()
    {
        if (this.useticks != 0 && --this.useticks <= 0)
        {
            if (this.inventory.containsItem(Items.GUNPOWDER))
            {
                this.useRealise();
            }
            this.useticks = 0;
        }
        if (!level.isClientSide() && (this.isOnGround() || this.isInWater()))
        {
            this.setDeltaMovement(this.getWheelsDeltaMovement());
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

        super.tick();
    }

    @Override
    public void startShooting(Player player)
    {
        if (this.delayticks <= 0 && this.useticks <= 0)
        {
            if (!this.level.isClientSide())
            {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundTypes.FUSE.get(), SoundSource.BLOCKS, this.getVolumeFromDist(this.distanceTo(player)), 0.8f);
            }
            this.useticks = this.type.usetime;
        }
    }

    @Override
    public void shoot()
    {
        if (!level.isClientSide())
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
            this.level.playLocalSound(pos.x, pos.y, pos.z, SoundTypes.MORTAR_SHOOTING.get(), SoundSource.BLOCKS, 1.5f/*this.getVolumeFromDist(1.5f, 64.0f, this.distanceTo(player))*/, 0.85f + this.level.random.nextFloat() * 0.3f, false);
        }

        this.delayticks = this.type.specs.delaytime.get();
    }

    public double getWheelsSpeed()
    {
        if (this.isOnGround())
        {
            return this.getViewVector(5.0f).multiply(1, 0, 1).dot(this.getDeltaMovement());
        }

        return 0.0;
    }

    public Vec3 getWheelsDeltaMovement()
    {
        if (this.isOnGround())
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
    @OnlyIn(Dist.CLIENT)
    public Crosshair createCrosshair()
    {
        return new ReloadingCrosshair();
    }

    @Override
    public Item getMachineItem()
    {
        return ModItems.CULVERIN.get();
    }
}