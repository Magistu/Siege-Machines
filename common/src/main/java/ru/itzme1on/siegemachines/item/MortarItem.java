package ru.itzme1on.siegemachines.item;

import net.minecraft.item.Item;
import ru.itzme1on.siegemachines.registry.ItemRegistry;

public class MortarItem extends MachineItem {
    public MortarItem() {
        super(new Item.Settings()
                .group(ItemRegistry.SIEGEMACHINES_GROUP),
                "mortar",
                "MORTAR"
        );
    }
}
