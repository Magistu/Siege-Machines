package magistu.siegemachines.client;

import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.client.renderer.*;
import magistu.siegemachines.entity.EntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SiegeMachines.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientListener
{
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenderers(final FMLClientSetupEvent event)
	{
        RenderingRegistry.registerEntityRenderingHandler(EntityTypes.MORTAR.get(), MortarGeoRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTypes.CULVERIN.get(), CulverinGeoRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTypes.TREBUCHET.get(), TrebuchetGeoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypes.CATAPULT.get(), CatapultGeoRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTypes.BALLISTA.get(), BallistaGeoRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTypes.BATTERING_RAM.get(), BatteringRamGeoRenderer::new);
	}
}
