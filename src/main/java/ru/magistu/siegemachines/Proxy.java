package ru.magistu.siegemachines;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import ru.magistu.siegemachines.entity.ModEntityTypes;
import ru.magistu.siegemachines.entity.machine.*;

public abstract class Proxy {
    public final void setup(IEventBus bus) {
        bus.addListener(Proxy::addEntityAttributes);
        sidedSetup(bus);
    }

    protected abstract void sidedSetup(IEventBus bus);

    private static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.MORTAR.get(), Cannon.setEntityAttributes(MachineType.MORTAR).build());
        event.put(ModEntityTypes.CULVERIN.get(), Cannon.setEntityAttributes(MachineType.CULVERIN).build());
        event.put(ModEntityTypes.TREBUCHET.get(), Trebuchet.setEntityAttributes(MachineType.TREBUCHET).build());
        event.put(ModEntityTypes.CATAPULT.get(), Catapult.setEntityAttributes(MachineType.CATAPULT).build());
        event.put(ModEntityTypes.BALLISTA.get(), Catapult.setEntityAttributes(MachineType.BALLISTA).build());
        event.put(ModEntityTypes.BATTERING_RAM.get(), BatteringRam.setEntityAttributes(MachineType.BATTERING_RAM).build());
        event.put(ModEntityTypes.SIEGE_LADDER.get(), SiegeLadder.setEntityAttributes(MachineType.SIEGE_LADDER).build());
    }
}
