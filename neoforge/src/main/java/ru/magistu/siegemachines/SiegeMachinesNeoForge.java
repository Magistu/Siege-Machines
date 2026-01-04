package ru.magistu.siegemachines;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import ru.magistu.siegemachines.client.ClientProxyForge;
import ru.magistu.siegemachines.config.SpecsConfig;
import ru.magistu.siegemachines.datagen.ModDatagen;
import ru.magistu.siegemachines.entity.EntityDataSerializers;
import ru.magistu.siegemachines.entity.machine.*;
import ru.magistu.siegemachines.entity.ModEntityTypes;
import ru.magistu.siegemachines.init.IngredientTypes;

// The value here should match an entry in the META-INF/mods.toml file
@SuppressWarnings("unused")
@Mod(SiegeMachines.ID)
public class SiegeMachinesNeoForge {

    public SiegeMachinesNeoForge(IEventBus bus, Dist dist, ModContainer modContainer) {
        bus.addListener(ModDatagen::gather);
        bus.addListener(SiegeMachinesNeoForge::addEntityAttributes);
        bus.addListener(PacketHandlerNeoForge::register);

        if (dist.isClient()) {
            ClientProxyForge.setup(bus);
        }

        SpecsConfig.init();
        IngredientTypes.register(bus);
        EntityDataSerializers.register(bus);
        SiegeMachines.init();
    }

    private static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.MORTAR.get(), Cannon.setEntityAttributes(MachineType.MORTAR).build());
        event.put(ModEntityTypes.CULVERIN.get(), Cannon.setEntityAttributes(MachineType.CULVERIN).build());
        event.put(ModEntityTypes.TREBUCHET.get(), Catapult.setEntityAttributes(MachineType.TREBUCHET).build());
        event.put(ModEntityTypes.CATAPULT.get(), Catapult.setEntityAttributes(MachineType.CATAPULT).build());
        event.put(ModEntityTypes.BALLISTA.get(), Catapult.setEntityAttributes(MachineType.BALLISTA).build());
        event.put(ModEntityTypes.BATTERING_RAM.get(), BatteringRam.setEntityAttributes(MachineType.BATTERING_RAM).build());
        event.put(ModEntityTypes.SIEGE_LADDER.get(), SiegeLadder.setEntityAttributes(MachineType.SIEGE_LADDER).build());
    }
}
