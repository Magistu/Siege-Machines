package ru.magistu.siegemachines.server;

import ru.magistu.siegemachines.data.recipes.ModRecipes;
import ru.magistu.siegemachines.data.recipes.SiegeWorkbenchRecipe;
import ru.magistu.siegemachines.proxy.IProxy;
import net.minecraft.core.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ServerProxy implements IProxy
{
    public void setup(IEventBus modEventBus, IEventBus forgeEventBus) {}

    public void clientSetup(FMLClientSetupEvent event) {}

    public void commonSetup(FMLCommonSetupEvent event)
    {
        Registry.register(Registry.RECIPE_TYPE, SiegeWorkbenchRecipe.TYPE_ID, ModRecipes.SIEGE_WORKBENCH_RECIPE);
    }

    public void onPreInit() {}
}
