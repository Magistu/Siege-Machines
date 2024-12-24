package ru.magistu.siegemachines;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class ModSoundTypes
{
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(SiegeMachines.ID, Registries.SOUND_EVENT);

	public static RegistrySupplier<SoundEvent> TREBUCHET_SHOOTING = SOUNDS.register("trebuchet_shooting",
			() -> SoundEvent.createVariableRangeEvent(SiegeMachines.id("trebuchet_shooting")));
	public static RegistrySupplier<SoundEvent> TREBUCHET_RELOADING = SOUNDS.register("trebuchet_reloading",
			() -> SoundEvent.createVariableRangeEvent(SiegeMachines.id("trebuchet_reloading")));
	public static RegistrySupplier<SoundEvent> CATAPULT_SHOOTING = SOUNDS.register("catapult_shooting",
			() -> SoundEvent.createVariableRangeEvent(SiegeMachines.id("catapult_shooting")));
	public static RegistrySupplier<SoundEvent> CATAPULT_RELOADING = SOUNDS.register("catapult_reloading",
			() -> SoundEvent.createVariableRangeEvent(SiegeMachines.id("catapult_reloading")));
	public static RegistrySupplier<SoundEvent> BALLISTA_SHOOTING = SOUNDS.register("ballista_shooting",
			() -> SoundEvent.createVariableRangeEvent(SiegeMachines.id("ballista_shooting")));
	public static RegistrySupplier<SoundEvent> BALLISTA_RELOADING = SOUNDS.register("ballista_reloading",
			() -> SoundEvent.createVariableRangeEvent(SiegeMachines.id("ballista_reloading")));
	public static RegistrySupplier<SoundEvent> MORTAR_SHOOTING = SOUNDS.register("mortar_shooting",
			() -> SoundEvent.createVariableRangeEvent(SiegeMachines.id("mortar_shooting")));
	public static RegistrySupplier<SoundEvent> FUSE = SOUNDS.register("fuse",
			() -> SoundEvent.createVariableRangeEvent(SiegeMachines.id("fuse")));
    public static RegistrySupplier<SoundEvent> CANNON_WHEELS = SOUNDS.register("cannon_wheels",
			() -> SoundEvent.createVariableRangeEvent(SiegeMachines.id("cannon_wheels")));
	public static RegistrySupplier<SoundEvent> RAM_WHEELS = SOUNDS.register("ram_wheels",
			() -> SoundEvent.createVariableRangeEvent(SiegeMachines.id("ram_wheels")));
	public static RegistrySupplier<SoundEvent> RAM_HITTING = SOUNDS.register("ram_hitting",
			() -> SoundEvent.createVariableRangeEvent(SiegeMachines.id("ram_hitting")));

	public static void register()
    {
        SOUNDS.register();
    }
}
