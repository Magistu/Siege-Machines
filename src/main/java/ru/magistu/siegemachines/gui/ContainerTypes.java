package ru.magistu.siegemachines.gui;

import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerTypes
{
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, SiegeMachines.MOD_ID);

    public static final RegistryObject<MenuType<MachineContainer>> MACHINE_CONTAINER = CONTAINER_TYPES.register("machine", () -> IForgeMenuType.create(MachineContainer::new));
    public static final RegistryObject<MenuType<SiegeWorkbenchContainer>> SIEGE_WORKBENCH_CONTAINER = CONTAINER_TYPES.register("siege_workbench", () -> IForgeMenuType.create(SiegeWorkbenchContainer::new));

    public static void register(IEventBus eventBus)
    {
        CONTAINER_TYPES.register(eventBus);
    }
}
