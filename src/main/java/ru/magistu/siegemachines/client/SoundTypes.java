package ru.magistu.siegemachines.client;

import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundTypes
{
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
			SiegeMachines.ID);

	public static RegistryObject<SoundEvent> TREBUCHET_SHOOTING = SOUNDS.register("trebuchet_shooting",
			() -> new SoundEvent(new ResourceLocation(SiegeMachines.ID, "trebuchet_shooting")));
	public static RegistryObject<SoundEvent> TREBUCHET_RELOADING = SOUNDS.register("trebuchet_reloading",
			() -> new SoundEvent(new ResourceLocation(SiegeMachines.ID, "trebuchet_reloading")));
	public static RegistryObject<SoundEvent> CATAPULT_SHOOTING = SOUNDS.register("catapult_shooting",
			() -> new SoundEvent(new ResourceLocation(SiegeMachines.ID, "catapult_shooting")));
	public static RegistryObject<SoundEvent> CATAPULT_RELOADING = SOUNDS.register("catapult_reloading",
			() -> new SoundEvent(new ResourceLocation(SiegeMachines.ID, "catapult_reloading")));
	public static RegistryObject<SoundEvent> BALLISTA_SHOOTING = SOUNDS.register("ballista_shooting",
			() -> new SoundEvent(new ResourceLocation(SiegeMachines.ID, "ballista_shooting")));
	public static RegistryObject<SoundEvent> BALLISTA_RELOADING = SOUNDS.register("ballista_reloading",
			() -> new SoundEvent(new ResourceLocation(SiegeMachines.ID, "ballista_reloading")));
	public static RegistryObject<SoundEvent> MORTAR_SHOOTING = SOUNDS.register("mortar_shooting",
			() -> new SoundEvent(new ResourceLocation(SiegeMachines.ID, "mortar_shooting")));
	public static RegistryObject<SoundEvent> FUSE = SOUNDS.register("fuse",
			() -> new SoundEvent(new ResourceLocation(SiegeMachines.ID, "fuse")));
    public static RegistryObject<SoundEvent> CANNON_WHEELS = SOUNDS.register("cannon_wheels",
			() -> new SoundEvent(new ResourceLocation(SiegeMachines.ID, "cannon_wheels")));
	public static RegistryObject<SoundEvent> RAM_WHEELS = SOUNDS.register("ram_wheels",
			() -> new SoundEvent(new ResourceLocation(SiegeMachines.ID, "ram_wheels")));
	public static RegistryObject<SoundEvent> RAM_HITTING = SOUNDS.register("ram_hitting",
			() -> new SoundEvent(new ResourceLocation(SiegeMachines.ID, "ram_hitting")));

	public static void register(IEventBus eventBus)
    {
        SOUNDS.register(eventBus);
    }
}
