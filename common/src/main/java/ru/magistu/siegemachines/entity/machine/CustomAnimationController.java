package ru.magistu.siegemachines.entity.machine;

import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;

class CustomAnimationController<T extends GeoAnimatable> extends AnimationController<T> {

    public CustomAnimationController(T animatable, AnimationStateHandler<T> animationHandler) {
        super(animatable, animationHandler);
    }

    public CustomAnimationController(T animatable, String name, AnimationStateHandler<T> animationHandler) {
        super(animatable, name, animationHandler);
    }

    public CustomAnimationController(T animatable, int transitionTickTime, AnimationStateHandler<T> animationHandler) {
        super(animatable, transitionTickTime, animationHandler);
    }

    public CustomAnimationController(T animatable, String name, int transitionTickTime, AnimationStateHandler<T> animationHandler) {
        super(animatable, name, transitionTickTime, animationHandler);
    }

    public void setAnimationState(State state) {
        this.animationState = state;
    }

}
