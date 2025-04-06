package ru.magistu.siegemachines.gui;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import ru.magistu.siegemachines.SiegeMachines;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(SiegeMachines.ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<SiegeWorkbenchMenu>> SIEGE_WORKBENCH_MENU = MENUS.register("siege_workbench", () -> new MenuType<>(SiegeWorkbenchMenu::new, FeatureFlags.DEFAULT_FLAGS));;

    public static void register() {
        MENUS.register();
    }
}
