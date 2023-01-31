package ru.magistu.siegemachines.network;

import io.netty.channel.ChannelHandler;
import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@ChannelHandler.Sharable
public class PacketOpenMachineInventory
{
	public PacketOpenMachineInventory() {}

	public static PacketOpenMachineInventory read(FriendlyByteBuf buf)
    {
        return new PacketOpenMachineInventory();
    }

	public static void write(PacketOpenMachineInventory message, FriendlyByteBuf buf) {}

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

	public static void handleServerSide(PacketOpenMachineInventory packet, ServerPlayer player)
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
