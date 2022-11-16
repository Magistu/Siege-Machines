package magistu.siegemachines.network;

import magistu.siegemachines.SiegeMachines;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PacketHandler
{
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(SiegeMachines.ID, "main"), () -> "1", "1"::equals, "1"::equals);

    protected static int currentId = 0;

    public static void init()
    {
        INSTANCE.registerMessage(getNextId(), PacketMachine.class, PacketMachine::write, PacketMachine::read, PacketMachine.Handler::handle);
        INSTANCE.registerMessage(getNextId(), PacketMachineControl.class, PacketMachineControl::write, PacketMachineControl::read, PacketMachineControl.Handler::handle);
        INSTANCE.registerMessage(getNextId(), PacketMachineUse.class, PacketMachineUse::write, PacketMachineUse::read, PacketMachineUse.Handler::handle);
        INSTANCE.registerMessage(getNextId(), PacketMachineUseRealise.class, PacketMachineUseRealise::write, PacketMachineUseRealise::read, PacketMachineUseRealise.Handler::handle);
        INSTANCE.registerMessage(getNextId(), PacketOpenMachineInventory.class, PacketOpenMachineInventory::write, PacketOpenMachineInventory::read, PacketOpenMachineInventory.Handler::handle);
        INSTANCE.registerMessage(getNextId(), PacketMachineInventorySlot.class, PacketMachineInventorySlot::write, PacketMachineInventorySlot::read, PacketMachineInventorySlot.Handler::handle);

    }

    public static int getNextId()
    {
        int id = currentId;
        currentId++;
        return id;
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }

    public static void sendTo(Object packet, ServerPlayerEntity player)
    {
        if (!(player instanceof net.minecraftforge.common.util.FakePlayer))
            INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendPacketToAllInArea(Object packet, BlockPos center, int rangesqr)
    {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers())
        {
            if (player.distanceToSqr(center.getX(), center.getY(), center.getZ()) < rangesqr)
            {
                sendTo(packet, player);
            }
        }
    }

    public static void sendPacketToAll(Object packet)
    {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers())
        {
            sendTo(packet, player);
        }
    }
}
