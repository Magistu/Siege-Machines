package ru.itzme1on.siegemachines.registry;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import ru.itzme1on.siegemachines.SiegeMachines;
import ru.itzme1on.siegemachines.entity.client.renderer.GiantArrowRenderer;
import ru.itzme1on.siegemachines.entity.machine.machines.Ballista;
import ru.itzme1on.siegemachines.entity.projectile.projectiles.GiantArrow;
import ru.itzme1on.siegemachines.entity.projectile.projectiles.Stone;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(SiegeMachines.MOD_ID, Registry.ENTITY_TYPE_KEY);

    public static final RegistrySupplier<EntityType<GiantArrow>> GIANT_ARROW = ENTITIES.register("giant_arrow", GiantArrow.TYPE);

    public static final RegistrySupplier<EntityType<Ballista>> BALLISTA = ENTITIES.register("ballista", Ballista.TYPE);

    public static final RegistrySupplier<EntityType<Stone>> STONE = ENTITIES.register("stone", Stone.TYPE);

    public static void init() {
        ENTITIES.register();
    }
}
