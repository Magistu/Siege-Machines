package ru.itzme1on.siegemachines.item;

import ru.itzme1on.siegemachines.registry.ItemRegistry;

public class TrebuchetItem extends MachineItem {
    public TrebuchetItem() {
        super(new Settings()
                .group(ItemRegistry.SIEGEMACHINES_GROUP),
                "trebuchet",
                "TREBUCHET"
        );
    }
}
