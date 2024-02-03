package ru.magistu.siegemachines.client.gui;

import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.client.gui.machine.MachineContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.magistu.siegemachines.client.gui.workbench.SiegeWorkbenchContainer;

public class ModMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SiegeMachines.ID);

	public static final RegistryObject<MenuType<MachineContainer>> MACHINE_CONTAINER = MENUS.register("machine", () -> IForgeMenuType.create(MachineContainer::new));
	public static final RegistryObject<MenuType<SiegeWorkbenchContainer>> SIEGE_WORKBENCH_CONTAINER = registerMenuType(SiegeWorkbenchContainer::new, "siege_workbench");

	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
		return MENUS.register(name, () -> IForgeMenuType.create(factory));
	}

	public static void register(IEventBus eventBus) {
		MENUS.register(eventBus);
	}
}
