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
public class PacketOpenMachineInventory
{
	public PacketOpenMachineInventory() {}

	public static PacketOpenMachineInventory read(PacketBuffer buf)
    {
        return new PacketOpenMachineInventory();
    }

	public static void write(PacketOpenMachineInventory message, PacketBuffer buf) {}

	public static class Handler
    {
        public static void handle(PacketOpenMachineInventory packet, Supplier<NetworkEvent.Context> ctx)
        {
            NetworkEvent.Context context = ctx.get();
            if (context.getDirection().getReceptionSide() == LogicalSide.SERVER)
            {
                context.enqueueWork(() -> PacketOpenMachineInventory.handleServerSide(packet, context.getSender()));
			}
            context.setPacketHandled(true);
        }
    }

	public static void handleServerSide(PacketOpenMachineInventory packet, ServerPlayerEntity player)
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

        machine.openInventoryGui();
	}
}
