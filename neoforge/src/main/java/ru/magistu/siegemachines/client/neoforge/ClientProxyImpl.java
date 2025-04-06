package ru.magistu.siegemachines.client.neoforge;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import ru.magistu.siegemachines.item.MachineItem;
import ru.magistu.siegemachines.item.ModItems;

public class ClientProxyImpl {

    public static void setupPlatform() {}

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ClientProxyImpl::extensions);
    }

    public static void extensions(RegisterClientExtensionsEvent event) {
        for (RegistrySupplier<Item> supplier : ModItems.ITEMS) {
            Item item = supplier.get();
            if (item instanceof MachineItem<?> machineItem) {
                event.registerItem(new IClientItemExtensions() {
                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        return machineItem.getRenderer();
                    }
                }, machineItem);
            }
        }
    }
}
