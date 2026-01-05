package ru.magistu.siegemachines.entity.projectile;

import ru.magistu.siegemachines.config.MissileSpecs;
import ru.magistu.siegemachines.config.SpecsConfig;

public enum MissileType
{
    NONE(SpecsConfig.STONE, FlightType.NONE),
    CANNONBALL(SpecsConfig.CANNONBALL, FlightType.SPINNING),
    STONE(SpecsConfig.STONE, FlightType.SPINNING),
    GIANT_STONE(SpecsConfig.GIANT_STONE, FlightType.SPINNING);

    public final MissileSpecs specs;
    public final FlightType flighttype;

    MissileType(MissileSpecs specs, FlightType headingtype)
    {
        this.specs = specs;
        this.flighttype = headingtype;
    }
}
