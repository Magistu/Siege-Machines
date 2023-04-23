package ru.itzme1on.siegemachines;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import ru.itzme1on.siegemachines.registry.EntityRegistry;
import ru.itzme1on.siegemachines.registry.ItemRegistry;
import ru.itzme1on.siegemachines.registry.SoundRegistry;

import java.util.UUID;

public class SiegeMachines {

    public static final String MOD_ID = "siegemachines";
    public static final int RENDER_UPDATE_RANGE = 128;
    public static final int RENDER_UPDATE_TIME = 20;
    public static final int RENDER_UPDATE_RANGE_SQR = RENDER_UPDATE_RANGE * RENDER_UPDATE_RANGE;

    public static final UUID CHAT_UUID = new UUID(100L, 100L);

    public static void init() {
        System.out.println("Hello from " + SiegeMachines.MOD_ID + " init!");
        
        ItemRegistry.init();
        EntityRegistry.init();
        SoundRegistry.init();

        EnvExecutor.runInEnv(Env.CLIENT, () -> SiegeMachinesClient::init);
    }
}
