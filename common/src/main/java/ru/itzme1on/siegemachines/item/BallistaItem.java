package ru.itzme1on.siegemachines.item;

import ru.itzme1on.siegemachines.registry.ItemRegistry;

public class BallistaItem extends MachineItem {
    public BallistaItem() {
        super(new Settings()
                .group(ItemRegistry.SIEGEMACHINES_GROUP),
                "ballista",
                "BALLISTA"
        );
    }
}
