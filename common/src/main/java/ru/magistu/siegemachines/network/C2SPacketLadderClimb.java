package ru.magistu.siegemachines.network;

import io.netty.channel.ChannelHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import ru.magistu.siegemachines.entity.machine.LadderSeat;
import ru.magistu.siegemachines.entity.machine.Machine;

@ChannelHandler.Sharable
public class C2SPacketLadderClimb implements C2SModPacket<RegistryFriendlyByteBuf> {

    boolean upwards;

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SPacketLadderClimb> STREAM_CODEC =
            ModPacket.streamCodec(C2SPacketLadderClimb::read);


    public static final Type<C2SPacketLadderClimb> TYPE = ModPacket.type(C2SPacketLadderClimb.class);


    public C2SPacketLadderClimb(boolean upwards) {
        this.upwards = upwards;
    }

    public static C2SPacketLadderClimb read(FriendlyByteBuf buf) {
        return new C2SPacketLadderClimb(buf.readBoolean());
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(upwards);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handleServer(ServerPlayer player) {
        Entity entity = player.getVehicle();
        if (entity instanceof LadderSeat seat) {
            seat.climb(upwards);
        }
    }
}
