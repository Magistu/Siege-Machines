package ru.itzme1on.siegemachines.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import ru.itzme1on.siegemachines.entity.machine.Machine;

import java.util.function.Supplier;

public class PacketMachine {
    private final int entityId;
    private final int delayTicks;
    private final int useTicks;
    private final float turretPitch;
    private final float turretYaw;

    public PacketMachine(PacketByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat(), buf.readFloat());
    }

    public PacketMachine(int entityId, int delayTicks, int useTicks, float turretPitch, float turretYaw) {
        this.entityId = entityId;
        this.delayTicks = delayTicks;
        this.useTicks = useTicks;
        this.turretPitch = turretPitch;
        this.turretYaw = turretYaw;
    }

    public void encode(PacketByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(delayTicks);
        buf.writeInt(useTicks);
        buf.writeFloat(turretPitch);
        buf.writeFloat(turretYaw);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (contextSupplier.get() == null || player == null || player.world == null)
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
