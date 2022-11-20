package ru.magistu.siegemachines.entity.machine;

import ru.magistu.siegemachines.entity.projectile.ProjectileBuilder;
import net.minecraft.world.phys.Vec3;

public enum MachineType
{
    MORTAR(80, 9, 0.0f, 85.0f, 0.0f, 0.0f, 0.5f, 0.5f, true, 10, 10, 200,
            new Vec3(-0.5, 0.0, -20.0).scale(1 / 16.0), new Vec3(0.0, 17.0, 7.0).scale(1 / 16.0), new Vec3(0.0, 0.0, 12.0).scale(1 / 16.0),
            2.5f, 0.2f, ProjectileBuilder.CANNON_AMMO, true),
    CULVERIN(80, 9, 0.0f, 85.0f, 0.0f, 0.0f, 0.5f, 0.5f, true, 10, 10, 200,
            new Vec3(-0.5, 0.0, -20.0).scale(1 / 16.0), new Vec3(0.0, 17.0, 7.0).scale(1 / 16.0), new Vec3(0.0, 0.0, 12.0).scale(1 / 16.0),
            3.0f, 0.2f, ProjectileBuilder.CANNON_AMMO, true),
    TREBUCHET(300, 9, -45.0f, 75.0f, 0.0f, 0.0f, 0.1f, 0.5f, true, 38, 137, 400,
            new Vec3(0.0, 0.0, 28.0).scale(1 / 16.0), new Vec3(0.0, 19.0, -3.0), new Vec3(0.0, 10.0, -1.0),
            2.8f, 0.2f, ProjectileBuilder.THROWING_AMMO, false),
    CATAPULT(100, 9, 0.0f, 75.0f, 0.0f, 0.0f, 0.2f, 1.0f, true, 2, 10, 200,
            new Vec3(30.0, 0.0, -40.0).scale(1 / 16.0), new Vec3(0.0, 51.0, -5.0).scale(1 / 16.0), new Vec3(0.0, 0.0, 0.0),
            2.0f, 0.2f, ProjectileBuilder.THROWING_AMMO, false),
    BALLISTA(50, 9, -30.0f, 60.0f, -180.0f, 180.0f, 0.0f, 3.0f, false, 1, 20, 120,
            new Vec3(0.0, 0.0, -30.0).scale(1 / 16.0), new Vec3(0.0, 22.5, 0.0).scale(1 / 16.0), new Vec3(0.0, 0.0, 17.0).scale(1 / 16.0),
            4.5f, 0.04f, ProjectileBuilder.BALLISTA_AMMO, false),
    BATTERING_RAM(250, 9, 0.0f, 0.0f, 0.0f, 0.0f, 0.1f, 0.0f, false, 5, 5, 100,
            new Vec3(12.0, 0.0, -48.0).scale(1 / 16.0), new Vec3(0.0, 26.0, 36.0).scale(1 / 16.0), new Vec3(0.0, 0.0, 32.0).scale(1 / 16.0),
            0.0f, 0.5f, ProjectileBuilder.NO_AMMO, false);

    public final int health;
    public final int containersize;
    public final float turretminpitch;
    public final float turretmaxpitch;
    public final float turretminyaw;
    public final float turretmaxyaw;
    public final float rotationspeed;
    public final float turretspeed;
    public final boolean yawfirst;
    public final int userealisetime;
    public final int usetime;
    public final int delaytime;
    public final Vec3 passengerpos;
    public final Vec3 turretpivot;
    public final Vec3 turretvector;
    public final float projectilespeed;
    public final float inaccuracy;
    public final ProjectileBuilder[] ammo;
    public final boolean usesgunpowder;

    MachineType(
            int health,
            int containersize,
            float turretminpitch,
            float turretmaxpitch,
            float turretminyaw,
            float turretmaxyaw,
            float rotationspeed,
            float turretspeed,
            boolean yawfirst,
            int shootingtime,
            int usetime,
            int delaytime,
            Vec3 passengerpos,
            Vec3 turretpivot,
            Vec3 turretvector,
            float projectilespeed,
            float inaccuracy,
            ProjectileBuilder[] ammo,
            boolean usesgunpowder)
    {
        this.health = health;
        this.containersize = containersize;
        this.turretminpitch = turretminpitch;
        this.turretmaxpitch = turretmaxpitch;
        this.turretminyaw = turretminyaw;
        this.turretmaxyaw = turretmaxyaw;
        this.rotationspeed = rotationspeed;
        this.turretspeed = turretspeed;
        this.yawfirst = yawfirst;
        this.userealisetime = shootingtime;
        this.usetime = usetime;
        this.delaytime = delaytime;
        this.passengerpos = passengerpos;
        this.turretpivot = turretpivot;
        this.turretvector = turretvector;
        this.projectilespeed = projectilespeed;
        this.inaccuracy = inaccuracy;
        this.ammo = ammo;
        this.usesgunpowder = usesgunpowder;
    }
}