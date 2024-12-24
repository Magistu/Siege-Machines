package ru.magistu.siegemachines.network;

import io.netty.channel.ChannelHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import ru.magistu.siegemachines.entity.machine.Machine;

@ChannelHandler.Sharable
public class C2SPacketMachineUse implements C2SModPacket<RegistryFriendlyByteBuf>
{

	public static final StreamCodec<RegistryFriendlyByteBuf, C2SPacketMachineUse> STREAM_CODEC =
			ModPacket.streamCodec(C2SPacketMachineUse::read);


	public static final Type<C2SPacketMachineUse> TYPE = ModPacket.type(C2SPacketMachineUse.class);


	public C2SPacketMachineUse() {}

	public static C2SPacketMachineUse read(FriendlyByteBuf buf)
    {
        return new C2SPacketMachineUse();
    }

	@Override
	public void write(RegistryFriendlyByteBuf buf) {
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@Override
	public void handleServer(ServerPlayer player) {
		Entity entity = player.getVehicle();
        if (entity instanceof Machine machine) {
            machine.use(player);
        }
    }
}
