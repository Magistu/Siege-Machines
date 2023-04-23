package ru.itzme1on.fabric;

import net.fabricmc.api.ModInitializer;
import ru.itzme1on.siegemachines.SiegeMachines;

public class SiegeMachinesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SiegeMachines.init();
    }
}
