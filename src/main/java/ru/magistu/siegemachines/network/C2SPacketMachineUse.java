package ru.magistu.siegemachines.network;

import io.netty.channel.ChannelHandler;
import net.minecraft.server.level.ServerPlayer;
import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@ChannelHandler.Sharable
public class C2SPacketMachineUse {

	public C2SPacketMachineUse() {}

	public static C2SPacketMachineUse read(FriendlyByteBuf buf) {
        return new C2SPacketMachineUse();
    }

	public static void write(C2SPacketMachineUse message, FriendlyByteBuf buf) {}

	public static class Handler {
        public static void handle(C2SPacketMachineUse packet, Supplier<NetworkEvent.Context> ctx) {
            NetworkEvent.Context context = ctx.get();
            if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
                context.enqueueWork(() -> C2SPacketMachineUse.handleServerSide(packet, context.getSender()));
			}
            context.setPacketHandled(true);
        }
    }

	public static void handleServerSide(C2SPacketMachineUse packet, ServerPlayer player)
	{
		if (player == null) {
			return;
		}
		Entity entity = player.getVehicle();
		if (entity instanceof Machine machine) {
			machine.use(player);
		}
	}
}
