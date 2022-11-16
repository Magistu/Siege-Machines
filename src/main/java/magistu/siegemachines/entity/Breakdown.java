package magistu.siegemachines.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.EntityExplosionContext;
import net.minecraft.world.Explosion;
import net.minecraft.world.ExplosionContext;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Breakdown
{
   private static final ExplosionContext EXPLOSION_DAMAGE_CALCULATOR = new ExplosionContext();
   private final boolean fire;
   private final Explosion.Mode blockInteraction;
   private final Random random = new Random();
   private final World level;
   private final double x;
   private final double y;
   private final double z;
   @Nullable
   private final Entity source;
   private final float radius;
   private final DamageSource damageSource;
   private final ExplosionContext damageCalculator;
   private final List<BlockPos> toBlow = Lists.newArrayList();
   private final Map<PlayerEntity, Vector3d> hitPlayers = Maps.newHashMap();
   private final Vector3d position;
   private final Explosion explosion;
   private final Entity machine;
   private final float power;

   public Breakdown(World p_i50007_1_, @Nullable Entity machine, @Nullable Entity p_i50007_2_, double p_i50007_3_, double p_i50007_5_, double p_i50007_7_, float p_i50007_9_, boolean p_i50007_10_, float power, Explosion.Mode p_i50007_11_) {
      this(p_i50007_1_, p_i50007_2_, machine, (DamageSource)null, (ExplosionContext)null, p_i50007_3_, p_i50007_5_, p_i50007_7_, p_i50007_9_, p_i50007_10_, power, p_i50007_11_);
   }

   public Breakdown(World p_i231610_1_, @Nullable Entity machine, @Nullable Entity p_i231610_2_, @Nullable DamageSource p_i231610_3_, @Nullable ExplosionContext p_i231610_4_, double p_i231610_5_, double p_i231610_7_, double p_i231610_9_, float p_i231610_11_, boolean p_i231610_12_, float power, Explosion.Mode p_i231610_13_) {
      this.explosion = new Explosion(p_i231610_1_, p_i231610_2_, p_i231610_3_, p_i231610_4_, p_i231610_5_, p_i231610_7_, p_i231610_9_, p_i231610_11_, p_i231610_12_, p_i231610_13_);
      this.level = p_i231610_1_;
      this.machine = machine;
      this.source = p_i231610_2_;
      this.radius = p_i231610_11_;
      this.x = p_i231610_5_;
      this.y = p_i231610_7_;
      this.z = p_i231610_9_;
      this.fire = p_i231610_12_;
      this.power = power;
      this.blockInteraction = p_i231610_13_;
      this.damageSource = p_i231610_3_ == null && p_i231610_2_ instanceof LivingEntity ? DamageSource.explosion((LivingEntity) p_i231610_2_) : p_i231610_3_;
      this.damageCalculator = p_i231610_4_ == null ? this.makeDamageCalculator(p_i231610_2_) : p_i231610_4_;
      this.position = new Vector3d(this.x, this.y, this.z);
   }

   private ExplosionContext makeDamageCalculator(@Nullable Entity p_234894_1_) {
      return (ExplosionContext)(p_234894_1_ == null ? EXPLOSION_DAMAGE_CALCULATOR : new EntityExplosionContext(p_234894_1_));
   }

   public static float getSeenPercent(Vector3d p_222259_0_, Entity p_222259_1_) {
      AxisAlignedBB axisalignedbb = p_222259_1_.getBoundingBox();
      double d0 = 1.0D / ((axisalignedbb.maxX - axisalignedbb.minX) * 2.0D + 1.0D);
      double d1 = 1.0D / ((axisalignedbb.maxY - axisalignedbb.minY) * 2.0D + 1.0D);
      double d2 = 1.0D / ((axisalignedbb.maxZ - axisalignedbb.minZ) * 2.0D + 1.0D);
      double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
      double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
      if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D)) {
         int i = 0;
         int j = 0;

         for(float f = 0.0F; f <= 1.0F; f = (float)((double)f + d0)) {
            for(float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1)) {
               for(float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2)) {
                  double d5 = MathHelper.lerp((double)f, axisalignedbb.minX, axisalignedbb.maxX);
                  double d6 = MathHelper.lerp((double)f1, axisalignedbb.minY, axisalignedbb.maxY);
                  double d7 = MathHelper.lerp((double)f2, axisalignedbb.minZ, axisalignedbb.maxZ);
                  Vector3d vector3d = new Vector3d(d5 + d3, d6, d7 + d4);
                  if (p_222259_1_.level.clip(new RayTraceContext(vector3d, p_222259_0_, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, p_222259_1_)).getType() == RayTraceResult.Type.MISS) {
                     ++i;
                  }

                  ++j;
               }
            }
         }

         return (float)i / (float)j;
      } else {
         return 0.0F;
      }
   }

   public void explode()
   {
      Set<BlockPos> set = Sets.newHashSet();
      int i = 16;

      for(int j = 0; j < 16; ++j) {
         for(int k = 0; k < 16; ++k) {
            for(int l = 0; l < 16; ++l) {
               if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                  double d0 = (double)((float)j / 15.0F * 2.0F - 1.0F);
                  double d1 = (double)((float)k / 15.0F * 2.0F - 1.0F);
                  double d2 = (double)((float)l / 15.0F * 2.0F - 1.0F);
                  double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                  d0 = d0 / d3;
                  d1 = d1 / d3;
                  d2 = d2 / d3;
                  float f = this.power * this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
                  double d4 = this.x;
                  double d6 = this.y;
                  double d8 = this.z;

                  for(float f1 = 0.3F; f > 0.0F; f -= this.power * 0.5F) {
                     BlockPos blockpos = new BlockPos(d4, d6, d8);
                     BlockState blockstate = this.level.getBlockState(blockpos);
                     FluidState fluidstate = this.level.getFluidState(blockpos);
                     Optional<Float> optional = this.damageCalculator.getBlockExplosionResistance(this.explosion, this.level, blockpos, blockstate, fluidstate);
                     if (optional.isPresent()) {
                        f -= (optional.get() + 0.3F) * 0.3F;
                     }

                     if (f > 0.0F && this.damageCalculator.shouldBlockExplode(this.explosion, this.level, blockpos, blockstate, f)) {
                        set.add(blockpos);
                     }

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
      List<Entity> list = this.level.getEntities(this.source, new AxisAlignedBB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
      Vector3d vector3d = new Vector3d(this.x, this.y, this.z);

      for(int k2 = 0; k2 < list.size(); ++k2) {
         Entity entity = list.get(k2);
         if (!entity.ignoreExplosion() && !entity.equals(this.machine) && !entity.equals(this.source)) {
            double d12 = (MathHelper.sqrt(entity.distanceToSqr(vector3d)) / f2);
            if (d12 <= 1.0D) {
               double d5 = entity.getX() - this.x;
               double d7 = (entity instanceof TNTEntity ? entity.getY() : entity.getEyeY()) - this.y;
               double d9 = entity.getZ() - this.z;
               double d13 = (double)MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
               if (d13 != 0.0D) {
                  d5 = d5 / d13;
                  d7 = d7 / d13;
                  d9 = d9 / d13;
                  double d14 = (double)getSeenPercent(vector3d, entity);
                  double d10 = (1.0D - d12) * d14;
                  if (!(entity instanceof ItemEntity)) {
                     entity.hurt(this.getDamageSource(), (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f2 + 1.0D)));
                  }
                  double d11 = d10;
                  if (entity instanceof LivingEntity) {
                     d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity)entity, d10);
                  }

                  entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
                  if (entity instanceof PlayerEntity) {
                     PlayerEntity playerentity = (PlayerEntity)entity;
                     if (!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.abilities.flying)) {
                        this.hitPlayers.put(playerentity, new Vector3d(d5 * d10, d7 * d10, d9 * d10));
                     }
                  }
               }
            }
         }
      }

   }

   public void finalizeExplosion(boolean p_77279_1_) {
      if (this.level.isClientSide) {
         //this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);
      }

      boolean flag = this.blockInteraction != Explosion.Mode.NONE;
      if (p_77279_1_) {
         if (!(this.radius < 2.0F) && flag) {
            this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
         } else {
            this.level.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
         }
      }

      if (flag) {
         ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
         Collections.shuffle(this.toBlow, this.level.random);

         for(BlockPos blockpos : this.toBlow) {
            BlockState blockstate = this.level.getBlockState(blockpos);
            Block block = blockstate.getBlock();
            if (!blockstate.isAir(this.level, blockpos)) {
               BlockPos blockpos1 = blockpos.immutable();
               this.level.getProfiler().push("explosion_blocks");
               if (this.level instanceof ServerWorld) {
                  TileEntity tileentity = blockstate.hasTileEntity() ? this.level.getBlockEntity(blockpos) : null;
                  LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld)this.level)).withRandom(this.level.random).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(blockpos)).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withOptionalParameter(LootParameters.BLOCK_ENTITY, tileentity).withOptionalParameter(LootParameters.THIS_ENTITY, this.source);
                  if (this.blockInteraction == Explosion.Mode.DESTROY) {
                     lootcontext$builder.withParameter(LootParameters.EXPLOSION_RADIUS, this.radius);
                  }

                  blockstate.getDrops(lootcontext$builder).forEach((p_229977_2_) -> {
                     addBlockDrops(objectarraylist, p_229977_2_, blockpos1);
                  });
               }

               this.level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 3);
               this.level.getProfiler().pop();
            }
         }

         for(Pair<ItemStack, BlockPos> pair : objectarraylist) {
            Block.popResource(this.level, pair.getSecond(), pair.getFirst());
         }
      }

      if (this.fire) {
         for(BlockPos blockpos2 : this.toBlow) {
            if (this.random.nextInt(3) == 0 && this.level.getBlockState(blockpos2).isAir() && this.level.getBlockState(blockpos2.below()).isSolidRender(this.level, blockpos2.below())) {
               this.level.setBlockAndUpdate(blockpos2, AbstractFireBlock.getState(this.level, blockpos2));
            }
         }
      }

   }

   private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> p_229976_0_, ItemStack p_229976_1_, BlockPos p_229976_2_) {
      int i = p_229976_0_.size();

      for(int j = 0; j < i; ++j) {
         Pair<ItemStack, BlockPos> pair = p_229976_0_.get(j);
         ItemStack itemstack = pair.getFirst();
         if (ItemEntity.areMergable(itemstack, p_229976_1_)) {
            ItemStack itemstack1 = ItemEntity.merge(itemstack, p_229976_1_, 16);
            p_229976_0_.set(j, Pair.of(itemstack1, pair.getSecond()));
            if (p_229976_1_.isEmpty()) {
               return;
            }
         }
      }

      p_229976_0_.add(Pair.of(p_229976_1_, p_229976_2_));
   }

   public DamageSource getDamageSource() {
      return this.damageSource;
   }

   public Map<PlayerEntity, Vector3d> getHitPlayers() {
      return this.hitPlayers;
   }

   @Nullable
   public LivingEntity getSourceMob() {
      if (this.source == null) {
         return null;
      } else if (this.source instanceof TNTEntity) {
         return ((TNTEntity)this.source).getOwner();
      } else if (this.source instanceof LivingEntity) {
         return (LivingEntity)this.source;
      } else {
         if (this.source instanceof ProjectileEntity) {
            Entity entity = ((ProjectileEntity)this.source).getOwner();
            if (entity instanceof LivingEntity) {
               return (LivingEntity)entity;
            }
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

   public Vector3d getPosition() {
      return this.position;
   }

   @Nullable
   public Entity getExploder() {
      return this.source;
   }
}
