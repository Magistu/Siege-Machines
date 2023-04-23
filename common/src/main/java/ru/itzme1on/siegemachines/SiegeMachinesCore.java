package ru.itzme1on.siegemachines;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import ru.itzme1on.siegemachines.registry.EntityRegistry;
import ru.itzme1on.siegemachines.registry.ItemRegistry;
import ru.itzme1on.siegemachines.registry.SoundRegistry;

public class SiegeMachinesCore {
    public static final String MOD_ID = "siegemachines";
    public static final int RENDER_UPDATE_RANGE = 128;
    public static final int RENDER_UPDATE_TIME = 20;

    public static void init() {
        System.out.println("Hello from " + SiegeMachinesCore.MOD_ID + " init!");
        
        ItemRegistry.init();
        EntityRegistry.init();
        SoundRegistry.init();

        EnvExecutor.runInEnv(Env.CLIENT, () -> SiegeMachinesClient::init);
    }
}
