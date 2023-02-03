package ru.itzme1on.siegemachines.network;

import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import ru.itzme1on.siegemachines.entity.machine.Machine;

import java.util.function.Supplier;

public class PacketMachine {
    private final int entityId;
    private final int delayTicks;
    private final int useTicks;
    private final float turretPitch;
    private final float turretYaw;

    public PacketMachine(int entityId, int delayTicks, int useTicks, float turretPitch, float turretYaw) {
        this.entityId = entityId;
        this.delayTicks = delayTicks;
        this.useTicks = useTicks;
        this.turretPitch = turretPitch;
        this.turretYaw = turretYaw;
    }

    public static PacketMachine read(FriendlyByteBuf buf) {
        return new PacketMachine(buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat(), buf.readFloat());
    }

    public static void write(PacketMachine message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeInt(message.delayTicks);
        buf.writeInt(message.useTicks);
        buf.writeFloat(message.turretPitch);
        buf.writeFloat(message.turretYaw);
    }

    public static class Handler {
        public void handle(PacketMachine packet, Supplier<NetworkManager.PacketContext> context) {
            NetworkManager.PacketContext ctx = context.get();
            if (ctx.getEnv() == EnvType.CLIENT)
                ctx.queue(() -> PacketMachine.handleClientSide(packet));
        }
    }

    public static void handleClientSide(PacketMachine packet) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (packet == null || player == null || player.level == null)
            return;

        Entity entity = player.level.getEntity(packet.entityId);
        if (!(entity instanceof Machine machine))
            return;

        machine.delayTicks = packet.delayTicks;
        machine.useTicks = packet.useTicks;
        machine.setTurretRotations(packet.turretPitch, packet.turretYaw);
    }
}
