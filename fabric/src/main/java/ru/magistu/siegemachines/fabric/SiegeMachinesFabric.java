package ru.magistu.siegemachines.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.neoforged.fml.config.ModConfig;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.config.SpecsConfig;
import ru.magistu.siegemachines.entity.ModEntityTypes;
import ru.magistu.siegemachines.entity.machine.Machine;
import ru.magistu.siegemachines.entity.machine.MachineType;
import ru.magistu.siegemachines.fabric.entity.ModEntityDataSerializers;
import ru.magistu.siegemachines.fabric.event.CommonEvents;
import ru.magistu.siegemachines.fabric.init.IngredientTypes;
import ru.magistu.siegemachines.network.ModNetwork;

public class SiegeMachinesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        // CRITICAL: Register custom entity data serializers FIRST before anything else
        ModEntityDataSerializers.register();

        NeoForgeConfigRegistry.INSTANCE.register(SiegeMachines.ID, ModConfig.Type.SERVER, SpecsConfig.SPEC, "siege-machines-specs.toml");

        // Initialize common mod content (this registers everything)
        SiegeMachines.init();

        // Initialize ingredient types
        IngredientTypes.register();

        // Initialize events
        CommonEvents.register();

        // Register networking
        ModNetwork.register();

        // Register entity attributes - FABRIC SPECIFIC, required for Mob entities
        registerEntityAttributes();
    }

    private void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntityTypes.MORTAR.get(), Machine.setEntityAttributes(MachineType.MORTAR));
        FabricDefaultAttributeRegistry.register(ModEntityTypes.CULVERIN.get(), Machine.setEntityAttributes(MachineType.CULVERIN));
        FabricDefaultAttributeRegistry.register(ModEntityTypes.TREBUCHET.get(), Machine.setEntityAttributes(MachineType.TREBUCHET));
        FabricDefaultAttributeRegistry.register(ModEntityTypes.CATAPULT.get(), Machine.setEntityAttributes(MachineType.CATAPULT));
        FabricDefaultAttributeRegistry.register(ModEntityTypes.BALLISTA.get(), Machine.setEntityAttributes(MachineType.BALLISTA));
        FabricDefaultAttributeRegistry.register(ModEntityTypes.BATTERING_RAM.get(), Machine.setEntityAttributes(MachineType.BATTERING_RAM));
        FabricDefaultAttributeRegistry.register(ModEntityTypes.SIEGE_LADDER.get(), Machine.setEntityAttributes(MachineType.SIEGE_LADDER));
    }
}
