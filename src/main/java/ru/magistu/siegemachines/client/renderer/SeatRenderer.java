package ru.magistu.siegemachines.client.renderer;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import ru.magistu.siegemachines.entity.machine.Seat;

public class SeatRenderer extends EntityRenderer<Seat>
{
	public SeatRenderer(EntityRendererProvider.Context p_174008_)
	{
		super(p_174008_);
	}

	public boolean shouldRender(Seat seat, Frustum camera, double camx, double camy, double camz) {
		return false;
	}

	@Override
	public ResourceLocation getTextureLocation(Seat pEntity)
	{
		return null;
	}
}
