package ru.magistu.siegemachines.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import ru.magistu.siegemachines.menu.SiegeWorkbenchMenu;

public class SiegeWorkbenchBlock extends CraftingTableBlock
{
    private static final Component CONTAINER_TITLE = Component.translatable("container.crafting");

    public SiegeWorkbenchBlock(Properties p_i48422_1_)
    {
        super(p_i48422_1_);
    }


    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (p_52229_, p_52230_, p_52231_) -> new SiegeWorkbenchMenu(p_52229_, p_52230_, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE
        );
    }
}
