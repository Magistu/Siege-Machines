package ru.magistu.siegemachines.gui;

import com.google.common.collect.Sets;
import ru.magistu.siegemachines.block.ModBlocks;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public class SiegeWorkbenchContainer extends AbstractContainerMenu
{
    private final CraftingContainer craftSlots = new CraftingContainer(this, 3, 3);
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final Player player;

    private int quickcraftType = -1;
    private int quickcraftStatus;
    private final Set<Slot> quickcraftSlots = Sets.newHashSet();

    public SiegeWorkbenchContainer(int p_i50089_1_, Inventory p_i50089_2_, FriendlyByteBuf buffer)
    {
        this(p_i50089_1_, p_i50089_2_, ContainerLevelAccess.NULL);
    }

    public SiegeWorkbenchContainer(int p_i50090_1_, Inventory p_i50090_2_, ContainerLevelAccess p_i50090_3_)
    {
        super(ContainerTypes.SIEGE_WORKBENCH_CONTAINER.get(), p_i50090_1_);
        this.access = p_i50090_3_;
        this.player = p_i50090_2_.player;
        this.addSlot(new ResultSlot(p_i50090_2_.player, this.craftSlots, this.resultSlots, 0, 124, 35));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                this.addSlot(new Slot(this.craftSlots, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        for (int k = 0; k < 3; ++k)
        {
            for (int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlot(new Slot(p_i50090_2_, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l)
        {
            this.addSlot(new Slot(p_i50090_2_, l, 8 + l * 18, 142));
        }

    }

    // to create resulting item
    protected static void slotChangedCraftingGrid(AbstractContainerMenu p_150547_, int p_217066_0_, Level p_217066_1_, Player p_217066_2_, CraftingContainer p_217066_3_, ResultContainer p_217066_4_)
    {
//        if (!p_217066_1_.isClientSide)
//        {
//            ServerPlayer serverplayerentity = (ServerPlayer) p_217066_2_;
//            ItemStack itemstack = ItemStack.EMPTY;
//            Optional<SiegeWorkbenchRecipe> optional = p_217066_1_.getServer().getRecipeManager().getRecipeFor(ModRecipes.SIEGE_WORKBENCH_SERIALIZER, p_217066_3_, p_217066_1_);
//            if (optional.isPresent())
//            {
//                SiegeWorkbenchRecipe icraftingrecipe = optional.get();
//                if (p_217066_4_.setRecipeUsed(p_217066_1_, serverplayerentity, icraftingrecipe))
//                {
//                    itemstack = icraftingrecipe.assemble(p_217066_3_);
//                }
//            }
//
//            p_217066_4_.setItem(0, itemstack);
//            serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(p_150547_.containerId, p_217066_0_, 0, itemstack));
//        }
    }

    @Override
    public void slotsChanged(@NotNull Container p_75130_1_)
    {
        this.access.execute((p_217069_1_, p_217069_2_) -> slotChangedCraftingGrid(this, this.containerId, p_217069_1_, this.player, this.craftSlots, this.resultSlots));
    }

    @Override
    public void removed(@NotNull Player p_75134_1_)
    {
        super.removed(p_75134_1_);
        this.access.execute((p_217068_2_, p_217068_3_) -> this.clearContainer(p_75134_1_, this.craftSlots));
    }

    @Override
    public boolean stillValid(Player p_75145_1_)
    {
        return stillValid(this.access, p_75145_1_, ModBlocks.SIEGE_WORKBENCH.get());
    }


    public void onTakeFromSlot(Slot slot, Player p_190901_1_, ItemStack p_190901_2_)
    {
        if (slot instanceof ResultSlot)
        {
            this.onCraft(p_190901_1_);
        }
        slot.onTake(p_190901_1_, p_190901_2_);
    }

    public <C extends Container, T extends Recipe<C>> NonNullList<ItemStack> getRemainingItemsFor(RecipeType<T> p_215369_1_, C p_215369_2_, Level p_215369_3_)
    {
        Optional<T> optional = p_215369_3_.getRecipeManager().getRecipeFor(p_215369_1_, p_215369_2_, p_215369_3_);
        if (optional.isPresent())
        {
            return optional.get().getRemainingItems(p_215369_2_);
        }
        else
        {
            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(p_215369_2_.getContainerSize(), ItemStack.EMPTY);
            for (int i = 0; i < nonnulllist.size(); ++i)
            {
                nonnulllist.set(i, p_215369_2_.getItem(i));
            }

            return nonnulllist;
        }
    }

    public void onCraft(Player p_190901_1_)
    {
        //slot.checkTakeAchievements(p_190901_2_);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(p_190901_1_);
        NonNullList<ItemStack> nonnulllist = this.getRemainingItemsFor(RecipeType.CRAFTING, this.craftSlots, p_190901_1_.level);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
        for(int i = 0; i < nonnulllist.size(); ++i)
        {
            ItemStack itemstack = this.craftSlots.getItem(i);
            ItemStack itemstack1 = nonnulllist.get(i);
            if (!itemstack.isEmpty())
            {
                this.craftSlots.removeItem(i, 64);
                itemstack = this.craftSlots.getItem(i);
            }

            if (!itemstack1.isEmpty())
            {
                if (itemstack.isEmpty())
                {
                    this.craftSlots.setItem(i, itemstack1);
                }
                else if (ItemStack.isSame(itemstack, itemstack1) && ItemStack.tagMatches(itemstack, itemstack1))
                {
                    itemstack1.grow(itemstack.getCount());
                    this.craftSlots.setItem(i, itemstack1);
                }
                else if (!this.player.getInventory().add(itemstack1))
                {
                    this.player.drop(itemstack1, false);
                }
            }
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player p_82846_1_, int p_82846_2_)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_82846_2_);
        if (slot != null && slot.hasItem())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (p_82846_2_ == 0)
            {
                this.access.execute((p_217067_2_, p_217067_3_) -> itemstack1.getItem().onCraftedBy(itemstack1, p_217067_2_, p_82846_1_));
                if (!this.moveItemStackTo(itemstack1, 10, 46, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            }
            else if (p_82846_2_ >= 10 && p_82846_2_ < 46)
            {
                if (!this.moveItemStackTo(itemstack1, 1, 10, false))
                {
                    if (p_82846_2_ < 37)
                    {
                        if (!this.moveItemStackTo(itemstack1, 37, 46, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(itemstack1, 10, 37, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if (!this.moveItemStackTo(itemstack1, 10, 46, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            this.onTakeFromSlot(slot, p_82846_1_, itemstack1);
            if (p_82846_2_ == 0)
            {
                p_82846_1_.drop(itemstack1, false);
            }
        }

        return itemstack;
    }

    public boolean canTakeItemForPickAll(ItemStack p_94530_1_, Slot p_94530_2_)
    {
        return p_94530_2_.container != this.resultSlots && super.canTakeItemForPickAll(p_94530_1_, p_94530_2_);
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize()
    {
        return 10;
    }

    @Override
    public void clicked(int p_184996_1_, int p_184996_2_, ClickType p_184996_3_, Player p_184996_4_)
    {
        try
        {
            this.doClick(p_184996_1_, p_184996_2_, p_184996_3_, p_184996_4_);
            this.slotsChanged(this.craftSlots);
            this.slotsChanged(this.resultSlots);
        }
        catch (Exception exception)
        {
            CrashReport crashreport = CrashReport.forThrowable(exception, "Container click");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Click info");
            crashreportcategory.setDetail("Menu Class", () -> this.getClass().getCanonicalName());
            crashreportcategory.setDetail("Slot Count", this.slots.size());
            crashreportcategory.setDetail("Slot", p_184996_1_);
            crashreportcategory.setDetail("Button", p_184996_2_);
            crashreportcategory.setDetail("Type", p_184996_3_);
            throw new ReportedException(crashreport);
        }
    }

    @Override
    protected void resetQuickCraft()
    {
        this.quickcraftStatus = 0;
        this.quickcraftSlots.clear();
    }

    private ItemStack doClick(int index1, int index2, ClickType clicktype, Player player)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Inventory playerinventory = player.getInventory();
        if (clicktype == ClickType.QUICK_CRAFT)
        {
           int i1 = this.quickcraftStatus;
           this.quickcraftStatus = getQuickcraftHeader(index2);
           if ((i1 != 1 || this.quickcraftStatus != 2) && i1 != this.quickcraftStatus)
           {
              this.resetQuickCraft();
           }
           else if (playerinventory.getSelected().isEmpty())
           {
              this.resetQuickCraft();
           }
           else if (this.quickcraftStatus == 0)
           {
              this.quickcraftType = getQuickcraftType(index2);
              if (isValidQuickcraftType(this.quickcraftType, player))
              {
                 this.quickcraftStatus = 1;
                 this.quickcraftSlots.clear();
              }
              else
              {
                 this.resetQuickCraft();
              }
           }
           else if (this.quickcraftStatus == 1)
           {
              Slot slot7 = this.slots.get(index1);
              ItemStack itemstack12 = playerinventory.getSelected();
              if (slot7 != null && canItemQuickReplace(slot7, itemstack12, true) && slot7.mayPlace(itemstack12) && (this.quickcraftType == 2 || itemstack12.getCount() > this.quickcraftSlots.size()) && this.canDragTo(slot7))
              {
                 this.quickcraftSlots.add(slot7);
              }
           }
           else if (this.quickcraftStatus == 2)
           {
              if (!this.quickcraftSlots.isEmpty())
              {
                 ItemStack itemstack10 = playerinventory.getSelected().copy();
                 int k1 = playerinventory.getSelected().getCount();

                 for(Slot slot8 : this.quickcraftSlots)
                 {
                    ItemStack itemstack13 = playerinventory.getSelected();
                    if (slot8 != null && canItemQuickReplace(slot8, itemstack13, true) && slot8.mayPlace(itemstack13) && (this.quickcraftType == 2 || itemstack13.getCount() >= this.quickcraftSlots.size()) && this.canDragTo(slot8))
                    {
                       ItemStack itemstack14 = itemstack10.copy();
                       int j3 = slot8.hasItem() ? slot8.getItem().getCount() : 0;
                       getQuickCraftSlotCount(this.quickcraftSlots, this.quickcraftType, itemstack14, j3);
                       int k3 = Math.min(itemstack14.getMaxStackSize(), slot8.getMaxStackSize(itemstack14));
                       if (itemstack14.getCount() > k3)
                       {
                          itemstack14.setCount(k3);
                       }

                       k1 -= itemstack14.getCount() - j3;
                       slot8.set(itemstack14);
                    }
                 }

                 itemstack10.setCount(k1);
                 playerinventory.setPickedItem(itemstack10);
              }

              this.resetQuickCraft();
           }
           else
           {
              this.resetQuickCraft();
           }
        }
        else if (this.quickcraftStatus != 0)
        {
           this.resetQuickCraft();
        }
        else if ((clicktype == ClickType.PICKUP || clicktype == ClickType.QUICK_MOVE) && (index2 == 0 || index2 == 1))
        {
           if (index1 == -999)
           {
              if (!playerinventory.getSelected().isEmpty())
              {
                 if (index2 == 0)
                 {
                    player.drop(playerinventory.getSelected(), true);
                    playerinventory.setPickedItem(ItemStack.EMPTY);
                 }

                 if (index2 == 1)
                 {
                    player.drop(playerinventory.getSelected().split(1), true);
                 }
              }
           }
           else if (clicktype == ClickType.QUICK_MOVE)
           {
              if (index1 < 0)
              {
                 return ItemStack.EMPTY;
              }

              Slot slot5 = this.slots.get(index1);
              if (slot5 == null || !slot5.mayPickup(player))
              {
                 return ItemStack.EMPTY;
              }

              for(ItemStack itemstack8 = this.quickMoveStack(player, index1); !itemstack8.isEmpty() && ItemStack.isSame(slot5.getItem(), itemstack8); itemstack8 = this.quickMoveStack(player, index1))
              {
                 itemstack = itemstack8.copy();
              }
           }
           else
           {
              if (index1 < 0)
              {
                 return ItemStack.EMPTY;
              }

              Slot slot6 = this.slots.get(index1);
              if (slot6 != null)
              {
                 ItemStack itemstack9 = slot6.getItem();
                 ItemStack itemstack11 = playerinventory.getSelected();
                 if (!itemstack9.isEmpty())
                 {
                    itemstack = itemstack9.copy();
                 }

                 if (itemstack9.isEmpty())
                 {
                    if (!itemstack11.isEmpty() && slot6.mayPlace(itemstack11))
                    {
                       int j2 = index2 == 0 ? itemstack11.getCount() : 1;
                       if (j2 > slot6.getMaxStackSize(itemstack11))
                       {
                          j2 = slot6.getMaxStackSize(itemstack11);
                       }

                       slot6.set(itemstack11.split(j2));
                    }
                 }
                 else if (slot6.mayPickup(player))
                 {
                    if (itemstack11.isEmpty())
                    {
                       if (itemstack9.isEmpty())
                       {
                          slot6.set(ItemStack.EMPTY);
                          playerinventory.setPickedItem(ItemStack.EMPTY);
                       }
                       else
                       {
                          int k2 = index2 == 0 ? itemstack9.getCount() : (itemstack9.getCount() + 1) / 2;
                          playerinventory.setPickedItem(slot6.remove(k2));
                          if (itemstack9.isEmpty())
                          {
                             slot6.set(ItemStack.EMPTY);
                          }

                          onTakeFromSlot(slot6, player, playerinventory.getSelected());
                       }
                    }
                    else if (slot6.mayPlace(itemstack11))
                    {
                       if (ItemStack.isSameItemSameTags(itemstack9, itemstack11))
                       {
                          int l2 = index2 == 0 ? itemstack11.getCount() : 1;
                          if (l2 > slot6.getMaxStackSize(itemstack11) - itemstack9.getCount())
                          {
                             l2 = slot6.getMaxStackSize(itemstack11) - itemstack9.getCount();
                          }

                          if (l2 > itemstack11.getMaxStackSize() - itemstack9.getCount())
                          {
                             l2 = itemstack11.getMaxStackSize() - itemstack9.getCount();
                          }
                          itemstack11.shrink(l2);
                          itemstack9.grow(l2);
                       }
                       else if (itemstack11.getCount() <= slot6.getMaxStackSize(itemstack11))
                       {
                          slot6.set(itemstack11);
                          playerinventory.setPickedItem(itemstack9);
                       }
                    }
                    else if (itemstack11.getMaxStackSize() > 1 && ItemStack.isSameItemSameTags(itemstack9, itemstack11) && !itemstack9.isEmpty())
                    {
                       int i3 = itemstack9.getCount();
                       if (i3 + itemstack11.getCount() <= itemstack11.getMaxStackSize())
                       {
                          itemstack11.grow(i3);
                          itemstack9 = slot6.remove(i3);
                          if (itemstack9.isEmpty())
                          {
                             slot6.set(ItemStack.EMPTY);
                          }

                          onTakeFromSlot(slot6, player, playerinventory.getSelected());
                       }
                    }
                 }

                 slot6.setChanged();
              }
           }
        }
        else if (clicktype == ClickType.SWAP)
        {
           Slot slot = this.slots.get(index1);
           ItemStack itemstack1 = playerinventory.getItem(index2);
           ItemStack itemstack2 = slot.getItem();
           if (!itemstack1.isEmpty() || !itemstack2.isEmpty())
           {
              if (itemstack1.isEmpty())
              {
                 if (slot.mayPickup(player))
                 {
                    playerinventory.setItem(index2, itemstack2);
                    //slot.onSwapCraft(itemstack2.getCount());
                    slot.set(ItemStack.EMPTY);
                    onTakeFromSlot(slot, player, itemstack2);
                 }
              }
              else if (itemstack2.isEmpty())
              {
                 if (slot.mayPlace(itemstack1))
                 {
                    int i = slot.getMaxStackSize(itemstack1);
                    if (itemstack1.getCount() > i)
                    {
                       slot.set(itemstack1.split(i));
                    }
                    else
                    {
                       slot.set(itemstack1);
                       playerinventory.setItem(index2, ItemStack.EMPTY);
                    }
                 }
              }
              else if (slot.mayPickup(player) && slot.mayPlace(itemstack1))
              {
                 int l1 = slot.getMaxStackSize(itemstack1);
                 if (itemstack1.getCount() > l1)
                 {
                    slot.set(itemstack1.split(l1));
                    onTakeFromSlot(slot, player, itemstack2);
                    if (!playerinventory.add(itemstack2))
                    {
                       player.drop(itemstack2, true);
                    }
                 }
                 else
                 {
                    slot.set(itemstack1);
                    playerinventory.setItem(index2, itemstack2);
                    onTakeFromSlot(slot, player, itemstack2);
                 }
              }
           }
        }
        else if (clicktype == ClickType.CLONE && player.getAbilities().instabuild && playerinventory.getSelected().isEmpty() && index1 >= 0)
        {
            Slot slot4 = this.slots.get(index1);
            if (slot4 != null && slot4.hasItem())
            {
                ItemStack itemstack7 = slot4.getItem().copy();
                itemstack7.setCount(itemstack7.getMaxStackSize());
                playerinventory.setPickedItem(itemstack7);
            }
        }
        else if (clicktype == ClickType.THROW && playerinventory.getSelected().isEmpty() && index1 >= 0)
        {
            Slot slot3 = this.slots.get(index1);
            if (slot3 != null && slot3.hasItem() && slot3.mayPickup(player))
            {
                ItemStack itemstack6 = slot3.remove(index2 == 0 ? 1 : slot3.getItem().getCount());
                onTakeFromSlot(slot3, player, itemstack6);
                player.drop(itemstack6, true);
            }
        }
        else if (clicktype == ClickType.PICKUP_ALL && index1 >= 0)
        {
            Slot slot2 = this.slots.get(index1);
            ItemStack itemstack5 = playerinventory.getSelected();
            if (!itemstack5.isEmpty() && (slot2 == null || !slot2.hasItem() || !slot2.mayPickup(player)))
            {
                int j1 = index2 == 0 ? 0 : this.slots.size() - 1;
                int i2 = index2 == 0 ? 1 : -1;

                for(int j = 0; j < 2; ++j)
                {
                    for(int k = j1; k >= 0 && k < this.slots.size() && itemstack5.getCount() < itemstack5.getMaxStackSize(); k += i2)
                    {
                        Slot slot1 = this.slots.get(k);
                        if (slot1.hasItem() && canItemQuickReplace(slot1, itemstack5, true) && slot1.mayPickup(player) && this.canTakeItemForPickAll(itemstack5, slot1))
                        {
                            ItemStack itemstack3 = slot1.getItem();
                            if (j != 0 || itemstack3.getCount() != itemstack3.getMaxStackSize())
                            {
                                int l = Math.min(itemstack5.getMaxStackSize() - itemstack5.getCount(), itemstack3.getCount());
                                ItemStack itemstack4 = slot1.remove(l);
                                itemstack5.grow(l);
                                if (itemstack4.isEmpty())
                                {
                                   slot1.set(ItemStack.EMPTY);
                                }

                                onTakeFromSlot(slot1, player, itemstack4);
                            }
                        }
                    }
                }
            }

            this.broadcastChanges();
        }

        return itemstack;
   }
}
