package ru.itzme1on.siegemachines.gui.machine.crosshair;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import ru.itzme1on.siegemachines.entity.machine.Machine;

@Environment(EnvType.CLIENT)
public class ReloadingCrosshair extends Crosshair {
    public int x, y;

    public ReloadingCrosshair() {
        super(9, 9);
        this.x = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2;
        this.y = MinecraftClient.getInstance().getWindow().getScaledHeight() / 2;
    }

    @Override
    public void render(MatrixStack matrixStack, float ticks, MinecraftClient mc, PlayerEntity player) {
        RenderSystem.assertOnRenderThread();

        if (!player.hasVehicle())
            return;

        Entity entity = player.getVehicle();

        if (entity instanceof Machine machine) {
            int width = 11;
            int height = 11;
            int imageX = 0;
            int imageY = 0;
            int originX = (mc.getWindow().getScaledWidth() - width) / 2;
            int originY = (mc.getWindow().getScaledHeight() - height) / 2;
            int animationSize = 23;
            if (machine.useTicks > 0)
                imageY = height;

            else if (machine.delayTicks > 0) {
                int number = (int) (((double) animationSize) * ((double) (machine.type.delayTime - machine.delayTicks) / (double) machine.type.delayTime));
                imageX = width;
                imageY = height * number;
            }

            RenderSystem.applyModelViewMatrix();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, CROSSHAIR_TEXTURES);

            mc.inGameHud.drawTexture(
                    matrixStack,
                    originX,
                    originY,
                    imageX,
                    imageY,
                    width,
                    height
            );

            RenderSystem.backupProjectionMatrix();
        }
    }
}
