package ru.magistu.siegemachines.util;

import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.RawAnimation;

public class BaseAnimations {
    public static RawAnimation SHOOTING_ANIM = RawAnimation.begin().then("Shooting", Animation.LoopType.HOLD_ON_LAST_FRAME);
    public static RawAnimation RELOADING_ANIM = RawAnimation.begin().then("Reloading", Animation.LoopType.HOLD_ON_LAST_FRAME);
    public static RawAnimation IDLE_RELOADED_ANIM = RawAnimation.begin().then("IdleReloaded", Animation.LoopType.LOOP);
    public static RawAnimation IDLE_NOT_RELOADED_ANIM = RawAnimation.begin().then("IdleNotReloaded", Animation.LoopType.LOOP);

    public static RawAnimation MOVING_ANIM = RawAnimation.begin().then("Moving", Animation.LoopType.LOOP);
    public static RawAnimation HITTING_ANIM = RawAnimation.begin().then("Hitting", Animation.LoopType.LOOP);
}
