package ru.magistu.siegemachines.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import ru.magistu.siegemachines.PacketHandlerNeoForge;
import ru.magistu.siegemachines.entity.machine.Machine;
import ru.magistu.siegemachines.mixin.EntityAccessForge;
import ru.magistu.siegemachines.network.C2SModPacket;
import ru.magistu.siegemachines.network.S2CModPacket;
import ru.magistu.siegemachines.platform.services.PlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class NeoForgePlatformHelper implements PlatformHelper {

    public static PayloadRegistrar registrar;

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public <T extends S2CModPacket<?>> void registerClientPlayPacket(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf,T> streamCodec) {
        registrar.playToClient(type, streamCodec, (p, t) -> p.handleClient());
    }

    @Override
    public <T extends C2SModPacket<?>> void registerServerPlayPacket(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        registrar.playToServer(type, streamCodec, (p, t) -> p.handleServer((ServerPlayer) t.player()));
    }

    @Override
    public void sendToClient(S2CModPacket<?> msg, ServerPlayer player) {
        PacketHandlerNeoForge.sendToClient(msg, player);
    }

    @Override
    public void sendToServer(C2SModPacket<?> msg) {
        PacketHandlerNeoForge.sendToServer(msg);
    }

    @Override
    public void onAddedToLevel(Entity entity) {
        ((EntityAccessForge)entity).setIsAddedToLevel(true);
    }

    @Override
    public void onRemovedFromLevel(Entity entity) {
        ((EntityAccessForge)entity).setIsAddedToLevel(false);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleMachineUse(int entityid) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        Entity entity = player.level().getEntity(entityid);
        if (entity instanceof Machine machine) {
            machine.use(player);
        }
    }
}