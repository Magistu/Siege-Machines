package ru.magistu.siegemachines.neoforge.plugins.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;

import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeHolder;
import ru.magistu.siegemachines.block.ModBlocks;
import ru.magistu.siegemachines.item.recipes.SiegeWorkbenchRecipe;
import ru.magistu.siegemachines.neoforge.mixin.RecipeManagerAccessForge;

import java.util.Collection;

@REIPluginClient
public class ReiClientPlugin implements REIClientPlugin {

  @Override
  public void registerCategories(CategoryRegistry registry) {
    registry.add(new SiegeWorkbenchCategory());
    registry.addWorkstations(SiegeWorkbenchCategory.ID, EntryStacks.of(ModBlocks.SIEGE_WORKBENCH.get()));
  }

  @Override
  public void registerDisplays(DisplayRegistry registry) {
    registry.registerRecipeFiller(SiegeWorkbenchRecipe.class, SiegeWorkbenchRecipe.Type.INSTANCE, SiegeWorkbenchRecipeDisplay::new);
    Collection<RecipeHolder<SiegeWorkbenchRecipe>> holders = ((RecipeManagerAccessForge)Minecraft.getInstance().level.getRecipeManager()).invokeByType(SiegeWorkbenchRecipe.Type.INSTANCE);
    for(RecipeHolder<SiegeWorkbenchRecipe> recipe : holders){
      DisplayRegistry.getInstance().add(recipe.value());
    }
  }
}