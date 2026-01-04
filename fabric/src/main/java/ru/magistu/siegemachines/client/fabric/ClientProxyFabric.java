package ru.magistu.siegemachines.client.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.CameraType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import ru.magistu.siegemachines.client.ClientProxy;
import ru.magistu.siegemachines.client.KeyBindings;
import ru.magistu.siegemachines.client.renderer.GiantArrowRenderer;
import ru.magistu.siegemachines.client.renderer.MachineGeoRenderer;
import ru.magistu.siegemachines.client.renderer.SeatRenderer;
import ru.magistu.siegemachines.client.renderer.model.*;
import ru.magistu.siegemachines.entity.ModEntityTypes;
import ru.magistu.siegemachines.entity.machine.SiegeLadder;
import ru.magistu.siegemachines.gui.machine.crosshair.Crosshair;
import ru.magistu.siegemachines.item.MachineItem;
import ru.magistu.siegemachines.item.ModItems;
import ru.magistu.siegemachines.network.C2SPacketMachineUse;
import ru.magistu.siegemachines.network.ModNetwork;
import ru.magistu.siegemachines.network.PacketOpenMachineInventory;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ClientProxyFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientProxy.setup();
        registerRenderers();
        ClientTickEvents.END_CLIENT_TICK.register(ClientProxyFabric::onKeyPressed);
        HudRenderCallback.EVENT.register(ClientProxyFabric::renderCrosshair);
    }

    private static void registerRenderers() {
        registerEntityRenderers();
        registerItemRenderers();
    }

    private static void registerEntityRenderers() {
        // Register entity renderers - matches working jar exactly
        EntityRendererRegistry.register(ModEntityTypes.CULVERIN.get(), ctx -> new MachineGeoRenderer<>(ctx, new CulverinModel(ResourceLocation.fromNamespaceAndPath("siegemachines", "culverin"))));
        EntityRendererRegistry.register(ModEntityTypes.TREBUCHET.get(), ctx -> new MachineGeoRenderer<>(ctx, new TrebuchetModel(ResourceLocation.fromNamespaceAndPath("siegemachines", "trebuchet"))));
        EntityRendererRegistry.register(ModEntityTypes.CATAPULT.get(), ctx -> new MachineGeoRenderer<>(ctx, new CatapultModel(ResourceLocation.fromNamespaceAndPath("siegemachines", "catapult"))));
        EntityRendererRegistry.register(ModEntityTypes.BALLISTA.get(), ctx -> new MachineGeoRenderer<>(ctx, new BallistaModel(ResourceLocation.fromNamespaceAndPath("siegemachines", "ballista"))));
        EntityRendererRegistry.register(ModEntityTypes.BATTERING_RAM.get(), ctx -> new MachineGeoRenderer<>(ctx, new BatteringRamGeoModel(ResourceLocation.fromNamespaceAndPath("siegemachines", "battering_ram"))));
        EntityRendererRegistry.register(ModEntityTypes.SIEGE_LADDER.get(), ctx -> new MachineGeoRenderer<>(ctx, new SiegeLadderModel(ResourceLocation.fromNamespaceAndPath("siegemachines", "siege_ladder"))));
        EntityRendererRegistry.register(ModEntityTypes.MORTAR.get(), ctx -> new MachineGeoRenderer<>(ctx, new MortarModel(ResourceLocation.fromNamespaceAndPath("siegemachines", "mortar"))));
        EntityRendererRegistry.register(ModEntityTypes.CANNONBALL.get(), ThrownItemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.GIANT_STONE.get(), ThrownItemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.STONE.get(), ThrownItemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.GIANT_ARROW.get(), GiantArrowRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.SEAT.get(), SeatRenderer::new);
    }

    private static void registerItemRenderers() {
        // Register item renderers
        for (Supplier<Item> supplier : ModItems.ITEMS) {
            Item item = supplier.get();
            if (item instanceof MachineItem<?> machineItem) {
                BuiltinItemRendererRegistry.INSTANCE.register(supplier.get(), machineItem.getRenderer()::renderByItem);
            }
        }
    }

    public static void renderCrosshair(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Options settings = mc.options;
        LocalPlayer player = mc.player;

        if (player == null || settings.hideGui || settings.getCameraType() != CameraType.FIRST_PERSON) {
            return;
        }

        Entity vehicle = player.getVehicle();
        if (vehicle != null) {
            Crosshair crosshair = ClientProxy.CROSSHAIR_FACTORIES.get(vehicle.getType());
            if (crosshair != null) {
                crosshair.render(guiGraphics, deltaTracker);
            }
        }
    }

    public static void onKeyPressed(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null) return;

        if (KeyBindings.MACHINE_USE.isDown() && !(player.getVehicle() instanceof SiegeLadder)) {
            ModNetwork.sendToServer(new C2SPacketMachineUse());
        }
        if (client.options.keyJump.isDown() && player.getVehicle() instanceof SiegeLadder) {
            ModNetwork.sendToServer(new C2SPacketMachineUse());
        }
        if (KeyBindings.MACHINE_INVENTORY.isDown()) {
            ModNetwork.sendToServer(new PacketOpenMachineInventory());
        }
    }
}
