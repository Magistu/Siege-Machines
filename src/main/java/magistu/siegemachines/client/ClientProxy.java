package magistu.siegemachines.client;

import magistu.siegemachines.gui.ModMenuTypes;
import magistu.siegemachines.gui.machine.MachineInventoryScreen;
import magistu.siegemachines.gui.siegeworkbench.SiegeWorkbenchScreen;
import magistu.siegemachines.proxy.IProxy;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

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

        KeyBindings.register();
    }

    public void commonSetup(FMLCommonSetupEvent event)
    {
//        Registry.register(Registry.RECIPE_TYPE, SiegeWorkbenchRecipe.Type.ID);
    }

    public void onPreInit() {}
}
