package ru.itzme1on.siegemachines.client.render.entity;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import ru.itzme1on.siegemachines.entity.EntityTypes;
import ru.itzme1on.siegemachines.entity.machine.*;

public class EntityAttributes
{
	public static void init()
	{
		EntityAttributeRegistry.register(EntityTypes.MORTAR, () -> Mortar.setEntityAttributes(MachineType.MORTAR));
		EntityAttributeRegistry.register(EntityTypes.CULVERIN, () -> Culverin.setEntityAttributes(MachineType.CULVERIN));
		EntityAttributeRegistry.register(EntityTypes.TREBUCHET, () -> Trebuchet.setEntityAttributes(MachineType.TREBUCHET));
		EntityAttributeRegistry.register(EntityTypes.CATAPULT, () -> Catapult.setEntityAttributes(MachineType.CATAPULT));
		EntityAttributeRegistry.register(EntityTypes.BALLISTA, () -> Ballista.setEntityAttributes(MachineType.BALLISTA));
		EntityAttributeRegistry.register(EntityTypes.BATTERING_RAM, () -> BatteringRam.setEntityAttributes(MachineType.BATTERING_RAM));
	}
}
