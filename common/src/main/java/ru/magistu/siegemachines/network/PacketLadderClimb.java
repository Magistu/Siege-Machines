package ru.magistu.siegemachines.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import io.netty.channel.ChannelHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import ru.magistu.siegemachines.entity.machine.LadderSeat;

import java.text.MessageFormat;

@ChannelHandler.Sharable
public class PacketLadderClimb implements ModPacket {

    boolean upwards;

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketLadderClimb> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.BOOL, p -> p.upwards, PacketLadderClimb::new);


    public static final Type<PacketLadderClimb> TYPE = ModPacket.type(PacketLadderClimb.class);


    public PacketLadderClimb(boolean upwards) {
        this.upwards = upwards;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void apply(NetworkManager.PacketContext ctx) {
        if (ctx.getEnvironment() == Env.CLIENT) {
            throw new IllegalStateException(MessageFormat.format("attempted to handle packet {0} on client side", TYPE));
        } else {
            ctx.queue(() -> handleServer(ctx.getPlayer()));
        }
    }

    private void handleServer(Player player) {
        Entity entity = player.getVehicle();
        if (entity instanceof LadderSeat seat) {
            seat.climb(upwards);
        }
    }
}
