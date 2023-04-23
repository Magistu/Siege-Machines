package ru.itzme1on.fabric;

import net.fabricmc.api.ModInitializer;
import ru.itzme1on.siegemachines.SiegeMachinesCore;

public class SiegeMachinesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SiegeMachinesCore.init();
    }
}
