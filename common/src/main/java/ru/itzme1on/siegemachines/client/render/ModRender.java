package ru.itzme1on.siegemachines.client.render;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import ru.itzme1on.siegemachines.client.render.entity.BallistaGeoRenderer;
import ru.itzme1on.siegemachines.client.render.entity.GiantArrowRenderer;
import ru.itzme1on.siegemachines.client.render.entity.MortarGeoRenderer;
import ru.itzme1on.siegemachines.client.render.entity.CulverinGeoRenderer;
import ru.itzme1on.siegemachines.client.render.entity.TrebuchetGeoRenderer;
import ru.itzme1on.siegemachines.client.render.entity.CatapultGeoRenderer;
import ru.itzme1on.siegemachines.client.render.entity.BatteringRamGeoRenderer;
import ru.itzme1on.siegemachines.entity.EntityTypes;

public class ModRender 
{
    public static void setup(MinecraftClient client)
    {
        EntityRendererRegistry.register(EntityTypes.MORTAR, MortarGeoRenderer::new);
        EntityRendererRegistry.register(EntityTypes.CULVERIN, CulverinGeoRenderer::new);
        EntityRendererRegistry.register(EntityTypes.TREBUCHET, TrebuchetGeoRenderer::new);
        EntityRendererRegistry.register(EntityTypes.CATAPULT, CatapultGeoRenderer::new);
        EntityRendererRegistry.register(EntityTypes.BALLISTA, BallistaGeoRenderer::new);
        EntityRendererRegistry.register(EntityTypes.BATTERING_RAM, BatteringRamGeoRenderer::new);

        EntityRendererRegistry.register(EntityTypes.CANNONBALL, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityTypes.STONE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityTypes.GIANT_ARROW, GiantArrowRenderer::new);

        setupPlatform(client);
    }

    @ExpectPlatform
    public static void setupPlatform(MinecraftClient client)
    {
        throw new AssertionError();
    }
}
