package ru.magistu.siegemachines.client.renderer.model;

import net.minecraft.resources.ResourceLocation;
import ru.magistu.siegemachines.entity.machine.Culverin;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.util.Optional;

public class CulverinGeoModel extends DefaultedEntityGeoModel<Culverin> {
    public CulverinGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public void setCustomAnimations(Culverin animatable, long instanceId, AnimationState<Culverin> animationState) {
        Optional<GeoBone> bone0 = getBone("Barrel");
        bone0.ifPresent(bone -> {
            bone.setRotX(-animatable.getTurretPitch() * (float) Math.PI / 180.0f);
            bone.setRotY(-animatable.getTurretYaw() * (float) Math.PI / 180.0f);
        });
        Optional<GeoBone> bone1 = getBone("Wheels");
        bone1.ifPresent(bone -> {
            bone.setRotX((float) (-animatable.getLerpedWheelPitch(animationState.getPartialTick())));
        });
    }
}
