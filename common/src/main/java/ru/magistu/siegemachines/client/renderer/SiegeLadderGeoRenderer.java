package ru.magistu.siegemachines.client.renderer;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.AABB;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.client.renderer.model.SiegeLadderGeoModel;
import ru.magistu.siegemachines.entity.machine.SiegeLadder;

public class SiegeLadderGeoRenderer extends MachineGeoRenderer<SiegeLadder>
{
	public SiegeLadderGeoRenderer(EntityRendererProvider.Context renderManager)
	{
		super(renderManager, new SiegeLadderGeoModel(SiegeMachines.id("siege_ladder")));
	}

	@Override
	public boolean shouldRender(SiegeLadder animatable, Frustum camera, double camx, double camy, double camz)
	{
		if (!animatable.shouldRender(camx, camy, camz)) {
			return false;
		} else if (animatable.noCulling) {
			return true;
		} else {
			AABB aabb = animatable.getBoundingBoxForCulling().inflate(5.0D);
			if (aabb.hasNaN() || aabb.getSize() == 0.0D) {
				aabb = new AABB(animatable.getX() - 2.0D, animatable.getY() - 2.0D, animatable.getZ() - 2.0D, animatable.getX() + 2.0D, animatable.getY() + 2.0D, animatable.getZ() + 2.0D);
			}

			return camera.isVisible(aabb);
		}
	}
}
