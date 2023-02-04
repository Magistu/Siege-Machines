package ru.itzme1on.siegemachines.network;

import dev.architectury.networking.NetworkChannel;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import ru.itzme1on.siegemachines.SiegeMachines;

public class PacketHandler {
    public static final NetworkChannel CHANNEL = NetworkChannel.create(new Identifier(SiegeMachines.MOD_ID, "networking_channel"));

    protected static int currentId = 0;

    public static void init() {
        CHANNEL.register(PacketMachine.class, PacketMachine::encode, PacketMachine::new, PacketMachine::apply);
    }

    public static void sendTo(Object packet, ServerPlayerEntity player) {
            CHANNEL.sendToPlayer(player, packet);
    }
}
