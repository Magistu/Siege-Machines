package ru.magistu.siegemachines.entity;

import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.entity.machine.*;
import ru.magistu.siegemachines.entity.projectile.Cannonball;
import ru.magistu.siegemachines.entity.projectile.GiantArrow;
import ru.magistu.siegemachines.entity.projectile.GiantStone;
import ru.magistu.siegemachines.entity.projectile.Stone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SiegeMachines.ID);

    public static final RegistryObject<EntityType<Cannonball>> CANNONBALL = addRegistry("cannonball", Cannonball::new, 0.5f, 0.5f, 10);
    public static final RegistryObject<EntityType<Stone>> STONE = addRegistry("stone", Stone::new, 0.6f, 0.6f, 10);
    public static final RegistryObject<EntityType<GiantStone>> GIANT_STONE = addRegistry("giant_stone", GiantStone::new, 1.1f, 1.1f, 10);
    public static final RegistryObject<EntityType<GiantArrow>> GIANT_ARROW = DEFERRED_REGISTER.register("giant_arrow", () -> EntityType.Builder.<GiantArrow>of(GiantArrow::new, MobCategory.MISC).clientTrackingRange(10).updateInterval(20).sized(0.5f, 0.5f).build(""));

    public static final RegistryObject<EntityType<Cannon>> MORTAR = addRegistry("mortar", (entitytype, level) -> new Cannon(entitytype, level, MachineType.MORTAR), 2.0f, 1.0f, 10);
    public static final RegistryObject<EntityType<Cannon>> CULVERIN = addRegistry("culverin", (entitytype, level) -> new Cannon(entitytype, level, MachineType.CULVERIN), 2.5f, 1.8f, 10);
    public static final RegistryObject<EntityType<Catapult>> TREBUCHET = addRegistry("trebuchet", (entitytype, level) -> new Catapult(entitytype, level, MachineType.TREBUCHET), 5.0f, 9.0f, 10);
    public static final RegistryObject<EntityType<Catapult>> CATAPULT = addRegistry("catapult", (entitytype, level) -> new Catapult(entitytype, level, MachineType.CATAPULT), 3.0f, 3.0f, 10);
    public static final RegistryObject<EntityType<Catapult>> BALLISTA = addRegistry("ballista", (entitytype, level) -> new Catapult(entitytype, level, MachineType.BALLISTA), 1.5f, 1.5f, 10);
    public static final RegistryObject<EntityType<BatteringRam>> BATTERING_RAM = addRegistry("battering_ram", BatteringRam::new, 4.0f, 3.0f, 10);
    public static final RegistryObject<EntityType<SiegeLadder>> SIEGE_LADDER = addRegistry("siege_ladder", SiegeLadder::new, 3.0f, 3.0f, 10);

    public static final RegistryObject<EntityType<Seat>> SEAT = addRegistry("seat", Seat::new, 0.0f, 0.0f);

    public static <T extends Entity> RegistryObject<EntityType<T>> addRegistry(String name, EntityType.EntityFactory<T> constructor, float sizex, float sizey) {
        return addRegistry(name, constructor, sizex, sizey, 1);
    }
    
    public static <T extends Entity> RegistryObject<EntityType<T>> addRegistry(String name, EntityType.EntityFactory<T> constructor, float sizex, float sizey, int trackingrange) {
        return DEFERRED_REGISTER.register(name, () -> EntityType.Builder.of(constructor, MobCategory.MISC).clientTrackingRange(trackingrange).sized(sizex, sizey).build(new ResourceLocation(SiegeMachines.ID, name).toString()));
    }

    public static void register(IEventBus eventBus) {
        DEFERRED_REGISTER.register(eventBus);
    }
}