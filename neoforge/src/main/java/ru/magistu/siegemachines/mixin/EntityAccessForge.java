package ru.magistu.siegemachines.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessForge {

    @Accessor void setIsAddedToLevel(boolean isAddedToLevel);
}
