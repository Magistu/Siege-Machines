package magistu.siegemachines.network;

import io.netty.channel.ChannelHandler;
import magistu.siegemachines.entity.machine.Machine;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

@ChannelHandler.Sharable
public class PacketMachineUseRealise
{
	private final int entityid;

	public PacketMachineUseRealise(int entityid)
	{
		this.entityid = entityid;
	}

	public static PacketMachineUseRealise read(PacketBuffer buf)
    {
        return new PacketMachineUseRealise(buf.readInt());
    }

	public static void write(PacketMachineUseRealise message, PacketBuffer buf)
    {
		buf.writeInt(message.entityid);
	}

	public static class Handler
    {
        public static void handle(PacketMachineUseRealise packet, Supplier<NetworkEvent.Context> ctx)
        {
            NetworkEvent.Context context = ctx.get();
            if (context.getDirection().getReceptionSide() == LogicalSide.SERVER)
            {
                context.enqueueWork(() -> PacketMachineUseRealise.handleEachSide(packet, context.getSender()));
			}
			else if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT)
			{
                context.enqueueWork(() -> PacketMachineUseRealise.handleClientSide(packet));
			}
            context.setPacketHandled(true);
        }
    }

	@OnlyIn(Dist.CLIENT)
	public static void handleClientSide(PacketMachineUseRealise packet)
	{
		handleEachSide(packet, Minecraft.getInstance().player);
	}

	public static void handleEachSide(PacketMachineUseRealise packet, PlayerEntity player)
	{
		if(packet == null || player == null || player.level == null)
		{
			return;
		}

		Entity entity = player.level.getEntity(packet.entityid);
        if (!(entity instanceof Machine))
        {
            return;
        }
		Machine machine = (Machine) entity;

        machine.useRealise();
	}
}
