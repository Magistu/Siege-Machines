package ru.magistu.siegemachines.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.item.ModItems;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> DEFERRED_REGISTER = DeferredRegister.create(SiegeMachines.ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> GROUP_SM = DEFERRED_REGISTER.register(SiegeMachines.id("machines"),() ->CreativeModeTab.builder(null,-1)
            .title(Component.translatable(SiegeMachines.ID + ".medieval_siege_machines")).
            icon(() -> new ItemStack(ModItems.MORTAR.get()))
            .displayItems((parameters, output) -> BuiltInRegistries.ITEM.stream()
                    .filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(SiegeMachines.ID))
                    .forEach(output::accept)
            )
            .build());

    public static void register() {
        DEFERRED_REGISTER.register();
    }

}
