package ru.magistu.siegemachines.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import ru.magistu.siegemachines.entity.Explosive;

import java.util.Optional;

public class ExplosiveBasedExplosionDamageCalculator extends ExplosionDamageCalculator {
    private final Explosive source;

    public ExplosiveBasedExplosionDamageCalculator(Explosive source) {
        this.source = source;
    }

    public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, FluidState fluid) {
        return super.getBlockExplosionResistance(explosion, reader, pos, state, fluid).map(
                r -> this.source.getBlockResistance(explosion, reader, pos, state, fluid, r));
    }

    public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
        return this.source.shouldBlockDestroy(explosion, reader, pos, state, power);
    }

    public float getExplosionDamageMultiplier() {
        return (float) this.source.getExplosionDamageMultiplier();
    }

    public boolean shouldDamageEntity(Explosion explosion, Entity victim) {
        return victim != this.source && this.source.shouldDamageEntity(explosion, victim);
    }
}
