package ru.itzme1on.siegemachines.item.forge;

import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import ru.itzme1on.siegemachines.client.render.Renderers;
import ru.itzme1on.siegemachines.entity.machine.Machine;
import ru.itzme1on.siegemachines.entity.machine.MachineType;
import ru.itzme1on.siegemachines.forge.item.MachineItemForge;
import ru.itzme1on.siegemachines.item.*;
import ru.itzme1on.siegemachines.item.ModItems;
import net.minecraft.entity.EntityType;
import java.util.function.Supplier;

public class ItemRegistryHelperImpl {
    public static <T extends Machine> RegistrySupplier<MachineItem<T>> registerMachineItem(String name, Item.Settings settings, Supplier<EntityType<T>> entityType, Supplier<MachineType> machineType, Renderers.ItemEnum modelkey)
    {
        return ModItems.ITEMS.register(name, Platform.isForge() ? () -> new MachineItemForge<>(settings, entityType, machineType, modelkey) : () -> new MachineItem<>(settings, entityType, machineType, modelkey));
    }
}
