package ru.itzme1on.siegemachines.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import ru.itzme1on.siegemachines.SiegeMachines;
import ru.itzme1on.siegemachines.entity.machine.Machine;

import java.util.List;

public class PacketOpenMachineInventory
{
    public static final Identifier ID = new Identifier(SiegeMachines.MOD_ID, "packet_machine_use");

    public static void sendToServer()
    {
        NetworkManager.sendToServer(ID, encode());
    }

    static PacketByteBuf encode()
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        return buf;
    }

    public static void apply(PacketByteBuf buf, NetworkManager.PacketContext context)
    {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (context == null || player == null || player.world == null)
            return;

        context.queue(() -> execute(player));
    }

    static void execute(ClientPlayerEntity player)
    {
        Entity entity = player.getVehicle();
        if (!(entity instanceof Machine))
            return;

        Machine machine = (Machine) entity;
        machine.openInventoryGui();
    }
}
