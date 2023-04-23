package ru.itzme1on.siegemachines.forge.item;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraftforge.client.IItemRenderProperties;
import ru.itzme1on.siegemachines.entity.client.MachineItemGeoRenderer;
import ru.itzme1on.siegemachines.entity.client.model.MachineItemModel;
import ru.itzme1on.siegemachines.item.MortarItem;

import java.util.function.Consumer;

public class MortarItemForge extends MortarItem {
    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BuiltinModelItemRenderer renderer = new MachineItemGeoRenderer(new MachineItemModel<>("mortar"));

            @Override
            public BuiltinModelItemRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }
}
