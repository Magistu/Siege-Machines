package ru.magistu.siegemachines.gui;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(SiegeMachines.ID, Registries.MENU);

	public static void register() {
		MENUS.register();
	}
}
