package ru.magistu.siegemachines.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import ru.magistu.siegemachines.ModTags;
import ru.magistu.siegemachines.config.SpecsConfig;
import ru.magistu.siegemachines.entity.machine.Machine;
import ru.magistu.siegemachines.util.CombatUtil;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class Missile extends ThrowableItemProjectile {
    public MissileType type = MissileType.STONE;
    protected Entity engine = null;

    public Missile(EntityType<? extends Missile> entitytype, Level level) {
        super(entitytype, level);
    }

    public Missile(EntityType<? extends Missile> entitytype, Level level, Vector3d pos, LivingEntity entity, Entity engine, MissileType type) {
        super(entitytype, entity.getControllingPassenger() != null ? entity.getControllingPassenger() : entity, level);
        this.type = type;
        this.engine = engine;
        this.setPos(pos.x, pos.y, pos.z);
    }

    @Override
    public void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityRTR = (EntityHitResult) result;
            Vec3 pos = entityRTR.getLocation();
            Entity entity = entityRTR.getEntity();
            float damage = (float) (this.type.specs.mass.get() * this.getDeltaMovement().length());

            DamageSource damagesource = damageSources().thrown(this, this.getOwner());
            if (entity instanceof LivingEntity livingentity) {
                damage += this.type.armorpiercing * (damage - CombatRules.getDamageAfterAbsorb(livingentity, damage, damagesource, livingentity.getArmorValue(), (float) livingentity.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue()));
            }

            if (!this.level().isClientSide() && this.type.explosive) {
                this.explode(pos.x, pos.y, pos.z, Explosion.BlockInteraction.DESTROY);
                this.remove(RemovalReason.KILLED);
            }

            if (this.canHurt(entity)) {
                entity.hurt(damagesource, damage);
            }
            Vec3 vector3d = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double) this.type.knockback * 0.6D);
            if (vector3d.lengthSqr() > 0.0D) {
                entity.push(vector3d.x, 0.1D, vector3d.z);
            }
        }

        if (result.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockRTR = (BlockHitResult) result;
            BlockPos blockpos = blockRTR.getBlockPos();
            BlockState blockstate = this.level().getBlockState(blockpos);
            boolean smoothimpact = SpecsConfig.ENABLE_SMOOTH_IMPACT.get() && blockstate.is(ModTags.Blocks.SMOOTH_IMPACT) && blockRTR.getDirection() == Direction.UP;

            if (blockRTR.getDirection() == Direction.UP) {
                if (this.type.explosive) {
                    for (int r = 0; r < this.type.specs.explosionpower.get(); ++r) {
                        for (int a = 0; a < 8; a++) {
                            float i = (float) (a * Math.PI / 4);

                            BlockPos pos = blockRTR.getBlockPos();


                            BlockPos pos2 = BlockPos.containing(pos.getX() + r * Mth.cos(i), pos.getY(), pos.getZ() - r * Mth.sin(i));
                            if (this.level().getBlockState(pos2) == Blocks.GRASS_BLOCK.defaultBlockState()) {
                                this.level().setBlockAndUpdate(pos2, Blocks.DIRT.defaultBlockState());
                            }
                        }
                    }
                }
                if (!this.level().isClientSide()) {
                    this.discard();
                    if (smoothimpact && this.type.explosive) {

                        this.explode(blockpos.getX(), blockpos.getY(), blockpos.getZ(), Explosion.BlockInteraction.KEEP);
                    }
                } else if (smoothimpact) {
                    this.dustExplosion(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), blockpos, this.type.specs.explosionpower.get() / 2, 50);
                }
            }
            if (!this.level().isClientSide() && !smoothimpact && this.type.explosive) {
                this.explode(blockpos.getX(), blockpos.getY(), blockpos.getZ(), Explosion.BlockInteraction.DESTROY);
            }
        }

        if (result.getType() == HitResult.Type.MISS && this.getOwner() instanceof Player player) {
            this.level().playSound(player, this.getOnPos(), SoundEvents.ANVIL_BREAK, SoundSource.AMBIENT, 1.0f, 1.0f);
            if (!this.level().isClientSide()) {
                this.discard();
            }
        }
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }

    public boolean canHurt(Entity victim) {
        return CombatUtil.canHurt(this.getOwner(), victim);
    }

    private void dustExplosion(ParticleOptions particle, BlockPos blockpos, double speed, int amount) {
        this.dustExplosion(particle, blockpos.getX(), blockpos.getY(), blockpos.getZ(), speed, amount);
    }

    private void dustExplosion(ParticleOptions particle, double x, double y, double z, double speed, int amount) {
        for (int i = 0; i < amount; ++i) {
            Vec3 movement = this.getDeltaMovement();
            double d0 = x - 0.05 + this.level().random.nextDouble() * 0.3;
            double d1 = y + 1.0;
            double d2 = z - 0.05 + this.level().random.nextDouble() * 0.3;
            double d3 = movement.x * this.level().random.nextDouble() * speed;
            double d4 = -movement.y * this.level().random.nextDouble() * speed * 10.0f;
            double d5 = movement.z * this.level().random.nextDouble() * speed;
            this.level().addParticle(particle, d0, d1, d2, d3, d4, d5);
        }
    }

    @Override
    public void tick() {
        if (this.type.flighttype == FlightType.SPINNING) {
            this.setXRot(this.getXRot() + 0.5f);
        }

        super.tick();
    }

    public MissileExplosion explode(double x, double y, double z, Explosion.BlockInteraction mode) {
        return this.explode(x, y, z, false, mode);
    }

    public MissileExplosion explode(double x, double y, double z, boolean fired, Explosion.BlockInteraction mode) {
        Entity source = this.getOwner();
        float size = this.type.specs.explosionpower.get().floatValue();
        if (this.engine != null) {
            size *= 2.0f;
        }
        Entity directSource = getDirectSourceEntityInternal(source);
        Entity indirectSource = getIndirectSourceEntityInternal(source);
        MissileExplosion explosion = new MissileExplosion(this.level(), source, this.level().damageSources().explosion(directSource, indirectSource), this.getExplosionDamageCalculator(), x, y, z, size, fired, mode, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE);
        //	if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(level(), explosion)) return explosion;
        explosion.explode();
        explosion.finalizeExplosion(true);
        return explosion;
    }

    @Nullable
    private Entity getDirectSourceEntityInternal(@Nullable Entity source) {
        return source == null ? this : source.getVehicle() == null ? source : source.getVehicle();
    }

    private ExplosionDamageCalculator getExplosionDamageCalculator() {
        if (this.engine == null) {
            return new ExplosionDamageCalculator();
        }
        if (this.engine instanceof Machine machine) {
            return new MachineBasedExplosionDamageCalculator(machine);
        }
        return new EntityBasedExplosionDamageCalculator(this.engine);
    }

    @Nullable
    private static LivingEntity getIndirectSourceEntityInternal(@Nullable Entity source) {
        if (source == null) {
            return null;
        } else if (source instanceof PrimedTnt primedtnt) {
            return primedtnt.getOwner();
        } else if (source instanceof Machine machine) {
            return machine.getControllingPassenger();
        } else if (source instanceof LivingEntity livingentity) {
            return livingentity;
        } else if (source instanceof Projectile projectile) {
            Entity entity = projectile.getOwner();
            if (entity instanceof LivingEntity livingentity) {
                return livingentity;
            }
        }

        return null;
    }
}
