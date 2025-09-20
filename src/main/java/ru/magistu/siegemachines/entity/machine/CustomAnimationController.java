package ru.magistu.siegemachines.entity.machine;

import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationController;

class CustomAnimationController<T extends GeoAnimatable> extends AnimationController<T> {

    public CustomAnimationController(T animatable, String name, int transitionTickTime, AnimationStateHandler<T> animationHandler) {
        super(animatable, name, transitionTickTime, animationHandler);
    }

    public void setAnimationState(State state) {
        this.animationState = state;
    }

}
