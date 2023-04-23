package ru.itzme1on.siegemachines.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.RegistrySupplier;
import org.apache.commons.lang3.NotImplementedException;
import ru.itzme1on.siegemachines.item.*;

public class ItemRegistryHelper {
    @ExpectPlatform
    public static RegistrySupplier<BallistaItem> registerBallistaMachineItem() {
        throw new NotImplementedException("Pizdec balliste");
    }

    @ExpectPlatform
    public static RegistrySupplier<BatteringRamItem> registerBatteringRamMachineItem() {
        throw new NotImplementedException("Pizdec battering ramu");
    }

    @ExpectPlatform
    public static RegistrySupplier<CatapultItem> registerCatapultMachineItem() {
        throw new NotImplementedException("Pizdec catapulte");
    }

    @ExpectPlatform
    public static RegistrySupplier<CulverinItem> registerCulverinMachineItem() {
        throw new NotImplementedException("Pizdec culverine");
    }

    @ExpectPlatform
    public static RegistrySupplier<MortarItem> registerMortarMachineItem() {
        throw new NotImplementedException("Pizdec mortire");
    }
    @ExpectPlatform
    public static RegistrySupplier<TrebuchetItem> registerTrebuchetMachineItem() {
        throw new NotImplementedException("Pizdec trebuchetu");
    }
}
