package ru.itzme1on.siegemachines.entity.projectile;

import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import ru.itzme1on.siegemachines.registry.ItemRegistry;

public abstract class Missile extends ThrowableItemProjectile {
    public MissileType type = MissileType.STONE;
    public Item item = ItemRegistry.STONE.get();

    public Missile(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public Missile(EntityType<? extends Missile> entityType, Level level, Vector3d pos, LivingEntity entity, MissileType type, Item item) {
        super(entityType, entity, level);
        this.type = type;
        this.item = item;
        this.setPos(pos.x, pos.y, pos.z);
    }

    @Override
    public @NotNull Item getDefaultItem() {
        return this.item;
    }

//    @Override
//    public Packet<?> getAddEntityPacket() {
//        return ;
//    }

    @Override
    public void onHit(HitResult result) {
        float f = 2.0F;
        if (result.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityRTR = (EntityHitResult)result;
            Vec3 pos = entityRTR.getLocation();
            Entity entity = entityRTR.getEntity();
            float damage = this.type.mass * (float) this.getDeltaMovement().length();

            DamageSource damagesource = new MissileDamageSource(this, this.getOwner());
            if (this.type.armorPiercing >= 1.0f)
                MissileDamageSource.thrown(this, this.getOwner());

            else if (this.type.armorPiercing > 0.0f && entity instanceof LivingEntity livingEntity) {
                if(livingEntity instanceof Player player) {
                    if(player.isBlocking() && (item == ItemRegistry.GIANT_ARROW.get() || item == Items.ARROW) && (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ShieldItem || player.isBlocking() && player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ShieldItem))
                        return;
                }
                damage -= (1.0f - this.type.armorPiercing) * (damage - CombatRules.getDamageAfterAbsorb(damage, 0, 0));
                MissileDamageSource.thrown(this, this.getOwner());
            }

            if (!this.level.isClientSide() && this.type.explosive) {
                this.level.explode(this.getOwner(), pos.x, pos.y, pos.z, 3.0F, Explosion.BlockInteraction.NONE);
                this.remove(RemovalReason.KILLED);
            }

            entity.hurt(damagesource, damage);
            Vec3 vector3d = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double) this.type.knockBack * 0.6D);
            if (vector3d.lengthSqr() > 0.0D)
                entity.push(vector3d.x, 0.1D, vector3d.z);
        }

        if (result.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockRTR = (BlockHitResult)result;
            BlockPos blockpos = blockRTR.getBlockPos();
            BlockState blockstate = this.level.getBlockState(blockpos);
            boolean smoothImpact = (blockstate == Blocks.SAND.defaultBlockState() ||
                    blockstate == Blocks.RED_SAND.defaultBlockState() ||
                    blockstate == Blocks.DIRT.defaultBlockState() ||
                    blockstate == Blocks.GRASS_BLOCK.defaultBlockState() ||
                    blockstate == Blocks.DIRT_PATH.defaultBlockState() ||
                    blockstate == Blocks.COARSE_DIRT.defaultBlockState() ||
                    blockstate == Blocks.SNOW_BLOCK.defaultBlockState()) &&
                    blockRTR.getDirection() == Direction.UP;

            if (blockRTR.getDirection() == Direction.UP) {
                if (this.type.explosive) {
                    for (int r = 0; r < this.type.explosionRadius; ++r) {
                        for (float a = 0; a < 2 * Math.PI; a += Math.PI / 4) {
                            BlockPos pos = blockRTR.getBlockPos().offset(r * Math.cos(a), 0, -r * Math.sin(a));
                            if (this.level.getBlockState(pos) == Blocks.GRASS_BLOCK.defaultBlockState())
                                this.level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
                        }
                    }
                }
                if (!this.level.isClientSide()) {
                    this.remove(RemovalReason.KILLED);
                    if (smoothImpact && this.type.explosive)
                        this.level.explode(this.getOwner(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.type.explosionRadius * f, Explosion.BlockInteraction.NONE);
                }

                else if (smoothImpact)
                    this.dustExplosion(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), blockpos, this.type.explosionRadius / 2, 50);
            }

            if (!this.level.isClientSide() && !smoothImpact && this.type.explosive)
                this.level.explode(this.getOwner(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.type.explosionRadius * f, Explosion.BlockInteraction.BREAK);
        }

        if (result.getType() == HitResult.Type.MISS) {
            this.level.playSound((Player)this.getOwner(), this.getOnPos(), SoundEvents.ANVIL_BREAK, SoundSource.AMBIENT, 1.0f, 1.0f);
            if(!this.level.isClientSide())
                this.remove(RemovalReason.KILLED);
        }
        if (!this.level.isClientSide())
            this.remove(RemovalReason.KILLED);
    }

    private void dustExplosion(ParticleOptions particle, BlockPos blockpos, double speed, int amount) {
        this.dustExplosion(particle, blockpos.getX(), blockpos.getY(), blockpos.getZ(), speed, amount);
    }

    private void dustExplosion(ParticleOptions particle, double x, double y, double z, double speed, int amount) {
        for (int i = 0; i < amount; ++i) {
            Vec3 movement = this.getDeltaMovement();
            double d0 = x - 0.05 + this.level.random.nextDouble() * 0.3;
            double d1 = y + 1.0;
            double d2 = z - 0.05 + this.level.random.nextDouble() * 0.3;
            double d3 = movement.x * this.level.random.nextDouble() * speed;
            double d4 = -movement.y * this.level.random.nextDouble() * speed * 10.0f;
            double d5 = movement.z * this.level.random.nextDouble() * speed;

            this.level.addParticle(particle, d0, d1, d2, d3, d4, d5);
        }
    }

    @Override
    public void tick() {
        if (this.type.flightType == FlightType.SPINNING)
            this.setXRot(this.getXRot() + 0.5f);

        super.tick();
    }
}
