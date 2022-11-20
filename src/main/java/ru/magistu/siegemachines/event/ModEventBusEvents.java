package ru.magistu.siegemachines.event;

import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.entity.EntityTypes;
import ru.magistu.siegemachines.entity.machine.*;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SiegeMachines.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
//        event.put(EntityTypes.MORTAR.get(), Mortar.setEntityAttributes(MachineType.MORTAR).build());
//        event.put(EntityTypes.CULVERIN.get(), Mortar.setEntityAttributes(MachineType.CULVERIN).build());
//        event.put(EntityTypes.TREBUCHET.get(), Trebuchet.setEntityAttributes(MachineType.TREBUCHET).build());
//        event.put(EntityTypes.CATAPULT.get(), Catapult.setEntityAttributes(MachineType.CATAPULT).build());
        event.put(EntityTypes.BALLISTA.get(), Ballista.setEntityAttributes(MachineType.BALLISTA).build());
//        event.put(EntityTypes.BATTERING_RAM.get(), Ballista.setEntityAttributes(MachineType.BATTERING_RAM).build());
    }
}
