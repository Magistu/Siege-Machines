package ru.magistu.siegemachines.plugin.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeHolder;
import ru.magistu.siegemachines.block.ModBlocks;
import ru.magistu.siegemachines.item.recipes.SiegeWorkbenchRecipe;

import java.util.Collection;

public class ReiClientPlugin implements REIClientPlugin {

  @Override
  public void registerCategories(CategoryRegistry registry) {
    registry.add(new SiegeWorkbenchCategory());
    registry.addWorkstations(SiegeWorkbenchCategory.ID, EntryStacks.of(ModBlocks.SIEGE_WORKBENCH.get()));
  }

  @Override
  public void registerDisplays(DisplayRegistry registry) {
    registry.registerRecipeFiller(SiegeWorkbenchRecipe.class, SiegeWorkbenchRecipe.Type.INSTANCE, SiegeWorkbenchRecipeDisplay::new);
    Collection<RecipeHolder<SiegeWorkbenchRecipe>> holders = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(SiegeWorkbenchRecipe.Type.INSTANCE);
    for (RecipeHolder<SiegeWorkbenchRecipe> recipe : holders){
      DisplayRegistry.getInstance().add(recipe.value());
    }
  }
}