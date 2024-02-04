package ru.magistu.siegemachines.client.gui.machine.crosshair;

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

    public int getAlignedY(AlignmentHelper.Alignment align, int offset)
    {
        switch (align)
        {
            case TOP_LEFT:
            case TOP_CENTER:
            case TOP_RIGHT:
                return 5 + offset;
            case CENTER_LEFT:
            case CENTER:
            case CENTER_RIGHT:
                return Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - this.height / 2 + 16 + offset;
            case BOTTOM_CENTER:
                return Minecraft.getInstance().getWindow().getGuiScaledHeight() - this.height - 65 + offset;
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                return Minecraft.getInstance().getWindow().getGuiScaledHeight() - this.height - 5 + offset;
        }
        return 0;
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