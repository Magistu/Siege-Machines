package ru.itzme1on.siegemachines.forge.item;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderProperties;
import ru.itzme1on.siegemachines.client.render.Renderers;
import ru.itzme1on.siegemachines.entity.machine.Machine;
import ru.itzme1on.siegemachines.entity.machine.MachineType;
import ru.itzme1on.siegemachines.item.MachineItem;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MachineItemForge <T extends Machine> extends MachineItem<T> {

    public MachineItemForge(Item.Settings settings, Supplier<EntityType<T>> entityType, Supplier<MachineType> machineType, Renderers.ItemEnum modelkey)
    {
        super(settings, entityType, machineType, modelkey);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BuiltinModelItemRenderer getItemStackRenderer() {
                return getRenderer();
            }
        });
    }
}
