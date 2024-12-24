package ru.magistu.siegemachines.entity.machine;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Seat extends Entity
{
	protected int lerpSteps;
	protected double lerpX;
	protected double lerpY;
	protected double lerpZ;
	protected double lerpYRot;
	protected double lerpXRot;
	
	public Seat(EntityType entitytype, Level level)
	{
		super(entitytype, level);
	}


	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {}
	
	public boolean shouldRender(double x, double y, double z)
	{
		return false;
	}

	public boolean shouldRenderAtSqrDistance(double distance)
	{
		return false;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}

	@Override
	public void tick()
	{
		if (this.lerpSteps > 0)
		{
			double d0 = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpSteps;
			double d2 = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpSteps;
			double d4 = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpSteps;
			double d6 = Mth.wrapDegrees(this.lerpYRot - (double) this.getYRot());
			this.setYRot(this.getYRot() + (float) d6 / (float) this.lerpSteps);
			this.setXRot(this.getXRot() + (float) (this.lerpXRot - (double) this.getXRot()) / (float) this.lerpSteps);
			--this.lerpSteps;
			this.setPos(d0, d2, d4);
			this.setRot(this.getYRot(), this.getXRot());
		}
		
		super.tick();
	}

	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements)
	{
		this.lerpX = x;
		this.lerpY = y;
		this.lerpZ = z;
		this.lerpYRot = yaw;
		this.lerpXRot = pitch;
		this.lerpSteps = posRotationIncrements;
	}
}
