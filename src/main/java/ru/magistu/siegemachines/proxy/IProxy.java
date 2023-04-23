package ru.magistu.siegemachines.proxy;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public interface IProxy
{
    void setup(IEventBus paramIEventBus1, IEventBus paramIEventBus2);

    public void clientSetup(FMLClientSetupEvent event);
    
    public void onPreInit();
}
