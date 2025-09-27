package ru.magistu.siegemachines.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import ru.magistu.siegemachines.entity.Explosive;
import ru.magistu.siegemachines.util.CombatUtil;
import javax.annotation.Nullable;
import java.util.Map;

public abstract class Missile extends ThrowableItemProjectile implements Explosive {
    public MissileType type = MissileType.STONE;
    protected Entity engine = null;
    private final ExplosiveBasedExplosionDamageCalculator explosionDamageCalculator = new ExplosiveBasedExplosionDamageCalculator(this);
    private final static Map<Block, Block> BLOCK_CRACKING_MAP = Map.of(
            Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS,
            Blocks.INFESTED_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS,
            Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS,
            Blocks.DEEPSLATE_TILES, Blocks.CRACKED_DEEPSLATE_TILES,
            Blocks.NETHER_BRICKS, Blocks.CRACKED_NETHER_BRICKS,
            Blocks.STONE, Blocks.COBBLESTONE,
            Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE,
            Blocks.GRASS_BLOCK, Blocks.DIRT
    );
    protected static final int MISSILE_EXPLOSION = 66;

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
    public void onHit(HitResult hit) {
        switch (hit.getType()) {
            case ENTITY -> handleEntityHit((EntityHitResult) hit);
            case BLOCK -> handleBlockHit((BlockHitResult) hit);
            case MISS -> handleMiss();
        }

        if (!this.level().isClientSide()) {
            this.discard();
        }
    }

    protected void handleEntityHit(EntityHitResult entityHit) {
        Vec3 pos = entityHit.getLocation();
        Entity victim = entityHit.getEntity();

        float damage = (float) (this.type.specs.mass.get() * this.getDeltaMovement().length() / 10.0);
        DamageSource damageSource = damageSources().thrown(this, getIndirectSourceEntityInternal());

        if (victim instanceof LivingEntity livingEntity) {
            float armor = CombatRules.getDamageAfterAbsorb(
                    livingEntity,
                    damage,
                    damageSource,
                    livingEntity.getArmorValue(),
                    (float) livingEntity.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue()
            );
            damage += this.type.specs.armorpiercing.get().floatValue() * (damage - armor);
        }

        if (!this.level().isClientSide() && this.type.specs.explosive.get()) {
            this.explode(pos.subtract(this.getDeltaMovement().normalize().scale(0.5)), Explosion.BlockInteraction.DESTROY);
        }

        if (this.canHurt(victim)) {
            victim.hurt(damageSource, damage);
            applyKnockback(victim);
        }
    }

    private void applyKnockback(Entity entity) {
        double knockback = this.type.specs.knockback.get();
        if (entity instanceof LivingEntity livingentity) {
            knockback *= (1.0 - livingentity.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE));
        }
        Vec3 knockbackVec = this.getDeltaMovement()
                .normalize()
                .scale(knockback);

        if (knockbackVec.lengthSqr() > 0.0D) {
            entity.push(knockbackVec.x, knockbackVec.y, knockbackVec.z);
        }
    }

    protected void handleBlockHit(BlockHitResult blockHit) {
        Vec3 pos = blockHit.getLocation();

        if (!this.level().isClientSide() && this.type.specs.explosive.get()) {
            Explosion.BlockInteraction interaction = getExplosionBlockInteraction();
            this.explode(pos.relative(blockHit.getDirection(), 0.5), interaction);
        }
    }

    private Explosion.BlockInteraction getExplosionBlockInteraction() {
        return this.level().getGameRules().getBoolean(GameRules.RULE_MOB_EXPLOSION_DROP_DECAY) ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY;
    }

    private void crackBlocks(BlockPos origin) {
        for (int r = 0; r < 1.5 * this.type.specs.explosionpower.get(); ++r) {
            for (int a = 0; a < 8; ++a) {
                for (int b = 0; b < 8; ++b) {
                    float phi = (float) (a * Math.PI / 4);
                    float theta = (float) (b * Math.PI / 4);

                    int dx = (int) (r * Math.sin(theta) * Math.cos(phi));
                    int dy = (int) (r * Math.cos(theta));
                    int dz = (int) (r * Math.sin(theta) * Math.sin(phi));

                    BlockPos pos2 = origin.offset(dx, dy, dz);
                    Block crackedBlock = BLOCK_CRACKING_MAP.get(this.level().getBlockState(pos2).getBlock());
                    if (crackedBlock != null) {
                        this.level().setBlockAndUpdate(pos2, crackedBlock.defaultBlockState());
                    }
                }
            }
        }
    }

    protected void handleMiss() {

    }

    public boolean canHurt(Entity victim) {
        return CombatUtil.canHurt(this.getOwner(), victim);
    }


    @Override
    public void tick() {
        if (this.type.flighttype == FlightType.SPINNING) {
            this.setXRot(this.getXRot() + 0.5f);
        }

        super.tick();
    }

    public Explosion explode(Vec3 pos, Explosion.BlockInteraction mode) {
        Entity source = this.getOwner();
        float size = this.getExplosionPower();
        Entity indirectSource = getIndirectSourceEntityInternal();
        double x = pos.x;
        double y = pos.y;
        double z = pos.z;
        boolean fired = this.type.specs.fired.get();
        MissileExplosion explosion = new MissileExplosion(level(), source, damageSources().explosion(this, indirectSource), explosionDamageCalculator, x, y, z, size, fired, mode, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE);
//        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(level(), explosion)) {
//            return explosion;
//        }
        crackBlocks(new BlockPos((int) x, (int) y, (int) z));
        explosion.explode();
        explosion.finalizeExplosion(true);
        level().broadcastEntityEvent(this, (byte) MISSILE_EXPLOSION);

        return explosion;
    }

    protected float getExplosionPower() {
        float defaultPower = this.type.specs.explosionpower.get().floatValue();
        float speed = (float) this.getDeltaMovement().length();
        float power = (float) (defaultPower * 0.3f + Math.log1p(speed) * defaultPower * 0.5f);
        return Mth.clamp(power, defaultPower * 0.7f, defaultPower * 1.5f);
    }

    @Override
    public float getBlockResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState blockState, FluidState fluidState, float resistance) {
        if (BLOCK_CRACKING_MAP.containsValue(level.getBlockState(pos).getBlock())) {
            resistance *= 0.83f;
        }
        float speed = (float) this.getDeltaMovement().length();
        float power = (float) (0.5f + Math.log1p(speed) * 0.5f);
        return resistance / Mth.clamp(power, 0.7f, 1.5f);
    }

    @Override
    public double getExplosionDamageMultiplier() {
        double defaultMultiplier = this.type.specs.explosiondamagemultiplier.get();
        double speed = this.getDeltaMovement().length();
        double multiplier = defaultMultiplier * 0.33 + Math.log1p(speed) * defaultMultiplier * 0.5;
        return Mth.clamp(multiplier, defaultMultiplier * 0.7, defaultMultiplier * 1.5);
    }

    @Override
    public boolean shouldDamageEntity(Explosion explosion, Entity victim) {
        return CombatUtil.canHurt(this.getOwner(), victim);
    }

    @Override
    public boolean shouldBlockDestroy(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
        return true;
    }

    @Nullable
    private Entity getIndirectSourceEntityInternal() {
        return this.getOwner() != null ? this.getOwner() : this.engine;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == MISSILE_EXPLOSION && this.level().isClientSide()) {
            this.explode(this.position(), Explosion.BlockInteraction.KEEP);
        } else {
            super.handleEntityEvent(id);
        }
    }
}
