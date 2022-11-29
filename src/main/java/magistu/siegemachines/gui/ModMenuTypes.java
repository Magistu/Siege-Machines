package magistu.siegemachines.gui;

import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.gui.machine.MachineContainer;
import magistu.siegemachines.gui.siegeworkbench.SiegeWorkbenchMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, SiegeMachines.ID);

    public static final RegistryObject<MenuType<MachineContainer>> MACHINE_CONTAINER =
            MENUS.register("machine", () -> IForgeMenuType.create(MachineContainer::new));
    public static final RegistryObject<MenuType<SiegeWorkbenchMenu>> SIEGE_WORKBENCH_CONTAINER =
            registerMenuType(SiegeWorkbenchMenu::new, "siege_workbench");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
