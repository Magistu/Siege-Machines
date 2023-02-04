//package ru.itzme1on.siegemachines.gui.machine;
//
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.item.ItemStack;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.screen.ScreenHandler;
//import net.minecraft.screen.slot.Slot;
//import ru.itzme1on.siegemachines.entity.machine.Machine;
//import ru.itzme1on.siegemachines.registry.MenusRegistry;
//
//import java.util.Objects;
//
//public class MachineContainer extends ScreenHandler {
//    private Machine machine;
//
//    public MachineContainer(final int id, final PlayerInventory inventory, final Machine machine) {
//        super(MenusRegistry.MACHINE_CONTAINER.get(), id);
//        if (machine == null)
//            return;
//
//        this.machine = machine;
//
//        for (int row = 0; row < 3; row++)
//            for (int col = 0; col < 9; col++)
//                this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 166 - (4 - row) * 18 - 10));
//
//        for (int row = 0; row < 1; row++)
//            for (int col = 0; col < 9; col++)
//                this.addSlot(new Slot(machine.inventory, col, 8 + col * 18, 18));
//    }
//
//    public MachineContainer(final int id, final PlayerInventory inventory, final PacketByteBuf data) {
//        this(id, inventory, getMachine(inventory));
//    }
//
//    private static Machine getMachine(final PlayerInventory inv) {
//        Objects.requireNonNull(inv, "Player Inventory Cannot Be Null.");
//        PlayerEntity player = inv.player;
//        Entity entity;
//        if (player.hasVehicle()) {
//            entity = player.getVehicle();
//            if (entity instanceof Machine)
//                return (Machine) player.getVehicle();
//        }
//
//        throw new IllegalStateException("Entity Is Not Correct.");
//    }
//
//    @Override
//    public boolean canUse(PlayerEntity player) {
//        return machine.inventory.canPlayerUse(player);
//    }
//
//    @Override
//    public ItemStack transferSlot(PlayerEntity player, int index) {
//        ItemStack itemStack = ItemStack.EMPTY;
//        Slot slot = this.slots.get(index);
//        if (slot.hasStack()) {
//            ItemStack item = slot.getStack();
//            itemStack = item.copy();
//            if (index < 9) {
//                if (!this.insertItem(item, 9, this.slots.size(), true))
//                    return ItemStack.EMPTY;
//                slot.onQuickTransfer(item, itemStack);
//            }
//
//            else if (!this.insertItem(item, 0, 9, false)) {
//                if (index < 9 + 27)
//                    if (!this.insertItem(item, 9 + 27, this.slots.size(), true))
//                        return ItemStack.EMPTY;
//
//                else if (!this.insertItem(item, 9, 9 + 27, false))
//                    return ItemStack.EMPTY;
//
//                return ItemStack.EMPTY;
//            }
//
//            if (item.getCount() == 0)
//                slot.setStack(ItemStack.EMPTY);
//
//            else slot.markDirty();
//
//            if (item.getCount() == itemStack.getCount())
//                return ItemStack.EMPTY;
//
//            slot.onTakeItem(player, itemStack);
//        }
//
//        return itemStack;
//    }
//}
