package ru.magistu.siegemachines.network;

import io.netty.channel.ChannelHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

@ChannelHandler.Sharable
public record PacketMachineInventorySlot(int entityid, int slot, ItemStack itemstack) implements S2CModPacket<RegistryFriendlyByteBuf> {

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketMachineInventorySlot> STREAM_CODEC =
            ModPacket.streamCodec(PacketMachineInventorySlot::read);


    public static final CustomPacketPayload.Type<PacketMachineInventorySlot> TYPE = ModPacket.type(PacketMachineInventorySlot.class);

    public static PacketMachineInventorySlot read(RegistryFriendlyByteBuf buf)
    {
        return new PacketMachineInventorySlot(buf.readInt(), buf.readInt(), ItemStack.STREAM_CODEC.decode(buf));
    }

    @Override
    public void handleClient() {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) {
            return;
        }

        Entity entity = player.level().getEntity(entityid);
        if (!(entity instanceof Machine))
        {
            return;
        }
        Machine machine = (Machine) entity;

        machine.inventory.setItem(slot, itemstack);
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(entityid);
        buf.writeInt(slot);
        ItemStack.STREAM_CODEC.encode(buf,itemstack);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
