package ru.magistu.siegemachines.client.renderer.model;

import net.minecraft.resources.ResourceLocation;
import ru.magistu.siegemachines.entity.machine.Catapult;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.util.Optional;

public class CatapultModel extends DefaultedEntityGeoModel<Catapult> {
    public CatapultModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public void setCustomAnimations(Catapult animatable, long instanceId, AnimationState<Catapult> animationState) {
        int useticks = animatable.getUseTicks();

        if (useticks <= 0) {
            GeoBone lever = getBone("Lever").orElseThrow();
            lever.setRotX(1.125f * animatable.getReloadProgress() - 1);
        }

        Optional<GeoBone> projectile = getBone("Cobblestone");

        boolean shouldrender = ((useticks <= 0 && animatable.shootingticks <= 0) || (useticks > 0 && animatable.shootingticks > 0)) && animatable.getDelayTicks() <= 0;
        boolean showProjectile = shouldrender && animatable.hasAmmo();
        projectile.ifPresent(bone -> bone.setHidden(!showProjectile));
    }
}
