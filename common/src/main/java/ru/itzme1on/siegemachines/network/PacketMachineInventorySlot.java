package ru.itzme1on.siegemachines.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import ru.itzme1on.siegemachines.SiegeMachines;
import ru.itzme1on.siegemachines.entity.machine.Machine;

import java.util.List;

public class PacketMachineInventorySlot
{
    public static final Identifier ID = new Identifier(SiegeMachines.MOD_ID, "packet_machine_inventory_slot");

    public static void sendToAllAround(Machine machine, int slot)
    {
        Box AABB = Box.of(Vec3d.ofCenter(machine.getBlockPos()), 2 * SiegeMachines.RENDER_UPDATE_RANGE, 2 * SiegeMachines.RENDER_UPDATE_RANGE, 2 * SiegeMachines.RENDER_UPDATE_RANGE);
        List<PlayerEntity> players = machine.world.getPlayers(TargetPredicate.createNonAttackable().setBaseMaxDistance(SiegeMachines.RENDER_UPDATE_RANGE), machine, AABB);
        players.forEach(player -> {
            if (player instanceof ServerPlayerEntity serverplayer)
                sendToPlayer(serverplayer, machine, slot);
        });
    }

    public static void sendToPlayer(ServerPlayerEntity player, Machine machine, int slot)
    {
        NetworkManager.sendToPlayer(player, ID, encode(machine, slot));
    }

    static PacketByteBuf encode(Machine machine, int slot)
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(machine.getId());
        buf.writeInt(slot);
        buf.writeItemStack(machine.inventory.getStack(slot));
        return buf;
    }

    public static void apply(PacketByteBuf buf, NetworkManager.PacketContext context)
    {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (context == null || player == null || player.world == null)
            return;

        int entityId = buf.readInt();
        int slot = buf.readInt();
        ItemStack itemstack = buf.readItemStack();

        context.queue(() -> execute(player, entityId, slot, itemstack));
    }

    static void execute(ClientPlayerEntity player, int entityId, int slot, ItemStack itemstack)
    {
        Entity entity = player.world.getEntityById(entityId);
        if (!(entity instanceof Machine machine))
            return;

        machine.inventory.setStack(slot, itemstack);
    }
}
