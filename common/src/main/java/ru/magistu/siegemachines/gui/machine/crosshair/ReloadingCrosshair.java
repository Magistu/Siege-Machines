package ru.magistu.siegemachines.gui.machine.crosshair;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import ru.magistu.siegemachines.entity.machine.Machine;

public class ReloadingCrosshair extends Crosshair {
    // Don't access Minecraft window during initialization - compute lazily
    public int x = 0;
    public int y = 0;

    public ReloadingCrosshair() {
        super(9, 9);
    }

    @Override
    public void render(GuiGraphics matrixstack, DeltaTracker ticks) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || !player.isPassenger()) {
            return;
        }

        // Update x/y lazily when we actually need them
        if (mc.getWindow() != null) {
            this.x = mc.getWindow().getGuiScaledWidth() / 2;
            this.y = mc.getWindow().getGuiScaledHeight() / 2;
        }

        Entity entity = player.getVehicle();
        if (entity instanceof Machine) {
            Machine machine = (Machine)entity;
            int width = 11;
            int height = 11;
            int imagex = 0;
            int imagey = 0;
            int originx = (matrixstack.guiWidth() - width) / 2;
            int originy = (matrixstack.guiHeight() - height) / 2;
            int animationsize = 23;
            if (machine.getUseTicks() > 0) {
                imagey = height;
            } else if (machine.getDelayTicks() > 0) {
                int delayTime = machine.type.specs.delaytime.get();
                int number = (int)((double)animationsize * ((double)(delayTime - machine.getDelayTicks()) / (double)delayTime));
                imagex = width;
                imagey = height * number;
            }
            RenderSystem.applyModelViewMatrix();
            RenderSystem.setShaderTexture(0, CROSSHAIR_TEXTURES);
            matrixstack.blit(CROSSHAIR_TEXTURES, originx, originy, imagex, imagey, width, height);
            RenderSystem.backupProjectionMatrix();
        }
    }
}
