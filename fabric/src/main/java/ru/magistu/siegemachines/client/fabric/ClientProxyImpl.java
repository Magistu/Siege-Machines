package ru.magistu.siegemachines.client.fabric;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.world.item.Item;
import ru.magistu.siegemachines.item.MachineItem;
import ru.magistu.siegemachines.item.ModItems;
import java.util.function.Supplier;

public class ClientProxyImpl {

    public static void setupPlatform() {
        for (Supplier<Item> supplier : ModItems.ITEMS) {
            Item item = supplier.get();
            if (item instanceof MachineItem<?> machineItem) {
                BuiltinItemRendererRegistry.INSTANCE.register(supplier.get(), machineItem.getRenderer()::renderByItem);
            }
        }
    }
}
