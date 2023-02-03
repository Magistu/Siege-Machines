package ru.itzme1on.siegemachines.entity.machine;

import net.minecraft.world.phys.Vec3;
import ru.itzme1on.siegemachines.entity.projectile.ProjectileBuilder;

public enum MachineType {
    BALLISTA(50, 1, -30.0f, 60.0f,  -180.0f, 180.0f, 4.5f, 0.0f, false, 1, 20, 120,
            new Vec3(0.0, 0.0, -30.0).scale(1 / 16.0), new Vec3(0.0, 22.5, 0.0).scale(1 / 16.0), new Vec3(0.0, 0.0, 17.0).scale(1 / 16.0),
            4.5f, 0.04f, ProjectileBuilder.BALLISTA_AMMO, false);

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
    public final Vec3 passengerPos;
    public final Vec3 turretPivot;
    public final Vec3 turretVector;
    public final float projectileSpeed;
    public final float inaccuracy;
    public final ProjectileBuilder[] ammo;
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
            Vec3 passengerPos,
            Vec3 turretPivot,
            Vec3 turretVector,
            float projectileSpeed,
            float inaccuracy,
            ProjectileBuilder[] ammo,
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
