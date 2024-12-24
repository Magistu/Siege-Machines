package ru.magistu.siegemachines.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import ru.magistu.siegemachines.block.ModBlocks;

public class SiegeWorkbenchMenu extends CraftingMenu {

    final ContainerLevelAccess access;

    public SiegeWorkbenchMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(containerId, playerInventory, access);
        this.access = access;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.SIEGE_WORKBENCH.get());
    }
}
