package ru.magistu.siegemachines.item;

import ru.magistu.siegemachines.config.MissileSpecs;
import ru.magistu.siegemachines.config.SpecsConfig;

public enum MissileType
{
    CANNONBALL(SpecsConfig.CANNONBALL, 1.5f, true, FlightType.SPINNING, 1.0f),
    STONE(SpecsConfig.STONE, 1.5f, true, FlightType.SPINNING, 1.0f),
    GIANT_STONE(SpecsConfig.GIANT_STONE, 3.0f, true, FlightType.SPINNING, 1.0f);
    //GIANT_ARROW(SpecsConfig.GIANT_ARROW, 1.5f, false, FlightType.AHEAD, 0.5f);

    public final MissileSpecs specs;
    public final float knockback;
    public final boolean explosive;
    public final FlightType flighttype;
    public final float armorpiercing;

    MissileType(MissileSpecs specs, float knockback, boolean explosive, FlightType headingtype, float armorpiercing)
    {
        this.specs = specs;
        this.knockback = knockback;
        this.explosive = explosive;
        this.flighttype = headingtype;
        this.armorpiercing = armorpiercing;
    }
}
