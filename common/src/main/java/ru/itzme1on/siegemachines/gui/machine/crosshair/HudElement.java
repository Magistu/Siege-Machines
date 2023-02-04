package ru.itzme1on.siegemachines.gui.machine.crosshair;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public abstract class HudElement {
    protected int width;
    protected int height;

    public int getAlignedX(AlignmentHelper.Alignment align, int offset) {
        return switch (align) {
            case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> 5 + offset;
            case TOP_CENTER, CENTER, BOTTOM_CENTER ->
                    MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - this.width / 2 + offset;
            case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT ->
                    MinecraftClient.getInstance().getWindow().getScaledWidth() - this.width - 5 + offset;
        };
    }

    public int getAlignedY(AlignmentHelper.Alignment align, int offset) {
        return switch (align) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> 5 + offset;
            case CENTER_LEFT, CENTER, CENTER_RIGHT ->
                    MinecraftClient.getInstance().getWindow().getScaledHeight() / 2 - this.height / 2 + 16 + offset;
            case BOTTOM_CENTER ->
                    MinecraftClient.getInstance().getWindow().getScaledHeight() - this.height - 65 + offset;
            case BOTTOM_LEFT, BOTTOM_RIGHT ->
                    MinecraftClient.getInstance().getWindow().getScaledHeight() - this.height - 5 + offset;
        };
    }

    public HudElement(int elementWidth, int elementHeight) {
        this.width = elementWidth;
        this.height = elementHeight;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public abstract void render(MatrixStack paramMatrixStack, float paramFloat);
}
