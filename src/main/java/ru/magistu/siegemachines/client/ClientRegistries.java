package ru.magistu.siegemachines.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.client.gui.ModMenuTypes;
import ru.magistu.siegemachines.client.gui.machine.MachineInventoryScreen;
import ru.magistu.siegemachines.client.gui.workbench.SiegeWorkbenchScreen;
import ru.magistu.siegemachines.client.renderer.GiantArrowRenderer;
import ru.magistu.siegemachines.client.renderer.MachineGeoRenderer;
import ru.magistu.siegemachines.client.renderer.SeatRenderer;
import ru.magistu.siegemachines.client.renderer.SiegeLadderGeoRenderer;
import ru.magistu.siegemachines.client.renderer.model.*;
import ru.magistu.siegemachines.entity.ModEntityTypes;

public class ClientRegistries {

    public static void clientSetup(FMLClientSetupEvent event)
    {
        MenuScreens.register(ModMenuTypes.MACHINE_CONTAINER.get(), MachineInventoryScreen::new);
        MenuScreens.register(ModMenuTypes.SIEGE_WORKBENCH_CONTAINER.get(), SiegeWorkbenchScreen::new);
    }

    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.MORTAR.get(), context -> new MachineGeoRenderer<>(context, new MortarModel(SiegeMachines.id("mortar"))));
        event.registerEntityRenderer(ModEntityTypes.CULVERIN.get(), context -> new MachineGeoRenderer<>(context, new CulverinModel(SiegeMachines.id("culverin"))));
        event.registerEntityRenderer(ModEntityTypes.TREBUCHET.get(), context -> new MachineGeoRenderer<>(context, new TrebuchetModel(SiegeMachines.id("trebuchet"))));
        event.registerEntityRenderer(ModEntityTypes.CATAPULT.get(), context -> new MachineGeoRenderer<>(context, new CatapultModel(SiegeMachines.id("catapult"))));
        event.registerEntityRenderer(ModEntityTypes.BALLISTA.get(),context -> new MachineGeoRenderer<>(context, new BallistaModel(SiegeMachines.id("ballista"))));
        event.registerEntityRenderer(ModEntityTypes.BATTERING_RAM.get(),context -> new MachineGeoRenderer<>(context, new BatteringRamGeoModel(SiegeMachines.id("battering_ram"))));
        event.registerEntityRenderer(ModEntityTypes.SIEGE_LADDER.get(), SiegeLadderGeoRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.CANNONBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.GIANT_STONE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.STONE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.GIANT_ARROW.get(), GiantArrowRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.SEAT.get(), SeatRenderer::new);
    }

    public static void onKeyRegister(RegisterKeyMappingsEvent ev) {
        ev.register(KeyBindings.MACHINE_USE);
        ev.register(KeyBindings.LADDER_CLIMB);
        ev.register(KeyBindings.MACHINE_INVENTORY);
    }
}
