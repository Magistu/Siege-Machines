package ru.itzme1on.siegemachines.events;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import ru.itzme1on.siegemachines.client.render.ModRender;

@Environment(EnvType.CLIENT)
public class ClientEvents
{
    public static void init() {
        ClientLifecycleEvent.CLIENT_SETUP.register(ClientEvents::onSetup);
    }

    public static void onSetup(MinecraftClient client)
    {
        ModRender.setup(client);
    }
}
