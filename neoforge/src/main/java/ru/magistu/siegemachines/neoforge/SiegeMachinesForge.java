package ru.magistu.siegemachines.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.client.neoforge.ClientProxyImpl;
import ru.magistu.siegemachines.config.SpecsConfig;
import ru.magistu.siegemachines.neoforge.datagen.ModDatagen;
import ru.magistu.siegemachines.neoforge.entity.ModEntityDataSerializers;
import ru.magistu.siegemachines.neoforge.init.IngredientTypes;

// The value here should match an entry in the META-INF/mods.toml file
@SuppressWarnings("unused")
@Mod(SiegeMachines.ID)
public class SiegeMachinesForge {

    public SiegeMachinesForge(IEventBus bus, Dist dist, ModContainer modContainer) {
        bus.addListener(ModDatagen::gather);

        modContainer.registerConfig(ModConfig.Type.SERVER, SpecsConfig.SPEC, "siege-machines-specs.toml");

        if (dist.isClient()) {
            ClientProxyImpl.register(bus);
        }

        IngredientTypes.register(bus);
        ModEntityDataSerializers.register(bus);
        SiegeMachines.init();
    }
}
