package ru.magistu.siegemachines.network;

import io.netty.channel.ChannelHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

@ChannelHandler.Sharable
public class PacketOpenMachineInventory implements C2SModPacket<RegistryFriendlyByteBuf>{

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketOpenMachineInventory> STREAM_CODEC =
            ModPacket.streamCodec(PacketOpenMachineInventory::read);


    public static final CustomPacketPayload.Type<PacketOpenMachineInventory> TYPE = ModPacket.type(PacketOpenMachineInventory.class);

	public PacketOpenMachineInventory() {}

	public static PacketOpenMachineInventory read(FriendlyByteBuf buf)
    {
        return new PacketOpenMachineInventory();
    }

    @Override
    public void handleServer(ServerPlayer player) {
        Entity entity = player.getVehicle();
        if (entity instanceof Machine machine) {
            machine.openInventoryGui();
        }
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
