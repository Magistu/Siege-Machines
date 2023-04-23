package ru.itzme1on.siegemachines.item;

import ru.itzme1on.siegemachines.registry.ItemRegistry;

public class CatapultItem extends MachineItem {
    public CatapultItem() {
        super(new Settings()
                .group(ItemRegistry.SIEGEMACHINES_GROUP),
                "catapult",
                "CATAPULT"
        );
    }
}
