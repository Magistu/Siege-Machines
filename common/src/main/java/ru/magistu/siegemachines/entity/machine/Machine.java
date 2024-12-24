package ru.magistu.siegemachines.entity.machine;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.component.CustomData;
import ru.magistu.siegemachines.ModTags;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import ru.magistu.siegemachines.util.CartesianGeometry;

import javax.annotation.Nullable;

public abstract class Machine extends Mob implements MenuProvider
{

	public MachineInventory inventory;
	public final MachineType type;

	protected float turretpitchprev = -25;
	protected float turretpitchdest = -25;
	protected float turretyawprev = getTurretYaw();
	protected float turretyawdest = getTurretYaw();
	protected float yawdest = this.getYRot();

	private static final EntityDataAccessor<Float> DATA_TURRET_PITCH = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_TURRET_YAW = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Integer> DATA_USE_TICKS = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DATA_DELAY_TICKS = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.INT);

	protected int renderupdateticks = 0;
	public int deploymentticks = 0;

	protected Machine(EntityType<? extends Mob> entitytype, Level level, MachineType type)
    {
        super(entitytype, level);
		this.type = type;
		setDelayTicks(type.specs.delaytime.get());
		this.inventory = new MachineInventory(9 * this.type.containerrows);
		
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(type.specs.durability.get());
		this.setHealth(type.specs.durability.get());
	}

	public static AttributeSupplier.Builder setEntityAttributes(MachineType type) {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, type.specs.durability.getDefault())
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.5F)
				.add(Attributes.MOVEMENT_SPEED, 0.0D)
				.add(Attributes.ATTACK_DAMAGE, 0.0D)
				.add(Attributes.FOLLOW_RANGE, 0.0D);
	}

	protected static final int USE_REALISE = 66;

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_TURRET_PITCH,-25f);
		builder.define(DATA_TURRET_YAW,0f);
		builder.define(DATA_USE_TICKS,-1);
		builder.define(DATA_DELAY_TICKS,0);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource damagesource) {
		return damagesource.is(ModTags.DamageTypes.MACHINE_IMMUNE_TO) || super.isInvulnerableTo(damagesource);
	}

	// TODO: adjust damage on hurt
	public float adjustDamage(DamageSource damagesource, float f) {
		if (damagesource.is(DamageTypeTags.IS_FIRE)) {
			f *= 1.5f;
		}

		if (damagesource.is(DamageTypeTags.IS_EXPLOSION)) {
			f *= 1.25f;
		}

		if (damagesource.isCreativePlayer()) {
			f *= 2;
		}

		if (damagesource.getEntity() instanceof AbstractArrow) {
			f *= 0.5f;
		}

		return f;
	}

	// TODO: get machine item
	public abstract Item getMachineItem();

	@Override
	public void tick() {
		if (this.deploymentticks > 0) {
			this.deploymentticks--;
		}
		super.tick();
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
	public LivingEntity getControllingPassenger() {
		return this.getFirstPassenger() instanceof LivingEntity livingentity ? livingentity : super.getControllingPassenger();
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
    			itemstack.save(registryAccess(),compoundnbt);
    		}
    		listnbt.add(compoundnbt);
    	}
    	nbt.put("Items", listnbt);
		nbt.put("TurretRotations", this.newFloatList(getTurretPitch(), getTurretYaw()));
		nbt.putInt("DelayTicks",getDelayTicks());
		nbt.putInt("UseTicks", getUseTicks());
	}

	@Override
	protected void dropEquipment() {
		super.dropEquipment();
		this.inventory.items.forEach(this::spawnAtLocation);
		this.inventory.clearContent();
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
         		this.inventory.items.set(i, ItemStack.parseOptional(registryAccess(),listnbt.getCompound(i)));
    		}
    	}
		if (nbt.contains("TurretRotations", 5))
		{
			ListTag turretrotations = nbt.getList("TurretRotations", 5);
			setTurretRotations(turretrotations.getFloat(0), turretrotations.getFloat(1));
		}
		if (nbt.contains("DelayTicks"))
		{
			setDelayTicks(nbt.getInt("DelayTicks"));
		}
		if (nbt.contains("UseTicks"))
		{
			setUseTicks(nbt.getInt("UseTicks"));
		}
	}

	public float getGlobalTurretYaw()
	{
		return Mth.lerp(0.5f, this.yRotO, this.getYRot()) + Mth.lerp(0.5f, this.turretyawprev,getTurretYaw());
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
		return Mth.lerp(f, this.turretpitchprev, entityData.get(DATA_TURRET_PITCH));
	}

	public float getTurretYaw(float f)
	{
		return Mth.lerp(f, this.turretyawprev, entityData.get(DATA_TURRET_YAW));
	}

	public float getTurretPitch()
	{
		return this.getTurretPitch(0.5f);
	}

	public float getTurretYaw()
	{
		return this.getTurretYaw(0.5f);
	}

	public int getUseTicks() {
		return entityData.get(DATA_USE_TICKS);
	}

	public void setUseTicks(int useTicks) {
		entityData.set(DATA_USE_TICKS,useTicks);
	}

	public int getDelayTicks() {
		return entityData.get(DATA_DELAY_TICKS);
	}

	public void setDelayTicks(int delayTicks) {
		entityData.set(DATA_DELAY_TICKS,delayTicks);
	}

	public void setTurretRotations(float pitch, float yaw)
	{
		this.turretpitchprev = getTurretPitch();
		this.turretyawprev = getTurretYaw();
		setTurretPitch(pitch);
		entityData.set(DATA_TURRET_YAW, yaw);
	}

	protected void setTurretPitch(float pitch) {
		entityData.set(DATA_TURRET_PITCH, pitch);
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

	public void updateMachineRender() {
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
		boolean shouldrotate = this.checkYaw(newyaw, this.getTurretYaw(), this.type.turretspeed);
		float newpitch = shouldrotate ? this.turn(this.getTurretPitch(), this.getTurretPitchDest(), this.type.turretspeed, this.type.turretminpitch, this.type.turretmaxpitch) : this.getTurretPitch();

		if (getTurretPitch() != newpitch || getTurretYaw() != newyaw)
			this.setTurretRotations(newpitch, newyaw);
	}

	public boolean checkYaw(float yaw, float currentYaw, float speed) {
		return !this.type.yawfirst || Math.abs(yaw - currentYaw) <= speed / 2 || yaw <= this.type.turretminyaw || yaw >= this.type.turretmaxyaw;
	}

	public float turn(float rotation, float rotationDest, float speed) {
		return this.turn(rotation, rotationDest, speed, -180, 180);
	}

	public float turn(float rotation, float rotationDest, float speed, float minRotation, float maxRotation)
	{
		boolean hasLimit = maxRotation - minRotation < 360;

		float deltaRotation = rotationDest - rotation;
		deltaRotation = Mth.wrapDegrees(deltaRotation);

		float newRotation;
		if (deltaRotation > speed / 2) {
			newRotation = rotation + speed;
		}
		else if (deltaRotation < -speed / 2) {
			newRotation = rotation - speed;
		}
		else {
			newRotation = rotation + deltaRotation / 2;
		}

		if (hasLimit) {
			if (newRotation > -minRotation) {
				newRotation = -minRotation;
			}
			if (newRotation < -maxRotation) {
				newRotation = -maxRotation;
			}
		}

		return newRotation;
	}

	// TODO: save inventory information
	public ItemStack getMachineItemWithData() {
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
		stack.set(DataComponents.ENTITY_DATA, CustomData.of(nbt));
		return stack;
	}

	protected float getVolumeFromDist(float dist) {
		return (float) 0.5 * Math.max((float) 6.0 - dist, 0.0f) / (float) 6.0;
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == USE_REALISE) {
			useRealise();
		} else {
			super.handleEntityEvent(id);
		}
	}

	public abstract void use(Player player);

	public abstract void useRealise();

	@Override
	public ChestMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
		return new ChestMenu(MenuType.GENERIC_9x1,id, inv, inventory,1);
	}

	public void openInventoryGui() {
		Entity passenger = this.getControllingPassenger();
		if (passenger instanceof ServerPlayer serverPlayer) {
			this.stopRiding();
			serverPlayer.openMenu(this);
		}
	}

	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity entity) {
		double yaw = (this.getGlobalTurretYaw()) * Math.PI / 180.0;

		return this.position().add(CartesianGeometry.applyRotations(this.type.passengerpos, 0.0, yaw));
	}

	@Override
    public void positionRider(@NotNull Entity entity,Entity.MoveFunction moveFunction) {
		MoveFunction setPos = Entity::setPos;
        if (this.hasPassenger(entity)) {
            double yaw = (this.getGlobalTurretYaw()) * Math.PI / 180.0;
            Vec3 pos = this.position().add(CartesianGeometry.applyRotations(this.type.passengerpos, 0.0, yaw));
			setPos.accept(entity, pos.x, pos.y, pos.z);
        }
    }


	public static class MachineInventory implements Container, Nameable 
	{
		private final int containersize;
		public NonNullList<ItemStack> items;

		public MachineInventory(int rows)
		{
			this.containersize = 9 * rows;
			this.items = NonNullList.withSize(this.containersize, ItemStack.EMPTY);
		}

		@Override
		public int getContainerSize()
		{
			return this.containersize;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public @NotNull ItemStack getItem(int id) {
			return this.items.get(id);
		}

		@Override
		public @NotNull ItemStack removeItem(int id, int p_70298_2_) {
			return this.items.set(id, ItemStack.EMPTY);
		}

		@Override
		public @NotNull ItemStack removeItemNoUpdate(int id) {
			return this.items.remove(id);
		}

		@Override
		public void setItem(int id, @NotNull ItemStack item) {
			this.items.set(id, item);
		}

		@Override
		public void setChanged() {}

		@Override
		public boolean stillValid(@NotNull Player player)
		{
			return true;
		}

		@Override
		public void clearContent() 
		{
			this.items = NonNullList.withSize(this.containersize, ItemStack.EMPTY);
		}

		public boolean containsItem(Item item) {
        	return this.items.stream().anyMatch(itemStack -> itemStack.getItem().equals(item));
    	}

		public void putItem(Item item) {
        	for (int i = 0; i < this.items.size(); ++i) {
				ItemStack itemstack = this.items.get(i);
				if (itemstack.isEmpty()) {
					this.items.set(i, new ItemStack(item));
					break;
				}
				if (itemstack.getItem().equals(item) && itemstack.getCount() < itemstack.getMaxStackSize()) {
					itemstack.setCount(itemstack.getCount() + 1);
					break;
				}
			}
    	}

		public void shrinkItem(Item item) {
        	for (ItemStack itemstack : this.items) {
				if (itemstack.getItem().equals(item)) {
					itemstack.shrink(1);
					break;
				}
			}
    	}

		@Override
		public Component getName() {
			return this.getName();
		}
	}
}