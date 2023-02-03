package ru.itzme1on.siegemachines.entity.machine;

import dev.architectury.networking.NetworkManager;
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
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.itzme1on.siegemachines.SiegeMachines;

import java.util.List;

public abstract class Machine extends Mob implements MenuProvider {
    public MachineInventory inventory = new MachineInventory();

    public static int rows = 1;
    public final MachineType type;
    private float turretPitch = -25.0f;
    private float turretPitchPrev = this.turretPitch;
    protected float turretPitchDest = this.turretPitch;
    private float turretYaw = 0.0f;
    private float turretYawPrev = this.turretYaw;
    protected float turretYawDest = this.turretYaw;
    protected float yawDest = this.getYRot();
    public int useTicks = -1;
    public int delayTicks;
    protected int renderUpdateTicks = 0;
    public int deploymentTicks = 0;

    protected Machine(EntityType<? extends Mob> entityType, Level level, MachineType type) {
        super(entityType, level);
        this.type = type;
        this.delayTicks = this.type.delayTime;
        rows = this.type.rows;
    }

    public static AttributeSupplier.Builder setEntityAttributes(MachineType type) {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, type.health)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5F)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D);
    }

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
        stack.addTagElement("EntityTag", nbt);
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

        if (damagesource.isExplosion())
            f *= 1.25f;

        if (damagesource.isCreativePlayer())
            f *= 2;

        if (damagesource.getEntity() instanceof AbstractArrow)
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
    public boolean hurt(@NotNull DamageSource damageSource, float f) {
//        if (!net.minecraftforge.common.ForgeHooks.onLivingAttack(this, damageSource, f)) return false;
        if (damageSource.getEntity() instanceof Player
                && !damageSource.isProjectile()
                && !damageSource.isExplosion()
                && !damageSource.isMagic()
                && this.getPassengers().isEmpty()) {
            this.spawnAtLocation(this.getMachineItemWithData());
            this.remove();

            return false;
        }

        if (this.isInvulnerableTo(damageSource))
            return false;

        else if (this.level.isClientSide)
            return false;

        else if (this.isDeadOrDying())
            return false;

        else if (damageSource.isFire() && this.hasEffect(MobEffects.FIRE_RESISTANCE))
            return false;

        else {
            f = adjustDamage(damageSource, f);

            this.noActionTime = 0;

            this.animationSpeed = 1.5F;
            boolean flag1 = true;
            if ((float) this.invulnerableTime > 10.0F) {
                if (f <= this.lastHurt)
                    return false;

                this.actuallyHurt(damageSource, f - this.lastHurt);
                this.lastHurt = f;
                flag1 = false;
            }

            else {
                this.lastHurt = f;
                this.invulnerableTime = 20;
                this.actuallyHurt(damageSource, f);
                this.hurtDuration = 10;
                this.hurtTime = this.hurtDuration;
            }

            this.hurtDir = 0.0F;
            Entity entity1 = damageSource.getEntity();

            if (entity1 != null) {
                if (entity1 instanceof LivingEntity)
                    this.setLastHurtByMob((LivingEntity) entity1);

                if (entity1 instanceof Player) {
                    this.lastHurtByPlayerTime = 1;
                    this.lastHurtByPlayer = (Player) entity1;
                }

                else if (entity1 instanceof TamableAnimal wolfEntity) {
                    if (wolfEntity.isTame()) {
                        this.lastHurtByPlayerTime = 100;
                        LivingEntity livingentity = wolfEntity.getOwner();

                        if (livingentity != null && livingentity.getType() == EntityType.PLAYER)
                            this.lastHurtByPlayer = (Player) livingentity;

                        else this.lastHurtByPlayer = null;
                    }
                }
            }

            if (flag1) {
                if (damageSource instanceof EntityDamageSource && ((EntityDamageSource) damageSource).isThorns())
                    this.level.broadcastEntityEvent(this, (byte) 33);

                else {
                    byte b0;

                    if (damageSource.isFire())
                        b0 = 37;

                    else if (damageSource == DamageSource.SWEET_BERRY_BUSH)
                        b0 = 44;

                    else b0 = 2;

                    this.level.broadcastEntityEvent(this, b0);
                }

                this.markHurt();

                if (entity1 != null) {
                    double d1 = entity1.getX() - this.getX();

                    double d0;
                    for (d0 = entity1.getZ() - this.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
                        d1 = (Math.random() - Math.random()) * 0.01D;

                    this.hurtDir = (float) (Mth.atan2(d0, d1) * (double) (180F / (float) Math.PI) - (double) this.getYRot());
                }

                else this.hurtDir = (float) ((int) (Math.random() * 2.0D) * 180);
            }

            if (this.isDeadOrDying()) {
                SoundEvent soundevent = this.getDeathSound();
                if (flag1 && soundevent != null)
                    this.playSound(soundevent, this.getSoundVolume(), this.getVoicePitch());

                this.die(damageSource);
            }
            else if (flag1)
                this.playHurtSound(damageSource);

            if (entity1 instanceof ServerPlayer)
                CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer) entity1, this, damageSource, f, f, false);

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
    public boolean removeWhenFarAway(double d) {
        return false;
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public boolean canBeControlledByRider() {
        return true;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        ListTag listNbt = new ListTag();

        for(ItemStack itemstack : this.inventory.items) {
            CompoundTag compoundNbt = new CompoundTag();
            if (!itemstack.isEmpty())
                itemstack.save(compoundNbt);

            listNbt.add(compoundNbt);
        }

        nbt.put("Items", listNbt);
        nbt.put("TurretRotations", this.newFloatList(this.turretPitch, this.turretYaw));
        nbt.putInt("DelayTicks", this.delayTicks);
        nbt.putInt("UseTicks", this.useTicks);
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource damageSource, int i, boolean bl) {
        super.dropCustomDeathLoot(damageSource, i, bl);
        this.inventory.items.forEach(this::spawnAtLocation);
        this.inventory.clearContent();
    }

    public void remove() {
        if (!this.dead) {
            this.dead = true;
            this.level.broadcastEntityEvent(this, (byte)3);
        }

        super.remove(RemovalReason.DISCARDED);
    }

    @Override
    public void handleEntityEvent(byte b) {
        switch (b) {
            case 2, 33, 36, 37, 44 -> {
                boolean flag1 = b == 33;
                boolean flag2 = b == 36;
                boolean flag3 = b == 37;
                boolean flag = b == 44;
                this.animationSpeed = 1.5F;
                this.invulnerableTime = 20;
                this.hurtDuration = 10;
                this.hurtTime = this.hurtDuration;
                this.hurtDir = 0.0F;
                if (flag1)
                    this.playSound(SoundEvents.THORNS_HIT, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

                DamageSource damagesource;
                if (flag3)
                    damagesource = DamageSource.ON_FIRE;

                else if (flag2)
                    damagesource = DamageSource.DROWN;

                else if (flag)
                    damagesource = DamageSource.SWEET_BERRY_BUSH;

                else damagesource = DamageSource.GENERIC;

                SoundEvent soundEvent = this.getHurtSound(damagesource);
                if (soundEvent != null)
                    this.playSound(soundEvent, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

                this.hurt(DamageSource.GENERIC, 0.0F);
            }

            case 3 -> {
                SoundEvent soundevent = this.getDeathSound();
                if (soundevent != null)
                    this.playSound(soundevent, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

                this.setHealth(0.0F);
                this.remove();
            }

            case 29, 30, 46 -> {
                for (int j = 0; j < 128; ++j) {
                    double d0 = (double) j / 127.0D;
                    float f = (this.random.nextFloat() - 0.5F) * 0.2F;
                    float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
                    float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
                    double d1 = Mth.lerp(d0, this.xo, this.getX()) + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth() * 2.0D;
                    double d2 = Mth.lerp(d0, this.yo, this.getY()) + this.random.nextDouble() * (double) this.getBbHeight();
                    double d3 = Mth.lerp(d0, this.zo, this.getZ()) + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth() * 2.0D;
                    this.level.addParticle(ParticleTypes.PORTAL, d1, d2, d3, f, f1, f2);
                }
            }

            case 54 -> HoneyBlock.showJumpParticles(this);

            default -> super.handleEntityEvent(b);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("Items", 9)) {
            ListTag listNbt = nbt.getList("Items", 10);

            for(int i = 0; i < this.inventory.items.size(); ++i)
                this.inventory.items.set(i, ItemStack.of(listNbt.getCompound(i)));
        }

        if (nbt.contains("TurretRotations", 5)) {
            ListTag turretRotations = nbt.getList("TurretRotations", 5);
            setTurretRotations(turretRotations.getFloat(0), turretRotations.getFloat(1));
        }

        if (nbt.contains("DelayTicks"))
            this.delayTicks = nbt.getInt("DelayTicks");

        if (nbt.contains("UseTicks"))
            this.useTicks = nbt.getInt("UseTicks");
    }

    public float getGlobalTurretYaw() {
        return Mth.lerp(0.5f, this.yRotO, this.getYRot()) + Mth.lerp(0.5f, this.turretYawPrev, this.turretYaw);
    }

    public void setYaw(float yaw) {
        this.setYRot(yaw);
        this.yRotO = this.getYRot();
        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.yBodyRot;
    }

    public float getYawDest() {
        return this.yawDest;
    }

    public void setYawDest(float yaw) {
        this.yawDest = yaw;
    }

    public float getTurretPitch(float f) {
        return Mth.lerp(f, this.turretPitchPrev, this.turretPitch);
    }

    public float getTurretYaw(float f) {
        return Mth.lerp(f, this.turretYawPrev, this.turretYaw);
    }

    public float getTurretPitch() {
        return this.getTurretPitch(0.5f);
    }

    public float getTurretYaw() {
        return this.getTurretYaw(0.5f);
    }

    public void setTurretRotations(float pitch, float yaw)
    {
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
        AABB RENDER_UPDATE_AABB = AABB.ofSize(Vec3.atCenterOf(this.blockPosition()), 2 * SiegeMachines.RENDER_UPDATE_RANGE, 2 * SiegeMachines.RENDER_UPDATE_RANGE, 2 * SiegeMachines.RENDER_UPDATE_RANGE);
        List<Player> players = this.level.getNearbyPlayers(
                TargetingConditions.forNonCombat().range(SiegeMachines.RENDER_UPDATE_RANGE),
                this, RENDER_UPDATE_AABB);
        if (!this.level.isClientSide()) {
//            NetworkManager.sendToPlayer();
//            PacketHandler.sendPacketToAllInArea(new PacketMachine(
//                    this.getId(),
//                    this.delayticks,
//                    this.useticks,
//                    this.turretpitch,
//                    this.turretyaw), this.blockPosition(), SiegeMachines.RENDER_UPDATE_RANGE_SQR);
        }
    }

    public static class MachineInventory implements Container, Nameable {
        public NonNullList<ItemStack> items = NonNullList.withSize(9 * rows, ItemStack.EMPTY);

        @Override
        public int getContainerSize() {
            return 9 * rows;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public ItemStack getItem(int id) {
            return this.items.get(id);
        }

        @Override
        public ItemStack removeItem(int id, int j) {
            return this.items.set(id, ItemStack.EMPTY);
        }

        @Override
        public ItemStack removeItemNoUpdate(int id) {
            return this.items.remove(id);
        }

        @Override
        public void setItem(int id, ItemStack itemStack) {
            this.items.set(id, itemStack);
        }

        @Override
        public void setChanged() {}

        @Override
        public boolean stillValid(Player player) {
            return true;
        }

        @Override
        public void clearContent() {
            this.items = NonNullList.withSize(9 * rows, ItemStack.EMPTY);
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
