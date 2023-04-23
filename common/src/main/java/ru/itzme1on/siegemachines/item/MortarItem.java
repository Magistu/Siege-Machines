package ru.itzme1on.siegemachines.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class MortarItem extends MachineItem {
    public MortarItem() {
        super(new Item.Settings()
                .group(ItemGroup.MISC),
                "mortar",
                "MORTAR"
        );
    }
}
