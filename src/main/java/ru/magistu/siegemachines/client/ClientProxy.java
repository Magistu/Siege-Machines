package ru.magistu.siegemachines.client;

import ru.magistu.siegemachines.Proxy;
import net.minecraftforge.eventbus.api.IEventBus;


public class ClientProxy extends Proxy {

    @Override
    protected void sidedSetup(IEventBus bus) {
        bus.addListener(ClientRegistries::clientSetup);
        bus.addListener(ClientRegistries::registerRenderers);
        bus.addListener(ClientRegistries::onKeyRegister);
    }
}
