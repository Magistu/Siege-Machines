package ru.magistu.siegemachines.entity.projectile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import ru.magistu.siegemachines.entity.machine.Machine;

import javax.annotation.Nullable;
import java.util.*;

public class MissileExplosion extends Explosion
{
   private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();
   private final boolean fire;
   private final BlockInteraction blockInteraction;
   private final Random random = new Random();
   private final Level level;
   private final double x;
   private final double y;
   private final double z;
   @Nullable
   private final Entity source;
   private final float radius;
   private final DamageSource damageSource;
   private final ExplosionDamageCalculator damageCalculator;
   private final List<BlockPos> toBlow = Lists.newArrayList();
   private final Map<Player, Vec3> hitPlayers = Maps.newHashMap();
   private final Vec3 position;
   private float damagemultiplier = 1.0f;

   public MissileExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius)
   {
      this(level, source, x, y, z, radius, false, BlockInteraction.DESTROY);
   }

   public MissileExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, List<BlockPos> pPositions)
   {
      this(level, source, x, y, z, radius, false, BlockInteraction.DESTROY, pPositions);
   }

   public MissileExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, boolean fired, BlockInteraction blockinteraction, List<BlockPos> pPositions)
   {
      this(level, source, x, y, z, radius, fired, blockinteraction);
      this.toBlow.addAll(pPositions);
   }

   public MissileExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, boolean fired, BlockInteraction blockinteraction)
   {
      this(level, source, null, null, x, y, z, radius, fired, blockinteraction);
   }

   public MissileExplosion(Level level, @Nullable Entity source, @Nullable DamageSource damagesource, @Nullable ExplosionDamageCalculator damagecalculator, double x, double y, double z, float radius, boolean fired, BlockInteraction blockinteraction)
   {
      super(level, source, damagesource, damagecalculator, x, y, z, radius, fired, blockinteraction);
      this.level = level;
      this.source = source;
      this.radius = radius;
      this.x = x;
      this.y = y;
      this.z = z;
      this.fire = fired;
      this.blockInteraction = blockinteraction;
      this.damageSource = damagesource == null ? DamageSource.explosion(this) : damagesource;
      this.damageCalculator = damagecalculator == null ? this.makeDamageCalculator(source) : damagecalculator;
      this.position = new Vec3(this.x, this.y, this.z);
      if (source != null && source.isPassenger())
      {
         Entity vehicle = source.getVehicle();
         if (vehicle instanceof Machine)
         {
            this.damagemultiplier = ((Machine) vehicle).type.specs.damagemultiplier.get();
         }
      }
   }

   private ExplosionDamageCalculator makeDamageCalculator(@Nullable Entity pEntity)
   {
      return pEntity == null ? EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(pEntity);
   }

   public static float getSeenPercent(Vec3 pExplosionVector, Entity pEntity)
   {
      AABB aabb = pEntity.getBoundingBox();
      double d0 = 1.0D / ((aabb.maxX - aabb.minX) * 2.0D + 1.0D);
      double d1 = 1.0D / ((aabb.maxY - aabb.minY) * 2.0D + 1.0D);
      double d2 = 1.0D / ((aabb.maxZ - aabb.minZ) * 2.0D + 1.0D);
      double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
      double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
      if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D))
      {
         int i = 0;
         int j = 0;

         for (double d5 = 0.0D; d5 <= 1.0D; d5 += d0)
         {
            for (double d6 = 0.0D; d6 <= 1.0D; d6 += d1)
            {
               for (double d7 = 0.0D; d7 <= 1.0D; d7 += d2)
               {
                  double d8 = Mth.lerp(d5, aabb.minX, aabb.maxX);
                  double d9 = Mth.lerp(d6, aabb.minY, aabb.maxY);
                  double d10 = Mth.lerp(d7, aabb.minZ, aabb.maxZ);
                  Vec3 vec3 = new Vec3(d8 + d3, d9, d10 + d4);
                  if (pEntity.level.clip(new ClipContext(vec3, pExplosionVector, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, pEntity)).getType() == HitResult.Type.MISS)
                  {
                     ++i;
                  }

                  ++j;
               }
            }
         }

         return (float)i / (float)j;
      }
      else
      {
         return 0.0F;
      }
   }

   /**
    * Does the first part of the explosion (destroy blocks)
    */
   @Override
   public void explode()
   {
      this.level.gameEvent(this.source, GameEvent.EXPLODE, new BlockPos(this.x, this.y, this.z));
      Set<BlockPos> set = Sets.newHashSet();

      for (int j = 0; j < 16; ++j)
      {
         for (int k = 0; k < 16; ++k)
         {
            for (int l = 0; l < 16; ++l)
            {
               if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15)
               {
                  double d0 = (float) j / 15.0F * 2.0F - 1.0F;
                  double d1 = (float) k / 15.0F * 2.0F - 1.0F;
                  double d2 = (float) l / 15.0F * 2.0F - 1.0F;
                  double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                  d0 /= d3;
                  d1 /= d3;
                  d2 /= d3;
                  float f = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
                  double d4 = this.x;
                  double d6 = this.y;
                  double d8 = this.z;

                  for (; f > 0.0F; f -= 0.22500001F)
                  {
                     BlockPos blockpos = new BlockPos(d4, d6, d8);
                     BlockState blockstate = this.level.getBlockState(blockpos);
                     FluidState fluidstate = this.level.getFluidState(blockpos);
                     if (!this.level.isInWorldBounds(blockpos))
                     {
                        break;
                     }

                     Optional<Float> optional = this.damageCalculator.getBlockExplosionResistance(this, this.level, blockpos, blockstate, fluidstate);
                     if (optional.isPresent())
                     {
                        f -= (optional.get() + 0.3F) * 0.3F;
                     }

                     if (f > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.level, blockpos, blockstate, f))
                     {
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
      float f2 = this.radius * 2.0F;
      int k1 = Mth.floor(this.x - (double)f2 - 1.0D);
      int l1 = Mth.floor(this.x + (double)f2 + 1.0D);
      int i2 = Mth.floor(this.y - (double)f2 - 1.0D);
      int i1 = Mth.floor(this.y + (double)f2 + 1.0D);
      int j2 = Mth.floor(this.z - (double)f2 - 1.0D);
      int j1 = Mth.floor(this.z + (double)f2 + 1.0D);
      List<Entity> list = this.level.getEntities(this.source, new AABB(k1, i2, j2, l1, i1, j1));
      net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.level, this, list, f2);
      Vec3 vec3 = new Vec3(this.x, this.y, this.z);

      for (Entity entity : list)
      {
         if (!entity.ignoreExplosion())
         {
            double d12 = Math.sqrt(entity.distanceToSqr(vec3)) / (double) f2;
            if (d12 <= 1.0D)
            {
               double d5 = entity.getX() - this.x;
               double d7 = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
               double d9 = entity.getZ() - this.z;
               double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
               if (d13 != 0.0D)
               {
                  d5 /= d13;
                  d7 /= d13;
                  d9 /= d13;
                  double d14 = getSeenPercent(vec3, entity);
                  double d10 = (1.0D - d12) * d14;
                  entity.hurt(this.getDamageSource(), this.damagemultiplier * (int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f2 + 1.0D));
                  double d11 = d10;
                  if (entity instanceof LivingEntity)
                  {
                     d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity) entity, d10);
                  }

                  entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
                  if (entity instanceof Player)
                  {
                     Player player = (Player) entity;
                     if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying))
                     {
                        this.hitPlayers.put(player, new Vec3(d5 * d10, d7 * d10, d9 * d10));
                     }
                  }
               }
            }
         }
      }

   }

   /**
    * Does the second part of the explosion (sound, particles, drop spawn)
    */
   @Override
   public void finalizeExplosion(boolean pSpawnParticles)
   {
      if (this.level.isClientSide)
      {
         this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);
      }

      boolean flag = this.blockInteraction != BlockInteraction.NONE;
      if (pSpawnParticles)
      {
         if (!(this.radius < 2.0F) && flag)
         {
            this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
         }
         else
         {
            this.level.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
         }
      }

      if (flag)
      {
         ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
         Collections.shuffle(this.toBlow, this.level.random);

         for (BlockPos blockpos : this.toBlow)
         {
            BlockState blockstate = this.level.getBlockState(blockpos);
            Block block = blockstate.getBlock();
            if (!blockstate.isAir())
            {
               BlockPos blockpos1 = blockpos.immutable();
               this.level.getProfiler().push("explosion_blocks");
               if (blockstate.canDropFromExplosion(this.level, blockpos, this) && this.level instanceof ServerLevel)
               {
                  BlockEntity blockentity = blockstate.hasBlockEntity() ? this.level.getBlockEntity(blockpos) : null;
                  LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel)this.level)).withRandom(this.level.random).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity).withOptionalParameter(LootContextParams.THIS_ENTITY, this.source);
                  if (this.blockInteraction == BlockInteraction.DESTROY)
                  {
                     lootcontext$builder.withParameter(LootContextParams.EXPLOSION_RADIUS, this.radius);
                  }

                  blockstate.getDrops(lootcontext$builder).forEach((p_46074_) -> {
                     addBlockDrops(objectarraylist, p_46074_, blockpos1);
                  });
               }

               blockstate.onBlockExploded(this.level, blockpos, this);
               this.level.getProfiler().pop();
            }
         }

         for (Pair<ItemStack, BlockPos> pair : objectarraylist)
         {
            Block.popResource(this.level, pair.getSecond(), pair.getFirst());
         }
      }

      if (this.fire)
      {
         for (BlockPos blockpos2 : this.toBlow)
         {
            if (this.random.nextInt(3) == 0 && this.level.getBlockState(blockpos2).isAir() && this.level.getBlockState(blockpos2.below()).isSolidRender(this.level, blockpos2.below()))
            {
               this.level.setBlockAndUpdate(blockpos2, BaseFireBlock.getState(this.level, blockpos2));
            }
         }
      }

   }

   private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> pDropPositionArray, ItemStack pStack, BlockPos pPos)
   {
      int i = pDropPositionArray.size();

      for (int j = 0; j < i; ++j)
      {
         Pair<ItemStack, BlockPos> pair = pDropPositionArray.get(j);
         ItemStack itemstack = pair.getFirst();
         if (ItemEntity.areMergable(itemstack, pStack))
         {
            ItemStack itemstack1 = ItemEntity.merge(itemstack, pStack, 16);
            pDropPositionArray.set(j, Pair.of(itemstack1, pair.getSecond()));
            if (pStack.isEmpty())
            {
               return;
            }
         }
      }

      pDropPositionArray.add(Pair.of(pStack, pPos));
   }

   @Override
   public DamageSource getDamageSource()
   {
      return this.damageSource;
   }

   @Override
   public Map<Player, Vec3> getHitPlayers()
   {
      return this.hitPlayers;
   }

   /**
    * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
    */
   @Nullable
   @Override
   public LivingEntity getSourceMob()
   {
      if (this.source == null)
      {
         return null;
      }
      else if (this.source instanceof PrimedTnt)
      {
         return ((PrimedTnt)this.source).getOwner();
      }
      else if (this.source instanceof LivingEntity)
      {
         return (LivingEntity)this.source;
      }
      else
      {
         if (this.source instanceof Projectile)
         {
            Entity entity = ((Projectile)this.source).getOwner();
            if (entity instanceof LivingEntity)
            {
               return (LivingEntity)entity;
            }
         }

         return null;
      }
   }

   @Override
   public void clearToBlow()
   {
      this.toBlow.clear();
   }

   @Override
   public List<BlockPos> getToBlow()
   {
      return this.toBlow;
   }

   @Override
   public Vec3 getPosition()
   {
      return this.position;
   }

   @Nullable
   @Override
   public Entity getExploder()
   {
      return this.source;
   }
}
