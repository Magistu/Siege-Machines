package magistu.siegemachines.entity;

import magistu.siegemachines.gui.Crosshair;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IReloading
{
    @OnlyIn(Dist.CLIENT)
    Crosshair createCrosshair();
}
