package ru.magistu.siegemachines.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.config.SpecsConfig;
import ru.magistu.siegemachines.fabric.entity.ModEntityDataSerializers;
import ru.magistu.siegemachines.fabric.init.IngredientTypes;

public class SiegeMachinesFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        NeoForgeConfigRegistry.INSTANCE.register(SiegeMachines.ID, ModConfig.Type.SERVER, SpecsConfig.SPEC, "siege-machines-specs.toml");

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        SiegeMachines.LOG.info("Hello Fabric world!");
        IngredientTypes.register();
//        CommonEvents.register();
        ModEntityDataSerializers.register();
        SiegeMachines.init();
    }
}
