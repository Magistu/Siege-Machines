package magistu.siegemachines.server;

import magistu.siegemachines.proxy.IProxy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ServerProxy implements IProxy
{
    public void setup(IEventBus modEventBus, IEventBus forgeEventBus) {}

    public void clientSetup(FMLClientSetupEvent event) {}

    public void onPreInit() {}
}
