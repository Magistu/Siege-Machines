package ru.itzme1on.siegemachines.item;

import ru.itzme1on.siegemachines.registry.ItemRegistry;

public class BatteringRamItem extends MachineItem {
    public BatteringRamItem() {
        super(new Settings()
                .group(ItemRegistry.SIEGEMACHINES_GROUP),
                "battering_ram",
                "BATTERING_RAM"
        );
    }
}
