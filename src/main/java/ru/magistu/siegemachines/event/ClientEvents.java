//package ru.magistu.siegemachines.event;
//
//import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
//import net.minecraftforge.client.event.RenderGuiOverlayEvent;
//import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
//import ru.magistu.siegemachines.SiegeMachines;
//import ru.magistu.siegemachines.client.KeyBindings;
//import ru.magistu.siegemachines.entity.Reloading;
//import ru.magistu.siegemachines.entity.machine.Machine;
//import ru.magistu.siegemachines.client.gui.machine.crosshair.Crosshair;
//import ru.magistu.siegemachines.entity.machine.SiegeLadder;
//import ru.magistu.siegemachines.network.C2SPacketMachineUse;
//import ru.magistu.siegemachines.network.ModNetwork;
//import ru.magistu.siegemachines.network.PacketOpenMachineInventory;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.Options;
//import net.minecraft.client.player.LocalPlayer;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.client.event.InputEvent;
//import net.minecraftforge.eventbus.api.EventPriority;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//
//@Mod.EventBusSubscriber
//public class ClientEvents
//{
//    public static Crosshair CROSSHAIR = null;
//
//    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
//    @OnlyIn(Dist.CLIENT)
//    public static void onKeyPressedEvent(InputEvent.Key ev)
//    {
//        if (ev.isCanceled())
//            return;
//
//        if (KeyBindings.MACHINE_USE.isDown())
//        {
//            LocalPlayer player = Minecraft.getInstance().player;
//            if (player != null && player.isPassenger() && player.getVehicle() instanceof Machine machine && !(machine instanceof SiegeLadder))
//            {
//                ModNetwork.sendToServer(new C2SPacketMachineUse());
//            }
//        }
//
//        if (KeyBindings.LADDER_CLIMB.isDown())
//        {
//            LocalPlayer player = Minecraft.getInstance().player;
//            if (player != null && player.isPassenger() && player.getVehicle() instanceof Machine)
//            {
//                ModNetwork.sendToServer(new C2SPacketMachineUse());
//            }
//        }
//
//        if (KeyBindings.MACHINE_INVENTORY.isDown())
//        {
//            LocalPlayer player = Minecraft.getInstance().player;
//            if (player != null && player.isPassenger() && player.getVehicle() instanceof Machine)
//            {
//                ModNetwork.sendToServer(new PacketOpenMachineInventory());
//            }
//        }
//    }
//
//    @SubscribeEvent
//    @OnlyIn(Dist.CLIENT)
//    public static void onRenderOverlayPre(RenderGuiOverlayEvent.Pre ev) {
//        if (ev.getOverlay().id() == VanillaGuiOverlay.CROSSHAIR.id()) {
//            Minecraft mc = Minecraft.getInstance();
//            Options settings = mc.options;
//            LocalPlayer player = mc.player;
//
//            boolean debugScreenVisible = settings.renderDebug && !settings.hideGui &&
//                    !player.isReducedDebugInfo() && !settings.reducedDebugInfo().get();
//
//            boolean thirdPerson = !settings.getCameraType().isFirstPerson();
//
//            if (debugScreenVisible || thirdPerson) {
//                return;
//            }
//
//            if (player.getVehicle() instanceof Reloading) {
//                CROSSHAIR.render(ev.getGuiGraphics(), ev.getPartialTick(), mc, player);
//                ev.setCanceled(true);
//            }
//        }
//    }
//
//    @Mod.EventBusSubscriber(modid = SiegeMachines.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
//    public static class ClientModBusEvents
//    {
//        @SubscribeEvent
//        public static void onKeyRegister(RegisterKeyMappingsEvent ev) {
//            ev.register(KeyBindings.MACHINE_USE);
//            ev.register(KeyBindings.LADDER_CLIMB);
//            ev.register(KeyBindings.MACHINE_INVENTORY);
//        }
//    }
//}
