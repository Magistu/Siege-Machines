package ru.magistu.siegemachines.client.renderer;

import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public abstract class MachineGeoRenderer<T extends Machine & GeoAnimatable> extends GeoEntityRenderer<T>
{
    protected MachineGeoRenderer(EntityRendererProvider.Context rendermanager, GeoModel<T> model)
    {
        super(rendermanager, model);
    }

    @Override
    protected float getDeathMaxRotation(T entity)
    {
		return 0.0F;
	}
}
