package ru.itzme1on.siegemachines.item;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import org.apache.commons.lang3.NotImplementedException;
import ru.itzme1on.siegemachines.client.render.Renderers;
import ru.itzme1on.siegemachines.entity.machine.Machine;
import ru.itzme1on.siegemachines.entity.machine.MachineType;

import java.util.function.Supplier;

public class ItemRegistryHelper {
    @ExpectPlatform
    public static <T extends Machine> RegistrySupplier<MachineItem<T>> registerMachineItem(String name, Item.Settings settings, Supplier<EntityType<T>> entityType, Supplier<MachineType> machineType, Renderers.ItemEnum modelkey) {
        throw new NotImplementedException("Pizdec machine");
    }
}
