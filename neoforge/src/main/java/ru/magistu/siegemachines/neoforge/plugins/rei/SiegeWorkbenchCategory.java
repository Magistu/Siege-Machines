package ru.magistu.siegemachines.neoforge.plugins.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;

import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.magistu.siegemachines.block.ModBlocks;
import ru.magistu.siegemachines.item.recipes.SiegeWorkbenchRecipe;

import java.util.List;

public class SiegeWorkbenchCategory implements DisplayCategory<SiegeWorkbenchRecipeDisplay> {

    public static final CategoryIdentifier<SiegeWorkbenchRecipeDisplay> ID = CategoryIdentifier.of(SiegeWorkbenchRecipe.TYPE_ID);

    @Override
    public @NotNull CategoryIdentifier<SiegeWorkbenchRecipeDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("rei.category.siegemachines.siege_workbench"); 
    }

    @Override
    public @NotNull Renderer getIcon() {
        return EntryStacks.of(new ItemStack(ModBlocks.SIEGE_WORKBENCH.get()));
    }

    @Override
    public @NotNull List<Widget> setupDisplay(SiegeWorkbenchRecipeDisplay display, Rectangle bounds) {
        List<Widget> widgets = new java.util.ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        int startX = bounds.x + 10;
        int startY = bounds.y + 10;
        
        for (int y = 0; y < display.getRecipe().pattern.height(); ++y) {
            for (int x = 0; x < display.getRecipe().pattern.width(); ++x) {
                int index = y * display.getRecipe().pattern.width() + x;
                if (index < display.getInputEntries().size()) { 
                    widgets.add(Widgets.createSlot(new Point(startX + x * 18, startY + y * 18)).entries(display.getInputEntries().get(index)).markInput());
                } else {
                    
                    widgets.add(Widgets.createSlot(new Point(startX + x * 18, startY + y * 18)).markInput());
                }
            }
        }
        
        widgets.add(Widgets.createArrow(new Point(startX + 18 * display.getRecipe().pattern.width() + 5, startY + 18)));
        
        widgets.add(Widgets.createSlot(new Point(startX + 18 * display.getRecipe().pattern.width() + 5 + 30, startY + 18)).entries(display.getOutputEntries().get(0)).markOutput());

        return widgets;
    }

    @Override
    public int getDisplayWidth(SiegeWorkbenchRecipeDisplay display) {
        return 160;
    }

    @Override
    public int getDisplayHeight() {
        return 70;
    }
}