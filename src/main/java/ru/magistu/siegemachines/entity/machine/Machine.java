package ru.magistu.siegemachines.entity.machine;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.*;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import ru.magistu.siegemachines.ModTags;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.api.enitity.Useable;
import ru.magistu.siegemachines.config.SpecsConfig;
import ru.magistu.siegemachines.util.CartesianGeometry;
import ru.magistu.siegemachines.util.HitUtil;

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
    protected float turretyawprev = getTurretYaw();
    private boolean stationary;

    private static final EntityDataAccessor<Float> DATA_TURRET_PITCH = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_TURRET_YAW = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_TURRET_PITCH_DEST = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_TURRET_YAW_DEST = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_YAW_DEST = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_USE_TICKS = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_DELAY_TICKS = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PREVENT_PICKUP_TICKS = SynchedEntityData.defineId(Machine.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<List<ItemStack>> DATA_INVENTORY_ITEMS = SynchedEntityData.defineId(Machine.class, ItemStackListSerializer.INSTANCE);

    protected int deploymentticks = 0;

    protected Runnable reloadsoundplayer;
    protected Runnable usesoundplayer;
    protected Runnable usereleasesoundplayer;

    protected Machine(EntityType<? extends Mob> entitytype, Level level, MachineType type) {
        super(entitytype, level);
        this.type = type;
        setDelayTicks(type.specs.delaytime.get());
        this.inventory = new MachineInventory(this.type.containerrows);

        this.applyAttributeSpecs();
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

    public void applyAttributeSpecs() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(type.specs.durability.get());
        double knockbackresistance = type.specs.knockbackresistance.get();
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(knockbackresistance);
        this.stationary = knockbackresistance > 0.949;
    }

    public void setDeploymentTicks(int value) {
        deploymentticks = value;
    }

    public static AttributeSupplier.Builder setEntityAttributes(MachineType type) {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, type.specs.durability.getDefault())
                .add(Attributes.KNOCKBACK_RESISTANCE, type.specs.knockbackresistance.getDefault())
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D);
    }

    protected static final int USE_RELEASE = 66;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TURRET_PITCH, -25f);
        this.entityData.define(DATA_TURRET_YAW, 0f);
        this.entityData.define(DATA_TURRET_PITCH_DEST, -25f);
        this.entityData.define(DATA_TURRET_YAW_DEST, 0f);
        this.entityData.define(DATA_YAW_DEST, 0f);
        this.entityData.define(DATA_USE_TICKS, 0);
        this.entityData.define(DATA_DELAY_TICKS, 0);
        this.entityData.define(PREVENT_PICKUP_TICKS, 0);
        this.entityData.define(DATA_INVENTORY_ITEMS, new ArrayList<>());
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
        } else if (source.getEntity() instanceof Player player && source.is(DamageTypes.PLAYER_ATTACK) && canDropAsItem()) {
            if (getPreventPickupTicks() > 0) {
                player.sendSystemMessage(Component.translatable(SiegeMachines.ID + ".wait", this.getPreventPickupTicks() / 20.0f).withStyle(ChatFormatting.RED));
            } else {
                this.spawnAtLocation(this.getMachineItemWithData());
                this.dropEquipment();
                this.remove(RemovalReason.DISCARDED);
            }
            return false;
        } else {
            setPreventPickupTicks(SpecsConfig.PREVENT_PICKUP_COOLDOWN.get());
            return super.hurt(source, adjustDamage(source, amount));
        }
    }

    protected boolean canDropAsItem() {
        return this.getPassengers().isEmpty();
    }

    public float adjustDamage(DamageSource damagesource, float f) {
        if (damagesource.is(DamageTypeTags.IS_FIRE)) {
            f *= SpecsConfig.FIRE_DAMAGE_MULTIPLIER.get().floatValue();
        }

        if (damagesource.is(DamageTypeTags.IS_EXPLOSION)) {
            f *= SpecsConfig.EXPLOSION_DAMAGE_MULTIPLIER.get().floatValue();
        }

        if (damagesource.is(DamageTypes.ARROW)) {
            f *= SpecsConfig.ARROW_DAMAGE_MULTIPLIER.get().floatValue();
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

        if (isStationary() && !level().isClientSide()) {
            this.stop();
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

        if (this.getPreventPickupTicks() > 0) {
            this.setPreventPickupTicks(this.getPreventPickupTicks() - 1);
        }

        super.tick();
    }

    public void stop() {
        Vec3 delta = this.getDeltaMovement();
        double y = Math.min(delta.y, 0.0);
        Vec3 adjusted = new Vec3(0.0, y, 0.0);

        this.setDeltaMovement(adjusted);
        this.setSpeed(0.0f);

        if (adjusted.lengthSqr() < 1e-6) {
            this.hasImpulse = false;
        }
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
        for(ItemStack itemstack : this.inventory.getItems()) {
            CompoundTag compound = new CompoundTag();
            if (!itemstack.isEmpty()) {
                itemstack.save(compound);
            }
            listnbt.add(compound);
        }
        nbt.put("Items", listnbt);
        nbt.put("TurretRotations", this.newFloatList(this.getTurretPitch(), this.getTurretYaw()));
        nbt.putInt("DelayTicks", getDelayTicks());
        nbt.putInt("UseTicks", getUseTicks());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("Items", 9)) {
            ListTag listnbt = nbt.getList("Items", 10);

            for(int i = 0; i < this.inventory.getItems().size(); ++i) {
                this.inventory.getItems().set(i, ItemStack.of(listnbt.getCompound(i)));
            }
        }
        this.setYawDest(this.getYaw());
        if (nbt.contains("TurretRotations", 5)) {
            ListTag turretrotations = nbt.getList("TurretRotations", 5);
            this.setTurretRotations(turretrotations.getFloat(0), turretrotations.getFloat(1));
            this.setTurretRotationsDest(getTurretPitch(), getTurretYaw());
        }
        if (nbt.contains("DelayTicks")) {
            this.setDelayTicks(nbt.getInt("DelayTicks"));
        }
        if (nbt.contains("UseTicks")) {
            this.setUseTicks(nbt.getInt("UseTicks"));
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
        stack.addTagElement("EntityTag", nbt);
        return stack;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int i, boolean recentlyHit) {
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
        return this.entityData.get(DATA_YAW_DEST);
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

    public void setUseTicks(int ticks) {
        entityData.set(DATA_USE_TICKS, ticks);
    }

    public int getDelayTicks() {
        return entityData.get(DATA_DELAY_TICKS);
    }

    public void setDelayTicks(int ticks) {
        entityData.set(DATA_DELAY_TICKS, ticks);
    }

    public int getPreventPickupTicks() {
        return entityData.get(PREVENT_PICKUP_TICKS);
    }

    public void setPreventPickupTicks(int ticks) {
        entityData.set(PREVENT_PICKUP_TICKS, ticks);
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

    protected void setTurretPitchDest(float pitch) {
        entityData.set(DATA_TURRET_PITCH_DEST, pitch);
    }

    protected void setTurretYaw(float yaw) {
        entityData.set(DATA_TURRET_YAW, yaw);
    }

    public void setYawDest(float yaw) {
        entityData.set(DATA_YAW_DEST, yaw);
    }

    public float getTurretPitchDest() {
        return this.entityData.get(DATA_TURRET_PITCH_DEST);
    }

    protected void setTurretYawDest(float yaw) {
        this.entityData.set(DATA_TURRET_YAW_DEST, yaw);
    }

    public float getTurretYawDest() {
        return this.entityData.get(DATA_TURRET_YAW_DEST);
    }

    public void setTurretRotationsDest(float pitch, float yaw) {
        this.setTurretPitchDest(pitch);
        this.setTurretYawDest(yaw);
    }

    public void updateYaw() {
        float newyaw = this.turn(this.getYaw(), this.getYawDest(), this.type.rotationspeed);

        if (this.getYaw() != newyaw)
            this.setYaw(newyaw);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive() && this.isVehicle() && getUseTicks() <= 0 && getDelayTicks() <= 0) {
            if (this.getControllingPassenger() instanceof Player player) {
                this.setTurretRotationsDest(player.getXRot(), player.getYRot() - this.getYaw());
                this.setYawDest(player.getYRot());
            }
            this.updateYaw();
            this.updateTurretRotations();
        } else {
            this.turretpitchprev = entityData.get(DATA_TURRET_PITCH);
            this.turretyawprev = entityData.get(DATA_TURRET_YAW);
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
        Vec3 origin = this.position();
        double yaw = this.getGlobalTurretYaw() * Math.PI / 180.0;
        Vec3 delta = CartesianGeometry.applyRotations(this.type.passengerpos, 0.0, yaw);
        HitResult hit = HitUtil.getBlockHitResult(origin, delta, this.level(), ClipContext.Block.COLLIDER);
        if (hit.getType() == HitResult.Type.MISS) {
            return origin.add(delta);
        }
        return hit.getLocation();
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

    @Override
    public boolean isStationary() {
        return stationary;
    }

    public int getDelayTime() {
        return type.specs.delaytime.get();
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        AABB box = this.getBoundingBox();
        return box.inflate(box.getXsize(), box.getYsize(), box.getZsize());
    }

    @Override
    public MachineType getMachineType() {
        return this.type;
    }

    @Override
    public LivingEntity asLivingEntity() {
        return this;
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
            if (!this.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
                stack.setCount(this.getMaxStackSize());
            }
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
                if (itemstack.isEmpty() || ItemStack.isSameItemSameTags(itemstack, stack) && itemstack.getCount() < itemstack.getMaxStackSize()) {
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
                if (ItemStack.isSameItemSameTags(itemstack, stack)) {
                    this.moveItemsBetweenStacks(stack, itemstack);
                    if (stack.isEmpty()) {
                        return;
                    }
                }
            }

        }

        private void moveItemsBetweenStacks(ItemStack stack, ItemStack other) {
            int i = this.getMaxStackSize();
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