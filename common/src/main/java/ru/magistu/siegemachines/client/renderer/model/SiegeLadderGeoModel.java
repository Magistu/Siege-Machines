package ru.magistu.siegemachines.client.renderer.model;

import net.minecraft.resources.ResourceLocation;
import ru.magistu.siegemachines.entity.machine.SiegeLadder;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.util.Optional;

public class SiegeLadderGeoModel extends DefaultedEntityGeoModel<SiegeLadder> {
    public SiegeLadderGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public void setCustomAnimations(SiegeLadder animatable, long instanceId, AnimationState<SiegeLadder> animationState) {
        float partialTick = animationState.getPartialTick();

        for (int i = 0; i < 6;i++) {
            Optional<GeoBone> bone1 = getBone("Wheel"+(i+1));
            bone1.ifPresent(bone -> {
                bone.setRotX((float) (-animatable.getLerpedWheelPitch(partialTick)));
            });
        }
    }
}
