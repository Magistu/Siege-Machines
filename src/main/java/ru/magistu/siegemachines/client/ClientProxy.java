package ru.magistu.siegemachines.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import ru.magistu.siegemachines.client.renderer.GiantArrowRenderer;
import ru.magistu.siegemachines.data.recipes.ModRecipes;
import ru.magistu.siegemachines.data.recipes.SiegeWorkbenchRecipe;
import ru.magistu.siegemachines.entity.EntityTypes;
import ru.magistu.siegemachines.entity.projectile.Stone;
import ru.magistu.siegemachines.gui.ContainerTypes;
import ru.magistu.siegemachines.gui.MachineInventoryScreen;
import ru.magistu.siegemachines.gui.SiegeWorkbenchScreen;
import ru.magistu.siegemachines.proxy.IProxy;

public class ClientProxy implements IProxy
{
    public void setup(IEventBus modEventBus, IEventBus forgeEventBus)
    {
        modEventBus.addListener(this::clientSetup);
    }

    public void clientSetup(FMLClientSetupEvent event)
    {
        MenuScreens.register(ContainerTypes.MACHINE_CONTAINER.get(), MachineInventoryScreen::new);
        MenuScreens.register(ContainerTypes.SIEGE_WORKBENCH_CONTAINER.get(), SiegeWorkbenchScreen::new);

        KeyBindings.register();

        EntityRenderers.register(EntityTypes.GIANT_ARROW.get(), GiantArrowRenderer::new);
    }

    public void commonSetup(FMLCommonSetupEvent event)
    {
        Registry.register(Registry.RECIPE_TYPE, SiegeWorkbenchRecipe.TYPE_ID, ModRecipes.SIEGE_WORKBENCH_RECIPE);
    }

    public void onPreInit() {}
}
