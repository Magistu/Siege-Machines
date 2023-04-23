package ru.itzme1on.siegemachines.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import ru.itzme1on.siegemachines.entity.machine.Machine;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public abstract class MachineGeoRenderer <T extends Machine & IAnimatable> extends GeoEntityRenderer<T> {
    protected MachineGeoRenderer(EntityRendererFactory.Context renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
    }

    @Override
    protected float getDeathMaxRotation(T animatable) {
        return 0.0F;
    }
}
