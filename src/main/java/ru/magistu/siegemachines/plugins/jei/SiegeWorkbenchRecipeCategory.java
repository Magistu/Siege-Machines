package ru.magistu.siegemachines.plugins.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.block.ModBlocks;
import ru.magistu.siegemachines.data.recipes.SiegeWorkbenchRecipe;

import javax.annotation.Nonnull;

public class SiegeWorkbenchRecipeCategory implements IRecipeCategory<SiegeWorkbenchRecipe> {
	public final static ResourceLocation UID = new ResourceLocation(SiegeMachines.ID, "siege_workbench_recipe_category");
	public final static ResourceLocation TEXTURE = new ResourceLocation(SiegeMachines.ID, "textures/gui/siege_workbench.png");

	private final IDrawable background;
	private final IDrawable icon;

	public SiegeWorkbenchRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 29, 16, 116, 54);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SIEGE_WORKBENCH.get()));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends SiegeWorkbenchRecipe> getRecipeClass() {
		return SiegeWorkbenchRecipe.class;
	}

	@Override
	public Component getTitle() {
		return new TranslatableComponent("category." + SiegeMachines.ID + ".siege_workbench_crafting");
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull SiegeWorkbenchRecipe recipe, @Nonnull IFocusGroup focusGroup) 
	{
		builder.addSlot(RecipeIngredientRole.OUTPUT, 94, 18).addItemStack(recipe.getResultItem());
		
		for (int i = 0; i < recipe.getWidth(); ++i)
		{
			for (int j = 0; j < recipe.getHeight(); ++j)
			{
				builder.addSlot(RecipeIngredientRole.INPUT, j * 18, i * 18).addItemStacks(recipe.getRecipeItems().get(j + recipe.getWidth() * i).getCountModifiedItemStacks());
			}
		}
	}
}
