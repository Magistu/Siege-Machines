package ru.magistu.siegemachines.client.renderer;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.client.renderer.model.*;
import ru.magistu.siegemachines.entity.ModEntityTypes;
import ru.magistu.siegemachines.entity.machine.Machine;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

import java.util.function.Supplier;

public class ModEntityRenderers {

    public static void register() {
        // Siege machines
        registerMachineRenderer(ModEntityTypes.MORTAR, new MortarModel(SiegeMachines.id("mortar")));
        registerMachineRenderer(ModEntityTypes.CULVERIN, new CulverinModel(SiegeMachines.id("culverin")));
        registerMachineRenderer(ModEntityTypes.TREBUCHET, new TrebuchetModel(SiegeMachines.id("trebuchet")));
        registerMachineRenderer(ModEntityTypes.CATAPULT, new CatapultModel(SiegeMachines.id("catapult")));
        registerMachineRenderer(ModEntityTypes.BALLISTA, new BallistaModel(SiegeMachines.id("ballista")));
        registerMachineRenderer(ModEntityTypes.BATTERING_RAM, new BatteringRamGeoModel(SiegeMachines.id("battering_ram")));

        // Special case for SiegeLadder
        EntityRendererRegistry.register(ModEntityTypes.SIEGE_LADDER, SiegeLadderGeoRenderer::new);

        // Projectiles
        EntityRendererRegistry.register(ModEntityTypes.CANNONBALL, ThrownItemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.GIANT_STONE, ThrownItemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.STONE, ThrownItemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.GIANT_ARROW, GiantArrowRenderer::new);

        // Seat
        EntityRendererRegistry.register(ModEntityTypes.LADDER_SEAT, SeatRenderer::new);
    }

    private static <T extends Machine & GeoAnimatable> void registerMachineRenderer(Supplier<EntityType<T>> type, GeoModel<T> model) {
        EntityRendererRegistry.register(type, ctx -> new MachineGeoRenderer<>(ctx, model));
    }
}