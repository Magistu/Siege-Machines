package ru.magistu.siegemachines.entity.machine;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public interface MachineGeoEntity extends GeoEntity {

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar data) {
        AnimationController<?> controller = new CustomAnimationController<>(this, "controller", 1, this::predicate);
        data.add(controller);
    }

    @Override
    default double getTick(Object entity) {
        return getUseTicks() > 0 ? GeoEntity.super.getTick(entity) : getDelayTime() - getDelayTicks();
    }

    RawAnimation getUsingRawAnimation();

    RawAnimation getReloadingAnimation();

    int getDelayTime();

    int getDelayTicks();

    int getUseTicks();
}
