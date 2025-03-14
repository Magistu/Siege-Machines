package ru.magistu.siegemachines.platform;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import ru.magistu.siegemachines.network.C2SModPacket;
import ru.magistu.siegemachines.network.S2CModPacket;
import ru.magistu.siegemachines.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformHelper implements IPlatformHelper {

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
    public <T extends S2CModPacket<?>> void registerClientPlayPacket(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        // TODO: implement
    }

    @Override
    public <T extends C2SModPacket<?>> void registerServerPlayPacket(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        // TODO: implement
    }

    @Override
    public void sendToClient(S2CModPacket<?> msg, ServerPlayer player) {
        // TODO: implement
    }

    @Override
    public void sendToServer(C2SModPacket<?> msg) {
        // TODO: implement
    }

    @Override
    public void onAddedToLevel(Entity entity) {
        // TODO: implement
    }
}
