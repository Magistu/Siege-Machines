package ru.magistu.siegemachines.neoforge.platform;

import net.minecraft.world.entity.Entity;
import ru.magistu.siegemachines.neoforge.mixin.EntityAccessForge;
import ru.magistu.siegemachines.platform.services.IPlatformHelper;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public void onAddedToLevel(Entity entity) {
        ((EntityAccessForge)entity).setIsAddedToLevel(true);
    }
}