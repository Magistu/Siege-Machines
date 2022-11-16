package magistu.plugins.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.block.ModBlocks;
import magistu.siegemachines.data.recipes.SiegeWorkbenchRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;

public class SiegeWorkbenchRecipeCategory implements IRecipeCategory<SiegeWorkbenchRecipe>
{
    public static final ResourceLocation ID = new ResourceLocation(SiegeMachines.ID, ".siege_workbench_recipe_category");

	private final IDrawable background;
	private final IDrawable overlay;
	private final IDrawable icon;

	public SiegeWorkbenchRecipeCategory(IGuiHelper helper)
    {
		this.background = helper.createBlankDrawable(116, 54);
		this.overlay = helper.createDrawable(new ResourceLocation(SiegeMachines.ID, "textures/gui/siege_workbench.png"), 29, 16, 116, 54);
		this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.SIEGE_WORKBENCH.get()));
	}

	@Override
	public @NotNull ResourceLocation getUid()
    {
		return ID;
	}

	@Override
	public @NotNull Class<? extends SiegeWorkbenchRecipe> getRecipeClass()
    {
		return SiegeWorkbenchRecipe.class;
	}

	@Override
	public @NotNull String getTitle()
    {
		return new TranslationTextComponent("category." + SiegeMachines.ID + ".siege_workbench_crafting").getString();
	}

	@Override
	public @NotNull IDrawable getBackground()
    {
		return background;
	}

	@Override
	public void draw(SiegeWorkbenchRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY)
	{
		this.overlay.draw(matrixStack, 0, 0);
	}

	@Override
	public @NotNull IDrawable getIcon()
    {
		return icon;
	}

    @Override
	public void setIngredients(SiegeWorkbenchRecipe recipe, IIngredients ingredients)
    {
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, SiegeWorkbenchRecipe recipe, IIngredients ingredients)
    {
		IGuiItemStackGroup group = recipeLayout.getItemStacks();

		int n = 0;

		group.init(n++, false, 94, 18);

		for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                group.init(n++, true, j * 18, i * 18);
            }
        }

		group.set(ingredients);
	}
}
