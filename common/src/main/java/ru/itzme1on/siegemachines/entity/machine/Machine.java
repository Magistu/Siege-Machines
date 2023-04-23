package ru.itzme1on.siegemachines.entity.machine;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.HoneyBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.itzme1on.siegemachines.network.PacketMachine;

public abstract class Machine extends MobEntity /*implements NamedScreenHandlerFactory*/ {
    public MachineInventory inventory = new MachineInventory();

    public static int rows = 1;
    public final MachineType type;
    private float turretPitch = -25.0f;
    private float turretPitchPrev = this.turretPitch;
    protected float turretPitchDest = this.turretPitch;
    private float turretYaw = 0.0f;
    private float turretYawPrev = this.turretYaw;
    protected float turretYawDest = this.turretYaw;
    protected float yawDest = this.getYaw();
    public int useTicks = -1;
    public int delayTicks;
    protected int renderUpdateTicks = 0;
    public int deploymentTicks = 0;

    protected Machine(EntityType<? extends MobEntity> entityType, World world, MachineType type) {
        super(entityType, world);
        this.type = type;
        this.delayTicks = this.type.delayTime;
        rows = this.type.rows;
    }

    public static DefaultAttributeContainer.Builder setEntityAttributes(MachineType type) {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, type.health)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5F)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 0.0D);
    }

    public ItemStack getMachineItemWithData() {
        ItemStack stack = new ItemStack(this.getMachineItem());
        NbtCompound nbt = this.writeNbt(new NbtCompound());
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
        stack.setSubNbt("EntityTag", nbt);

        return stack;
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource damageSource) {
        return damageSource == DamageSource.CACTUS ||
                damageSource == DamageSource.WITHER ||
                damageSource == DamageSource.MAGIC ||
                damageSource == DamageSource.DROWN ||
                damageSource == DamageSource.STARVE ||
                super.isInvulnerableTo(damageSource);
    }

    public float adjustDamage(DamageSource damagesource, float f) {
        if (damagesource.isFire())
            f *= 1.5f;

        if (damagesource.isExplosive())
            f *= 1.25f;

        if (damagesource.isSourceCreativePlayer())
            f *= 2;

        if (damagesource.getAttacker() instanceof PersistentProjectileEntity)
            f *= 0.5f;

        return f;
    }

    public abstract Item getMachineItem();

    @Override
    public void tick() {
        if (this.deploymentTicks > 0)
            this.deploymentTicks--;

        super.tick();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof PlayerEntity
                && !source.isProjectile()
                && !source.isExplosive()
                && !source.isMagic()
                && this.getPassengerList().isEmpty()) {
            this.dropStack(this.getMachineItemWithData());
            this.remove();

            return false;
        }

        if (this.isInvulnerableTo(source))
            return false;

        else if (this.world.isClient)
            return false;

        else if (this.isDead())
            return false;

        else if (source.isFire() && this.hasStatusEffect(StatusEffects.FIRE_RESISTANCE))
            return false;

        else {
            amount = adjustDamage(source, amount);

            this.despawnCounter = 0;

            this.limbDistance = 1.5F;
            boolean flag1 = true;
            if ((float) this.timeUntilRegen > 10.0F) {
                if (amount <= this.lastDamageTaken)
                    return false;

                this.applyDamage(source, amount - this.lastDamageTaken);
                this.lastDamageTaken = amount;
                flag1 = false;
            }

            else {
                this.lastDamageTaken = amount;
                this.timeUntilRegen = 20;
                this.applyDamage(source, amount);
                this.maxHurtTime = 10;
                this.hurtTime = this.maxHurtTime;
            }

            this.knockbackVelocity = 0.0F;
            Entity entity1 = source.getAttacker();

            if (entity1 != null) {
                if (entity1 instanceof LivingEntity)
                    this.setAttacker((LivingEntity) entity1);

                if (entity1 instanceof PlayerEntity) {
                    this.playerHitTimer = 1;
                    this.attackingPlayer = (PlayerEntity) entity1;
                }

                else if (entity1 instanceof TameableEntity wolfEntity) {
                    if (wolfEntity.isTamed()) {
                        this.playerHitTimer = 100;
                        LivingEntity livingentity = wolfEntity.getOwner();

                        if (livingentity != null && livingentity.getType() == EntityType.PLAYER)
                            this.attackingPlayer = (PlayerEntity) livingentity;

                        else this.attackingPlayer = null;
                    }
                }
            }

            if (flag1) {
                if (source instanceof EntityDamageSource && ((EntityDamageSource) source).isThorns())
                    this.world.sendEntityStatus(this, (byte) 33);

                else {
                    byte b0;

                    if (source.isFire()) b0 = 37;

                    else if (source == DamageSource.SWEET_BERRY_BUSH) b0 = 44;

                    else b0 = 2;

                    this.world.sendEntityStatus(this, b0);
                }

                this.scheduleVelocityUpdate();

                if (entity1 != null) {
                    double d1 = entity1.getX() - this.getX();

                    double d0;
                    for (d0 = entity1.getZ() - this.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
                        d1 = (Math.random() - Math.random()) * 0.01D;

                    this.knockbackVelocity = (float) (MathHelper.atan2(d0, d1) * (double) (180F / (float) Math.PI) - (double) this.getYaw());
                } else {
                    this.knockbackVelocity = (float) ((int) (Math.random() * 2.0D) * 180);
                }
            }

            if (this.isDead()) {
                SoundEvent soundevent = this.getDeathSound();
                if (flag1 && soundevent != null)
                    this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());

                this.onDeath(source);
            }

            else if (flag1) this.playHurtSound(source);

            if (entity1 instanceof ServerPlayerEntity)
                Criteria.PLAYER_HURT_ENTITY.trigger((ServerPlayerEntity) entity1, this, source, amount, amount, false);

            return true;
        }
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Nullable
    @Override
    public Entity getPrimaryPassenger() {
        return this.getPassengerList().isEmpty() ? null : this.getPassengerList().get(0);
    }

    @Override
    public boolean canBeControlledByRider() {
        return true;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        NbtList nbtList = new NbtList();
        for (ItemStack itemStack : this.inventory.items) {
            NbtCompound nbtCompound = new NbtCompound();
            if (!itemStack.isEmpty())
                itemStack.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        nbt.put("Items", nbtList);
        nbt.put("TurretRotations", this.toNbtList(this.turretPitch, this.turretYaw));
        nbt.putInt("DelayTicks", this.delayTicks);
        nbt.putInt("UseTicks", this.useTicks);
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        super.dropEquipment(source, lootingMultiplier, allowDrops);
        this.inventory.items.forEach(this::dropStack);
        this.inventory.clear();
    }

    public void remove() {
        if (!this.dead) {
            this.dead = true;
            this.world.sendEntityStatus(this, (byte)3);
        }

        super.remove(RemovalReason.DISCARDED);
    }

    public void handleStatus(byte status) {
        switch (status) {
            case 2, 33, 36, 37, 44 -> {
                boolean flag1 = status == 33;
                boolean flag2 = status == 36;
                boolean flag3 = status == 37;
                boolean flag = status == 44;
                this.limbDistance = 1.5F;
                this.timeUntilRegen = 20;
                this.maxHurtTime = 10;
                this.hurtTime = this.maxHurtTime;
                this.knockbackVelocity = 0.0F;
                if (flag1)
                    this.playSound(SoundEvents.ENCHANT_THORNS_HIT, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                DamageSource damagesource;
                if (flag3) {
                    damagesource = DamageSource.ON_FIRE;
                } else if (flag2) {
                    damagesource = DamageSource.DROWN;
                } else if (flag) {
                    damagesource = DamageSource.SWEET_BERRY_BUSH;
                } else {
                    damagesource = DamageSource.GENERIC;
                }
                SoundEvent soundEvent1 = this.getHurtSound(damagesource);
                if (soundEvent1 != null) {
                    this.playSound(soundEvent1, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                }
                this.damage(DamageSource.GENERIC, 0.0F);
            }
            case 3 -> {
                SoundEvent soundevent = this.getDeathSound();
                if (soundevent != null) {
                    this.playSound(soundevent, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                }
                this.setHealth(0.0F);
                this.remove();
            }
            case 29, 30, 46 -> {
                for (int j = 0; j < 128; ++j) {
                    double d0 = (double) j / 127.0D;
                    float f = (this.random.nextFloat() - 0.5F) * 0.2F;
                    float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
                    float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
                    double d1 = MathHelper.lerp(d0, this.prevX, this.getX()) + (this.random.nextDouble() - 0.5D) * (double) this.getWidth() * 2.0D;
                    double d2 = MathHelper.lerp(d0, this.prevY, this.getY()) + this.random.nextDouble() * (double) this.getHeight();
                    double d3 = MathHelper.lerp(d0, this.prevZ, this.getZ()) + (this.random.nextDouble() - 0.5D) * (double) this.getWidth() * 2.0D;
                    this.world.addParticle(ParticleTypes.PORTAL, d1, d2, d3, f, f1, f2);
                }
            }
            case 54 -> HoneyBlock.addRichParticles(this);
            default -> super.handleStatus(status);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("Items", 9)) {
            NbtList nbtList = nbt.getList("Items", 10);
            for (int i = 0; i < this.inventory.items.size(); ++i)
                this.inventory.items.set(i, ItemStack.fromNbt(nbtList.getCompound(i)));
        }

        if (nbt.contains("TurretRotations", 5)) {
            NbtList turretRotations = nbt.getList("TurretRotations", 5);
            setTurretRotations(turretRotations.getFloat(0), turretRotations.getFloat(1));
        }

        if (nbt.contains("DelayTicks"))
            this.delayTicks = nbt.getInt("DelayTicks");

        if (nbt.contains("UseTicks"))
            this.useTicks = nbt.getInt("UseTicks");
    }

    public float getGlobalTurretYaw() {
        return MathHelper.lerp(0.5f, this.prevYaw, this.getYaw()) + MathHelper.lerp(0.5f, this.turretYawPrev, this.turretYaw);
    }

    public void setY(float yaw) {
        this.setYaw(yaw);
        this.prevYaw = this.getYaw();
        this.bodyYaw = this.getYaw();
        this.headYaw = this.bodyYaw;
    }

    public float getYawDest() {
        return this.yawDest;
    }

    public void setYawDest(float yaw) {
        this.yawDest = yaw;
    }

    public float getTurretPitch(float f) {
        return MathHelper.lerp(f, this.turretPitchPrev, this.turretPitch);
    }

    public float getTurretYaw(float f) {
        return MathHelper.lerp(f, this.turretYawPrev, this.turretYaw);
    }

    public float getTurretPitch() {
        return this.getTurretPitch(0.5f);
    }

    public float getTurretYaw() {
        return this.getTurretYaw(0.5f);
    }

    public void setTurretRotations(float pitch, float yaw) {
        this.turretPitchPrev = this.turretPitch;
        this.turretYawPrev = this.turretYaw;
        this.turretPitch = pitch;
        this.turretYaw = yaw;
    }

    public float getTurretPitchDest() {
        return this.turretPitchDest;
    }

    public float getTurretYawDest() {
        return this.turretYawDest;
    }

    public void setTurretRotationsDest(float pitch, float yaw) {
        this.turretPitchDest = pitch;
        this.turretYawDest = yaw;
    }

    public void updateMachineRender() {
        if (!this.world.isClient)
            PacketMachine.sendToAllAround(this);
    }

    public void updateYaw() {
        float newYaw = this.turn((float) this.getY(), this.getYawDest(), this.type.rotationSpeed);

        if (this.yawDest != newYaw)
            this.setYaw(newYaw);
    }

    public void updateTurretRotations() {
        float newYaw = this.turn(this.getTurretYaw(), this.getTurretYawDest(), this.type.turretSpeed, this.type.turretMinYaw, this.type.turretMaxYaw);
        boolean shouldRotate = this.checkYaw(newYaw, this.getTurretYaw(), this.type.turretSpeed);
        float newPitch = shouldRotate ? this.turn(this.getTurretPitch(), this.getTurretPitchDest(), this.type.turretSpeed, this.type.turretMinPitch, this.type.turretMaxPitch) : this.getTurretPitch();

        if (this.turretPitch != newPitch || this.turretYaw != newYaw)
            this.setTurretRotations(newPitch, newYaw);
    }

    public boolean checkYaw(float yaw, float currentYaw, float speed) {
        return !this.type.yawFirst || Math.abs(yaw - currentYaw) <= speed / 2 || yaw <= this.type.turretMinYaw || yaw >= this.type.turretMaxYaw;
    }

    public float turn(float rotation, float rotationDest, float speed) {
        return this.turn(rotation, rotationDest, speed, -180, 180);
    }

    public float turn(float rotation, float rotationDest, float speed, float minRotation, float mapitchation) {
        boolean hasLimit = mapitchation - minRotation < 360;

        float deltaRotation = rotationDest - rotation;
        deltaRotation = MathHelper.wrapDegrees(deltaRotation);

        float newRotation;
        if (deltaRotation > speed / 2)
            newRotation = rotation + speed;

        else if (deltaRotation < -speed / 2)
            newRotation = rotation - speed;

        else newRotation = rotation + deltaRotation / 2;

        if (hasLimit) {
            if (newRotation > -minRotation)
                newRotation = -minRotation;

            if (newRotation < -mapitchation)
                newRotation = -mapitchation;
        }

        return newRotation;
    }

    protected static Vec3d applyRotation(Vec3d vec, double pitch, double yaw) {
        double d0 = vec.x * Math.cos(yaw) - vec.y * Math.sin(pitch) * Math.sin(yaw) - vec.z * Math.sin(yaw) * Math.cos(pitch);
        double d1 = vec.y * Math.cos(pitch) - vec.z * Math.sin(pitch);
        double d2 = vec.x * Math.sin(yaw) + vec.y * Math.sin(pitch) * Math.cos(yaw) + vec.z * Math.cos(yaw) * Math.cos(pitch);

        return new Vec3d(d0, d1, d2);
    }

    protected float getVolumeFromDist(float dist) {
        return (float) 0.5 * Math.max((float) 6.0 - dist, 0.0f) / (float) 6.0;
    }

    public abstract void use(PlayerEntity player);

    public abstract void useRealise();

    public static class MachineInventory implements Inventory, Nameable {
        public DefaultedList<ItemStack> items = DefaultedList.ofSize(9 * rows, ItemStack.EMPTY);

        @Override
        public int size() {
            return 9 * rows;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public ItemStack getStack(int slot) {
            return this.items.get(slot);
        }

        @Override
        public ItemStack removeStack(int slot, int amount) {
            return this.items.set(slot, ItemStack.EMPTY);
        }

        @Override
        public ItemStack removeStack(int slot) {
            return this.items.remove(slot);
        }

        @Override
        public void setStack(int slot, ItemStack stack) {
            this.items.set(slot, stack);
        }

        @Override
        public void markDirty() {}

        @Override
        public boolean canPlayerUse(PlayerEntity player) {
            return true;
        }

        @Override
        public void clear() {
            this.items = DefaultedList.ofSize(9 * rows, ItemStack.EMPTY);
        }

        public boolean containsItem(Item item) {
            return this.items.stream().anyMatch(itemStack -> itemStack.getItem().equals(item));
        }

        public void putItem(Item item) {
            for (int i = 0; i < this.items.size(); ++i) {
                ItemStack itemStack = this.items.get(i);
                if (itemStack.isEmpty()) {
                    this.items.set(i, new ItemStack(item));
                    break;
                }
                if (itemStack.getItem().equals(item) && itemStack.getCount() < itemStack.getMaxCount()) {
                    itemStack.setCount(itemStack.getCount() + 1);
                    break;
                }
            }
        }

        public void shrinkItem(Item item) {
            for (ItemStack itemStack : this.items) {
                if (itemStack.getItem().equals(item)) {
                    itemStack.decrement(1);
                    break;
                }
            }
        }

        @Override
        public Text getName() {
            return this.getName();
        }
    }
}
