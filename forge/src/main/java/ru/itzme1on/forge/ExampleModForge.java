package ru.itzme1on.forge;

import dev.architectury.platform.forge.EventBuses;
import ru.itzme1on.siegemachines.SiegeMachines;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SiegeMachines.MOD_ID)
public class ExampleModForge {
    public ExampleModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(SiegeMachines.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        SiegeMachines.init();
    }
}
