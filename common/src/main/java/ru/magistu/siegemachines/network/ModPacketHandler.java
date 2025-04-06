package ru.magistu.siegemachines.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import ru.magistu.siegemachines.SiegeMachines;
import java.util.List;
import java.util.Locale;


public class ModPacketHandler {

    public static void register() {
        registerClientPlayPacket(S2CPacketMachineUse.TYPE, S2CPacketMachineUse.STREAM_CODEC);
        registerServerPlayPacket(C2SPacketMachineUse.TYPE, C2SPacketMachineUse.STREAM_CODEC);
        registerServerPlayPacket(C2SPacketLadderClimb.TYPE, C2SPacketLadderClimb.STREAM_CODEC);
        registerServerPlayPacket(PacketOpenMachineInventory.TYPE, PacketOpenMachineInventory.STREAM_CODEC);
    }

    public static <T extends S2CModPacket<?>> void registerClientPlayPacket(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf,T> streamCodec) {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, type, streamCodec,
                (p, ctx) -> ctx.queue(p::handleClient));
    }

    public static <T extends C2SModPacket<?>> void registerServerPlayPacket(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, type, streamCodec,
                (p, ctx) -> ctx.queue(() -> p.handleServer((ServerPlayer) ctx.getPlayer())));
    }

    public static void sendPacketToAllInArea(ServerLevel level, S2CModPacket<?> packet, BlockPos center, int rangesqr) {
        List<ServerPlayer> playerList = level.players();
        for (ServerPlayer player : playerList) {
            if (player.distanceToSqr(center.getX(), center.getY(), center.getZ()) < rangesqr) {
                sendToClient(packet, player);
            }
        }
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
