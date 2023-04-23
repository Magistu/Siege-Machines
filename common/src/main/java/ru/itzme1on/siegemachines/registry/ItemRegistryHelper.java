package ru.itzme1on.siegemachines.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.RegistrySupplier;
import org.apache.commons.lang3.NotImplementedException;
import ru.itzme1on.siegemachines.item.MortarItem;

public class ItemRegistryHelper {
    @ExpectPlatform
    public static RegistrySupplier<MortarItem> registerMortarMachineItem() {
        throw new NotImplementedException("Pizdec");
    }
}
