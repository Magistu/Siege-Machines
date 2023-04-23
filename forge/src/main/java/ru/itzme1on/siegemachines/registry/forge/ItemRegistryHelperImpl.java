package ru.itzme1on.siegemachines.registry.forge;

import dev.architectury.platform.Platform;
import net.minecraft.item.Item;
import ru.itzme1on.siegemachines.entity.machine.MachineType;
import ru.itzme1on.siegemachines.item.*;
import ru.itzme1on.siegemachines.registry.ItemRegistry;

import java.util.function.Supplier;

public class ItemRegistryHelperImpl {
    public static RegistrySupplier<MachineItem<T>> registerMachineItem(String name, Item.Settings settings, Supplier<EntityType<T>> entityType, Supplier<MachineType> machineType)
    {
        return ItemRegistry.ITEMS.register(name, Platform.isForge() ? () -> new MachineItemForge<T>(settings, entityType, machineType) : () -> new MachineItem<>(settings, entityType, machineType));
    }
}
