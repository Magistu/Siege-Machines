package ru.magistu.siegemachines.network;

import io.netty.channel.ChannelHandler;
import net.minecraft.client.player.LocalPlayer;
import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@ChannelHandler.Sharable
public class S2CPacketMachineUse
{
	private final int entityid;

	public S2CPacketMachineUse(int entityid) {
		this.entityid = entityid;
	}

	public static S2CPacketMachineUse read(FriendlyByteBuf buf) {
        return new S2CPacketMachineUse(buf.readInt());
    }

	public static void write(S2CPacketMachineUse message, FriendlyByteBuf buf) {
		buf.writeInt(message.entityid);
	}

	public static class Handler {
        public static void handle(S2CPacketMachineUse packet, Supplier<NetworkEvent.Context> ctx) {
            NetworkEvent.Context context = ctx.get();
			if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                context.enqueueWork(() -> S2CPacketMachineUse.handleClientSide(packet));
			}
            context.setPacketHandled(true);
        }
    }

	@OnlyIn(Dist.CLIENT)
	public static void handleClientSide(S2CPacketMachineUse packet) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}
        Entity entity = player.level().getEntity(packet.entityid);
		if (entity instanceof Machine machine) {
			machine.use(player);
		}
	}
}
