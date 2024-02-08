package ru.magistu.siegemachines.client;

import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.client.renderer.*;
import ru.magistu.siegemachines.entity.EntityTypes;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SiegeMachines.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientListener
{
	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event)
	{
        event.registerEntityRenderer(EntityTypes.MORTAR.get(), MortarGeoRenderer::new);
		event.registerEntityRenderer(EntityTypes.CULVERIN.get(), CulverinGeoRenderer::new);
		event.registerEntityRenderer(EntityTypes.TREBUCHET.get(), TrebuchetGeoRenderer::new);
        event.registerEntityRenderer(EntityTypes.CATAPULT.get(), CatapultGeoRenderer::new);
		event.registerEntityRenderer(EntityTypes.BALLISTA.get(), BallistaGeoRenderer::new);
		event.registerEntityRenderer(EntityTypes.BATTERING_RAM.get(), BatteringRamGeoRenderer::new);
		event.registerEntityRenderer(EntityTypes.SIEGE_LADDER.get(), SiegeLadderGeoRenderer::new);

		event.registerEntityRenderer(EntityTypes.CANNONBALL.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(EntityTypes.GIANT_STONE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(EntityTypes.STONE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(EntityTypes.GIANT_ARROW.get(), GiantArrowRenderer::new);

		event.registerEntityRenderer(EntityTypes.SEAT.get(), SeatRenderer::new);
	}
}
