package ru.magistu.siegemachines.entity;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import ru.magistu.siegemachines.entity.machine.*;

public class ModEntityAttributes {
    public static void register() {
        EntityAttributeRegistry.register(ModEntityTypes.MORTAR, () -> Cannon.setEntityAttributes(MachineType.MORTAR));
        EntityAttributeRegistry.register(ModEntityTypes.CULVERIN, () -> Cannon.setEntityAttributes(MachineType.CULVERIN));
        EntityAttributeRegistry.register(ModEntityTypes.TREBUCHET, () -> Trebuchet.setEntityAttributes(MachineType.TREBUCHET));
        EntityAttributeRegistry.register(ModEntityTypes.CATAPULT, () -> Catapult.setEntityAttributes(MachineType.CATAPULT));
        EntityAttributeRegistry.register(ModEntityTypes.BALLISTA, () -> Catapult.setEntityAttributes(MachineType.BALLISTA));
        EntityAttributeRegistry.register(ModEntityTypes.BATTERING_RAM, () -> BatteringRam.setEntityAttributes(MachineType.BATTERING_RAM));
        EntityAttributeRegistry.register(ModEntityTypes.SIEGE_LADDER, () -> SiegeLadder.setEntityAttributes(MachineType.SIEGE_LADDER));
    }
}
