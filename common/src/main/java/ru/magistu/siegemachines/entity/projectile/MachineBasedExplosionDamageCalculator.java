package ru.magistu.siegemachines.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import ru.magistu.siegemachines.entity.machine.Machine;
import ru.magistu.siegemachines.util.CombatUtil;

import java.util.Optional;

public class MachineBasedExplosionDamageCalculator extends ExplosionDamageCalculator {
    private final Machine source;

    public MachineBasedExplosionDamageCalculator(Machine source) {
        this.source = source;
    }

    public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, FluidState fluid) {
        return super.getBlockExplosionResistance(explosion, reader, pos, state, fluid).map((p_45913_) -> {
            return this.source.getBlockExplosionResistance(explosion, reader, pos, state, fluid, p_45913_);
        });
    }

    public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
        return this.source.shouldBlockExplode(explosion, reader, pos, state, power);
    }

    public float getEntityDamageAmount(Explosion explosion, Entity entity) {
        return (float) this.source.getExplosionDamageMultiplier() * super.getEntityDamageAmount(explosion, entity);
    }

    public boolean shouldDamageEntity(Explosion explosion, Entity victim) {
        return victim != this.source && CombatUtil.canHurt(this.source, victim);
    }
}
