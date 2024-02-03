package ru.magistu.siegemachines.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import ru.magistu.siegemachines.client.gui.workbench.SiegeWorkbenchContainer;

public class SiegeWorkbench extends CraftingTableBlock
{
    private static final Component CONTAINER_TITLE = Component.translatable("container.crafting");

    public SiegeWorkbench(Properties p_i48422_1_)
    {
        super(p_i48422_1_);
    }

    public @NotNull InteractionResult use(@NotNull BlockState blockstate, Level level, @NotNull BlockPos blockpos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result)
    {
        if (level.isClientSide)
        {
            return InteractionResult.SUCCESS;
        }
        else
        {
            NetworkHooks.openScreen((ServerPlayer) player, this.getMenuProvider(blockstate, level, blockpos));
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public MenuProvider getMenuProvider(@NotNull BlockState blockstate, @NotNull Level level, @NotNull BlockPos blockpos)
    {
        return new SimpleMenuProvider((p_220270_2_, p_220270_3_, p_220270_4_) ->
                new SiegeWorkbenchContainer(p_220270_2_, p_220270_3_, ContainerLevelAccess.create(level, blockpos)), CONTAINER_TITLE);
    }


}
