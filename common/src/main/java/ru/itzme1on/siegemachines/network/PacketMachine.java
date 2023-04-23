package ru.itzme1on.siegemachines.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
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


public class PacketMachine
{
    public static final Identifier ID = new Identifier(SiegeMachines.MOD_ID, "packet_machine");

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

    static PacketByteBuf encode(Machine machine)
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(machine.getId());
        buf.writeInt(machine.delayTicks);
        buf.writeInt(machine.useTicks);
        buf.writeFloat(machine.getTurretPitch(0.5f));
        buf.writeFloat(machine.getTurretYaw(0.5f));
        return buf;
    }

    public static void apply(PacketByteBuf buf, NetworkManager.PacketContext context)
    {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (context == null || player == null || player.world == null)
            return;

        int entityId = buf.readInt();
        int delayTicks = buf.readInt();
        int useTicks = buf.readInt();
        float turretPitch = buf.readFloat();
        float turretYaw = buf.readFloat();

        context.queue(() -> execute(player, entityId, delayTicks, useTicks, turretPitch, turretYaw));
    }

    static void execute(ClientPlayerEntity player, int entityId, int delayTicks, int useTicks, float turretPitch, float turretYaw)
    {
        Entity entity = player.world.getEntityById(entityId);
        if (!(entity instanceof Machine machine))
            return;

        machine.delayTicks = delayTicks;
        machine.useTicks = useTicks;
        machine.setTurretRotations(turretPitch, turretYaw);
    }
}
