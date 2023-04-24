package ru.itzme1on.siegemachines.client.gui;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import ru.itzme1on.siegemachines.SiegeMachines;

public class ModMenus {
    public static final DeferredRegister<ScreenHandlerType<?>> MENUS = DeferredRegister.create(SiegeMachines.MOD_ID, Registry.MENU_KEY);

//    public static RegistrySupplier<ScreenHandlerType<MachineContainer>> MACHINE_CONTAINER = MENUS.register("machine", );
}
