package ru.magistu.siegemachines.entity.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import ru.magistu.siegemachines.ModTags;
import ru.magistu.siegemachines.api.enitity.Useable;
import ru.magistu.siegemachines.config.SpecsConfig;
import ru.magistu.siegemachines.util.CartesianGeometry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public abstract class Machine extends Mob implements MenuProvider, Useable {

    public MachineInventory inventory;
    public final MachineType type;

    protected float turretpitchprev = -25;
    protected float turretpitchdest = -25;
    protected float turretyawprev = getTurretYaw();
    protected float turretyawdest = getTurretYaw();
    protected float yawdest = this.getYRot();

    public static final EntityDataSerializer<List<ItemStack>> ITEM_STACKS_SERIALIZER = new EntityDataSerializer<>() {
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, List<ItemStack>> codec() {
            return ItemStack.OPTIONAL_LIST_STREAM_CODEC;
        }

        public @NotNull List<ItemStack> copy(@NotNull List<ItemStack> itemstacks) {
            return new ArrayList<>(itemstacks);
        }
    };

    private static final EntityDataAccessor<Float> DATA_TURRET_PITCH = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_TURRET_YAW = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_USE_TICKS = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_DELAY_TICKS = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<List<ItemStack>> DATA_INVENTORY_ITEMS = SynchedEntityData.defineId(Machine.class, ITEM_STACKS_SERIALIZER);

    public int deploymentticks = 0;

    protected Runnable reloadsoundplayer;
    protected Runnable usesoundplayer;
    protected Runnable usereleasesoundplayer;

    protected Machine(EntityType<? extends Mob> entitytype, Level level, MachineType type) {
        super(entitytype, level);
        this.type = type;
        setDelayTicks(type.specs.delaytime.get());
        this.inventory = new MachineInventory(this.type.containerrows);

        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(type.specs.durability.get());
        this.setHealth(type.specs.durability.get());

        this.setTurretRotations(-type.turretinitpitch, type.turretinityaw);
        this.turretpitchprev = -type.turretinitpitch;
        this.turretyawprev = type.turretinityaw;
        this.setTurretRotationsDest(-type.turretinitpitch, type.turretinityaw);

        if (this.type.reloadsoundduration > 0 && this.type.reloadvolume > 0 && this.type.reloadsound != null) {
            this.reloadsoundplayer = this::playReloadSound;
        } else {
            this.reloadsoundplayer = () -> {
            };
        }
        if (this.type.usevolume > 0 && this.type.usesound != null) {
            this.usesoundplayer = this::playUseSound;
        } else {
            this.usesoundplayer = () -> {
            };
        }
        if (this.type.usereleasevolume > 0 && this.type.usereleasesound != null) {
            this.usereleasesoundplayer = this::playUseReleaseSound;
        } else {
            this.usereleasesoundplayer = () -> {
            };
        }
    }

    public static AttributeSupplier.Builder setEntityAttributes(MachineType type) {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, type.specs.durability.getDefault())
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5F)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D);
    }

    protected static final int USE_RELEASE = 66;

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TURRET_PITCH, -25f);
        builder.define(DATA_TURRET_YAW, 0f);
        builder.define(DATA_USE_TICKS, 0);
        builder.define(DATA_DELAY_TICKS, 0);
        builder.define(DATA_INVENTORY_ITEMS, new ArrayList<>());
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damagesource) {
        return damagesource.is(ModTags.DamageTypes.MACHINE_IMMUNE_TO) || super.isInvulnerableTo(damagesource);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (this.level().isClientSide) {
            return false;
        } else if (this.isDeadOrDying()) {
            return false;
        } else if (source.is(DamageTypeTags.IS_FIRE) && this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            return false;
        } else if (source.getEntity() instanceof Player && source.is(DamageTypes.PLAYER_ATTACK) && this.getPassengers().isEmpty()) {
            this.spawnAtLocation(this.getMachineItemWithData());
            this.dropEquipment();
            this.remove(RemovalReason.DISCARDED);
            return false;
        } else {
            return super.hurt(source, adjustDamage(source, amount));
        }
    }

    public float adjustDamage(DamageSource damagesource, float f) {
        if (damagesource.is(DamageTypeTags.IS_FIRE)) {
            f *= SpecsConfig.FIRE_DAMAGE_MULTIPLIER.get();
        }

        if (damagesource.is(DamageTypeTags.IS_EXPLOSION)) {
            f *= SpecsConfig.EXPLOSION_DAMAGE_MULTIPLIER.get();
        }

        if (damagesource.is(DamageTypes.ARROW)) {
            f *= SpecsConfig.ARROW_DAMAGE_MULTIPLIER.get();
        }

        return f;
    }

    public Item getMachineItem() {
        return type.machineitem.get();
    }

    @Override
    public void tick() {
        int useticks = getUseTicks();
        if (useticks > 0) {
            setUseTicks(--useticks);
            if (useticks <= 0) {
                setUseTicks(0);
                setDelayTicks(this.type.specs.delaytime.get());
            }
        }

        if (isStationary() && !level().isClientSide() && this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.0, 1.0, 0.0));
        }

        int delayticks = getDelayTicks();
        if (delayticks > 0 && this.hasControllingPassenger()) {
            if (delayticks % this.type.reloadsoundduration == 0) {
                this.reloadsoundplayer.run();
            }
            setDelayTicks(--delayticks);
        }

        if (this.deploymentticks > 0) {
            this.deploymentticks--;
        }

        super.tick();
    }

    private void playReloadSound() {
        Vec3 pos = this.position();
        this.level().playLocalSound(pos.x, pos.y, pos.z, this.type.reloadsound.get(), this.getSoundSource(), this.type.reloadvolume, 1.0f, false);
    }

    private void playUseSound() {
        Vec3 pos = this.position();
        this.level().playLocalSound(pos.x, pos.y, pos.z, this.type.usesound.get(), this.getSoundSource(), this.type.usevolume, 1.0f, false);
    }

    private void playUseReleaseSound() {
        Vec3 pos = this.position();
        this.level().playLocalSound(pos.x, pos.y, pos.z, this.type.usereleasesound.get(), this.getSoundSource(), this.type.usereleasevolume, 1.0f, false);
    }

    @Override
    public float getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState blockState, FluidState fluidState, float resistance) {
        System.out.println("resistance: " + resistance);
        if (resistance < 4.3f) {
            return 3.0f;
        }
        return resistance * 0.7f;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource p_184601_1_) {
        return null;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return false;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.getFirstPassenger() instanceof LivingEntity livingentity ? livingentity : super.getControllingPassenger();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        ListTag listnbt = new ListTag();
        for (ItemStack itemstack : this.inventory.getItems()) {
            CompoundTag compound;
            if (!itemstack.isEmpty()) {
                compound = (CompoundTag) ItemStack.CODEC.encodeStart(registryAccess().createSerializationContext(NbtOps.INSTANCE), itemstack).resultOrPartial(
                        error -> System.err.println("Failed to save ItemStack: " + error)).orElse(new CompoundTag());
            } else {
                compound = new CompoundTag();
            }
            listnbt.add(compound);
        }
        nbt.put("Items", listnbt);
        nbt.putInt("DelayTicks", getDelayTicks());
        nbt.putInt("UseTicks", getUseTicks());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("Items", 9)) {
            ListTag listnbt = nbt.getList("Items", 10);

            for (int i = 0; i < this.inventory.getItems().size(); ++i) {
                this.inventory.getItems().set(i, ItemStack.parseOptional(registryAccess(), listnbt.getCompound(i)));
            }
        }
        if (nbt.contains("DelayTicks", 3)) {
            setDelayTicks(nbt.getInt("DelayTicks"));
        }
        if (nbt.contains("UseTicks", 3)) {
            setUseTicks(nbt.getInt("UseTicks"));
        }
    }

    public ItemStack getMachineItemWithData() {
        ItemStack stack = new ItemStack(this.getMachineItem());
        CompoundTag nbt = new CompoundTag();
        this.saveWithoutId(nbt);
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
        nbt.remove("Items");
        String id = this.getEncodeId();
        if (id != null) {
            nbt.putString("id", id);
        }
        stack.set(DataComponents.ENTITY_DATA, CustomData.of(nbt));
//		System.out.println(nbt);
        return stack;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        Arrays.stream(this.type.wreckage.get()).forEach(this::spawnAtLocation);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        this.inventory.getItems().forEach(this::spawnAtLocation);
        this.inventory.clearContent();
    }

    public float getGlobalTurretYaw() {
        return Mth.lerp(0.5f, this.yRotO, this.getYRot()) + getTurretYaw();
    }

    public float getYaw() {
        return this.getYRot();
    }

    public void setYaw(float yaw) {
        this.setYRot(yaw);
        this.yRotO = this.getYRot();
        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.yBodyRot;
    }

    public float getYawDest() {
        return this.yawdest;
    }

    public void setYawDest(float yaw) {
        this.yawdest = yaw;
    }

    public float getTurretPitch(float f) {
        return Mth.lerp(f, this.turretpitchprev, entityData.get(DATA_TURRET_PITCH));
    }

    public float getTurretYaw(float f) {
        return Mth.lerp(f, this.turretyawprev, entityData.get(DATA_TURRET_YAW));
    }

    public float getTurretPitch() {
        return this.getTurretPitch(0.5f);
    }

    public float getTurretYaw() {
        return this.getTurretYaw(0.5f);
    }

    public int getUseTicks() {
        return entityData.get(DATA_USE_TICKS);
    }

    public void setUseTicks(int useTicks) {
        entityData.set(DATA_USE_TICKS, useTicks);
    }

    public int getDelayTicks() {
        return entityData.get(DATA_DELAY_TICKS);
    }

    public void setDelayTicks(int delayTicks) {
        entityData.set(DATA_DELAY_TICKS, delayTicks);
    }

    public void setTurretRotations(float pitch, float yaw) {
        this.turretpitchprev = getTurretPitch();
        this.turretyawprev = getTurretYaw();
        setTurretPitch(pitch);
        setTurretYaw(yaw);
    }

    protected void setTurretPitch(float pitch) {
        entityData.set(DATA_TURRET_PITCH, pitch);
    }

    protected void setTurretYaw(float yaw) {
        entityData.set(DATA_TURRET_YAW, yaw);
    }

    public float getTurretPitchDest() {
        return this.turretpitchdest;
    }

    public float getTurretYawDest() {
        return this.turretyawdest;
    }

    public void setTurretRotationsDest(float pitch, float yaw) {
        this.turretpitchdest = pitch;
        this.turretyawdest = yaw;
    }

    public void updateYaw() {
        float newyaw = this.turn(this.getYaw(), this.getYawDest(), this.type.rotationspeed);

        if (this.getYaw() != newyaw)
            this.setYaw(newyaw);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
            if (this.isVehicle() && getUseTicks() <= 0 && getDelayTicks() <= 0) {
                LivingEntity livingentity = this.getControllingPassenger();

                this.setTurretRotationsDest(livingentity.getXRot(), livingentity.getYRot() - this.getYaw());
                this.setYawDest(livingentity.getYRot());

                this.updateYaw();
                this.updateTurretRotations();
            }
        }
    }

    public void updateTurretRotations() {
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

    public float turn(float rotation, float rotationDest, float speed, float minRotation, float maxRotation) {
        boolean hasLimit = maxRotation - minRotation < 360;

        float deltaRotation = rotationDest - rotation;
        deltaRotation = Mth.wrapDegrees(deltaRotation);

        float newRotation;
        if (deltaRotation > speed / 2) {
            newRotation = rotation + speed;
        } else if (deltaRotation < -speed / 2) {
            newRotation = rotation - speed;
        } else {
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

    @Override
    public void handleEntityEvent(byte id) {
        if (id == USE_RELEASE) {
            useRelease();
        } else {
            super.handleEntityEvent(id);
        }
    }

    public abstract void use(LivingEntity entity);

    public abstract void useRelease();

    @Override
    public ChestMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
        return new ChestMenu(MenuType.GENERIC_9x1, id, inv, inventory, 1);
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
    public void positionRider(@NotNull Entity entity, @NotNull MoveFunction moveFunction) {
        MoveFunction setPos = Entity::setPos;
        if (this.hasPassenger(entity)) {
            double yaw = (this.getGlobalTurretYaw()) * Math.PI / 180.0;
            Vec3 pos = this.position().add(CartesianGeometry.applyRotations(this.type.passengerpos, 0.0, yaw));
            setPos.accept(entity, pos.x, pos.y, pos.z);
        }
    }

    @SuppressWarnings("unused")
    public boolean shouldRiderSit() {
        return false;
    }

    public boolean isStationary() {
        return false;
    }

    public int getDelayTime() {
        return type.specs.delaytime.get();
    }

    public class MachineInventory implements Container, StackedContentsCompatible, Nameable {
        private final int size;

        public MachineInventory(int rows) {
            this.size = 9 * rows;
            this.setItems(IntStream.range(0, this.size).boxed().map(i -> ItemStack.EMPTY).collect(Collectors.collectingAndThen(Collectors.toList(), ArrayList::new)));
        }

        @Override
        public int getContainerSize() {
            return this.size;
        }

        @Override
        public boolean isEmpty() {
            return this.getItems().isEmpty();
        }

        public void setItems(List<ItemStack> itemstacks) {
            Machine.this.entityData.set(DATA_INVENTORY_ITEMS, itemstacks);
        }

        public @NotNull List<ItemStack> getItems() {
            return Machine.this.entityData.get(DATA_INVENTORY_ITEMS);
        }

        @Override
        public void setChanged() {
            Machine.this.entityData.set(DATA_INVENTORY_ITEMS, this.getItems(), true);
        }

        @Override
        public @NotNull ItemStack getItem(int i) {
            return this.getItems().get(i);
        }

        @Override
        public @NotNull ItemStack removeItem(int i, int count) {
            ItemStack itemstack = ContainerHelper.removeItem(getItems(), i, count);
            if (!itemstack.isEmpty()) {
                this.setChanged();
            }

            return itemstack;
        }

        @Override
        public @NotNull ItemStack removeItemNoUpdate(int i) {
            return ContainerHelper.removeItem(getItems(), i, 1);
        }

        @Override
        public void setItem(int i, @NotNull ItemStack stack) {
            this.getItems().set(i, stack);
            stack.limitSize(this.getMaxStackSize(stack));
            this.setChanged();
        }

        @Override
        public boolean stillValid(@NotNull Player player) {
            return true;
        }

        @Override
        public void clearContent() {
            this.setItems(NonNullList.withSize(this.size, ItemStack.EMPTY));
        }

        public boolean containsItem(Item item) {
            return this.getItems().stream().anyMatch(itemStack -> itemStack.getItem().equals(item));
        }

        public boolean canAddItem(ItemStack stack) {
            boolean flag = false;

            for (ItemStack itemstack : this.getItems()) {
                if (itemstack.isEmpty() || ItemStack.isSameItemSameComponents(itemstack, stack) && itemstack.getCount() < itemstack.getMaxStackSize()) {
                    flag = true;
                    break;
                }
            }

            return flag;
        }

        public ItemStack addItem(ItemStack stack) {
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                ItemStack itemstack = stack.copy();
                this.moveItemToOccupiedSlotsWithSameType(itemstack);
                if (itemstack.isEmpty()) {
                    return ItemStack.EMPTY;
                } else {
                    this.moveItemToEmptySlots(itemstack);
                    return itemstack.isEmpty() ? ItemStack.EMPTY : itemstack;
                }
            }
        }

        private void moveItemToOccupiedSlotsWithSameType(ItemStack stack) {
            for (int i = 0; i < this.size; ++i) {
                ItemStack itemstack = this.getItem(i);
                if (ItemStack.isSameItemSameComponents(itemstack, stack)) {
                    this.moveItemsBetweenStacks(stack, itemstack);
                    if (stack.isEmpty()) {
                        return;
                    }
                }
            }

        }

        private void moveItemsBetweenStacks(ItemStack stack, ItemStack other) {
            int i = this.getMaxStackSize(other);
            int j = Math.min(stack.getCount(), i - other.getCount());
            if (j > 0) {
                other.grow(j);
                stack.shrink(j);
                this.setChanged();
            }

        }

        private void moveItemToEmptySlots(ItemStack stack) {
            for (int i = 0; i < this.size; ++i) {
                ItemStack itemstack = this.getItem(i);
                if (itemstack.isEmpty()) {
                    this.setItem(i, stack.copyAndClear());
                    return;
                }
            }

        }

        public ItemStack removeItemType(Item item, int amount) {
            ItemStack itemstack = new ItemStack(item, 0);

            for (int i = this.size - 1; i >= 0; --i) {
                ItemStack itemstack1 = this.getItem(i);
                if (itemstack1.getItem().equals(item)) {
                    int j = amount - itemstack.getCount();
                    ItemStack itemstack2 = itemstack1.split(j);
                    itemstack.grow(itemstack2.getCount());
                    if (itemstack.getCount() == amount) {
                        break;
                    }
                }
            }

            if (!itemstack.isEmpty()) {
                this.setChanged();
            }

            return itemstack;
        }

        @Override
        public @NotNull Component getName() {
            return Machine.this.getName();
        }

        @Override
        public void fillStackedContents(@NotNull StackedContents helper) {

            for (ItemStack itemstack : this.getItems()) {
                helper.accountStack(itemstack);
            }
        }
    }
}