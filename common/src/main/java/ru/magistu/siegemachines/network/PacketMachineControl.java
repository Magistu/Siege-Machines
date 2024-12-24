package ru.magistu.siegemachines.network;

import io.netty.channel.ChannelHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

@ChannelHandler.Sharable
public class PacketMachineControl implements C2SModPacket<RegistryFriendlyByteBuf>
{
	public PacketMachineControl() {}

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketMachineControl> STREAM_CODEC =
            ModPacket.streamCodec(PacketMachineControl::read);


    public static final CustomPacketPayload.Type<PacketMachineControl> TYPE = ModPacket.type(PacketMachineControl.class);

	public static PacketMachineControl read(FriendlyByteBuf buf)
    {
        return new PacketMachineControl();
    }

    @Override
    public void handleServer(ServerPlayer player) {
        if(player == null) {
            return;
        }

        Entity entity = player.getVehicle();
        if (!(entity instanceof Machine machine))
        {
            return;
        }

        machine.setTurretRotations(player.getXRot(), player.getYRot());
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
