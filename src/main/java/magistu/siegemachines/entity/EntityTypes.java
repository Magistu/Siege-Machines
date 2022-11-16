package magistu.siegemachines.entity;

import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.entity.machine.*;
import magistu.siegemachines.entity.projectile.Cannonball;
import magistu.siegemachines.entity.projectile.GiantArrow;
import magistu.siegemachines.entity.projectile.GiantStone;
import magistu.siegemachines.entity.projectile.Stone;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypes
{
    public static final DeferredRegister<EntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, SiegeMachines.ID);

    public static final RegistryObject<EntityType<Cannonball>> CANNONBALL = addRegistry("cannonball", Cannonball::new, 0.5f, 0.5f, 8);
    public static final RegistryObject<EntityType<Stone>> STONE = addRegistry("stone", Stone::new, 0.6f, 0.6f, 8);
    public static final RegistryObject<EntityType<GiantStone>> GIANT_STONE = addRegistry("giant_stone", GiantStone::new, 1.1f, 1.1f, 8);
    public static final RegistryObject<EntityType<GiantArrow>> GIANT_ARROW = addRegistry("giant_arrow", GiantArrow::new, 0.5f, 0.5f, 8, 20);

    public static final RegistryObject<EntityType<Mortar>> MORTAR = addRegistry("mortar", Mortar::new, 2.0f, 1.0f, 8);
    public static final RegistryObject<EntityType<Culverin>> CULVERIN = addRegistry("culverin", Culverin::new, 2.0f, 1.0f, 8);
    public static final RegistryObject<EntityType<Trebuchet>> TREBUCHET = addRegistry("trebuchet", Trebuchet::new, 5.0f, 9.0f, 8);
    public static final RegistryObject<EntityType<Catapult>> CATAPULT = addRegistry("catapult", Catapult::new, 3.0f, 3.0f, 8);
    public static final RegistryObject<EntityType<Ballista>> BALLISTA = addRegistry("ballista", Ballista::new, 1.5f, 1.5f, 8);
    public static final RegistryObject<EntityType<BatteringRam>> BATTERING_RAM = addRegistry("battering_ram", BatteringRam::new, 4.0f, 3.0f);


    public static <T extends Entity> RegistryObject<EntityType<T>> addRegistry(String name, EntityType.IFactory<T> constructor, float sizex, float sizey)
    {
        return DEFERRED_REGISTER.register(name, () -> EntityType.Builder.of(constructor, EntityClassification.MISC).sized(sizex, sizey).build(new ResourceLocation(SiegeMachines.ID, name).toString()));
    }

    public static <T extends Entity> RegistryObject<EntityType<T>> addRegistry(String name, EntityType.IFactory<T> constructor, float sizex, float sizey, int trackingrange)
    {
        return DEFERRED_REGISTER.register(name, () -> EntityType.Builder.of(constructor, EntityClassification.MISC).clientTrackingRange(trackingrange).sized(sizex, sizey).build(new ResourceLocation(SiegeMachines.ID, name).toString()));
    }

    public static <T extends Entity> RegistryObject<EntityType<T>> addRegistry(String name, EntityType.IFactory<T> constructor, float sizex, float sizey, int trackingrange, int updateinterval)
    {
        return DEFERRED_REGISTER.register(name, () -> EntityType.Builder.of(constructor, EntityClassification.MISC).clientTrackingRange(trackingrange).updateInterval(updateinterval).sized(sizex, sizey).build(new ResourceLocation(SiegeMachines.ID, name).toString()));
    }

    public static void register(IEventBus eventBus)
    {
        DEFERRED_REGISTER.register(eventBus);
    }
}