package ru.magistu.siegemachines.client.renderer.model;

import net.minecraft.resources.ResourceLocation;
import ru.magistu.siegemachines.entity.machine.Ballista;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.util.Optional;

public class BallistaGeoModel extends DefaultedEntityGeoModel<Ballista> {
    public BallistaGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public void setCustomAnimations(Ballista animatable, long instanceId, AnimationState<Ballista> animationState) {
        Optional<GeoBone> turret = getBone("Balliste");
        turret.ifPresent(bone -> {
            bone.setRotX(-animatable.getTurretPitch() * (float) Math.PI / 180.0f);
            bone.setRotY(-animatable.getTurretYaw() * (float) Math.PI / 180.0f);
        });

        Optional<GeoBone> projectile = getBone("BallistaArrow");
        projectile.ifPresent(bone -> {
            bone.setRotX(-animatable.getTurretPitch() * (float) Math.PI / 180.0f);
            bone.setRotY(-animatable.getTurretYaw() * (float) Math.PI / 180.0f);
        });

        int useticks = animatable.getUseTicks();
        boolean shouldrender = ((useticks <= 0 && animatable.shootingticks <= 0) || (useticks > 0 && animatable.shootingticks > 0)) && animatable.getDelayTicks() <= 0;
        boolean showArrow = shouldrender && animatable.hasAmmo();
        projectile.ifPresent(bone -> bone.setHidden(!showArrow));
    }
}
