package ru.itzme1on.siegemachines.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ru.itzme1on.siegemachines.SiegeMachines;

@Mod(SiegeMachines.MOD_ID)
public class SiegeMachinesForge {
    public SiegeMachinesForge() {
        // Submit our event bus to let architectury register our content on the right time
        SiegeMachines.init();
    }
}
