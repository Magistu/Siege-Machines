package ru.magistu.siegemachines.network;

import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;


public class ModNetwork {
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(SiegeMachines.ID, "main"), () -> "1", "1"::equals, "1"::equals);

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(++id, C2SPacketMachineUse.class, C2SPacketMachineUse::write, C2SPacketMachineUse::read, C2SPacketMachineUse.Handler::handle);
        INSTANCE.registerMessage(++id, S2CPacketMachineUse.class, S2CPacketMachineUse::write, S2CPacketMachineUse::read, S2CPacketMachineUse.Handler::handle);
        INSTANCE.registerMessage(++id, PacketOpenMachineInventory.class, PacketOpenMachineInventory::write, PacketOpenMachineInventory::read, PacketOpenMachineInventory.Handler::handle);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }

    public static void sendTo(Object packet, ServerPlayer player) {
        if (!(player instanceof net.minecraftforge.common.util.FakePlayer)) {
            INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public static void sendPacketToAllInArea(Object packet, BlockPos center, int rangesqr) {
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            if (player.distanceToSqr(center.getX(), center.getY(), center.getZ()) < rangesqr) {
                sendTo(packet, player);
            }
        }
    }
}
