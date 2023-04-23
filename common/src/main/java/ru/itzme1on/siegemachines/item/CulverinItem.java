package ru.itzme1on.siegemachines.item;

import ru.itzme1on.siegemachines.registry.ItemRegistry;

public class CulverinItem extends MachineItem {
    public CulverinItem() {
        super(new Settings()
                .group(ItemRegistry.SIEGEMACHINES_GROUP),
                "culverin",
                "CULVERIN"
        );
    }
}
