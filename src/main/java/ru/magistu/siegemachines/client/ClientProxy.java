package ru.magistu.siegemachines.client;

import ru.magistu.siegemachines.client.gui.ModMenuTypes;
import ru.magistu.siegemachines.client.gui.workbench.SiegeWorkbenchScreen;
import ru.magistu.siegemachines.client.gui.machine.MachineInventoryScreen;
import ru.magistu.siegemachines.proxy.IProxy;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy implements IProxy
{
    public void setup(IEventBus modEventBus, IEventBus forgeEventBus)
    {
        modEventBus.addListener(this::clientSetup);
    }

    public void clientSetup(FMLClientSetupEvent event)
    {
        MenuScreens.register(ModMenuTypes.MACHINE_CONTAINER.get(), MachineInventoryScreen::new);
        MenuScreens.register(ModMenuTypes.SIEGE_WORKBENCH_CONTAINER.get(), SiegeWorkbenchScreen::new);
    }

    public void onPreInit() {}
}
