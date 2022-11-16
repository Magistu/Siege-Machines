package magistu.siegemachines.gui;

import magistu.siegemachines.SiegeMachines;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypes
{
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, SiegeMachines.ID);

    public static final RegistryObject<ContainerType<MachineContainer>> MACHINE_CONTAINER = CONTAINER_TYPES.register("machine", () -> IForgeContainerType.create(MachineContainer::new));
    public static final RegistryObject<ContainerType<SiegeWorkbenchContainer>> SIEGE_WORKBENCH_CONTAINER = CONTAINER_TYPES.register("siege_workbench", () -> IForgeContainerType.create(SiegeWorkbenchContainer::new));

    public static void register(IEventBus eventBus)
    {
        CONTAINER_TYPES.register(eventBus);
    }
}
