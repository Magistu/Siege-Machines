package ru.magistu.siegemachines.api.enitity;

import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;

public interface Useable
{
    void use(@Nullable LivingEntity entity);
    
    UsageType getUsage();
    
    default boolean canShoot() {
        return this.getUsage() == UsageType.SHOOT;
    }
    
    enum UsageType
    {
        NONE,
        SHOOT,
        RAM,
        CLIMB
    }
    
    void setTurretRotationsDest(float pitch, float yaw);

    void setYawDest(float yaw);

    float getGlobalTurretYaw();

    float getTurretYaw();

    float getTurretPitch();
}