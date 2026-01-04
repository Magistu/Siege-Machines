package ru.magistu.siegemachines.fabric.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import ru.magistu.siegemachines.entity.machine.Machine;
import ru.magistu.siegemachines.network.C2SModPacket;
import ru.magistu.siegemachines.network.S2CModPacket;
import ru.magistu.siegemachines.platform.services.PlatformHelper;

public class FabricPlatformHelper implements PlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <T extends S2CModPacket<?>> void registerClientPlayPacket(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        PayloadTypeRegistry.playS2C().register(type, codec);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPlayNetworking.registerGlobalReceiver(type, (packet, context) -> {
                context.client().execute(() -> packet.handleClient());
            });
        }
    }

    @Override
    public <T extends C2SModPacket<?>> void registerServerPlayPacket(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> codec) {

        PayloadTypeRegistry.playC2S().register(type, codec);

        ServerPlayNetworking.registerGlobalReceiver(type, (packet, context) -> {
            context.server().execute(() -> packet.handleServer(context.player()));
        });
    }

    @Override
    public void sendToClient(S2CModPacket<?> msg, ServerPlayer player) {
        ServerPlayNetworking.send(player, msg);
    }

    @Override
    public void sendToServer(C2SModPacket<?> msg) {
        ClientPlayNetworking.send(msg);
    }

    @Override
    public void onAddedToLevel(Entity entity) {
        // Entity lifecycle is managed by Minecraft - no action needed
    }

    @Override
    public void onRemovedFromLevel(Entity entity) {
        // Entity lifecycle is managed by Minecraft - no action needed
    }

    @Override
    @Environment(EnvType.CLIENT)
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
