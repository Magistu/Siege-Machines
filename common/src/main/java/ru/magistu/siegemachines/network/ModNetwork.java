package ru.magistu.siegemachines.network;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.platform.Services;

import java.util.List;
import java.util.Locale;

public class ModNetwork {

    public static void register() {
        Services.PLATFORM.registerClientPlayPacket(S2CPacketMachineUse.TYPE, S2CPacketMachineUse.STREAM_CODEC);
        Services.PLATFORM.registerServerPlayPacket(C2SPacketMachineUse.TYPE, C2SPacketMachineUse.STREAM_CODEC);
        Services.PLATFORM.registerServerPlayPacket(PacketOpenMachineInventory.TYPE, PacketOpenMachineInventory.STREAM_CODEC);
    }

    public static void sendToServer(C2SModPacket<?> packet) {
        Services.PLATFORM.sendToServer(packet);
    }

    public static void sendTo(S2CModPacket<?> packet, ServerPlayer player) {//todo check for fake players
        Services.PLATFORM.sendToClient(packet, player);
    }

    public static void sendPacketToAllInArea(ServerLevel level, S2CModPacket<?> packet, BlockPos center, int rangesqr) {
        List<ServerPlayer> playerList = level.players();
        for (ServerPlayer player : playerList) {
            if (player.distanceToSqr(center.getX(), center.getY(), center.getZ()) < rangesqr) {
                sendTo(packet, player);
            }
        }
    }


    public static ResourceLocation packet(Class<?> clazz) {
        return SiegeMachines.id(clazz.getName().toLowerCase(Locale.ROOT));
    }


}
