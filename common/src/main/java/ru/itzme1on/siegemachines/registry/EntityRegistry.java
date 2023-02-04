package ru.itzme1on.siegemachines.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import ru.itzme1on.siegemachines.SiegeMachines;
import ru.itzme1on.siegemachines.entity.projectile.projectiles.GiantArrow;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(SiegeMachines.MOD_ID, Registry.ENTITY_TYPE_KEY);

//    public static final RegistrySupplier<EntityType<GiantArrow>> GIANT_ARROW = ENTITIES.register("giant_arrow", );

    public static void init() {
        ENTITIES.register();
    }
}
