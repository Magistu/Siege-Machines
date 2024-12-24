package ru.magistu.siegemachines.client.renderer.model;

import net.minecraft.resources.ResourceLocation;
import ru.magistu.siegemachines.entity.machine.Catapult;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.util.Optional;

public class CatapultGeoModel extends DefaultedEntityGeoModel<Catapult> {
    public CatapultGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }


    @Override
    public void setCustomAnimations(Catapult animatable, long instanceId, AnimationState<Catapult> animationState) {
        float partialTick = animationState.getPartialTick();
        if (animatable.state == Catapult.State.RELOADING) {
            Optional<GeoBone> bone1 = getBone("Lever");
            bone1.ifPresent(bone -> {
                bone.setRotX(1.125f*animatable.getReloadProgress() - 1);

            });
        }
    }
}
