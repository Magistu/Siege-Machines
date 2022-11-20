package ru.magistu.siegemachines.data.recipes;

import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public interface ISiegeWorkbenchRecipe extends Recipe<CraftingContainer>
{
    ResourceLocation TYPE_ID = new ResourceLocation(SiegeMachines.MOD_ID, "siege_workbench");

    @Override
    default @NotNull RecipeType<?> getType(){
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }

    @Override
    default boolean canCraftInDimensions(int width, int height)
    {
        return true;
    }

    @Override
    default boolean isSpecial()
    {
        return true;
    }
}
