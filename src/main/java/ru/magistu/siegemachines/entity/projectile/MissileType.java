package ru.magistu.siegemachines.entity.projectile;

public enum MissileType
{
    CANNONBALL(15.0f, 1.5f, true, 3.0f, FlightType.SPINNING, 1.0f),
    STONE(50.0f, 1.5f, true, 3.0f, FlightType.SPINNING, 1.0f),
    GIANT_ARROW(5.0f, 1.5f, false, 0.0f, FlightType.AHEAD, 0.5f);

    public final float mass;
    public final float knockback;
	public final boolean explosive;
	public final float explosionradius;
    public final FlightType flighttype;
    public final float armorpiercing;

    MissileType(float mass, float knockback, boolean explosive, float explosionradius, FlightType headingtype, float armorpiercing)
    {
        this.mass = mass;
        this.knockback = knockback;
        this.explosive = explosive;
        this.explosionradius = explosionradius;
        this.flighttype = headingtype;
        this.armorpiercing = armorpiercing;
    }
}
