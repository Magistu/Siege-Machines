package ru.magistu.siegemachines.client.renderer.model;

import net.minecraft.resources.ResourceLocation;
import ru.magistu.siegemachines.entity.machine.Cannon;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;


public class MortarModel extends DefaultedEntityGeoModel<Cannon> {
    public MortarModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public void setCustomAnimations(Cannon animatable, long instanceId, AnimationState<Cannon> animationState) {
        float partialTick = animationState.getPartialTick();
        GeoBone barrel = getBone("Barrel").orElseThrow();
        barrel.setRotX(-animatable.getTurretPitch(partialTick) * (float) Math.PI / 180.0f);
        barrel.setRotY(-animatable.getTurretYaw(partialTick) * (float) Math.PI / 180.0f);

        GeoBone wheels = getBone("Wheels").orElseThrow();
        wheels.setRotX((float) (-animatable.getLerpedWheelPitch(partialTick)));
    }
}
