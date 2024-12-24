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
import ru.magistu.siegemachines.entity.machine.*;
import ru.magistu.siegemachines.entity.ModEntityTypes;
import ru.magistu.siegemachines.init.IngredientTypes;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SiegeMachines.ID)
public class SiegeMachinesForge {

    public SiegeMachinesForge(IEventBus bus, Dist dist, ModContainer modContainer) {
        bus.addListener(ModDatagen::gather);
        bus.addListener(this::addEntityAttributes);
        bus.addListener(PacketHandlerNeoForge::register);

        modContainer.registerConfig(ModConfig.Type.SERVER,SpecsConfig.SPEC,"siege-machines-specs.toml");

        if (dist.isClient()) {
            ClientProxyForge.setup(bus);
        }

        IngredientTypes.register(bus);
        SiegeMachines.init();
    }

    void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.MORTAR.get(), Mortar.setEntityAttributes(MachineType.MORTAR).build());
        event.put(ModEntityTypes.CULVERIN.get(), Mortar.setEntityAttributes(MachineType.CULVERIN).build());
        event.put(ModEntityTypes.TREBUCHET.get(), Trebuchet.setEntityAttributes(MachineType.TREBUCHET).build());
        event.put(ModEntityTypes.CATAPULT.get(), Catapult.setEntityAttributes(MachineType.CATAPULT).build());
        event.put(ModEntityTypes.BALLISTA.get(), Ballista.setEntityAttributes(MachineType.BALLISTA).build());
        event.put(ModEntityTypes.BATTERING_RAM.get(), BatteringRam.setEntityAttributes(MachineType.BATTERING_RAM).build());
        event.put(ModEntityTypes.SIEGE_LADDER.get(), SiegeLadder.setEntityAttributes(MachineType.SIEGE_LADDER).build());
    }
}
