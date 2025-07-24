package ru.magistu.siegemachines.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import ru.magistu.siegemachines.SiegeMachines;
import java.util.Locale;

public class ModPacketHandler {

    public static void register() {
        if (Platform.getEnvironment() == Env.CLIENT) {
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, PacketMachineUse.TYPE, PacketMachineUse.STREAM_CODEC, PacketMachineUse::apply);
        } else {
            NetworkManager.registerS2CPayloadType(PacketMachineUse.TYPE, PacketMachineUse.STREAM_CODEC);
        }
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, PacketMachineUse.TYPE, PacketMachineUse.STREAM_CODEC, PacketMachineUse::apply);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, PacketOpenMachineInventory.TYPE, PacketOpenMachineInventory.STREAM_CODEC, PacketOpenMachineInventory::apply);
    }

    public static void sendPacketToAllInArea(ServerLevel level, ModPacket packet, BlockPos center, int rangesqr) {
        NetworkManager.sendToPlayers(() -> level.players().stream().filter(p -> p.distanceToSqr(center.getX(), center.getY(), center.getZ()) < rangesqr).iterator(), packet);
    }

    public static ResourceLocation packet(Class<?> clazz) {
        return SiegeMachines.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

    public static void sendToClient(CustomPacketPayload packet, ServerPlayer player) {
        NetworkManager.sendToPlayer(player, packet);
    }

    public static void sendToServer(CustomPacketPayload packet) {
        NetworkManager.sendToServer(packet);
    }
}
