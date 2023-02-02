package ru.itzme1on.fabric;

import ru.itzme1on.siegemachines.SiegeMachines;
import net.fabricmc.api.ModInitializer;

public class ExampleModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SiegeMachines.init();
    }
}
