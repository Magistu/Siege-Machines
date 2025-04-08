package ru.magistu.siegemachines.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface ModPacket extends CustomPacketPayload {

    static <P extends ModPacket> Type<P> type(Class<P> pClass) {
        return new Type<>(ModPacketHandler.packet(pClass));
    }

    void apply(NetworkManager.PacketContext ctx);
}
