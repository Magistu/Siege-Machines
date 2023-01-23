package ru.magistu.siegemachines.client.renderer;

import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public abstract class MachineGeoRenderer<T extends Machine & IAnimatable> extends GeoEntityRenderer<T>
{
    protected MachineGeoRenderer(EntityRendererProvider.Context rendermanager, AnimatedGeoModel<T> model)
    {
        super(rendermanager, model);
    }

    @Override
    protected float getDeathMaxRotation(T entity)
    {
		return 0.0F;
	}
}
