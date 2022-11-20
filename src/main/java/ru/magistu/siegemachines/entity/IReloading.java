package ru.magistu.siegemachines.entity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ru.magistu.siegemachines.gui.Crosshair;

public interface IReloading
{
    @OnlyIn(Dist.CLIENT)
    Crosshair createCrosshair();
}
