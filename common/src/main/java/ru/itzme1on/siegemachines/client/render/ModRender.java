package ru.itzme1on.siegemachines.client.render;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import ru.itzme1on.siegemachines.client.render.entity.BallistaGeoRenderer;
import ru.itzme1on.siegemachines.client.render.entity.GiantArrowRenderer;
import ru.itzme1on.siegemachines.client.render.entity.MortarGeoRenderer;
import ru.itzme1on.siegemachines.registry.EntityRegistry;

public class ModRender {
    public static void setup(MinecraftClient client) {
        EntityRendererRegistry.register(EntityRegistry.MORTAR, MortarGeoRenderer::new);
//        EntityRendererRegistry.register(EntityRegistry.CULVERIN, CulverinGeoRenderer::new);
//        EntityRendererRegistry.register(EntityRegistry.TREBUCHET, TrebuchetGeoRenderer::new);
//        EntityRendererRegistry.register(EntityRegistry.CATAPULT, CatapultGeoRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BALLISTA, BallistaGeoRenderer::new);
//        EntityRendererRegistry.register(EntityRegistry.BATTERING_RAM, BatteringRamGeoRenderer::new);

        EntityRendererRegistry.register(EntityRegistry.CANNONBALL, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.STONE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.GIANT_ARROW, GiantArrowRenderer::new);

        setupPlatform(client);
    }

    @ExpectPlatform
    public static void setupPlatform(MinecraftClient client)
    {
        throw new AssertionError();
    }
}
