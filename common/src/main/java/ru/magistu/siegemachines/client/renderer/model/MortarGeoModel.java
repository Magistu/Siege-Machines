package ru.magistu.siegemachines.client.renderer.model;

import net.minecraft.resources.ResourceLocation;
import ru.magistu.siegemachines.entity.machine.Mortar;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.util.Optional;

public class MortarGeoModel extends DefaultedEntityGeoModel<Mortar> {
    public MortarGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public void setCustomAnimations(Mortar animatable, long instanceId, AnimationState<Mortar> animationState) {
        float partialTick = animationState.getPartialTick();
        Optional<GeoBone> bone0 = getBone("Barrel");
        bone0.ifPresent(bone -> {
            bone.setRotX(-animatable.getTurretPitch(partialTick) * (float) Math.PI / 180.0f);
            bone.setRotY(-animatable.getTurretYaw(partialTick) * (float) Math.PI / 180.0f);
        });

        Optional<GeoBone> bone1 = getBone("Wheels");
        bone1.ifPresent(bone -> {
            bone.setRotX((float) (-animatable.getLerpedWheelPitch(animationState.getPartialTick())));
        });
    }
}
