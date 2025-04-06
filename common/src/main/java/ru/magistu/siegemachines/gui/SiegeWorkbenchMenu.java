package ru.magistu.siegemachines.gui;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import ru.magistu.siegemachines.item.recipes.ModRecipeSerializers;
import ru.magistu.siegemachines.item.recipes.SiegeWorkbenchRecipe;

public class SiegeWorkbenchMenu extends RecipeBookMenu<CraftingInput, SiegeWorkbenchRecipe> {
    private final CraftingContainer craftSlots;
    private final ResultContainer resultSlots;
    private final ContainerLevelAccess access;
    private final Player player;
    private boolean placingRecipe;

    public SiegeWorkbenchMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public SiegeWorkbenchMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenuTypes.SIEGE_WORKBENCH_MENU.get(), containerId);
        this.craftSlots = new TransientCraftingContainer(this, 3, 3);
        this.resultSlots = new ResultContainer();
        this.access = access;
        this.player = playerInventory.player;
        this.addSlot(new SiegeWorkbenchResultSlot(playerInventory.player, this.craftSlots, this.resultSlots, 0, 124, 35));

        int l;
        int i1;
        for(l = 0; l < 3; ++l) {
            for(i1 = 0; i1 < 3; ++i1) {
                this.addSlot(new Slot(this.craftSlots, i1 + l * 3, 30 + i1 * 18, 17 + l * 18));
            }
        }

        for(l = 0; l < 3; ++l) {
            for(i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInventory, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }

        for(l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
        }

    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, CraftingContainer craftSlots, ResultContainer resultSlots, @Nullable RecipeHolder<SiegeWorkbenchRecipe> recipe) {
        if (!level.isClientSide) {
            CraftingInput craftinginput = craftSlots.asCraftInput();
            ServerPlayer serverplayer = (ServerPlayer)player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<RecipeHolder<SiegeWorkbenchRecipe>> optional = level.getServer().getRecipeManager().getRecipeFor(ModRecipeSerializers.SIEGE_WORKBENCH_RECIPE, craftinginput, level, recipe);
            if (optional.isPresent()) {
                RecipeHolder<SiegeWorkbenchRecipe> recipeholder = (RecipeHolder)optional.get();
                SiegeWorkbenchRecipe SiegeWorkbenchRecipe = (SiegeWorkbenchRecipe)recipeholder.value();
                if (resultSlots.setRecipeUsed(level, serverplayer, recipeholder)) {
                    ItemStack itemstack1 = SiegeWorkbenchRecipe.assemble(craftinginput, level.registryAccess());
                    if (itemstack1.isItemEnabled(level.enabledFeatures())) {
                        itemstack = itemstack1;
                    }
                }
            }

            resultSlots.setItem(0, itemstack);
            menu.setRemoteSlot(0, itemstack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemstack));
        }

    }

    public void slotsChanged(Container inventory) {
        if (!this.placingRecipe) {
            this.access.execute((p_344363_, p_344364_) -> {
                slotChangedCraftingGrid(this, p_344363_, this.player, this.craftSlots, this.resultSlots, (RecipeHolder)null);
            });
        }

    }

    public void beginPlacingRecipe() {
        this.placingRecipe = true;
    }

    public void finishPlacingRecipe(RecipeHolder<SiegeWorkbenchRecipe> recipe) {
        this.placingRecipe = false;
        this.access.execute((p_344361_, p_344362_) -> {
            slotChangedCraftingGrid(this, p_344361_, this.player, this.craftSlots, this.resultSlots, recipe);
        });
    }

    public void fillCraftSlotsStackedContents(StackedContents itemHelper) {
        this.craftSlots.fillStackedContents(itemHelper);
    }

    public void clearCraftingContent() {
        this.craftSlots.clearContent();
        this.resultSlots.clearContent();
    }

    public boolean recipeMatches(RecipeHolder<SiegeWorkbenchRecipe> recipe) {
        return recipe.value().matches(this.craftSlots.asCraftInput(), this.player.level());
    }

    public void removed(Player player) {
        super.removed(player);
        this.access.execute((p_39371_, p_39372_) -> {
            this.clearContainer(player, this.craftSlots);
        });
    }

    public boolean stillValid(Player player) {
        return true;
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 0) {
                this.access.execute((p_39378_, p_39379_) -> {
                    itemstack1.getItem().onCraftedBy(itemstack1, p_39378_, player);
                });
                if (!this.moveItemStackTo(itemstack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 10 && index < 46) {
                if (!this.moveItemStackTo(itemstack1, 1, 10, false)) {
                    if (index < 37) {
                        if (!this.moveItemStackTo(itemstack1, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            if (index == 0) {
                player.drop(itemstack1, false);
            }
        }

        return itemstack;
    }

    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }

    public int getResultSlotIndex() {
        return 0;
    }

    public int getGridWidth() {
        return this.craftSlots.getWidth();
    }

    public int getGridHeight() {
        return this.craftSlots.getHeight();
    }

    public int getSize() {
        return 10;
    }

    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    public boolean shouldMoveToInventory(int slotIndex) {
        return slotIndex != this.getResultSlotIndex();
    }
}
