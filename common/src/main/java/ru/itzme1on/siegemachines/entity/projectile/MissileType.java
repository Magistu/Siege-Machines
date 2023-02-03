package ru.itzme1on.siegemachines.entity.projectile;

public enum MissileType {
    CANNONBALL(15.0f, 1.5f, true, 3.0f, FlightType.SPINNING, 1.0f),
    STONE(50.0f, 1.5f, true, 3.0f, FlightType.SPINNING, 1.0f),
    GIANT_ARROW(5.0f, 1.5f, false, 0.0f, FlightType.AHEAD, 0.5f);

    public final float mass;
    public final float knockBack;
    public final boolean explosive;
    public final float explosionRadius;
    public final FlightType flightType;
    public final float armorPiercing;

    MissileType(float mass, float knockBack, boolean explosive, float explosionRadius, FlightType flightType, float armorPiercing) {
        this.mass = mass;
        this.knockBack = knockBack;
        this.explosive = explosive;
        this.explosionRadius = explosionRadius;
        this.flightType = flightType;
        this.armorPiercing = armorPiercing;
    }
}
