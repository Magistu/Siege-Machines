package ru.magistu.siegemachines.entity.machine;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class Trebuchet extends Catapult implements ShootingGeoEntity {

    public Trebuchet(EntityType<? extends Mob> entitytype, Level level) {
        super(entitytype, level, MachineType.TREBUCHET);
    }
}