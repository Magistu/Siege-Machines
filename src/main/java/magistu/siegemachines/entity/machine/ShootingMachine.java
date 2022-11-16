package magistu.siegemachines.entity.machine;

import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.entity.IReloading;
import magistu.siegemachines.entity.projectile.Missile;
import magistu.siegemachines.entity.projectile.ProjectileBuilder;
import magistu.siegemachines.network.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Random;

public abstract class ShootingMachine extends Machine implements IReloading
{
    public int shootingticks = 0;

    protected ShootingMachine(EntityType<? extends MobEntity> entitytype, World level, MachineType type)
    {
        super(entitytype, level, type);
    }

    public abstract void startShooting(PlayerEntity player);

    public void shoot()
    {
        if (this.type.ammo.length == 0)
        {
            return;
        }
        ProjectileBuilder projectilebuilder = this.getProjectileBuilder();
        if (projectilebuilder.equals(ProjectileBuilder.NONE))
        {
            Entity passenger = this.getControllingPassenger();
            if (passenger instanceof PlayerEntity)
            {
                passenger.sendMessage(new TranslationTextComponent(SiegeMachines.ID + ".no_ammo").withStyle(TextFormatting.RED), SiegeMachines.CHAT_UUID);
            }
            return;
        }
        LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();
        ProjectileEntity projectile = projectilebuilder.factory.create(projectilebuilder.entitytype, this.level, this.getShotPos(), livingentity == null ? this : livingentity, projectilebuilder.projectileitem);
        if (projectile instanceof Missile)
        {
            Missile missile = (Missile) projectile;
            missile.setItem(new ItemStack(missile.getDefaultItem()));
        }
        projectile.shootFromRotation(this, this.getTurretPitch(), this.getGlobalTurretYaw(), 0.0f, this.type.specs.projectilespeed.get(), this.type.specs.inaccuracy.get());
        this.level.addFreshEntity(projectile);
        if (!this.level.isClientSide)
        {
            int i = this.inventory.shrinkItem(projectilebuilder.item);
            PacketHandler.sendPacketToAll(
                    new PacketMachineInventorySlot(this.getId(), i, this.inventory.getItem(i)));
        }
    }

    @Override
    public void use(PlayerEntity player)
    {
        if (!this.level.isClientSide)
        {
            PacketHandler.sendPacketToAllInArea(new PacketMachineUse(this.getId()), this.blockPosition(), SiegeMachines.RENDER_UPDATE_RANGE_SQR);
        }

        this.startShooting(player);
    }

    @Override
    public void useRealise()
    {
        if (!this.level.isClientSide)
        {
            PacketHandler.sendPacketToAllInArea(new PacketMachineUseRealise(this.getId()), this.blockPosition(), SiegeMachines.RENDER_UPDATE_RANGE_SQR);
        }

        this.shoot();
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getItemInHand(hand);

		if (this.isValidAmmo(stack))
		{
			if (!this.hasAmmo())
			{
				if (!player.isCreative())
				{
					stack.shrink(1);
				}
                this.inventory.putItem(stack.getItem());
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

    protected Vector3d getShotPos()
    {
        double pitch = this.getTurretPitch() * Math.PI / 180.0;
        double yaw = (this.getViewYRot(0.5f) + this.getTurretYaw()) * Math.PI / 180.0;

        return this.position().add(applyRotations(this.type.turretpivot, 0.0, yaw).add(applyRotations(this.type.turretvector, pitch, yaw)));
    }

    protected Vector3d getShotView()
    {
        double pitch = this.getTurretPitch() * Math.PI / 180.0;
        double yaw = this.getGlobalTurretYaw() * Math.PI / 180.0;

        double d0 = -Math.sin(yaw) * Math.cos(pitch);
        double d1 = -Math.sin(pitch);
        double d2 = Math.cos(yaw) * Math.cos(pitch);

        return new Vector3d(d0, d1, d2).normalize();
    }

    protected void blowParticles(IParticleData particle, double speed, int amount)
    {
        for (int i = 0; i < amount; ++i)
        {
            Vector3d pos = this.getShotPos();
            Vector3d inaccuracy = new Vector3d(new Random().nextGaussian() * 0.2,
                                               new Random().nextGaussian() * 0.2,
                                               new Random().nextGaussian() * 0.2);
            Vector3d velocity = this.getShotView().add(inaccuracy).scale(speed);

            this.level.addParticle(particle, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z);
        }
    }

    @Override
    public void updateMachineRender()
	{
        super.updateMachineRender();
		if (!this.level.isClientSide())
		{
            for (int i = 0; i < this.inventory.getContainerSize(); ++i)
            {
                if (this.isValidAmmo(this.inventory.getItem(i)))
                {
                    PacketHandler.sendPacketToAllInArea(
                            new PacketMachineInventorySlot(this.getId(), i, this.inventory.getItem(i)),
                            this.blockPosition(),
                            SiegeMachines.RENDER_UPDATE_RANGE_SQR);
                }
            }
		}
	}

    public boolean isValidAmmo(ItemStack stack)
	{
		return this.isValidAmmo(stack.getItem());
	}

    public boolean isValidAmmo(Item entry)
	{
		return Arrays.stream(this.type.ammo).anyMatch(builder -> builder.item.equals(entry));
	}

	public ItemStack getAmmo()
	{
		return this.inventory.items.stream().filter(this::isValidAmmo).findFirst().orElse(ItemStack.EMPTY);
	}

	public boolean hasAmmo()
	{
        return this.inventory.items.stream().anyMatch(this::isValidAmmo);
	}

    public ProjectileBuilder getProjectileBuilder()
    {
        ItemStack ammo = this.getAmmo();
        if (ammo.equals(ItemStack.EMPTY))
        {
            return ProjectileBuilder.NONE;
        }
        return Arrays.stream(this.type.ammo).filter(builder -> builder.item.equals(ammo.getItem())).findFirst().orElse(ProjectileBuilder.NONE);
    }
}
