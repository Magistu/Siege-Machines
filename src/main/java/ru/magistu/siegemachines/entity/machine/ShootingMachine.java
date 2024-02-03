package ru.magistu.siegemachines.entity.machine;

import com.mojang.math.Vector3d;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.entity.IReloading;
import ru.magistu.siegemachines.entity.projectile.Missile;
import ru.magistu.siegemachines.entity.projectile.ProjectileBuilder;
import ru.magistu.siegemachines.network.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import ru.magistu.siegemachines.util.CartesianGeometry;

import java.util.Arrays;
import java.util.Random;

public abstract class ShootingMachine extends Machine implements IReloading
{
    public int shootingticks = 0;

    protected ShootingMachine(EntityType<? extends Mob> entitytype, Level level, MachineType type)
    {
        super(entitytype, level, type);
    }

    public abstract void startShooting(Player player);

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
            if (passenger instanceof Player)
            {
                passenger.sendSystemMessage(Component.translatable(SiegeMachines.ID + ".no_ammo").withStyle(ChatFormatting.RED));
            }
            return;
        }
        LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();
        Vec3 shotpos = this.getShotPos();
        Projectile projectile = projectilebuilder.build(this.level, new Vector3d(shotpos.x, shotpos.y, shotpos.z), livingentity == null ? this : livingentity);
        if (projectile instanceof Missile)
        {
            Missile missile = (Missile) projectile;
            missile.setItem(new ItemStack(missile.getDefaultItem()));
        }
        projectile.shootFromRotation(this, this.getTurretPitch(), this.getGlobalTurretYaw(), 0.0f, this.type.specs.projectilespeed.get(), this.type.specs.inaccuracy.get());
        this.level.addFreshEntity(projectile);
        this.inventory.shrinkItem(projectilebuilder.item);
    }

    @Override
    public void use(Player player)
    {
        if (!this.level.isClientSide())
        {
            PacketHandler.sendPacketToAllInArea(new PacketMachineUse(this.getId()), this.blockPosition(), SiegeMachines.RENDER_UPDATE_RANGE_SQR);
        }

        this.startShooting(player);
    }

    @Override
    public void useRealise()
    {
        if (!this.level.isClientSide())
        {
            PacketHandler.sendPacketToAllInArea(new PacketMachineUseRealise(this.getId()), this.blockPosition(), SiegeMachines.RENDER_UPDATE_RANGE_SQR);
        }
        this.shoot();
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand)
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
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

    protected Vec3 getShotPos()
    {
        double pitch = this.getTurretPitch() * Math.PI / 180.0;
        double yaw = (this.getViewYRot(0.5f) + this.getTurretYaw()) * Math.PI / 180.0;

        return this.position().add(CartesianGeometry.applyRotations(this.type.turretpivot, 0.0, yaw).add(CartesianGeometry.applyRotations(this.type.turretvector, pitch, yaw)));
    }

    protected Vec3 getShotView()
    {
        double pitch = this.getTurretPitch() * Math.PI / 180.0;
        double yaw = this.getGlobalTurretYaw() * Math.PI / 180.0;

        double d0 = -Math.sin(yaw) * Math.cos(pitch);
        double d1 = -Math.sin(pitch);
        double d2 = Math.cos(yaw) * Math.cos(pitch);

        return new Vec3(d0, d1, d2).normalize();
    }

    protected void blowParticles(ParticleOptions particle, double speed, int amount)
    {
        for (int i = 0; i < amount; ++i)
        {
            Vec3 pos = this.getShotPos();
            Vec3 inaccuracy = new Vec3(new Random().nextGaussian() * 0.2,
                                               new Random().nextGaussian() * 0.2,
                                               new Random().nextGaussian() * 0.2);
            Vec3 velocity = this.getShotView().add(inaccuracy).scale(speed);

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
