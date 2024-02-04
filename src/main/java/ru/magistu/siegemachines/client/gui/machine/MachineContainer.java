package ru.magistu.siegemachines.client.gui.machine;

import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import ru.magistu.siegemachines.client.gui.ModMenuTypes;

import java.util.Objects;

public class MachineContainer extends AbstractContainerMenu
{
	private Player player;
	private Machine machine;
	int x, y, z;
	private IItemHandler internal;
	private boolean bound = false;


	public MachineContainer(final int id, final Inventory inv, final Machine machine)
    {
		super(ModMenuTypes.MACHINE_CONTAINER.get(), id);
		if (machine == null)
		{
			return;
		}
		this.machine = machine;

		for (int row = 0; row < 3; row++)
		{
			for (int col = 0; col < 9; col++)
			{
				this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 166 - (4 - row) * 18 - 10));
			}
		}

		for (int col = 0; col < 9; col++)
		{
			this.addSlot(new Slot(inv, col, 8 + col * 18, 142));
		}

		for (int row = 0; row < 1; row++)
		{
			for (int col = 0; col < 9; col++)
			{
				this.addSlot(new Slot(machine.inventory, col + row * 9, 8 + col * 18, 18 + row * 18));
			}
		}
	}

	public MachineContainer(final int id, final Inventory inv, final FriendlyByteBuf data)
    {
		this(id, inv, getMachine(inv));
	}

	private static Machine getMachine(final Inventory inv)
	{
		Objects.requireNonNull(inv, "Player Inventory Cannot Be Null.");
		Player player = inv.player;
		Entity entity;
		if (player.isPassenger())
		{
			entity = player.getVehicle();
			if (entity instanceof Machine)
			{
				return (Machine) player.getVehicle();
			}
		}

		throw new IllegalStateException("Entity Is Not Correct.");
	}

	@Override
	public boolean stillValid(Player player)
	{
		return machine.inventory.stillValid(player);
	}

	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index)
    {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem())
		{
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < 9)
			{
				if (!this.moveItemStackTo(itemstack1, 9, this.slots.size(), true))
				{
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(itemstack1, itemstack);
			}
			else if (!this.moveItemStackTo(itemstack1, 0, 9, false))
			{
				if (index < 9 + 27)
				{
					if (!this.moveItemStackTo(itemstack1, 9 + 27, this.slots.size(), true))
					{
						return ItemStack.EMPTY;
					}
				}
				else
				{
					if (!this.moveItemStackTo(itemstack1, 9, 9 + 27, false))
					{
						return ItemStack.EMPTY;
					}
				}
				return ItemStack.EMPTY;
			}
			if (itemstack1.getCount() == 0)
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
			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}
}
