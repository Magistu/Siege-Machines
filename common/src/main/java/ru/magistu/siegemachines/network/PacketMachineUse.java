package ru.magistu.siegemachines.network;

import dev.architectury.networking.NetworkManager;
import io.netty.channel.ChannelHandler;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import ru.magistu.siegemachines.entity.machine.Machine;

@ChannelHandler.Sharable
public class PacketMachineUse implements ModPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketMachineUse> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.entityid,
            PacketMachineUse::new);

    public static final Type<PacketMachineUse> TYPE = ModPacket.type(PacketMachineUse.class);

    private final int entityid;

    public PacketMachineUse(int entityid) {
        this.entityid = entityid;
    }

    public void apply(NetworkManager.PacketContext ctx) {
        if (ctx.getEnvironment() == Env.CLIENT) {
            ctx.queue(() -> handleClient(ctx.getPlayer()));
        } else {
            ctx.queue(() -> handleServer(ctx.getPlayer()));
        }
    }

    private void handleServer(Player player) {
        Entity entity = player.level().getEntity(entityid);
        if (entity instanceof Machine machine) {
            machine.use(player);
        }
    }

    @Environment(EnvType.CLIENT)
    private void handleClient(Player player) {
        if (player == null) {
            return;
        }

        Entity entity = player.level().getEntity(entityid);
        if (!(entity instanceof Machine machine)) {
            return;
        }

        machine.use(player);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
