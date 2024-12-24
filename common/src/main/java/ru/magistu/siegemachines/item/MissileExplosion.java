package ru.magistu.siegemachines.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MissileExplosion extends Explosion
{
   public MissileExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, List<BlockPos> toBlow, BlockInteraction blockInteraction, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, Holder<SoundEvent> explosionSound) {
      super(level, source, x, y, z, radius, toBlow, blockInteraction, smallExplosionParticles, largeExplosionParticles, explosionSound);
   }

   public MissileExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, boolean fire, BlockInteraction blockInteraction, List<BlockPos> positions) {
      super(level, source, x, y, z, radius, fire, blockInteraction, positions);
   }

   public MissileExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, boolean fire, BlockInteraction blockInteraction) {
      super(level, source, x, y, z, radius, fire, blockInteraction);
   }

   public MissileExplosion(Level level, @Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float radius, boolean fire, BlockInteraction blockInteraction, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, Holder<SoundEvent> explosionSound) {
      super(level, source, damageSource, damageCalculator, x, y, z, radius, fire, blockInteraction, smallExplosionParticles, largeExplosionParticles, explosionSound);
   }
}
