package ru.itzme1on.siegemachines.entity.projectile;

import dev.architectury.networking.NetworkManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.Packet;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.NotNull;
import ru.itzme1on.siegemachines.item.ModItems;

public abstract class Missile extends ThrownItemEntity {
    public MissileType type = MissileType.STONE;
    public Item item = ModItems.STONE.get();

    public Missile(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public Missile(EntityType<? extends Missile> entityType, World world, Vector3d pos, LivingEntity entity, MissileType type, Item item) {
        super(entityType, entity, world);
        this.type = type;
        this.item = item;
        this.setPos(pos.x, pos.y, pos.z);
    }

    @Override
    public @NotNull Item getDefaultItem() {
        return this.item;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return NetworkManager.createAddEntityPacket(this);
    }

    @Override
    public void onCollision(HitResult result) {
        float f = 2.0F;
        if (result.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityRTR = (EntityHitResult)result;
            Vec3d pos = entityRTR.getPos();
            Entity entity = entityRTR.getEntity();
            float damage = this.type.mass * (float) this.getVelocity().length();

            DamageSource damagesource = new MissileDamageSource(this, this.getOwner());
            if (this.type.armorPiercing >= 1.0f)
                MissileDamageSource.thrown(this, this.getOwner(), true);

            else if (this.type.armorPiercing > 0.0f && entity instanceof LivingEntity livingEntity) {
                if(livingEntity instanceof PlayerEntity player)
                    if(player.isBlocking() && (item == ModItems.GIANT_ARROW.get() || item == Items.ARROW) && (player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof ShieldItem || player.isBlocking() && player.getStackInHand(Hand.OFF_HAND).getItem() instanceof ShieldItem))
                        return;

                damage -= (1.0f - this.type.armorPiercing) * (damage - DamageUtil.getDamageLeft(damage, 0, 0));
                MissileDamageSource.thrown(this, this.getOwner(), true);
            }

            if (!this.world.isClient && this.type.explosive) {
                this.world.createExplosion(this.getOwner(), pos.x, pos.y, pos.z, 3.0F, Explosion.DestructionType.NONE);
                this.remove(RemovalReason.KILLED);
            }

            entity.damage(damagesource, damage);
            Vec3d vector3d = this.getVelocity().multiply(1.0D, 0.0D, 1.0D).normalize().multiply((double) this.type.knockBack * 0.6D);
            if (vector3d.lengthSquared() > 0.0D)
                entity.pushAwayFrom(entity);
        }

        if (result.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockRTR = (BlockHitResult)result;
            BlockPos blockpos = blockRTR.getBlockPos();
            BlockState blockstate = this.world.getBlockState(blockpos);
            boolean smoothImpact = (blockstate == Blocks.SAND.getDefaultState() ||
                    blockstate == Blocks.RED_SAND.getDefaultState() ||
                    blockstate == Blocks.DIRT.getDefaultState() ||
                    blockstate == Blocks.GRASS_BLOCK.getDefaultState() ||
                    blockstate == Blocks.DIRT_PATH.getDefaultState() ||
                    blockstate == Blocks.COARSE_DIRT.getDefaultState() ||
                    blockstate == Blocks.SNOW_BLOCK.getDefaultState()) &&
                    blockRTR.getSide() == Direction.UP;

            if (blockRTR.getSide() == Direction.UP) {
                if (this.type.explosive) {
                    for (int r = 0; r < this.type.explosionRadius; ++r) {
                        for (float a = 0; a < 2 * Math.PI; a += Math.PI / 4) {
                            BlockPos pos = blockRTR.getBlockPos().add(r * Math.cos(a), 0, -r * Math.sin(a));
                            if (this.world.getBlockState(pos) == Blocks.GRASS_BLOCK.getDefaultState())
                                this.world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                        }
                    }
                }
                if (!this.world.isClient) {
                    this.remove(RemovalReason.KILLED);
                    if (smoothImpact && this.type.explosive)
                        this.world.createExplosion(this.getOwner(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.type.explosionRadius * f, Explosion.DestructionType.NONE);
                }

                else if (smoothImpact)
                    this.dustExplosion(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockstate), blockpos, this.type.explosionRadius / 2, 50);
            }

            if (!this.world.isClient && !smoothImpact && this.type.explosive)
                this.world.createExplosion(this.getOwner(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.type.explosionRadius * f, Explosion.DestructionType.BREAK);
        }

        if (result.getType() == HitResult.Type.MISS) {
            this.world.playSound((PlayerEntity) this.getOwner(), this.getLandingPos(), SoundEvents.BLOCK_ANVIL_BREAK, SoundCategory.AMBIENT, 1.0f, 1.0f);
            if(!this.world.isClient)
                this.remove(RemovalReason.KILLED);
        }

        if (!this.world.isClient)
            this.remove(RemovalReason.KILLED);
    }

    private void dustExplosion(ParticleEffect particle, BlockPos blockpos, double speed, int amount) {
        this.dustExplosion(particle, blockpos.getX(), blockpos.getY(), blockpos.getZ(), speed, amount);
    }

    private void dustExplosion(ParticleEffect particle, double x, double y, double z, double speed, int amount) {
        for (int i = 0; i < amount; ++i) {
            Vec3d movement = this.getVelocity();
            double d0 = x - 0.05 + this.world.random.nextDouble() * 0.3;
            double d1 = y + 1.0;
            double d2 = z - 0.05 + this.world.random.nextDouble() * 0.3;
            double d3 = movement.x * this.world.random.nextDouble() * speed;
            double d4 = -movement.y * this.world.random.nextDouble() * speed * 10.0f;
            double d5 = movement.z * this.world.random.nextDouble() * speed;

            this.world.addParticle(particle, d0, d1, d2, d3, d4, d5);
        }
    }

    @Override
    public void tick() {
        if (this.type.flightType == FlightType.SPINNING)
            this.setPitch(this.getPitch() + 0.5f);

        super.tick();
    }
}
