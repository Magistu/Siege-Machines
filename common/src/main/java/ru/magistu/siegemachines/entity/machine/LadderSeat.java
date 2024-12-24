package ru.magistu.siegemachines.entity.machine;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import ru.magistu.siegemachines.entity.ModEntityTypes;


public class LadderSeat extends Seat
{
	public final float climbspeed = 0.02f;
	private float highness = 0.0f;
	public final SiegeLadder parent;
	
	public LadderSeat(SiegeLadder parent)
	{
		super(ModEntityTypes.SEAT.get(), parent.level());
		this.parent = parent;
	}
	
	public float getHighness()
	{
		return this.highness;
	}

	public void setHighness(float highness)
	{
		this.highness = highness;
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand)
	{
		InteractionResult result = super.interact(player, hand);
		if (result.consumesAction())
			return result;
		
		if (player.isSecondaryUseActive())
			return InteractionResult.PASS;
		else if (this.isVehicle())
			return InteractionResult.PASS;
		else if (!this.level().isClientSide)
			return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
		else
			return InteractionResult.SUCCESS;
	}
	
	//@Override
	public boolean shouldRiderSit()
	{
		return false;
	}

	@Override
	protected void removePassenger(Entity entity)
	{
		this.highness = 0.0f;
		super.removePassenger(entity);
	}
	
	public float climb()
	{
		if (this.getFirstPassenger() instanceof LivingEntity livingentity)
		{
			if (livingentity.zza < -this.climbspeed && this.highness >= this.climbspeed)
				return this.highness - this.climbspeed;
			if (livingentity.zza > this.climbspeed && this.highness <= 1.0f - this.climbspeed)
				return this.highness + this.climbspeed;
		}
		
		return this.highness;
	}
}
