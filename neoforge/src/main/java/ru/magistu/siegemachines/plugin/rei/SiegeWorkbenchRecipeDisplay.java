package ru.magistu.siegemachines.plugin.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;
import ru.magistu.siegemachines.item.recipes.SiegeWorkbenchRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SiegeWorkbenchRecipeDisplay implements Display {
    private final SiegeWorkbenchRecipe recipe;
    private final List<EntryIngredient> inputEntries;
    private final List<EntryIngredient> outputEntries;

    public SiegeWorkbenchRecipeDisplay(RecipeHolder<SiegeWorkbenchRecipe> recipe) {
        this.recipe = recipe.value();
        this.inputEntries = new ArrayList<>();
        for (Ingredient ingredient : recipe.value().pattern.ingredients()) {
            inputEntries.add(EntryIngredients.ofIngredient(ingredient));
        }
        this.outputEntries = Collections.singletonList(EntryIngredients.of(recipe.value().getResultItem(null)));
    }

    public SiegeWorkbenchRecipe getRecipe() {
        return recipe;
    }

    @Override
    public @NotNull List<EntryIngredient> getInputEntries() {
        return inputEntries;
    }

    @Override
    public @NotNull List<EntryIngredient> getOutputEntries() {
        return outputEntries;
    }

    @Override
    public @NotNull CategoryIdentifier<?> getCategoryIdentifier() {
        return SiegeWorkbenchCategory.ID;
    }
}