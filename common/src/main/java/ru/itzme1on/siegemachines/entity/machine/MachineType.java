package ru.itzme1on.siegemachines.entity.machine;

import net.minecraft.util.math.Vec3d;
import ru.itzme1on.siegemachines.entity.projectile.ProjectileBuilder;

public enum MachineType {
    MORTAR(80, 9, 0.0f, 85.0f, 0.0f, 0.0f, 0.5f, 0.5f, true, 10, 10, 200,
            new Vec3d(-0.5, 0.0, -20.0).multiply(1 / 16.0), new Vec3d(0.0, 17.0, 7.0).multiply(1 / 16.0), new Vec3d(0.0, 0.0, 12.0).multiply(1 / 16.0),
            2.5f, 0.2f, ProjectileBuilder.CANNON_AMMO, true),
    CULVERIN(80, 9, 0.0f, 85.0f, 0.0f, 0.0f, 0.5f, 0.5f, true, 10, 10, 200,
            new Vec3d(-0.5, 0.0, -20.0).multiply(1 / 16.0), new Vec3d(0.0, 17.0, 7.0).multiply(1 / 16.0), new Vec3d(0.0, 0.0, 12.0).multiply(1 / 16.0),
            3.0f, 0.2f, ProjectileBuilder.CANNON_AMMO, true),
    TREBUCHET(300, 9, -45.0f, 75.0f, 0.0f, 0.0f, 0.1f, 0.5f, true, 38, 137, 400,
            new Vec3d(0.0, 0.0, 28.0).multiply(1 / 16.0), new Vec3d(0.0, 19.0, -3.0), new Vec3d(0.0, 10.0, -1.0),
            2.8f, 0.2f, ProjectileBuilder.THROWING_AMMO, false),
    CATAPULT(100, 9, 0.0f, 75.0f, 0.0f, 0.0f, 0.2f, 1.0f, true, 2, 10, 200,
            new Vec3d(30.0, 0.0, -40.0).multiply(1 / 16.0), new Vec3d(0.0, 51.0, -5.0).multiply(1 / 16.0), new Vec3d(0.0, 0.0, 0.0),
            2.0f, 0.2f, ProjectileBuilder.THROWING_AMMO, false),
    BALLISTA(50, 9, -30.0f, 60.0f, -180.0f, 180.0f, 0.0f, 3.0f, false, 1, 20, 120,
            new Vec3d(0.0, 0.0, -30.0).multiply(1 / 16.0), new Vec3d(0.0, 22.5, 0.0).multiply(1 / 16.0), new Vec3d(0.0, 0.0, 17.0).multiply(1 / 16.0),
            4.5f, 0.04f, ProjectileBuilder.BALLISTA_AMMO, false),
    BATTERING_RAM(250, 9, 0.0f, 0.0f, 0.0f, 0.0f, 0.1f, 0.0f, false, 5, 5, 100,
            new Vec3d(12.0, 0.0, -48.0).multiply(1 / 16.0), new Vec3d(0.0, 26.0, 36.0).multiply(1 / 16.0), new Vec3d(0.0, 0.0, 32.0).multiply(1 / 16.0),
            0.0f, 0.5f, ProjectileBuilder.NO_AMMO, false);

    public final int health;
    public final int rows;
    public final float turretMinPitch;
    public final float turretMaxPitch;
    public final float turretMinYaw;
    public final float turretMaxYaw;
    public final float rotationSpeed;
    public final float turretSpeed;
    public final boolean yawFirst;
    public final int useRealiseTime;
    public final int useTime;
    public final int delayTime;
    public final Vec3d passengerPos;
    public final Vec3d turretPivot;
    public final Vec3d turretVector;
    public final float projectileSpeed;
    public final float inaccuracy;
    public final ProjectileBuilder<?>[] ammo;
    public final boolean usesGunpowder;

    MachineType(
            int health,
            int rows,
            float turretMinPitch,
            float turretMaxPitch,
            float turretMinYaw,
            float turretMaxYaw,
            float rotationSpeed,
            float turretSpeed,
            boolean yawFirst,
            int shootingTime,
            int useTime,
            int delayTime,
            Vec3d passengerPos,
            Vec3d turretPivot,
            Vec3d turretVector,
            float projectileSpeed,
            float inaccuracy,
            ProjectileBuilder<?>[] ammo,
            boolean usesGunpowder) {
        this.health = health;
        this.rows = rows;
        this.turretMinPitch = turretMinPitch;
        this.turretMaxPitch = turretMaxPitch;
        this.turretMinYaw = turretMinYaw;
        this.turretMaxYaw = turretMaxYaw;
        this.rotationSpeed = rotationSpeed;
        this.turretSpeed = turretSpeed;
        this.yawFirst = yawFirst;
        this.useRealiseTime = shootingTime;
        this.useTime = useTime;
        this.delayTime = delayTime;
        this.passengerPos = passengerPos;
        this.turretPivot = turretPivot;
        this.turretVector = turretVector;
        this.projectileSpeed = projectileSpeed;
        this.inaccuracy = inaccuracy;
        this.ammo = ammo;
        this.usesGunpowder = usesGunpowder;
    }
}
