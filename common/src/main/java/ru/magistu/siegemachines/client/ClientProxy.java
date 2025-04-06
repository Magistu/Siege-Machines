package ru.magistu.siegemachines.client;

import dev.architectury.event.EventResult;
import dev.architectury.hooks.client.screen.ScreenAccess;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.CameraType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.apache.commons.lang3.NotImplementedException;
import org.lwjgl.glfw.GLFW;
import ru.magistu.siegemachines.entity.ModEntityTypes;
import ru.magistu.siegemachines.entity.machine.LadderSeat;
import ru.magistu.siegemachines.entity.machine.Machine;
import ru.magistu.siegemachines.entity.machine.SiegeLadder;
import ru.magistu.siegemachines.gui.ModMenuTypes;
import ru.magistu.siegemachines.gui.SiegeWorkbenchScreen;
import ru.magistu.siegemachines.gui.machine.crosshair.Crosshair;
import ru.magistu.siegemachines.gui.machine.crosshair.ReloadingCrosshair;
import ru.magistu.siegemachines.network.C2SPacketLadderClimb;
import ru.magistu.siegemachines.network.C2SPacketMachineUse;
import ru.magistu.siegemachines.network.ModPacketHandler;
import ru.magistu.siegemachines.network.PacketOpenMachineInventory;
import java.util.HashMap;
import java.util.Map;


@Environment(EnvType.CLIENT)
public class ClientProxy {

    public static final Map<EntityType<?>, Crosshair> CROSSHAIR_FACTORIES = new HashMap<>();

    public static EventResult guiInitPre(Screen screen, ScreenAccess access) {
        CROSSHAIR_FACTORIES.put(ModEntityTypes.BALLISTA.get(), new ReloadingCrosshair());
        CROSSHAIR_FACTORIES.put(ModEntityTypes.CULVERIN.get(), new ReloadingCrosshair());
        CROSSHAIR_FACTORIES.put(ModEntityTypes.MORTAR.get(), new ReloadingCrosshair());
        CROSSHAIR_FACTORIES.put(ModEntityTypes.CATAPULT.get(), new ReloadingCrosshair());
        CROSSHAIR_FACTORIES.put(ModEntityTypes.TREBUCHET.get(), new ReloadingCrosshair());
        return EventResult.pass();
    }

    public static void setup() {
        MenuRegistry.registerScreenFactory(ModMenuTypes.SIEGE_WORKBENCH_MENU.get(), SiegeWorkbenchScreen::new);
        setupPlatform();
    }

    @ExpectPlatform
    public static void setupPlatform() {
        throw new NotImplementedException();
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

    public static EventResult onKeyPressed(Minecraft client, int keyCode, int scanCode, int action, int modifiers) {
        if (KeyBindings.MACHINE_USE.isDown()) {
            LocalPlayer player = client.player;
            if (player != null && player.isPassenger() && player.getVehicle() instanceof Machine machine && !(machine instanceof SiegeLadder)) {
                ModPacketHandler.sendToServer(new C2SPacketMachineUse());
            }
        }

        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            LocalPlayer player = client.player;
            if (player != null && player.isPassenger() && player.getVehicle() instanceof SiegeLadder) {
                ModPacketHandler.sendToServer(new C2SPacketMachineUse());
            }
        }

        if (KeyBindings.MACHINE_INVENTORY.isDown()) {
            LocalPlayer player = client.player;
            if (player != null && player.isPassenger() && player.getVehicle() instanceof Machine) {
                ModPacketHandler.sendToServer(new PacketOpenMachineInventory());
            }
        }

        if (keyCode == GLFW.GLFW_KEY_W) {
            LocalPlayer player = client.player;
            if (player != null && player.isPassenger() && player.getVehicle() instanceof LadderSeat ladderSeat) {
                ModPacketHandler.sendToServer(new C2SPacketLadderClimb(true));
            }
        }

        if (keyCode == GLFW.GLFW_KEY_S) {
            LocalPlayer player = client.player;
            if (player != null && player.isPassenger() && player.getVehicle() instanceof LadderSeat ladderSeat) {
                ModPacketHandler.sendToServer(new C2SPacketLadderClimb(false));
            }
        }

        return EventResult.pass();
    }
}
