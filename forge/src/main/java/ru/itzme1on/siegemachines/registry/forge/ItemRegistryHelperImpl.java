package ru.itzme1on.siegemachines.registry.forge;

import dev.architectury.registry.registries.RegistrySupplier;
import ru.itzme1on.siegemachines.item.MortarItem;
import ru.itzme1on.siegemachines.registry.ItemRegistry;

public class ItemRegistryHelperImpl {
    public static RegistrySupplier<MortarItem> registerMortarMachineItem() {
        return ItemRegistry.ITEMS.register("mortar", MortarItem::new);
    }
}
