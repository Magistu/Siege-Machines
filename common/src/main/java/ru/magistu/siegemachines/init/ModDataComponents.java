package ru.magistu.siegemachines.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import ru.magistu.siegemachines.SiegeMachines;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DEFERRED_REGISTER = DeferredRegister.create(SiegeMachines.ID, Registries.DATA_COMPONENT_TYPE);

    //public static final RegistrySupplier<DataComponentType<>>

}
