package magistu.siegemachines.entity.machine;

import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.client.SoundTypes;
import magistu.siegemachines.gui.Crosshair;
import magistu.siegemachines.gui.ReloadingCrosshair;
import magistu.siegemachines.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class Trebuchet extends ShootingMachine implements IAnimatable
{
    private final AnimationFactory factory = new AnimationFactory(this);

    static AnimationBuilder SHOOTING_ANIM = new AnimationBuilder().addAnimation("Shooting", true);
    static AnimationBuilder RELOADING_ANIM = new AnimationBuilder().addAnimation("Reloading", true);
    static AnimationBuilder IDLE_RELOADED_ANIM = new AnimationBuilder().addAnimation("IdleReloaded", true);
    static AnimationBuilder IDLE_NOT_RELOADED_ANIM = new AnimationBuilder().addAnimation("IdleNotReloaded", true);

    private final MachinePartEntity[] subentities;
    private final MachinePartEntity backside;

    private final Vector3d backsidepos;

    public enum State
    {
        SHOOTING,
        RELOADING,
        IDLE_RELOADED,
        IDLE_NOT_RELOADED
    }
    public State state = State.RELOADING;

    public Trebuchet(EntityType<? extends MobEntity> entitytype, World level)
    {
        super(entitytype, level, MachineType.TREBUCHET);
        this.backside = new MachinePartEntity(this, "backside", 5.0F, 2.0F);
        this.backsidepos = new Vector3d(0.0, 0.0, -85.0).scale(1.0 / 16.0);
        this.subentities = new MachinePartEntity[] { this.backside };
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        switch (state)
        {
            case SHOOTING:
                event.getController().setAnimation(SHOOTING_ANIM);
                return PlayState.CONTINUE;
            case IDLE_RELOADED:
                event.getController().setAnimation(IDLE_RELOADED_ANIM);
                return PlayState.CONTINUE;
            case RELOADING:
                event.getController().setAnimation(RELOADING_ANIM);
                return PlayState.CONTINUE;
            case IDLE_NOT_RELOADED:
                event.getController().setAnimation(IDLE_NOT_RELOADED_ANIM);
                return PlayState.CONTINUE;
        }

        return PlayState.CONTINUE;
	}

    private void tickPart(MachinePartEntity subentity, double p_226526_2_, double p_226526_4_, double p_226526_6_)
    {
        subentity.setPos(this.getX() + p_226526_2_, this.getY() + p_226526_4_, this.getZ() + p_226526_6_);
    }

    @Override
    public void aiStep()
    {
        Vector3d[] avector3d = new Vector3d[this.subentities.length];

        Vector3d pos = this.position().add(applyRotations(this.backsidepos, 0.0, this.getYaw()));
        this.tickPart(this.backside, pos.x, pos.y, pos.z);

        for(int i = 0; i < this.subentities.length; ++i)
        {
            avector3d[i] = new Vector3d(this.subentities[i].getX(), this.subentities[i].getY(), this.subentities[i].getZ());
        }

        for(int i = 0; i < this.subentities.length; ++i)
        {
           this.subentities[i].xo = avector3d[i].x;
           this.subentities[i].yo = avector3d[i].y;
           this.subentities[i].zo = avector3d[i].z;
           this.subentities[i].xOld = avector3d[i].x;
           this.subentities[i].yOld = avector3d[i].y;
           this.subentities[i].zOld = avector3d[i].z;
        }

        super.aiStep();
    }

    public MachinePartEntity[] getSubEntities() {
      return this.subentities;
   }

    @Override
    public net.minecraftforge.entity.PartEntity<?>[] getParts()
    {
        return this.subentities;
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
        ItemStack stack = player.getItemInHand(hand);

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
            this.level.playLocalSound(pos.x, pos.y, pos.z, SoundTypes.TREBUCHET_SHOOTING.get(), SoundCategory.BLOCKS, 1.0f, 1.0f, false);
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
            if (this.isVehicle() && this.useticks <= 0 && this.delayticks <= 0)
            {
			    LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();

                this.setTurretRotations(livingentity.xRot, this.getTurretYaw());
                this.updateTurretRotations();

                this.setYawDest(livingentity.yRot);
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

        if (!level.isClientSide() && (this.isOnGround() || this.isInWater()))
        {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.0, 1.0, 0.0));
        }

        if (this.delayticks > 0 && this.isVehicle())
        {
            if (this.delayticks % 40 == 0)
            {
                Vector3d pos = this.position();
                this.level.playLocalSound(pos.x, pos.y, pos.z, SoundTypes.TREBUCHET_RELOADING.get(), SoundCategory.BLOCKS, 1.0f, 1.0f, false);
            }
            if (--this.delayticks <= 0)
            {
                this.state = State.IDLE_RELOADED;
            }
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
        return ModItems.TREBUCHET.get();
    }
}