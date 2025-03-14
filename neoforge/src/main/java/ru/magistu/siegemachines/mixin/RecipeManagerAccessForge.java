package ru.magistu.siegemachines.mixin;

import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;

@Mixin(RecipeManager.class)
public interface RecipeManagerAccessForge {

    @Invoker
    <I extends RecipeInput, T extends Recipe<I>> Collection<RecipeHolder<T>> invokeByType(RecipeType<T> type);
}
