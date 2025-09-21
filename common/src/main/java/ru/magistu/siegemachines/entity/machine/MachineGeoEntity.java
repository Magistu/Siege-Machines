package ru.magistu.siegemachines.entity.machine;

import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.*;

public interface MachineGeoEntity extends GeoEntity {

    default <E extends GeoAnimatable> PlayState predicate(AnimationState<E> event) {
        ((CustomAnimationController<?>) event.getController()).setAnimationState(AnimationController.State.RUNNING);
        if (getUseTicks() > 0) {
            event.getController().setAnimation(getUsingRawAnimation());
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(getReloadingAnimation());
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
