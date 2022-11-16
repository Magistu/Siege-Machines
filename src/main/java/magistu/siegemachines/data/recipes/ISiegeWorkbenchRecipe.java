package magistu.siegemachines.data.recipes;

import magistu.siegemachines.SiegeMachines;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public interface ISiegeWorkbenchRecipe extends IRecipe<CraftingInventory>
{
    ResourceLocation TYPE_ID = new ResourceLocation(SiegeMachines.ID, "siege_workbench");

    @Override
    default @NotNull IRecipeType<?> getType(){
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
