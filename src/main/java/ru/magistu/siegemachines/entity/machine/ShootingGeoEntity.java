package ru.magistu.siegemachines.entity.machine;

import ru.magistu.siegemachines.util.BaseAnimations;
import software.bernie.geckolib.core.animation.RawAnimation;

public interface ShootingGeoEntity extends MachineGeoEntity {

    @Override
    default RawAnimation getUsingRawAnimation() {
        return BaseAnimations.SHOOTING_ANIM;
    }

    @Override
    default RawAnimation getReloadingAnimation() {
        return BaseAnimations.RELOADING_ANIM;
    }
}
