package ru.magistu.siegemachines.entity;

import ru.magistu.siegemachines.client.gui.machine.crosshair.Crosshair;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IReloading
{
    @OnlyIn(Dist.CLIENT)
    Crosshair createCrosshair();
}
