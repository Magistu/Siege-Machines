package ru.itzme1on.siegemachines;

import net.minecraft.world.phys.AABB;
import ru.itzme1on.siegemachines.registry.ItemRegistry;

public class SiegeMachines {
    public static final String MOD_ID = "siegemachines";
    public static final int RENDER_UPDATE_RANGE = 128;

    public static void init() {
        System.out.println("Hello from " + SiegeMachines.MOD_ID + " init!");

        ItemRegistry.ITEMS.register();
    }
}
