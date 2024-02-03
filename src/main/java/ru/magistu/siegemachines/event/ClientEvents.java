package ru.magistu.siegemachines.event;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.client.KeyBindings;
import ru.magistu.siegemachines.entity.IReloading;
import ru.magistu.siegemachines.entity.machine.Machine;
import ru.magistu.siegemachines.client.gui.machine.crosshair.Crosshair;
import ru.magistu.siegemachines.network.PacketHandler;
import ru.magistu.siegemachines.network.PacketOpenMachineInventory;
import ru.magistu.siegemachines.network.PacketMachineUse;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class ClientEvents
{
    public static Crosshair CROSSHAIR = null;

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    @OnlyIn(Dist.CLIENT)
    public static void onKeyPressedEvent(InputEvent.Key ev)
    {
        if (ev.isCanceled())
            return;
        
        if (KeyBindings.MACHINE_USE.isDown())
        {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.isPassenger() && player.getVehicle() instanceof Machine machine && machine.usekey == KeyBindings.MACHINE_USE)
            {
                PacketHandler.sendToServer(new PacketMachineUse(player.getVehicle().getId()));
            }
        }

        if (KeyBindings.LADDER_CLIMB.isDown())
        {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.isPassenger() && player.getVehicle() instanceof Machine machine && machine.usekey == KeyBindings.LADDER_CLIMB)
            {
                PacketHandler.sendToServer(new PacketMachineUse(player.getVehicle().getId()));
            }
        }

        if (KeyBindings.MACHINE_INVENTORY.isDown())
        {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.isPassenger() && player.getVehicle() instanceof Machine)
            {
                PacketHandler.sendToServer(new PacketOpenMachineInventory());
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderOverlayPre(RenderGuiOverlayEvent.Pre ev) {
        if (ev.getOverlay().id() == VanillaGuiOverlay.CROSSHAIR.id()) {
            Minecraft mc = Minecraft.getInstance();
            Options settings = mc.options;
            LocalPlayer player = mc.player;

            if ((settings.renderDebug && !settings.hideGui && !player.isReducedDebugInfo() && !settings.reducedDebugInfo().get()) || settings.getCameraType().compareTo(CameraType.FIRST_PERSON) != 0)
            {
                return;
            }

            if (player.isPassenger())
            {
                Entity entity = player.getVehicle();
                if (entity instanceof IReloading)
                {
                    if (CROSSHAIR == null)
                        CROSSHAIR = ((IReloading) entity).createCrosshair();
                    
                    CROSSHAIR.render(ev.getPoseStack(), ev.getPartialTick(), mc, player);
                    ev.setCanceled(true);
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = SiegeMachines.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents 
    {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent ev) {
            ev.register(KeyBindings.MACHINE_USE);
            ev.register(KeyBindings.LADDER_CLIMB);
            ev.register(KeyBindings.MACHINE_INVENTORY);
        }
    }
}
