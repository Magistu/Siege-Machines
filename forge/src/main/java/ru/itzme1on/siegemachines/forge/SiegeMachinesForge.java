package ru.itzme1on.siegemachines.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ru.itzme1on.siegemachines.SiegeMachinesCore;

@Mod(SiegeMachinesCore.MOD_ID)
public class SiegeMachinesForge {
    public SiegeMachinesForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(SiegeMachinesCore.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        SiegeMachinesCore.init();
    }
}
