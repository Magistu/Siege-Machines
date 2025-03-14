package ru.magistu.siegemachines.client.renderer.model;

import net.minecraft.resources.ResourceLocation;
import ru.magistu.siegemachines.entity.machine.Catapult;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BallistaModel extends DefaultedEntityGeoModel<Catapult> {
    public BallistaModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public void setCustomAnimations(Catapult animatable, long instanceId, AnimationState<Catapult> animationState) {
        float partialTick = animationState.getPartialTick();

        GeoBone turret = getBone("Balliste").orElseThrow();
        turret.setRotX(-animatable.getTurretPitch(partialTick) * (float) Math.PI / 180.0f);
        turret.setRotY(-animatable.getTurretYaw(partialTick) * (float) Math.PI / 180.0f);

        GeoBone projectile = getBone("BallistaArrow").orElseThrow();

        projectile.setRotX(-animatable.getTurretPitch(partialTick) * (float) Math.PI / 180.0f);
        projectile.setRotY(-animatable.getTurretYaw(partialTick) * (float) Math.PI / 180.0f);

        int useticks = animatable.getUseTicks();
        boolean shouldrender = ((useticks <= 0 && animatable.shootingticks <= 0) || (useticks > 0 && animatable.shootingticks > 0)) && animatable.getDelayTicks() <= 0;
        boolean showProjectile = shouldrender && animatable.hasAmmo();
        projectile.setHidden(!showProjectile);
    }
}
