package ru.itzme1on.siegemachines;

import dev.architectury.platform.Platform;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.utils.Env;
import ru.itzme1on.siegemachines.entity.client.renderer.BallistaGeoRenderer;
import ru.itzme1on.siegemachines.entity.client.renderer.GiantArrowRenderer;
import ru.itzme1on.siegemachines.entity.machine.machines.Ballista;
import ru.itzme1on.siegemachines.entity.projectile.projectiles.GiantArrow;

public class SiegeMachinesClient {
    public static void init() {
        if (Platform.getEnvironment() == Env.CLIENT) {
            EntityRendererRegistry.register(GiantArrow.TYPE, GiantArrowRenderer::new);
            EntityModelLayerRegistry.register(Ballista.TYPE, BallistaGeoRenderer::new);
        }
    }
}
