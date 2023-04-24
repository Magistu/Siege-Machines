package ru.itzme1on.siegemachines;

import ru.itzme1on.siegemachines.client.render.entity.EntityAttributes;
import ru.itzme1on.siegemachines.events.ClientEvents;
import ru.itzme1on.siegemachines.events.CommonEvents;
import ru.itzme1on.siegemachines.network.PacketHandler;
import ru.itzme1on.siegemachines.entity.EntityTypes;
import ru.itzme1on.siegemachines.item.ModItems;
import ru.itzme1on.siegemachines.audio.ModSounds;

import java.util.UUID;

public class SiegeMachines {

    public static final String MOD_ID = "siegemachines";
    public static final int RENDER_UPDATE_RANGE = 128;
    public static final int RENDER_UPDATE_TIME = 20;
    public static final int RENDER_UPDATE_RANGE_SQR = RENDER_UPDATE_RANGE * RENDER_UPDATE_RANGE;

    public static final UUID CHAT_UUID = new UUID(100L, 100L);

    public static void init() {
        System.out.println("Hello from " + SiegeMachines.MOD_ID + " init!");
        
        ModItems.init();
        EntityTypes.init();
        ModSounds.init();
        PacketHandler.init();
        EntityAttributes.init();
        ClientEvents.init();
        CommonEvents.init();
    }
}
