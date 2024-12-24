package ru.magistu.siegemachines.gui.machine.crosshair;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.resources.ResourceLocation;


public abstract class Crosshair extends HudElement
{
    protected static final ResourceLocation CROSSHAIR_TEXTURES = SiegeMachines.id("textures/gui/crosshairs.png");

    public Crosshair(int width, int height)
    {
        super(width, height);
    }

    public abstract void render(GuiGraphics matrixstack, DeltaTracker ticks);
}