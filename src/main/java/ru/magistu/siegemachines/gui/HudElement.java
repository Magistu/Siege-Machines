package ru.magistu.siegemachines.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class HudElement
{
    protected int width;
    protected int height;

    public int getAlignedX(AlignmentHelper.Alignment align, int offset)
    {
        switch (align)
        {
            case TOP_LEFT:
            case CENTER_LEFT:
            case BOTTOM_LEFT:
                return 5 + offset;
            case TOP_CENTER:
            case CENTER:
            case BOTTOM_CENTER:
                return Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - this.width / 2 + offset;
            case TOP_RIGHT:
            case CENTER_RIGHT:
            case BOTTOM_RIGHT:
                return Minecraft.getInstance().getWindow().getGuiScaledWidth() - this.width - 5 + offset;
        }
        return 0;
    }

    public int getAlignedY(AlignmentHelper.Alignment align, int offset) {
        return switch (align) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> 5 + offset;
            case CENTER_LEFT, CENTER, CENTER_RIGHT ->
                    Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - this.height / 2 + 16 + offset;
            case BOTTOM_CENTER -> Minecraft.getInstance().getWindow().getGuiScaledHeight() - this.height - 65 + offset;
            case BOTTOM_LEFT, BOTTOM_RIGHT ->
                    Minecraft.getInstance().getWindow().getGuiScaledHeight() - this.height - 5 + offset;
        };
    }

    public HudElement(int elementWidth, int elementHeight)
    {
        this.width = elementWidth;
        this.height = elementHeight;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public abstract void render(PoseStack paramMatrixStack, float paramFloat);
}