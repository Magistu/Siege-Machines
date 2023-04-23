package ru.itzme1on.siegemachines.registry;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import ru.itzme1on.siegemachines.SiegeMachinesCore;

public class MenusRegistry {
    public static final DeferredRegister<ScreenHandlerType<?>> MENUS = DeferredRegister.create(SiegeMachinesCore.MOD_ID, Registry.MENU_KEY);

//    public static RegistrySupplier<ScreenHandlerType<MachineContainer>> MACHINE_CONTAINER = MENUS.register("machine", );
}
