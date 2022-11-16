package magistu.siegemachines.gui;

import magistu.siegemachines.entity.machine.Machine;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MachineContainer extends Container
{
	private final int containersize;
	private final Machine machine;

	public MachineContainer(final int id, final PlayerInventory inv, final Machine machine)
    {
		super(ContainerTypes.MACHINE_CONTAINER.get(), id);
		this.containersize = machine.inventory.getContainerSize();
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

		int n = 0;
		int col_size = (int) Math.ceil((double)this.containersize / 9);
		for (int row = 0; row < col_size; row++)
		{
			for (int col = 0; col < 9 && n < this.containersize; col++)
			{
				this.addSlot(new Slot(machine.inventory, col + row * 9, 8 + col * 18, 18 + row * 18));
				n++;
			}
		}
	}

	public MachineContainer(final int id, final PlayerInventory inv, final PacketBuffer data)
    {
		this(id, inv, getMachine(inv));
	}

	private static Machine getMachine(final PlayerInventory inv)
	{
		Objects.requireNonNull(inv, "Player Inventory Cannot Be Null.");
		PlayerEntity player = inv.player;
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
	public boolean stillValid(PlayerEntity player)
	{
		return machine.inventory.stillValid(player);
	}

	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull PlayerEntity player, int index)
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

	public int getContainerSize() 
	{
		return this.containersize;
	}
}
