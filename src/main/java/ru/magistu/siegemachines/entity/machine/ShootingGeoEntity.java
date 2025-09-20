package ru.magistu.siegemachines.entity.machine;

import ru.magistu.siegemachines.util.BaseAnimations;

public interface ShootingGeoEntity extends MachineGeoEntity {

    @Override
    default software.bernie.geckolib.core.animation.RawAnimation getUsingRawAnimation() {
        return BaseAnimations.SHOOTING_ANIM;
    }

    @Override
    default software.bernie.geckolib.core.animation.RawAnimation getReloadingAnimation() {
        return BaseAnimations.RELOADING_ANIM;
    }
}
