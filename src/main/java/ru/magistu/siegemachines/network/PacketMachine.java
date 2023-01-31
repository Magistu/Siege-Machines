package ru.magistu.siegemachines.network;

import io.netty.channel.ChannelHandler;
import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@ChannelHandler.Sharable
public class PacketMachine
{
	private final int entityid;
	private final int delayticks;
	private final int useticks;
	private final float turretyaw;
	private final float turretpitch;

	public PacketMachine(int entityid, int delayticks, int useticks, float turretpitch, float turretyaw)
	{
		this.entityid = entityid;
		this.delayticks = delayticks;
		this.useticks = useticks;
		this.turretpitch = turretpitch;
		this.turretyaw = turretyaw;
	}

	public static PacketMachine read(FriendlyByteBuf buf)
    {
        return new PacketMachine(buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat(), buf.readFloat());
    }

	public static void write(PacketMachine message, FriendlyByteBuf buf)
    {
		buf.writeInt(message.entityid);
		buf.writeInt(message.delayticks);
		buf.writeInt(message.useticks);
		buf.writeFloat(message.turretpitch);
		buf.writeFloat(message.turretyaw);
	}

	public static class Handler
    {
        public static void handle(PacketMachine packet, Supplier<NetworkEvent.Context> ctx)
        {
            NetworkEvent.Context context = ctx.get();
            if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT)
            {
                context.enqueueWork(() -> PacketMachine.handleClientSide(packet));
			}
            context.setPacketHandled(true);
        }
    }

	@OnlyIn(Dist.CLIENT)
	public static void handleClientSide(PacketMachine packet)
	{
		LocalPlayer player = Minecraft.getInstance().player;
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

		machine.delayticks = packet.delayticks;
		machine.useticks = packet.useticks;
		machine.setTurretRotations(packet.turretpitch, packet.turretyaw);
	}
}
