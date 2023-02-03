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

    public PacketMachine(FriendlyByteBuf buf) 
    {
        this(buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat(), buf.readFloat());
    }

    public PacketMachine(int entityId, int delayTicks, int useTicks, float turretPitch, float turretYaw) {
        this.entityId = entityId;
        this.delayTicks = delayTicks;
        this.useTicks = useTicks;
        this.turretPitch = turretPitch;
        this.turretYaw = turretYaw;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(delayTicks);
        buf.writeInt(useTicks);
        buf.writeFloat(turretPitch);
        buf.writeFloat(turretYaw);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (contextSupplier.get() == null || player == null || player.level == null)
            return;

        Entity entity = player.level.getEntity(entityId);
        if (!(entity instanceof Machine machine))
            return;

        machine.delayTicks = delayTicks;
        machine.useTicks = useTicks;
        machine.setTurretRotations(turretPitch, turretYaw);
    }
}
