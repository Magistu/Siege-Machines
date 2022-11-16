package magistu.siegemachines.entity.projectile;

import magistu.siegemachines.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.ExplosionContext;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public abstract class Missile extends ProjectileItemEntity
{
	public MissileType type = MissileType.STONE;
	public Item item = ModItems.STONE.get();

	public Missile(EntityType<? extends Missile> entitytype, World level)
    {
		super(entitytype, level);
	}

	public Missile(EntityType<? extends Missile> entitytype, World level, Vector3d pos, LivingEntity entity, MissileType type, Item item)
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
	public @NotNull IPacket<?> getAddEntityPacket()
    {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void onHit(RayTraceResult result)
    {
		if (result.getType() == RayTraceResult.Type.ENTITY)
        {
			EntityRayTraceResult entityRTR = (EntityRayTraceResult)result;
			Vector3d pos = entityRTR.getLocation();
			Entity entity = entityRTR.getEntity();
			float damage = this.type.specs.mass.get() * (float) this.getDeltaMovement().length();

			DamageSource damagesource = DamageSource.thrown(this, this.getOwner());
			if (this.type.armorpiercing >= 1.0f)
			{
				damagesource = damagesource.bypassArmor();
			}
			else if (this.type.armorpiercing > 0.0f && entity instanceof LivingEntity)
			{
				LivingEntity livingentity = (LivingEntity) entity;
				damage -= (1.0f - this.type.armorpiercing) * (damage - CombatRules.getDamageAfterAbsorb(damage, (float) livingentity.getArmorValue(), (float) livingentity.getAttributeValue(Attributes.ARMOR_TOUGHNESS)));
				damagesource = damagesource.bypassArmor();
			}

			if (!this.level.isClientSide() && this.type.explosive)
            {
				this.level.explode(this.getOwner(), pos.x, pos.y, pos.z, 3.0F, Explosion.Mode.NONE);
				this.remove();
			}

			entity.hurt(damagesource, damage);
			Vector3d vector3d = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double) this.type.knockback * 0.6D);
			if (vector3d.lengthSqr() > 0.0D)
			{
				entity.push(vector3d.x, 0.1D, vector3d.z);
			}
		}

		if (result.getType() == RayTraceResult.Type.BLOCK)
        {
			BlockRayTraceResult blockRTR = (BlockRayTraceResult)result;
			BlockPos blockpos = blockRTR.getBlockPos();
			BlockState blockstate = this.level.getBlockState(blockpos);
			boolean smoothimpact = 
					!this.type.specs.destroysground.get() && (
					blockstate == Blocks.SAND.defaultBlockState() ||
					blockstate == Blocks.RED_SAND.defaultBlockState() ||
					blockstate == Blocks.DIRT.defaultBlockState() ||
					blockstate == Blocks.GRASS_BLOCK.defaultBlockState() ||
					blockstate == Blocks.GRASS_PATH.defaultBlockState() ||
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
							BlockPos pos = blockRTR.getBlockPos().offset(r * MathHelper.cos(a), 0, -r * MathHelper.sin(a));
							if (this.level.getBlockState(pos) == Blocks.GRASS_BLOCK.defaultBlockState())
							{
								this.level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
							}
						}
					}
				}
				if (!this.level.isClientSide())
				{
					this.remove();
					if (smoothimpact && this.type.explosive)
					{
						this.level.explode(this.getOwner(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.type.specs.explosionpower.get(), Explosion.Mode.NONE);
					}
				}
				else if (smoothimpact)
				{
					this.dustExplosion(new BlockParticleData(ParticleTypes.BLOCK, blockstate).setPos(blockpos), blockpos, this.type.specs.explosionpower.get() / 2, 50);
				}
			}
			if (!this.level.isClientSide() && !smoothimpact && this.type.explosive)
			{
				this.level.explode(this.getOwner(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.type.specs.explosionpower.get(), Explosion.Mode.BREAK);
			}
		}

		if (result.getType() == RayTraceResult.Type.MISS)
        {
			this.level.playSound((PlayerEntity)this.getOwner(), this.getOnPos(), SoundEvents.ANVIL_BREAK, SoundCategory.AMBIENT, 1.0f, 1.0f);
			if(!this.level.isClientSide())
            {
				this.remove();
			}
		}
		if (!this.level.isClientSide())
        {
			this.remove();
		}
	}

	private void dustExplosion(IParticleData particle, BlockPos blockpos, double speed, int amount)
	{
		this.dustExplosion(particle, blockpos.getX(), blockpos.getY(), blockpos.getZ(), speed, amount);
	}

	private void dustExplosion(IParticleData particle, double x, double y, double z, double speed, int amount)
    {
        for (int i = 0; i < amount; ++i)
        {
			Vector3d movement = this.getDeltaMovement();
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
			this.xRot += 0.5;
			this.setRot(this.yRot, this.xRot);
		}

		super.tick();
	}
}
