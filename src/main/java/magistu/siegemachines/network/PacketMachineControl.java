package magistu.siegemachines.network;

import io.netty.channel.ChannelHandler;
import magistu.siegemachines.entity.machine.Machine;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

@ChannelHandler.Sharable
public class PacketMachineControl
{
	public PacketMachineControl() {}

	public static PacketMachineControl read(PacketBuffer buf)
    {
        return new PacketMachineControl();
    }

	public static void write(PacketMachineControl message, PacketBuffer buf) {}

	public static class Handler
    {
        public static void handle(PacketMachineControl packet, Supplier<NetworkEvent.Context> ctx)
        {
            NetworkEvent.Context context = ctx.get();
            if (context.getDirection().getReceptionSide() == LogicalSide.SERVER)
            {
                context.enqueueWork(() -> PacketMachineControl.handleServerSide(packet, context.getSender()));
			}
            context.setPacketHandled(true);
        }
    }

	public static void handleServerSide(PacketMachineControl packet, ServerPlayerEntity player)
	{
		if(packet == null || player == null || !player.isPassenger())
		{
			return;
		}

		Entity entity = player.getVehicle();
        if (!(entity instanceof Machine))
        {
            return;
        }
		Machine machine = (Machine) entity;

		machine.setTurretRotations(player.xRot, player.yRot);
	}
}
