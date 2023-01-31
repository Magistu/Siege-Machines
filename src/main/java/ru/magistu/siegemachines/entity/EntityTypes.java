package ru.magistu.siegemachines.entity;

import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.entity.machine.*;
import ru.magistu.siegemachines.entity.projectile.Cannonball;
import ru.magistu.siegemachines.entity.projectile.GiantArrow;
import ru.magistu.siegemachines.entity.projectile.Stone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypes
{
    public static final DeferredRegister<EntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SiegeMachines.ID);

    public static final RegistryObject<EntityType<Cannonball>> CANNONBALL = addRegistry("cannonball", Cannonball::new, 0.5f, 0.5f);
    public static final RegistryObject<EntityType<Stone>> STONE = addRegistry("stone", Stone::new, 0.6f, 0.6f);
    public static final RegistryObject<EntityType<GiantArrow>> GIANT_ARROW = DEFERRED_REGISTER.register("giant_arrow", () -> EntityType.Builder.<GiantArrow>of(GiantArrow::new, MobCategory.MISC).clientTrackingRange(4).updateInterval(20).sized(0.5f, 0.5f).build(new ResourceLocation(SiegeMachines.ID, "giant_arrow").toString()));

    public static final RegistryObject<EntityType<Mortar>> MORTAR = addRegistry("mortar", Mortar::new, 2.0f, 1.0f);
    public static final RegistryObject<EntityType<Culverin>> CULVERIN = addRegistry("culverin", Culverin::new, 2.0f, 1.0f);
    public static final RegistryObject<EntityType<Trebuchet>> TREBUCHET = addRegistry("trebuchet", Trebuchet::new, 5.0f, 9.0f);
    public static final RegistryObject<EntityType<Catapult>> CATAPULT = addRegistry("catapult", Catapult::new, 3.0f, 3.0f);
    public static final RegistryObject<EntityType<Ballista>> BALLISTA = addRegistry("ballista", Ballista::new, 1.5f, 1.5f);
    public static final RegistryObject<EntityType<BatteringRam>> BATTERING_RAM = addRegistry("battering_ram", BatteringRam::new, 4.0f, 3.0f);

    public static <T extends Entity> RegistryObject<EntityType<T>> addRegistry(String name, EntityType.EntityFactory<T> constructor, float sizex, float sizey)
    {
        return DEFERRED_REGISTER.register(name, () -> EntityType.Builder.of(constructor, MobCategory.MISC).sized(sizex, sizey).build(new ResourceLocation(SiegeMachines.ID, name).toString()));
    }

    public static void register(IEventBus eventBus)
    {
        DEFERRED_REGISTER.register(eventBus);
    }
}