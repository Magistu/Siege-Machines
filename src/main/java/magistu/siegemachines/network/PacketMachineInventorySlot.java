package magistu.siegemachines.network;

import io.netty.channel.ChannelHandler;
import magistu.siegemachines.entity.machine.Machine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

@ChannelHandler.Sharable
public class PacketMachineInventorySlot
{
    private final int entityid;
    private final int slot;
    private ItemStack itemstack = ItemStack.EMPTY;

    public PacketMachineInventorySlot(int entityid, int slot, ItemStack itemstack)
    {
        this.entityid = entityid;
        this.slot = slot;
        this.itemstack = itemstack.copy();
    }

    public static PacketMachineInventorySlot read(PacketBuffer buf)
    {
        return new PacketMachineInventorySlot(buf.readInt(), buf.readInt(), buf.readItem());
    }

    public static void write(PacketMachineInventorySlot message, PacketBuffer buf)
    {
        buf.writeInt(message.entityid);
        buf.writeInt(message.slot);
        buf.writeItemStack(message.itemstack, false);
    }

    public static class Handler
    {
        public static void handle(PacketMachineInventorySlot packet, Supplier<NetworkEvent.Context> ctx)
        {
            NetworkEvent.Context context = ctx.get();
            if (context.getDirection().getReceptionSide() == LogicalSide.SERVER)
            {
                context.enqueueWork(() -> PacketMachineInventorySlot.handleEachSide(packet, context.getSender()));
			}
			else if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT)
			{
                context.enqueueWork(() -> PacketMachineInventorySlot.handleClientSide(packet));
			}
            context.setPacketHandled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
	public static void handleClientSide(PacketMachineInventorySlot packet)
	{
		handleEachSide(packet, Minecraft.getInstance().player);
	}

	public static void handleEachSide(PacketMachineInventorySlot packet, PlayerEntity player)
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

        machine.inventory.setItem(packet.slot, packet.itemstack);
    }
}
