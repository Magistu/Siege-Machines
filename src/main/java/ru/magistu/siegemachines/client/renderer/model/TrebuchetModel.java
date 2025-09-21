package ru.magistu.siegemachines.client.renderer.model;

import net.minecraft.resources.ResourceLocation;
import ru.magistu.siegemachines.entity.machine.Catapult;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.util.Optional;

public class TrebuchetModel extends DefaultedEntityGeoModel<Catapult> {
    public TrebuchetModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public void setCustomAnimations(Catapult animatable, long instanceId, AnimationState<Catapult> animationState) {
        Optional<GeoBone> projectile = getBone("Cobblestone");

        int useticks = animatable.getUseTicks();
        boolean shouldrender = ((useticks <= 0 && animatable.shootingticks <= 0) || (useticks > 0 && animatable.shootingticks > 0)) && animatable.getDelayTicks() <= 0;
        boolean showProjectile = shouldrender && animatable.hasAmmo();
        projectile.ifPresent(bone -> bone.setHidden(!showProjectile));
    }
}
