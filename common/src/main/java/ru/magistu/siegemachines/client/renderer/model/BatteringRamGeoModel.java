package ru.magistu.siegemachines.client.renderer.model;

import net.minecraft.resources.ResourceLocation;
import ru.magistu.siegemachines.entity.machine.BatteringRam;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.util.Optional;

public class BatteringRamGeoModel extends DefaultedEntityGeoModel<BatteringRam> {
    public BatteringRamGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public void setCustomAnimations(BatteringRam animatable, long instanceId, AnimationState<BatteringRam> animationState) {
        float partialTick = animationState.getPartialTick();

        for (int i = 0; i < 6;i++) {
            Optional<GeoBone> bone1 = getBone("Wheel"+(i+1));
            bone1.ifPresent(bone -> {
                bone.setRotX((float) (-animatable.getLerpedWheelPitch(partialTick)));
            });
        }
    }
}
