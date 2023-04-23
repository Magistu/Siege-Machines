package ru.itzme1on.siegemachines.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import ru.itzme1on.siegemachines.SiegeMachines;
import ru.itzme1on.siegemachines.entity.machine.Machine;

import java.util.List;

public class PacketMachineUse
{
    public static final Identifier ID = new Identifier(SiegeMachines.MOD_ID, "packet_machine_use");

    public static void sendToAllAround(Machine machine)
    {
        Box AABB = Box.of(Vec3d.ofCenter(machine.getBlockPos()), 2 * SiegeMachines.RENDER_UPDATE_RANGE, 2 * SiegeMachines.RENDER_UPDATE_RANGE, 2 * SiegeMachines.RENDER_UPDATE_RANGE);
        List<PlayerEntity> players = machine.world.getPlayers(TargetPredicate.createNonAttackable().setBaseMaxDistance(SiegeMachines.RENDER_UPDATE_RANGE), machine, AABB);
        players.forEach(player -> {
            if (player instanceof ServerPlayerEntity serverplayer)
                sendToPlayer(serverplayer, machine);
        });
    }

    public static void sendToPlayer(ServerPlayerEntity player, Machine machine)
    {
        NetworkManager.sendToPlayer(player, ID, encode(machine));
    }

    public static void sendToServer(Machine machine)
    {
        NetworkManager.sendToServer(ID, encode(machine));
    }

    static PacketByteBuf encode(Machine machine)
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(machine.getId());
        return buf;
    }

    public static void apply(PacketByteBuf buf, NetworkManager.PacketContext context)
    {
        if (context == null)
            return;

//        PlayerEntity player = context.getEnv() == EnvType.SERVER ? MinecraftClient.getInstance().player : context.getPlayer();
        PlayerEntity player = context.getEnv() == EnvType.CLIENT ? MinecraftClient.getInstance().player : context.getPlayer();

        if (player == null || player.world == null)
            return;

        int entityId = buf.readInt();

        context.queue(() -> execute(player, entityId));
    }

    static void execute(PlayerEntity player, int entityId)
    {
        Entity entity = player.world.getEntityById(entityId);
        if (!(entity instanceof Machine))
            return;

        Machine machine = (Machine) entity;
        machine.use(player);
    }
}
