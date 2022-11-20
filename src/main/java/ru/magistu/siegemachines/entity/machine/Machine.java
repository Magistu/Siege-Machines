package ru.magistu.siegemachines.entity.machine;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.gui.MachineContainer;
import ru.magistu.siegemachines.network.PacketHandler;
import ru.magistu.siegemachines.network.PacketMachine;

import javax.annotation.Nullable;

public abstract class Machine extends Mob implements MenuProvider
{
	public MachineInventory inventory = new MachineInventory();

	public static int containersize = 9;

	public final MachineType type;

	private float turretpitch = -25.0f;
	private float turretpitchprev = this.turretpitch;
	protected float turretpitchdest = this.turretpitch;
	private float turretyaw = 0.0f;
	private float turretyawprev = this.turretyaw;
	protected float turretyawdest = this.turretyaw;
	protected float yawdest = this.getYRot();

	public int useticks = -1;
	public int delayticks;
	protected int renderupdateticks = 0;
	public int deploymentticks = 0;

	protected Machine(EntityType<? extends Mob> entitytype, Level level, MachineType type)
    {
        super(entitytype, level);
		this.type = type;
		this.delayticks = this.type.delaytime;
		containersize = this.type.containersize;
	}

	public static AttributeSupplier.Builder setEntityAttributes(MachineType type)
	{
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, type.health)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.5F)
				.add(Attributes.MOVEMENT_SPEED, 0.0D)
				.add(Attributes.ATTACK_DAMAGE, 0.0D)
				.add(Attributes.FOLLOW_RANGE, 0.0D);
	}

	public ItemStack getMachineItemWithData()
	{
		ItemStack stack = new ItemStack(this.getMachineItem());
        CompoundTag nbt = this.saveWithoutId(new CompoundTag());
        nbt.remove("Pos");
        nbt.remove("Motion");
        nbt.remove("FallDistance");
        nbt.remove("Fire");
        nbt.remove("Air");
        nbt.remove("OnGround");
        nbt.remove("Invulnerable");
        nbt.remove("PortalCooldown");
        nbt.remove("UUID");
        nbt.remove("Passengers");
        nbt.remove("DelayTicks");
        nbt.remove("UseTicks");
        stack.addTagElement("EntityTag", nbt);
        return stack;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource damagesource)
	{
		return damagesource == DamageSource.CACTUS ||
				damagesource == DamageSource.WITHER ||
				damagesource == DamageSource.MAGIC ||
				damagesource == DamageSource.DROWN ||
				damagesource == DamageSource.STARVE ||
				super.isInvulnerableTo(damagesource);
	}

	public float adjustDamage(DamageSource damagesource, float f)
	{
		if (damagesource.isFire())
		{
			f *= 1.5f;
		}
		if (damagesource.isExplosion())
		{
			f *= 1.25f;
		}
		if (damagesource.getEntity() instanceof AbstractArrow)
		{
			f *= 0.5;
		}
		return f;
	}

	public abstract Item getMachineItem();

	@Override
	public void tick()
	{
		if (this.deploymentticks > 0)
		{
			this.deploymentticks--;
		}
		super.tick();
	}

	@Override
	public boolean hurt(@NotNull DamageSource damagesource, float f)
	{
		if (!net.minecraftforge.common.ForgeHooks.onLivingAttack(this, damagesource, f)) return false;
		if (damagesource.getEntity() instanceof Player && !damagesource.isProjectile() && !damagesource.isExplosion() && !damagesource.isMagic() && this.getPassengers().isEmpty())
        {
            this.spawnAtLocation(this.getMachineItemWithData());
            this.remove();
            return false;
        }
		if (this.isInvulnerableTo(damagesource))
		{
			return false;
		}
		else if (this.level.isClientSide)
		{
			return false;
		}
		else if (this.isDeadOrDying())
		{
			return false;
		}
		else if (damagesource.isFire() && this.hasEffect(MobEffects.FIRE_RESISTANCE))
		{
			return false;
		}
		else
		{
			f = adjustDamage(damagesource, f);

			this.noActionTime = 0;

			this.animationSpeed = 1.5F;
			boolean flag1 = true;
			if ((float) this.invulnerableTime > 10.0F)
			{
				if (f <= this.lastHurt)
				{
					return false;
				}

				this.actuallyHurt(damagesource, f - this.lastHurt);
				this.lastHurt = f;
				flag1 = false;
			}
			else
			{
				this.lastHurt = f;
				this.invulnerableTime = 20;
				this.actuallyHurt(damagesource, f);
				this.hurtDuration = 10;
				this.hurtTime = this.hurtDuration;
			}

			this.hurtDir = 0.0F;
			Entity entity1 = damagesource.getEntity();
			if (entity1 != null)
			{
				if (entity1 instanceof LivingEntity)
				{
					this.setLastHurtByMob((LivingEntity) entity1);
				}

				if (entity1 instanceof Player)
				{
					this.lastHurtByPlayerTime = 100;
					this.lastHurtByPlayer = (Player) entity1;
				}
				else if (entity1 instanceof TamableAnimal)
				{
					TamableAnimal wolfentity = (TamableAnimal) entity1;
					if (wolfentity.isTame())
					{
						this.lastHurtByPlayerTime = 100;
						LivingEntity livingentity = wolfentity.getOwner();
						if (livingentity != null && livingentity.getType() == EntityType.PLAYER)
						{
							this.lastHurtByPlayer = (Player) livingentity;
						}
						else
						{
							this.lastHurtByPlayer = null;
						}
					}
				}
			}

			if (flag1)
			{
				if (damagesource instanceof EntityDamageSource && ((EntityDamageSource) damagesource).isThorns())
				{
					this.level.broadcastEntityEvent(this, (byte) 33);
				}
				else
				{
					byte b0;
					if (damagesource.isFire())
					{
						b0 = 37;
					}
					else if (damagesource == DamageSource.SWEET_BERRY_BUSH)
					{
						b0 = 44;
					}
					else
					{
						b0 = 2;
					}

					this.level.broadcastEntityEvent(this, b0);
				}

				this.markHurt();

				if (entity1 != null)
				{
					double d1 = entity1.getX() - this.getX();

					double d0;
					for (d0 = entity1.getZ() - this.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
					{
						d1 = (Math.random() - Math.random()) * 0.01D;
					}

					this.hurtDir = (float) (Mth.atan2(d0, d1) * (double) (180F / (float) Math.PI) - (double) this.getYRot());
				}
				else
				{
					this.hurtDir = (float) ((int) (Math.random() * 2.0D) * 180);
				}
			}

			if (this.isDeadOrDying())
			{
				SoundEvent soundevent = this.getDeathSound();
				if (flag1 && soundevent != null)
				{
					this.playSound(soundevent, this.getSoundVolume(), this.getVoicePitch());
				}

				this.die(damagesource);
			}
			else if (flag1)
			{
				this.playHurtSound(damagesource);
			}

			if (entity1 instanceof ServerPlayer)
			{
				CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer) entity1, this, damagesource, f, f, false);
			}

			return true;
		}
	}

	@Override
	@Nullable
	protected SoundEvent getHurtSound(@NotNull DamageSource p_184601_1_)
	{
		return null;
	}

	@Override
	@Nullable
	protected SoundEvent getDeathSound()
	{
		return null;
	}

	@Override
	public boolean removeWhenFarAway(double p_213397_1_)
	{
		return false;
	}

    @Nullable
	@Override
	public Entity getControllingPassenger()
    {
		return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
	}

    @Override
	public boolean canBeControlledByRider()
    {
		return true;
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag nbt)
	{
		super.addAdditionalSaveData(nbt);

		ListTag listnbt = new ListTag();
    	for(ItemStack itemstack : this.inventory.items)
		{
    		CompoundTag compoundnbt = new CompoundTag();
    		if (!itemstack.isEmpty())
			{
    			itemstack.save(compoundnbt);
    		}
    		listnbt.add(compoundnbt);
    	}
    	nbt.put("Items", listnbt);
		nbt.put("TurretRotations", this.newFloatList(this.turretpitch, this.turretyaw));
		nbt.putInt("DealyTicks", this.delayticks);
		nbt.putInt("UseTicks", this.useticks);
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource p_213333_1_, int p_213333_2_, boolean p_213333_3_)
	{
		super.dropCustomDeathLoot(p_213333_1_, p_213333_2_, p_213333_3_);
		this.inventory.items.forEach(this::spawnAtLocation);
		this.inventory.clearContent();
	}

    public void remove()
    {
        if (!this.dead)
        {
            this.dead = true;
            this.level.broadcastEntityEvent(this, (byte)3);
        }
        super.remove(RemovalReason.DISCARDED);
    }

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte p_70103_1_)
	{
		switch(p_70103_1_)
		{
			case 2:
			case 33:
			case 36:
			case 37:
			case 44:
				boolean flag1 = p_70103_1_ == 33;
				boolean flag2 = p_70103_1_ == 36;
				boolean flag3 = p_70103_1_ == 37;
				boolean flag = p_70103_1_ == 44;
				this.animationSpeed = 1.5F;
				this.invulnerableTime = 20;
				this.hurtDuration = 10;
				this.hurtTime = this.hurtDuration;
				this.hurtDir = 0.0F;
				if (flag1)
				{
					this.playSound(SoundEvents.THORNS_HIT, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
				}

				DamageSource damagesource;
				if (flag3)
				{
					damagesource = DamageSource.ON_FIRE;
				}
				else if (flag2)
				{
					damagesource = DamageSource.DROWN;
				}
				else if (flag)
				{
					damagesource = DamageSource.SWEET_BERRY_BUSH;
				}
				else
				{
					damagesource = DamageSource.GENERIC;
				}

				SoundEvent soundevent1 = this.getHurtSound(damagesource);
				if (soundevent1 != null)
				{
					this.playSound(soundevent1, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
				}

				this.hurt(DamageSource.GENERIC, 0.0F);
				break;
			case 3:
				SoundEvent soundevent = this.getDeathSound();
				if (soundevent != null)
				{
					this.playSound(soundevent, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
				}
				this.setHealth(0.0F);
				this.remove();
				break;
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 31:
			case 32:
			case 34:
			case 35:
			case 38:
			case 39:
			case 40:
			case 41:
			case 42:
			case 43:
			case 45:
			case 53:
			default:
				super.handleEntityEvent(p_70103_1_);
				break;
			case 29:
			case 30:
			case 46:
				int i = 128;

				for (int j = 0; j < 128; ++j)
				{
					double d0 = (double) j / 127.0D;
					float f = (this.random.nextFloat() - 0.5F) * 0.2F;
					float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
					float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
					double d1 = Mth.lerp(d0, this.xo, this.getX()) + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth() * 2.0D;
					double d2 = Mth.lerp(d0, this.yo, this.getY()) + this.random.nextDouble() * (double) this.getBbHeight();
					double d3 = Mth.lerp(d0, this.zo, this.getZ()) + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth() * 2.0D;
					this.level.addParticle(ParticleTypes.PORTAL, d1, d2, d3, (double) f, (double) f1, (double) f2);
				}
				break;
			case 54:
				HoneyBlock.showJumpParticles(this);
				break;
		}
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag nbt)
	{
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("Items", 9))
		{
        	ListTag listnbt = nbt.getList("Items", 10);

    		for(int i = 0; i < this.inventory.items.size(); ++i)
			{
         		this.inventory.items.set(i, ItemStack.of(listnbt.getCompound(i)));
    		}
    	}
		if (nbt.contains("TurretRotations", 5))
		{
			ListTag turretrotations = nbt.getList("TurretRotations", 5);
			setTurretRotations(turretrotations.getFloat(0), turretrotations.getFloat(1));
		}
		if (nbt.contains("DealyTicks"))
		{
			this.delayticks = nbt.getInt("DealyTicks");
		}
		if (nbt.contains("UseTicks"))
		{
			this.useticks = nbt.getInt("UseTicks");
		}
	}

	public float getGlobalTurretYaw()
	{
		return Mth.lerp(0.5f, this.yRotO, this.getYRot()) + Mth.lerp(0.5f, this.turretyawprev, this.turretyaw);
	}

	public float getYaw()
	{
		return this.getYRot();
	}

	public void setYaw(float yaw)
	{
		this.setYRot(yaw);
		this.yRotO = this.getYRot();
		this.yBodyRot = this.getYRot();
		this.yHeadRot = this.yBodyRot;
	}

	public float getYawDest()
	{
		return this.yawdest;
	}

	public void setYawDest(float yaw)
	{
		this.yawdest = yaw;
	}

	public float getTurretPitch(float f)
	{
		return Mth.lerp(f, this.turretpitchprev, this.turretpitch);
	}

	public float getTurretYaw(float f)
	{
		return Mth.lerp(f, this.turretyawprev, this.turretyaw);
	}

	public float getTurretPitch()
	{
		return this.getTurretPitch(0.5f);
	}

	public float getTurretYaw()
	{
		return this.getTurretYaw(0.5f);
	}

	public void setTurretRotations(float pitch, float yaw)
	{
		this.turretpitchprev = this.turretpitch;
		this.turretyawprev = this.turretyaw;
		this.turretpitch = pitch;
		this.turretyaw = yaw;
	}

	public float getTurretPitchDest()
	{
		return this.turretpitchdest;
	}

	public float getTurretYawDest()
	{
		return this.turretyawdest;
	}

	public void setTurretRotationsDest(float pitch, float yaw)
	{
		this.turretpitchdest = pitch;
		this.turretyawdest = yaw;
	}

	public void updateMachineRender()
	{
		if (!this.level.isClientSide())
		{
			PacketHandler.sendPacketToAllInArea(new PacketMachine(
					this.getId(),
					this.delayticks,
					this.useticks,
					this.turretpitch,
					this.turretyaw), this.blockPosition(), SiegeMachines.RENDER_UPDATE_RANGE_SQR);
		}
	}

	public void updateYaw()
	{
		float newyaw = this.turn(this.getYaw(), this.getYawDest(), this.type.rotationspeed);

		if (this.getYaw() != newyaw)
			this.setYaw(newyaw);
	}

	public void updateTurretRotations()
	{
		float newyaw = this.turn(this.getTurretYaw(), this.getTurretYawDest(), this.type.turretspeed, this.type.turretminyaw, this.type.turretmaxyaw);
		boolean shouldrotate = this.checkYaw(newyaw, this.getTurretYaw(), this.type.turretspeed, this.type.turretminyaw, this.type.turretmaxyaw);
		float newpitch = shouldrotate ? this.turn(this.getTurretPitch(), this.getTurretPitchDest(), this.type.turretspeed, this.type.turretminpitch, this.type.turretmaxpitch) : this.getTurretPitch();

		if (this.turretpitch != newpitch || this.turretyaw != newyaw)
			this.setTurretRotations(newpitch, newyaw);
	}

	public boolean checkYaw(float yaw, float currentyaw, float speed, float minyaw, float maxuaw)
	{
		return !this.type.yawfirst || Math.abs(yaw - currentyaw) <= speed / 2 || yaw <= this.type.turretminyaw || yaw >= this.type.turretmaxyaw;
	}

	public float turn(float rotation, float rotationdest, float speed)
	{
		return this.turn(rotation, rotationdest, speed, -180, 180);
	}

	public float turn(float rotation, float rotationdest, float speed, float minrotation, float maxrotation)
	{
		boolean haslimit = maxrotation - minrotation < 360;

		float deltarotation = rotationdest - rotation;
		deltarotation = Mth.wrapDegrees(deltarotation);

		float newrotation;
		if (deltarotation > speed / 2)
		{
			newrotation = rotation + speed;
		}
		else if (deltarotation < -speed / 2)
		{
			newrotation = rotation - speed;
		}
		else
		{
			newrotation = rotation + deltarotation / 2;
		}

		if (haslimit)
		{
			if (newrotation > -minrotation)
			{
				newrotation = -minrotation;
			}
			if (newrotation < -maxrotation)
			{
				newrotation = -maxrotation;
			}
		}

		return newrotation;
	}

	protected static Vec3 applyRotations(Vec3 vec, double pitch, double yaw)
	{
		double d0 = vec.x * Math.cos(yaw) - vec.y * Math.sin(pitch) * Math.sin(yaw) - vec.z * Math.sin(yaw) * Math.cos(pitch);
		double d1 = vec.y * Math.cos(pitch) - vec.z * Math.sin(pitch);
		double d2 = vec.x * Math.sin(yaw) + vec.y * Math.sin(pitch) * Math.cos(yaw) + vec.z * Math.cos(yaw) * Math.cos(pitch);
		return new Vec3(d0, d1, d2);
	}

	protected float getVolumeFromDist(float maxvolume, float maxdist, float dist)
	{
		return maxvolume * Math.max(maxdist - dist, 0.0f) / maxdist;
	}

	public abstract void use(Player player);

	public abstract void useRealise();

	@Override
	public MachineContainer createMenu(int id, @NotNull Inventory inv, @NotNull Player player)
	{
		return new MachineContainer(id, inv, this);
	}

	public void openInventoryGui()
	{
		Entity passenger = this.getControllingPassenger();
		if (passenger instanceof ServerPlayer)
		{
			this.stopRiding();
			NetworkHooks.openGui((ServerPlayer) passenger, this, this.blockPosition());
		}
	}

	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity entity)
	{
		double yaw = (this.getGlobalTurretYaw()) * Math.PI / 180.0;

		return this.position().add(applyRotations(this.type.passengerpos, 0.0, yaw));
	}

	@Override
	public boolean shouldRiderSit()
	{
		return false;
	}

	@Override
    public void positionRider(@NotNull Entity entity)
    {
		MoveFunction setpos = Entity::setPos;
        if (this.hasPassenger(entity))
        {
            double yaw = (this.getGlobalTurretYaw()) * Math.PI / 180.0;

            Vec3 pos = this.position().add(applyRotations(this.type.passengerpos, 0.0, yaw));
            setpos.accept(entity, pos.x, pos.y, pos.z);
        }
    }


	public class MachineInventory implements Container, Nameable
	{
		public NonNullList<ItemStack> items = NonNullList.withSize(containersize, ItemStack.EMPTY);

		@Override
		public int getContainerSize()
		{
			return containersize;
		}

		@Override
		public boolean isEmpty()
		{
			return false;
		}

		@Override
		public @NotNull ItemStack getItem(int id)
		{
			return this.items.get(id);
		}

		@Override
		public @NotNull ItemStack removeItem(int id, int p_70298_2_)
		{
			return this.items.set(id, ItemStack.EMPTY);
		}

		@Override
		public @NotNull ItemStack removeItemNoUpdate(int id)
		{
			return this.items.remove(id);
		}

		@Override
		public void setItem(int id, @NotNull ItemStack item)
		{
			this.items.set(id, item);
		}

		@Override
		public void setChanged()
		{

		}

		@Override
		public boolean stillValid(@NotNull Player player)
		{
			return true;
		}

		@Override
		public void clearContent()
		{
			this.items = NonNullList.withSize(containersize, ItemStack.EMPTY);
		}

		public boolean containsItem(Item item)
    	{
        	return this.items.stream().anyMatch(itemstack -> itemstack.getItem().equals(item));
    	}

		public void putItem(Item item)
    	{
        	for (int i = 0; i < this.items.size(); ++i)
			{
				ItemStack itemstack = this.items.get(i);
				if (itemstack.isEmpty())
				{
					this.items.set(i, new ItemStack(item));
					break;
				}
				if (itemstack.getItem().equals(item) && itemstack.getCount() < itemstack.getMaxStackSize())
				{
					itemstack.setCount(itemstack.getCount() + 1);
					break;
				}
			}
    	}

		public void shrinkItem(Item item)
    	{
        	for (ItemStack itemstack : this.items)
			{
				if (itemstack.getItem().equals(item))
				{
					itemstack.shrink(1);
					break;
				}
			}
    	}

		@Override
		public Component getName()
		{
			return this.getName();
		}
	}
}