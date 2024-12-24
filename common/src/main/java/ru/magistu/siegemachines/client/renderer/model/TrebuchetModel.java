package ru.magistu.siegemachines.client.renderer.model;

import net.minecraft.resources.ResourceLocation;
import ru.magistu.siegemachines.entity.machine.Trebuchet;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.util.Optional;

public class TrebuchetModel extends DefaultedEntityGeoModel<Trebuchet> {
    public TrebuchetModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public void setCustomAnimations(Trebuchet animatable, long instanceId, AnimationState<Trebuchet> animationState) {
        float partialTicks = animationState.getPartialTick();
        Optional<GeoBone> projectile = getBone("Cobblestone");
        boolean show = (animatable.state == Trebuchet.State.IDLE_RELOADED || animatable.shootingticks > 0) && animatable.hasAmmo();
        projectile.ifPresent(bone -> bone.setHidden(!show));
    }
}
