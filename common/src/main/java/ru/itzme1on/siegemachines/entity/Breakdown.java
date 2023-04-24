package ru.itzme1on.siegemachines.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Breakdown {
    private static final ExplosionBehavior EXPLOSION_DAMAGE_CALCULATOR = new ExplosionBehavior();
    private final boolean fire;
    private final Explosion.DestructionType blockInteraction;
    private final Random random = new Random();
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    @Nullable
    private final Entity source;
    private final float radius;
    private final DamageSource damageSource;
    private final ExplosionBehavior damageCalculator;
    private final List<BlockPos> toBlow = Lists.newArrayList();
    private final Map<PlayerEntity, Vec3d> hitPlayers = Maps.newHashMap();
    private final Vec3d position;
    private final Explosion explosion;
    private final Entity machine;
    private final float power;

    public Breakdown(World world, @Nullable Entity machine, @Nullable Entity source, double x, double y, double z, float radius, boolean fire, float power, Explosion.DestructionType blockInteraction) {
        this(world, source, machine, null, null, x, y, z, radius, fire, power, blockInteraction);
    }

    public Breakdown(World world, @Nullable Entity machine, @Nullable Entity source, @Nullable DamageSource damageSource,
                     @Nullable ExplosionBehavior damageCalculator, double x, double y, double z, float radius, boolean fire, float power, Explosion.DestructionType blockInteraction) {
        this.fire = fire;
        this.blockInteraction = blockInteraction;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.source = source;
        this.radius = radius;
        this.damageSource = damageSource == null && source instanceof LivingEntity ? DamageSource.explosion((LivingEntity) source) : damageSource;
        this.damageCalculator = damageCalculator == null ? this.makeDamageCalculator(source) : damageCalculator;
        this.position = new Vec3d(this.x, this.y, this.z);
        this.explosion = new Explosion(world, source, x, y, z, radius, fire, blockInteraction);
        this.machine = machine;
        this.power = power;
    }


    private ExplosionBehavior makeDamageCalculator(@Nullable Entity entity) {
        return (entity == null ? EXPLOSION_DAMAGE_CALCULATOR : new EntityExplosionBehavior(entity));
    }

    public static float getSeenPercent(Vec3d vec, Entity entity) {
        Box box = entity.getBoundingBox();
        double d0 = 1.0D / ((box.maxX - box.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((box.maxY - box.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((box.maxZ - box.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
        
        if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D)) {
            int i = 0;
            int j = 0;

            for(float f = 0.0F; f <= 1.0F; f = (float)((double)f + d0)) {
                for(float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1)) {
                    for(float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2)) {
                        double d5 = MathHelper.lerp(f, box.minX, box.maxX);
                        double d6 = MathHelper.lerp(f1, box.minY, box.maxY);
                        double d7 = MathHelper.lerp(f2, box.minZ, box.maxZ);
                        Vec3d vector3d = new Vec3d(d5 + d3, d6, d7 + d4);
                        
                        if (entity.world.raycast(new RaycastContext(vector3d, vec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity)).getType() == HitResult.Type.MISS) ++i;

                        ++j;
                    }
                }
            }

            return (float)i / (float)j;
        } else {
            return 0.0F;
        }
    }

    public void explode() {
        Set<BlockPos> set = Sets.newHashSet();

        for(int j = 0; j < 16; ++j) {
            for(int k = 0; k < 16; ++k) {
                for(int l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = (float)j / 15.0F * 2.0F - 1.0F;
                        double d1 = (float)k / 15.0F * 2.0F - 1.0F;
                        double d2 = (float)l / 15.0F * 2.0F - 1.0F;
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = this.power * this.radius * (0.7F + this.world.random.nextFloat() * 0.6F);
                        double d4 = this.x;
                        double d6 = this.y;
                        double d8 = this.z;

                        for(; f > 0.0F; f -= this.power * 0.5F) {
                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            BlockState blockstate = this.world.getBlockState(blockpos);
                            FluidState fluidstate = this.world.getFluidState(blockpos);
                            Optional<Float> optional = this.damageCalculator.getBlastResistance(this.explosion, this.world, blockpos, blockstate, fluidstate);
                            if (optional.isPresent()) f -= (optional.get() + 0.3F) * 0.3F;

                            if (f > 0.0F && this.damageCalculator.canDestroyBlock(this.explosion, this.world, blockpos, blockstate, f)) set.add(blockpos);

                            d4 += d0 * (double)0.3F;
                            d6 += d1 * (double)0.3F;
                            d8 += d2 * (double)0.3F;
                        }
                    }
                }
            }
        }

        this.toBlow.addAll(set);
        float f2 = 1.5f * this.radius;
        int k1 = MathHelper.floor(this.x - (double)f2 - 1.0D);
        int l1 = MathHelper.floor(this.x + (double)f2 + 1.0D);
        int i2 = MathHelper.floor(this.y - (double)f2 - 1.0D);
        int i1 = MathHelper.floor(this.y + (double)f2 + 1.0D);
        int j2 = MathHelper.floor(this.z - (double)f2 - 1.0D);
        int j1 = MathHelper.floor(this.z + (double)f2 + 1.0D);

        List<Entity> list = this.world.getOtherEntities(this.source, new Box(k1, i2, j2, l1, i1, j1));
        Vec3d vector3d = new Vec3d(this.x, this.y, this.z);

        for (Entity entity : list) {
            if (!entity.isImmuneToExplosion() && !entity.equals(this.machine) && !entity.equals(this.source)) {
                double d12 = (MathHelper.sqrt((float) entity.squaredDistanceTo(vector3d)) / f2);

                if (d12 <= 1.0D) {
                    double d5 = entity.getX() - this.x;
                    double d7 = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y;
                    double d9 = entity.getZ() - this.z;
                    double d13 = MathHelper.sqrt((float) (d5 * d5 + d7 * d7 + d9 * d9));

                    if (d13 != 0.0D) {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = getSeenPercent(vector3d, entity);
                        double d10 = (1.0D - d12) * d14;

                        if (!(entity instanceof ItemEntity)) {
                            entity.damage(this.getDamageSource(), (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f2 + 1.0D)));
                        }

                        double d11 = d10;

                        if (entity instanceof LivingEntity)
                            d11 = ProtectionEnchantment.transformExplosionKnockback((LivingEntity) entity, d10);

                        entity.setVelocity(entity.getVelocity().add(d5 * d11, d7 * d11, d9 * d11));

                        if (entity instanceof PlayerEntity playerEntity) {
                            if (!playerEntity.isSpectator() && (!playerEntity.isCreative() || !playerEntity.getAbilities().flying))
                                this.hitPlayers.put(playerEntity, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
                        }
                    }
                }
            }
        }

    }

    public void finalizeExplosion(boolean bool) {
        if (this.world.isClient)
            // TODO это было закомменчено
            this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F, false);

        boolean flag = this.blockInteraction != Explosion.DestructionType.NONE;
        
        if (bool) {
            if (!(this.radius < 2.0F) && flag)
                this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            
            else this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
        }

        if (flag) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList = new ObjectArrayList<>();
            Collections.shuffle(this.toBlow, this.world.random);

            for(BlockPos blockpos : this.toBlow) {
                BlockState blockstate = this.world.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (!blockstate.isAir()) {
                    BlockPos blockPos1 = blockpos.toImmutable();
                    this.world.getProfiler().push("explosion_blocks");
                    if (this.world instanceof ServerWorld) {
                        BlockEntity tileEntity = blockstate.hasBlockEntity() ? this.world.getBlockEntity(blockpos) : null;
                        LootContext.Builder lootContext$builder = (new LootContext.Builder((ServerWorld) this.world)).random(this.world.random)
                                .parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(blockpos))
                                .parameter(LootContextParameters.TOOL, ItemStack.EMPTY)
                                .optionalParameter(LootContextParameters.BLOCK_ENTITY, tileEntity)
                                .optionalParameter(LootContextParameters.THIS_ENTITY, this.source);
                        
                        if (this.blockInteraction == Explosion.DestructionType.DESTROY)
                            lootContext$builder.parameter(LootContextParameters.EXPLOSION_RADIUS, this.radius);

                        blockstate.getDroppedStacks(lootContext$builder).forEach((p_229977_2_) -> {
                            addBlockDrops(objectArrayList, p_229977_2_, blockPos1);
                        });
                    }

                    this.world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);
                    this.world.getProfiler().pop();
                }
            }

            for(Pair<ItemStack, BlockPos> pair : objectArrayList) {
                Block.dropStack(this.world, pair.getSecond(), pair.getFirst());
            }
        }

        if (this.fire) {
            for(BlockPos blockPos2 : this.toBlow) {
                if (this.random.nextInt(3) == 0 && this.world.getBlockState(blockPos2).isAir() && this.world.getBlockState(blockPos2.down()).isOpaqueFullCube(this.world, blockPos2.down()))
                    this.world.setBlockState(blockPos2, FireBlock.getState(this.world, blockPos2));
            }
        }

    }

    private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> list, ItemStack item, BlockPos pos) {
        int i = list.size();

        for(int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = list.get(j);
            ItemStack itemStack = pair.getFirst();
            if (ItemEntity.canMerge(itemStack, item)) {
                ItemStack itemStack1 = ItemEntity.merge(itemStack, item, 16);
                list.set(j, Pair.of(itemStack1, pair.getSecond()));
                if (item.isEmpty()) return;
            }
        }

        list.add(Pair.of(item, pos));
    }

    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    public Map<PlayerEntity, Vec3d> getHitPlayers() {
        return this.hitPlayers;
    }

    @Nullable
    public LivingEntity getSourceMob() {
        if (this.source == null) {
            return null;
        } else if (this.source instanceof TntEntity) {
            return ((TntEntity) this.source).getCausingEntity();
        } else if (this.source instanceof LivingEntity) {
            return (LivingEntity) this.source;
        } else {
            if (this.source instanceof ProjectileEntity) {
                Entity entity = ((ProjectileEntity) this.source).getOwner();
                if (entity instanceof LivingEntity) return (LivingEntity) entity;
            }

            return null;
        }
    }

    public void clearToBlow() {
        this.toBlow.clear();
    }

    public List<BlockPos> getToBlow() {
        return this.toBlow;
    }

    public Vec3d getPosition() {
        return this.position;
    }

    @Nullable
    public Entity getExploder() {
        return this.source;
    }
}