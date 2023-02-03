package ru.itzme1on.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ru.itzme1on.siegemachines.SiegeMachines.MOD_ID)
public class SiegeMachines {
    public SiegeMachines() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ru.itzme1on.siegemachines.SiegeMachines.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ru.itzme1on.siegemachines.SiegeMachines.init();
    }
}
