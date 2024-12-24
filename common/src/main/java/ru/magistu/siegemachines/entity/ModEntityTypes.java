package ru.magistu.siegemachines.entity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.entity.machine.*;
import ru.magistu.siegemachines.item.Cannonball;
import ru.magistu.siegemachines.item.GiantArrow;
import ru.magistu.siegemachines.item.GiantStone;
import ru.magistu.siegemachines.item.Stone;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntityTypes
{
    public static final DeferredRegister<EntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(SiegeMachines.ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<Cannonball>> CANNONBALL = addRegistry("cannonball", Cannonball::new, 0.5f, 0.5f);
    public static final RegistrySupplier<EntityType<Stone>> STONE = addRegistry("stone", Stone::new, 0.6f, 0.6f);
    public static final RegistrySupplier<EntityType<GiantStone>> GIANT_STONE = addRegistry("giant_stone", GiantStone::new, 1.1f, 1.1f);
    public static final RegistrySupplier<EntityType<GiantArrow>> GIANT_ARROW = DEFERRED_REGISTER.register("giant_arrow", () -> EntityType.Builder.<GiantArrow>of(GiantArrow::new, MobCategory.MISC).clientTrackingRange(4).updateInterval(20).sized(0.5f, 0.5f).build(""));

    public static final RegistrySupplier<EntityType<Mortar>> MORTAR = addRegistry("mortar", Mortar::new, 2.0f, 1.0f, 10);
    public static final RegistrySupplier<EntityType<Culverin>> CULVERIN = addRegistry("culverin", Culverin::new, 2.5f, 1.8f, 10);
    public static final RegistrySupplier<EntityType<Trebuchet>> TREBUCHET = addRegistry("trebuchet", Trebuchet::new, 5.0f, 9.0f, 10);
    public static final RegistrySupplier<EntityType<Catapult>> CATAPULT = addRegistry("catapult", Catapult::new, 3.0f, 3.0f, 10);
    public static final RegistrySupplier<EntityType<Ballista>> BALLISTA = addRegistry("ballista", Ballista::new, 1.5f, 1.5f, 10);
    public static final RegistrySupplier<EntityType<BatteringRam>> BATTERING_RAM = addRegistry("battering_ram", BatteringRam::new, 4.0f, 3.0f, 10);
    public static final RegistrySupplier<EntityType<SiegeLadder>> SIEGE_LADDER = addRegistry("siege_ladder", SiegeLadder::new, 3.0f, 3.0f, 10);
    
    public static final RegistrySupplier<EntityType<Seat>> SEAT = addRegistry("seat", Seat::new, 0.0f, 0.0f);

    public static <T extends Entity> RegistrySupplier<EntityType<T>> addRegistry(String name, EntityType.EntityFactory<T> constructor, float sizex, float sizey)
    {
        return addRegistry(name, constructor, sizex, sizey, 1);
    }
    
    public static <T extends Entity> RegistrySupplier<EntityType<T>> addRegistry(String name, EntityType.EntityFactory<T> constructor, float sizex, float sizey, int trackingrange)
    {
        return DEFERRED_REGISTER.register(name, () -> EntityType.Builder.of(constructor, MobCategory.MISC).clientTrackingRange(trackingrange).sized(sizex, sizey).build(""));
    }

    public static void register() {
        DEFERRED_REGISTER.register();
    }
}