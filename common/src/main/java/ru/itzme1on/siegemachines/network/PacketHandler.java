package ru.itzme1on.siegemachines.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import ru.itzme1on.siegemachines.SiegeMachines;

import java.util.List;

public class PacketHandler
{
    public static void init()
    {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, PacketMachineUse.ID, PacketMachineUse::apply);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, PacketMachineUseRealise.ID, PacketMachineUseRealise::apply);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, PacketOpenMachineInventory.ID, PacketOpenMachineInventory::apply);

        if (Platform.getEnvironment() == Env.CLIENT)
        {
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, PacketMachineUse.ID, PacketMachineUse::apply);
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, PacketMachineUseRealise.ID, PacketMachineUseRealise::apply);
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, PacketMachine.ID, PacketMachine::apply);
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, PacketMachineInventorySlot.ID, PacketMachineInventorySlot::apply);
        }
    }

    public static void sendTo(Object packet, ServerPlayerEntity player) {
            CHANNEL.sendToPlayer(player, packet);
    }
}
