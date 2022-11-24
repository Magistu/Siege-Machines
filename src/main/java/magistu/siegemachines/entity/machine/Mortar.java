package magistu.siegemachines.entity.machine;

import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.client.SoundTypes;
import magistu.siegemachines.entity.IReloading;
import magistu.siegemachines.entity.projectile.Cannonball;
import magistu.siegemachines.entity.projectile.Missile;
import magistu.siegemachines.gui.Crosshair;
import magistu.siegemachines.gui.ReloadingCrosshair;
import magistu.siegemachines.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Mortar extends ShootingMachine implements IAnimatable, IReloading
{
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    static AnimationBuilder MOVING_ANIM = new AnimationBuilder().addAnimation("Moving", true);

    public int shootingticks = 0;

    private double wheelspitch = 0.0;
    private double wheelsspeed = 0.0;
    private int wheelssoundticks = 10;

    public Mortar(EntityType<? extends MobEntity> entitytype, World level)
    {
        super(entitytype, level, MachineType.MORTAR);
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
            return wheelspitch += 0.013 * this.wheelsspeed;
        }, this::wheels_predicate);
		data.addAnimationController(wheels_controller);
	}

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem().equals(Items.FLINT_AND_STEEL))
        {
            if (this.useticks <= 0 && this.shootingticks <= 0)
            {
                stack.hurtAndBreak(1, player, (p_213833_1_) ->
                {
                    p_213833_1_.broadcastBreakEvent(hand);
                    net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, this.useItem, hand);
                });
                this.startShooting(player);
            }
            return ActionResultType.SUCCESS;
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
            return ActionResultType.SUCCESS;
        }
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

    @Override
	public void travel(Vector3d pos)
    {
		if (this.isAlive())
        {
            if (this.isVehicle())
            {
			    LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();

                this.setTurretRotationsDest(livingentity.xRot, livingentity.yRot - this.getYaw());
                this.setYawDest(livingentity.yRot);

                this.updateYaw();
                this.updateTurretRotations();

				float f0 = livingentity.xxa * 0.2f;
				float f1 = livingentity.zza;
				if (f1 <= 0.0f)
                {
					f1 *= 0.5f;
				}
				this.setSpeed(0.04f);

                pos = new Vector3d(f0, pos.y, f1);
			}
            super.travel(pos);
		}
	}

    @Override
    public void tick()
    {
        if (this.useticks != 0 && --this.useticks <= 0)
        {
            this.useticks = 0;
            this.delayticks = this.type.specs.delaytime.get();
        }

        if (this.shootingticks != 0 && --this.shootingticks <= 0)
        {
            if (this.inventory.containsItem(Items.GUNPOWDER))
            {
                this.useRealise();
            }
            else if (!this.level.isClientSide())
            {
                Entity passenger = this.getControllingPassenger();
                if (passenger instanceof PlayerEntity)
                {
                    passenger.sendMessage(new TranslationTextComponent(SiegeMachines.ID + ".no_gunpowder").withStyle(TextFormatting.RED), SiegeMachines.CHAT_UUID);
                }
            }
            this.shootingticks = 0;
        }

        if (!level.isClientSide() && this.isOnGround())
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

        if (this.getWheelsSpeed() > 0.0081 && this.wheelssoundticks-- <= 0)
        {
            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundTypes.CANNON_WHEELS.get(), SoundCategory.NEUTRAL, 0.3f, 1.0f, true);
            this.wheelssoundticks = 20;
        }

        super.tick();
    }

    @Override
    public void startShooting(PlayerEntity player)
    {
        if (this.delayticks <= 0 && this.useticks <= 0 && this.shootingticks <= 0)
        {
            if (!this.level.isClientSide())
            {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundTypes.FUSE.get(), SoundCategory.BLOCKS, this.getVolumeFromDist(0.5f, 6.0f, this.distanceTo(player)), 0.8f);
            }
            this.useticks = this.type.usetime;
            this.shootingticks = this.type.userealisetime;
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
            Vector3d pos = this.position();
            this.level.playLocalSound(pos.x, pos.y, pos.z, SoundTypes.MORTAR_SHOOTING.get(), SoundCategory.BLOCKS, 1.5f/*this.getVolumeFromDist(1.5f, 64.0f, this.distanceTo(player))*/, 0.85f + this.level.random.nextFloat() * 0.3f, false);
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

    public Vector3d getWheelsDeltaMovement()
    {
        if (this.isOnGround() || this.isInWater())
        {
            Vector3d view = this.getViewVector(1.0f);
            Vector3d movement = this.getDeltaMovement();

            double d0 = movement.x * view.x + movement.z * view.z;

            double d1 = d0 * view.x;
            double d2 = 0.0;
            double d3 = d0 * view.z;

            return new Vector3d(d1, d2, d3);
        }

        return Vector3d.ZERO;
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
        return ModItems.MORTAR.get();
    }
}