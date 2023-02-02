package ru.itzme1on.siegemachines;

import ru.itzme1on.siegemachines.item.ModItems;

public class SiegeMachines {
    public static final String MOD_ID = "siegemachines";

    public static void init() {
        System.out.println("Hello from " + SiegeMachines.MOD_ID + " init!");

        ModItems.ITEMS.register();
    }
}
