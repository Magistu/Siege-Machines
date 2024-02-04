package ru.magistu.siegemachines.plugins.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.item.recipes.ModRecipes;
import ru.magistu.siegemachines.item.recipes.SiegeWorkbenchRecipe;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JeiSupport implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(SiegeMachines.ID, "jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new
				SiegeWorkbenchRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		List<SiegeWorkbenchRecipe> recipes = rm.getAllRecipesFor(ModRecipes.SIEGE_WORKBENCH_RECIPE);
		registration.addRecipes(new RecipeType<>(SiegeWorkbenchRecipeCategory.UID, SiegeWorkbenchRecipe.class), recipes);
	}
}