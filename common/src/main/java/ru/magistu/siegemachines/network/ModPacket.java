package ru.magistu.siegemachines.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface ModPacket<T extends FriendlyByteBuf> extends CustomPacketPayload {

    static <T extends FriendlyByteBuf,P extends ModPacket<T>> StreamCodec<T, P> streamCodec(StreamDecoder<T,P> decoder){
        return StreamCodec.ofMember(ModPacket::write, decoder);
    }

    static  <T extends FriendlyByteBuf,P extends ModPacket<T>> Type<P> type(Class<P> pClass){
        return new Type<>(PacketHandler.packet(pClass));
    }

    void write(T buf) ;
}
