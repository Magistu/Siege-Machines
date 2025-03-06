package ru.magistu.siegemachines.network;

import io.netty.channel.ChannelHandler;
import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@ChannelHandler.Sharable
public class PacketMachineUseRelease
{
	private final int entityid;

	public PacketMachineUseRelease(int entityid)
	{
		this.entityid = entityid;
	}

	public static PacketMachineUseRelease read(FriendlyByteBuf buf)
    {
        return new PacketMachineUseRelease(buf.readInt());
    }

	public static void write(PacketMachineUseRelease message, FriendlyByteBuf buf)
    {
		buf.writeInt(message.entityid);
	}

	public static class Handler
    {
        public static void handle(PacketMachineUseRelease packet, Supplier<NetworkEvent.Context> ctx)
        {
            NetworkEvent.Context context = ctx.get();
            if (context.getDirection().getReceptionSide() == LogicalSide.SERVER)
            {
                context.enqueueWork(() -> PacketMachineUseRelease.handleEachSide(packet, context.getSender()));
			}
			else if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT)
			{
                context.enqueueWork(() -> PacketMachineUseRelease.handleClientSide(packet));
			}
            context.setPacketHandled(true);
        }
    }

	@OnlyIn(Dist.CLIENT)
	public static void handleClientSide(PacketMachineUseRelease packet)
	{
		handleEachSide(packet, Minecraft.getInstance().player);
	}

	public static void handleEachSide(PacketMachineUseRelease packet, Player player)
	{
		if(packet == null || player == null || player.level() == null)
		{
			return;
		}

		Entity entity = player.level().getEntity(packet.entityid);
        if (!(entity instanceof Machine))
        {
            return;
        }
		Machine machine = (Machine) entity;

        machine.useRelease();
	}
}
