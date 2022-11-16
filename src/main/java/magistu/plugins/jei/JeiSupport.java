package magistu.plugins.jei;

import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.data.recipes.ModRecipes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;

import java.util.Collection;
import java.util.stream.Collectors;

@JeiPlugin
public class JeiSupport implements IModPlugin
{
    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(SiegeMachines.ID, "jei_plugin");

	@Override
	public ResourceLocation getPluginUid()
	{
		return PLUGIN_ID;
	}

	@SuppressWarnings("resource")
	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

		registration.addRecipes(getRecipes(manager, ModRecipes.SIEGE_WORKBENCH_RECIPE), SiegeWorkbenchRecipeCategory.ID);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();

		registration.addRecipeCategories(new SiegeWorkbenchRecipeCategory(helper));
	}

	private static Collection<?> getRecipes(RecipeManager manager, IRecipeType<?> type)
	{
		return manager.getRecipes().parallelStream().filter(recipe -> recipe.getType() == type).collect(Collectors.toList());
	}
}
