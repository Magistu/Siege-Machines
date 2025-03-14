package ru.magistu.siegemachines.client.renderer.model;

import net.minecraft.resources.ResourceLocation;
import ru.magistu.siegemachines.entity.machine.SiegeLadder;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SiegeLadderModel extends DefaultedEntityGeoModel<SiegeLadder> {
    public SiegeLadderModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public void setCustomAnimations(SiegeLadder animatable, long instanceId, AnimationState<SiegeLadder> animationState) {
        float partialTick = animationState.getPartialTick();

        for (int i = 1; i <= 6; i++) {
            GeoBone wheel = getBone("Wheel" + i).orElseThrow();
            wheel.setRotX((float) (-animatable.getLerpedWheelPitch(partialTick)));
        }
    }
}
