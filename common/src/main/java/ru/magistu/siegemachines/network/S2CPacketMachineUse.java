package ru.magistu.siegemachines.network;

import io.netty.channel.ChannelHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import ru.magistu.siegemachines.platform.Services;

@ChannelHandler.Sharable
public class S2CPacketMachineUse implements S2CModPacket<RegistryFriendlyByteBuf> {

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CPacketMachineUse> STREAM_CODEC =
            ModPacket.streamCodec(S2CPacketMachineUse::read);


    public static final CustomPacketPayload.Type<S2CPacketMachineUse> TYPE = ModPacket.type(S2CPacketMachineUse.class);

    private final int entityid;

    public S2CPacketMachineUse(int entityid) {
        this.entityid = entityid;
    }

    public static S2CPacketMachineUse read(FriendlyByteBuf buf) {
        return new S2CPacketMachineUse(buf.readInt());
    }

    @Override
    public void handleClient() {
        handleClientInernal(entityid);
    }

    public static void handleClientInernal(int entityid) {
        Services.PLATFORM.handleMachineUse(entityid);
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(entityid);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
