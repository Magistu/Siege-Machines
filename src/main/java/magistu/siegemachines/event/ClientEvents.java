package magistu.siegemachines.event;

import magistu.siegemachines.client.KeyBindings;
import magistu.siegemachines.entity.IReloading;
import magistu.siegemachines.entity.machine.Machine;
import magistu.siegemachines.gui.Crosshair;
import magistu.siegemachines.network.PacketHandler;
import magistu.siegemachines.network.PacketOpenMachineInventory;
import magistu.siegemachines.network.PacketMachineUse;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
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
        if (KeyBindings.MACHINE_USE.isDown())
        {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player != null && player.isPassenger() && player.getVehicle() instanceof Machine)
            {
                PacketHandler.sendToServer(new PacketMachineUse(player.getVehicle().getId()));
            }
        }

        if (KeyBindings.MACHINE_INVENTORY.isDown())
        {
            ClientPlayerEntity player = Minecraft.getInstance().player;
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
        if (ev.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
        {
            Minecraft mc = Minecraft.getInstance();
            GameSettings settings = mc.options;
            ClientPlayerEntity player = mc.player;

            if ((settings.renderDebug && !settings.hideGui && !player.isReducedDebugInfo() && !settings.reducedDebugInfo) || settings.getCameraType().compareTo(PointOfView.FIRST_PERSON) != 0)
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