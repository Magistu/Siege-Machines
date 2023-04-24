package ru.itzme1on.siegemachines.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.itzme1on.siegemachines.client.gui.machine.crosshair.Crosshair;

public interface IReloading {
    @Environment(EnvType.CLIENT)
    Crosshair createCrosshair();
}
