package ru.magistu.siegemachines.entity.projectile;

import com.mojang.math.Vector3d;
import net.minecraft.world.level.ExplosionDamageCalculator;
import ru.magistu.siegemachines.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
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
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class Missile extends ThrowableItemProjectile
{
	public MissileType type = MissileType.STONE;
	public Item item = ModItems.STONE.get();

	public Missile(EntityType<? extends Missile> entitytype, Level level)
	{
		super(entitytype, level);
	}

	public Missile(EntityType<? extends Missile> entitytype, Level level, Vector3d pos, LivingEntity entity, MissileType type, Item item)
	{
		super(entitytype, entity, level);
		this.type = type;
		this.item = item;
		this.setPos(pos.x, pos.y, pos.z);
	}

	@Override
	public @NotNull Item getDefaultItem()
	{
		return this.item;
	}

	@Override
	public @NotNull Packet<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
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
			float damage = this.type.specs.mass.get() * (float) this.getDeltaMovement().length();

			DamageSource damagesource = DamageSource.thrown(this, this.getOwner());
			if (this.type.armorpiercing >= 1.0f)
			{
				damagesource = damagesource.bypassArmor();
			}
			else if (this.type.armorpiercing > 0.0f && entity instanceof LivingEntity livingentity)
			{
				if(livingentity instanceof Player player) {
					if(player.isBlocking() && (item == ModItems.GIANT_ARROW.get() || item == Items.ARROW) && (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ShieldItem || player.isBlocking() && player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ShieldItem)) {
						return;
					}
				}
				damage -= (1.0f - this.type.armorpiercing) * (damage - CombatRules.getDamageAfterAbsorb(damage, 0, 0));
			}

			if (!this.level.isClientSide() && this.type.explosive)
			{
				this.explode(pos.x, pos.y, pos.z, 3.0F, Explosion.BlockInteraction.NONE);
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
			BlockState blockstate = this.level.getBlockState(blockpos);
			boolean smoothimpact = (blockstate == Blocks.SAND.defaultBlockState() ||
					blockstate == Blocks.RED_SAND.defaultBlockState() ||
					blockstate == Blocks.DIRT.defaultBlockState() ||
					blockstate == Blocks.GRASS_BLOCK.defaultBlockState() ||
					blockstate == Blocks.DIRT_PATH.defaultBlockState() ||
					blockstate == Blocks.COARSE_DIRT.defaultBlockState() ||
					blockstate == Blocks.SNOW_BLOCK.defaultBlockState()) &&
					blockRTR.getDirection() == Direction.UP;

			if (blockRTR.getDirection() == Direction.UP)
			{
				if (this.type.explosive)
				{
					for (int r = 0; r < this.type.specs.explosionpower.get(); ++r)
					{
						for (float a = 0; a < 2 * Math.PI; a += Math.PI / 4)
						{
							BlockPos pos = blockRTR.getBlockPos().offset(r * Math.cos(a), 0, -r * Math.sin(a));
							if (this.level.getBlockState(pos) == Blocks.GRASS_BLOCK.defaultBlockState())
							{
								this.level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
							}
						}
					}
				}
				if (!this.level.isClientSide())
				{
					this.remove(RemovalReason.KILLED);
					if (smoothimpact && this.type.explosive)
					{

						this.explode(blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.type.specs.explosionpower.get() * f, Explosion.BlockInteraction.NONE);
					}
				}
				else if (smoothimpact)
				{
					this.dustExplosion(new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(blockpos), blockpos, this.type.specs.explosionpower.get() / 2, 50);
				}
			}
			if (!this.level.isClientSide() && !smoothimpact && this.type.explosive)
			{
				this.explode(blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.type.specs.explosionpower.get() * f, Explosion.BlockInteraction.BREAK);
			}
		}

		if (result.getType() == HitResult.Type.MISS)
		{
			this.level.playSound((Player)this.getOwner(), this.getOnPos(), SoundEvents.ANVIL_BREAK, SoundSource.AMBIENT, 1.0f, 1.0f);
			if(!this.level.isClientSide())
			{
				this.remove(RemovalReason.KILLED);
			}
		}
		if (!this.level.isClientSide())
		{
			this.remove(RemovalReason.KILLED);
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
			double d0 = x - 0.05 + this.level.random.nextDouble() * 0.3;
			double d1 = y + 1.0;
			double d2 = z - 0.05 + this.level.random.nextDouble() * 0.3;
			double d3 = movement.x * this.level.random.nextDouble() * speed;
			double d4 = -movement.y * this.level.random.nextDouble() * speed * 10.0f;
			double d5 = movement.z * this.level.random.nextDouble() * speed;
			this.level.addParticle(particle, d0, d1, d2, d3, d4, d5);
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
		return this.explode(null, null, x, y, z, radius, false, mode);
	}

	public MissileExplosion explode(@Nullable DamageSource source, @Nullable ExplosionDamageCalculator context, double x, double y, double z, float size, boolean fired, Explosion.BlockInteraction mode)
	{
		MissileExplosion explosion = new MissileExplosion(this.level, this.getOwner(), source, context, x, y, z, size, fired, mode);
		if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(level, explosion)) return explosion;
		explosion.explode();
		explosion.finalizeExplosion(true);
		return explosion;
	}
}
