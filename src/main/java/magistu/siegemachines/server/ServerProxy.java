package magistu.siegemachines.server;

import magistu.siegemachines.proxy.IProxy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ServerProxy implements IProxy
{
    public void setup(IEventBus modEventBus, IEventBus forgeEventBus) {}

    public void clientSetup(FMLClientSetupEvent event) {}

    public void commonSetup(FMLCommonSetupEvent event) {
//        Registry.register(Registry.RECIPE_TYPE, SiegeWorkbenchRecipe.Type.ID, ModRecipes.SIEGE_WORKBENCH_SERIALIZER.get());
    }

    public void onPreInit() {}
}
