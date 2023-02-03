package ru.itzme1on.siegemachines.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import ru.itzme1on.siegemachines.SiegeMachines;
import ru.itzme1on.siegemachines.entity.projectile.projectiles.Stone;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(SiegeMachines.MOD_ID, Registry.ENTITY_TYPE_REGISTRY);

    public static final RegistrySupplier<EntityType<Stone>> STONE = addRegistry("stone", Stone::new, 0.6f, 0.6f);

    public static final RegistryObject<EntityType<GiantArrow>> GIANT_ARROW = DEFERRED_REGISTER.register("giant_arrow", () -> EntityType.Builder.<GiantArrow>of(GiantArrow::new, MobCategory.MISC)
            .clientTrackingRange(4)
            .updateInterval(20)
            .sized(0.5f, 0.5f)
            .build(new ResourceLocation(SiegeMachines.ID, "giant_arrow").toString()));

    public static final RegistryObject<EntityType<Ballista>> BALLISTA = addRegistry("ballista", Ballista::new, 1.5f, 1.5f);

    public static <T extends Entity> RegistrySupplier<EntityType<T>> addRegistry(
            String name,
            EntityType.EntityFactory<T> factory,
            float sizeX,
            float sizeY) {
        return DEFERRED_REGISTER.register(name, () -> EntityType.Builder.of(factory, MobCategory.MISC)
                .sized(sizeX, sizeY)
                .build(new ResourceLocation(SiegeMachines.MOD_ID, name).toString()));
    }
}
