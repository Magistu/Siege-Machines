package ru.magistu.siegemachines.event;

import ru.magistu.siegemachines.client.KeyBindings;
import ru.magistu.siegemachines.entity.IReloading;
import ru.magistu.siegemachines.entity.machine.Machine;
import ru.magistu.siegemachines.gui.machine.crosshair.Crosshair;
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
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class ClientEvents
{
    public static Crosshair crosshair = null;

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    @OnlyIn(Dist.CLIENT)
    public static void onKeyPressedEvent(InputEvent.KeyInputEvent ev)
    {
        if (ev.isCanceled())
            return;
        
        if (KeyBindings.MACHINE_USE.isDown())
        {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.isPassenger() && player.getVehicle() instanceof Machine)
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
    public static void onRenderOverlayPre(RenderGameOverlayEvent.Pre ev)
    {
        if (ev.getType() == RenderGameOverlayEvent.ElementType.LAYER)
        {
            Minecraft mc = Minecraft.getInstance();
            Options settings = mc.options;
            LocalPlayer player = mc.player;

            if ((settings.renderDebug && !settings.hideGui && !player.isReducedDebugInfo() && !settings.reducedDebugInfo) || settings.getCameraType().compareTo(CameraType.FIRST_PERSON) != 0)
            {
                return;
            }

            if (player.isPassenger())
            {
                Entity entity = player.getVehicle();
                if (entity instanceof IReloading)
                {
                    if (crosshair == null)
                    {
                        crosshair = ((IReloading) entity).createCrosshair();
                    }
                    crosshair.render(ev.getMatrixStack(), ev.getPartialTicks(), mc, player);
                    ev.setCanceled(true);
                }
            }
        }
    }
}
