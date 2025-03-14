package ru.magistu.siegemachines.gui;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import ru.magistu.siegemachines.item.recipes.ModRecipeSerializers;
import ru.magistu.siegemachines.item.recipes.SiegeWorkbenchRecipe;
import java.util.Optional;

public class SiegeWorkbenchResultSlot extends Slot {
    private final CraftingContainer craftSlots;
    private final Player player;
    private int removeCount;

    public SiegeWorkbenchResultSlot(Player player, CraftingContainer craftSlots, Container container, int slot, int xPosition, int yPosition) {
        super(container, slot, xPosition, yPosition);
        this.player = player;
        this.craftSlots = craftSlots;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void onSwapCraft(int numItemsCrafted) {
        this.removeCount += numItemsCrafted;
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        if (this.removeCount > 0) {
            stack.onCraftedBy(this.player.level(), this.player, this.removeCount);
        }

        if (this.container instanceof RecipeCraftingHolder recipecraftingholder) {
            recipecraftingholder.awardUsedRecipes(this.player, this.craftSlots.getItems());
        }

        this.removeCount = 0;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        CraftingInput.Positioned positioned = this.craftSlots.asPositionedCraftInput();
        CraftingInput craftinginput = positioned.input();
        int i = positioned.left();
        int j = positioned.top();
        RecipeManager manager = player.level().getRecipeManager();
        NonNullList<ItemStack> nonnulllist = manager.getRemainingItemsFor(ModRecipeSerializers.SIEGE_WORKBENCH_RECIPE, craftinginput, player.level());
        Optional<RecipeHolder<SiegeWorkbenchRecipe>> recipe = manager.getRecipeFor(ModRecipeSerializers.SIEGE_WORKBENCH_RECIPE, craftinginput, player.level());
        if(recipe.isEmpty()) return;

        for (int k = 0; k < craftinginput.height(); k++) {
            for (int l = 0; l < craftinginput.width(); l++) {
                int i1 = l + i + (k + j) * this.craftSlots.getWidth();
                ItemStack itemstack = this.craftSlots.getItem(i1);
                ItemStack itemstack1 = nonnulllist.get(l + k * craftinginput.width());
                Ingredient recipeElement = recipe.get().value().getIngredients().get(l + k * craftinginput.width());
                if (!itemstack.isEmpty() && recipeElement.getItems().length != 0) {
                    this.craftSlots.removeItem(i1, recipeElement.getItems()[0].getCount());
                    itemstack = this.craftSlots.getItem(i);
                }

                if (!itemstack1.isEmpty()) {
                    if (itemstack.isEmpty()) {
                        this.craftSlots.setItem(i1, itemstack1);
                    } else if (ItemStack.isSameItemSameComponents(itemstack, itemstack1)) {
                        itemstack1.grow(itemstack.getCount());
                        this.craftSlots.setItem(i1, itemstack1);
                    } else if (!this.player.getInventory().add(itemstack1)) {
                        this.player.drop(itemstack1, false);
                    }
                }
            }
        }
    }
}
