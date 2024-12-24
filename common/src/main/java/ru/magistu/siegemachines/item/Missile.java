package ru.magistu.siegemachines.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;

import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import ru.magistu.siegemachines.ModTags;

public abstract class Missile extends ThrowableItemProjectile
{
	public MissileType type = MissileType.STONE;

	public Missile(EntityType<? extends Missile> entitytype, Level level)
	{
		super(entitytype, level);
	}

	public Missile(EntityType<? extends Missile> entitytype, Level level, Vector3d pos, LivingEntity entity, MissileType type)
	{
		super(entitytype, entity, level);
		this.type = type;
		this.setPos(pos.x, pos.y, pos.z);
	}

	@Override
	public void onHit(HitResult result)
	{
		float f = 2.0F;
		if (result.getType() == HitResult.Type.ENTITY)
		{
			EntityHitResult entityRTR = (EntityHitResult)result;
			Vec3 pos = entityRTR.getLocation();
			Entity entity = entityRTR.getEntity();
			float damage = (float) (this.type.specs.mass.get() * this.getDeltaMovement().length());

			DamageSource damagesource = damageSources().thrown(this, this.getOwner());
			if (this.type.armorpiercing >= 1.0f)
			{
				//damagesource = damagesource.bypassArmor();
			}
			else if (this.type.armorpiercing > 0.0f && entity instanceof LivingEntity livingentity)
			{
				if(livingentity instanceof Player player) {
					if(player.isBlocking() && (getItem().getItem() == ModItems.GIANT_ARROW.get() || getItem().getItem() == Items.ARROW) && (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ShieldItem || player.isBlocking() && player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ShieldItem)) {
						return;
					}
				}
				damage -= (1.0f - this.type.armorpiercing) * (damage - CombatRules.getDamageAfterAbsorb(livingentity,damage,damagesource, 0, 0));
			}

			if (!this.level().isClientSide() && this.type.explosive)
			{
				this.explode(pos.x, pos.y, pos.z, 3.0F, Explosion.BlockInteraction.KEEP);
				this.remove(RemovalReason.KILLED);
			}

			entity.hurt(damagesource, damage);
			Vec3 vector3d = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double) this.type.knockback * 0.6D);
			if (vector3d.lengthSqr() > 0.0D)
			{
				entity.push(vector3d.x, 0.1D, vector3d.z);
			}
		}

		if (result.getType() == HitResult.Type.BLOCK)
		{
			BlockHitResult blockRTR = (BlockHitResult)result;
			BlockPos blockpos = blockRTR.getBlockPos();
			BlockState blockstate = this.level().getBlockState(blockpos);
			boolean smoothimpact = blockstate.is(ModTags.Blocks.SMOOTH_IMPACT) && blockRTR.getDirection() == Direction.UP;

			if (blockRTR.getDirection() == Direction.UP)
			{
				if (this.type.explosive)
				{
					for (int r = 0; r < this.type.specs.explosionpower.get(); ++r)
					{
						for (int a = 0; a < 8; a ++) {
							float i = (float) (a * Math.PI/4);

							BlockPos pos = blockRTR.getBlockPos();


							BlockPos pos2 = BlockPos.containing(pos.getX() + r * Mth.cos(i), pos.getY(),pos.getZ() -r * Mth.sin(i));
							if (this.level().getBlockState(pos2) == Blocks.GRASS_BLOCK.defaultBlockState())
							{
								this.level().setBlockAndUpdate(pos2, Blocks.DIRT.defaultBlockState());
							}
						}
					}
				}
				if (!this.level().isClientSide())
				{
					this.discard();
					if (smoothimpact && this.type.explosive)
					{

						this.explode(blockpos.getX(), blockpos.getY(), blockpos.getZ(), (float) (this.type.specs.explosionpower.get() * f), Explosion.BlockInteraction.KEEP);
					}
				}
				else if (smoothimpact)
				{
					this.dustExplosion(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), blockpos, this.type.specs.explosionpower.get() / 2, 50);
				}
			}
			if (!this.level().isClientSide() && !smoothimpact && this.type.explosive)
			{
				this.explode(blockpos.getX(), blockpos.getY(), blockpos.getZ(), (float) (this.type.specs.explosionpower.get() * f), Explosion.BlockInteraction.DESTROY);
			}
		}

		if (result.getType() == HitResult.Type.MISS)
		{
			this.level().playSound((Player)this.getOwner(), this.getOnPos(), SoundEvents.ANVIL_BREAK, SoundSource.AMBIENT, 1.0f, 1.0f);
			if(!this.level().isClientSide())
			{
				this.discard();
			}
		}
		if (!this.level().isClientSide())
		{
			this.discard();
		}
	}

	private void dustExplosion(ParticleOptions particle, BlockPos blockpos, double speed, int amount)
	{
		this.dustExplosion(particle, blockpos.getX(), blockpos.getY(), blockpos.getZ(), speed, amount);
	}

	private void dustExplosion(ParticleOptions particle, double x, double y, double z, double speed, int amount)
	{
		for (int i = 0; i < amount; ++i)
		{
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
	public void tick()
	{
		if (this.type.flighttype == FlightType.SPINNING)
		{
			this.setXRot(this.getXRot() + 0.5f);
		}

		super.tick();
	}

	public MissileExplosion explode(double x, double y, double z, float radius, Explosion.BlockInteraction mode)
	{
		return this.explode(x, y, z, radius, false, mode);
	}

	public MissileExplosion explode(double x, double y, double z, float size, boolean fired, Explosion.BlockInteraction mode)
	{
		MissileExplosion explosion = new MissileExplosion(this.level(), this.getOwner(), x, y, z, size, fired, mode);
	//	if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(level(), explosion)) return explosion;
		explosion.explode();
		explosion.finalizeExplosion(true);
		return explosion;
	}
}
