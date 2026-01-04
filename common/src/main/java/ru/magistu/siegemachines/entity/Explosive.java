package ru.magistu.siegemachines.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public interface Explosive {

    float getBlockResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState blockState, FluidState fluidState, float resistance);

    double getExplosionDamageMultiplier();

    boolean shouldBlockDestroy(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power);

    boolean shouldDamageEntity(Explosion explosion, Entity victim);
}
