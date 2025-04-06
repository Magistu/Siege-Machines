package ru.magistu.siegemachines.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.magistu.siegemachines.client.ClientProxy;

@Mixin(Gui.class)
public abstract class MixinGui {

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void onRenderCrosshair(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo callback) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Entity vehicle = player.getVehicle();
            if (vehicle != null && ClientProxy.CROSSHAIR_FACTORIES.containsKey(vehicle.getType())) {
                callback.cancel();
            }
        }
    }
}
