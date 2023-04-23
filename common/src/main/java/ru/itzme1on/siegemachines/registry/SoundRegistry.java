package ru.itzme1on.siegemachines.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.itzme1on.siegemachines.SiegeMachines;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(SiegeMachines.MOD_ID, Registry.SOUND_EVENT_KEY);

    public static final RegistrySupplier<SoundEvent> BALLISTA_SHOOTING = registerSound("ballista_shooting");
    public static final RegistrySupplier<SoundEvent> BALLISTA_RELOADING = registerSound("ballista_reloading");
    public static final RegistrySupplier<SoundEvent> CATAPULT_SHOOTING = registerSound("catapult_shooting");
    public static final RegistrySupplier<SoundEvent> CATAPULT_RELOADING = registerSound("catapult_reloading");
    public static final RegistrySupplier<SoundEvent> TREBUCHET_SHOOTING = registerSound("trebuchet_shooting");
    public static final RegistrySupplier<SoundEvent> TREBUCHET_RELOADING = registerSound("trebuchet_reloading");

    public static final RegistrySupplier<SoundEvent> MORTAR_SHOOTING = registerSound("mortar_shooting");
    public static final RegistrySupplier<SoundEvent> FUSE = registerSound("fuse");
    public static final RegistrySupplier<SoundEvent> CANNON_WHEELS = registerSound("cannon_wheels");
    public static final RegistrySupplier<SoundEvent> RAM_WHEELS = registerSound("ram_wheels");
    public static final RegistrySupplier<SoundEvent> RAM_HITTING = registerSound("ram_hitting");

    private static RegistrySupplier<SoundEvent> registerSound(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new Identifier(SiegeMachines.MOD_ID, name.replaceAll("_", "."))));
    }

    public static void init() {
        SOUNDS.register();
    }
}
