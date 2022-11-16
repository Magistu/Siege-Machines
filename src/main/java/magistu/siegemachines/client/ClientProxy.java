package magistu.siegemachines.client;

import magistu.siegemachines.client.renderer.GiantArrowRenderer;
import magistu.siegemachines.entity.EntityTypes;
import magistu.siegemachines.gui.ContainerTypes;
import magistu.siegemachines.gui.MachineInventoryScreen;
import magistu.siegemachines.gui.SiegeWorkbenchScreen;
import magistu.siegemachines.proxy.IProxy;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy implements IProxy
{
    public void setup(IEventBus modEventBus, IEventBus forgeEventBus)
    {
        modEventBus.addListener(this::clientSetup);
    }

    public void clientSetup(FMLClientSetupEvent event)
    {
        ScreenManager.register(ContainerTypes.MACHINE_CONTAINER.get(), MachineInventoryScreen::new);
        ScreenManager.register(ContainerTypes.SIEGE_WORKBENCH_CONTAINER.get(), SiegeWorkbenchScreen::new);

        ItemRenderer renderer = event.getMinecraftSupplier().get().getItemRenderer();
        RenderingRegistry.registerEntityRenderingHandler(EntityTypes.CANNONBALL.get(), (renderManager) -> new SpriteRenderer<>(renderManager, renderer));
        RenderingRegistry.registerEntityRenderingHandler(EntityTypes.STONE.get(), (renderManager) -> new SpriteRenderer<>(renderManager, renderer));
        RenderingRegistry.registerEntityRenderingHandler(EntityTypes.GIANT_STONE.get(), (renderManager) -> new SpriteRenderer<>(renderManager, renderer));
        RenderingRegistry.registerEntityRenderingHandler(EntityTypes.GIANT_ARROW.get(), GiantArrowRenderer::new);
        KeyBindings.register();
    }

    public void onPreInit() {}
}
