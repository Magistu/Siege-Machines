package magistu.siegemachines.block;

import magistu.siegemachines.gui.SiegeWorkbenchContainer;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class SiegeWorkbench extends CraftingTableBlock
{
    private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("container.crafting");

    public SiegeWorkbench(Properties p_i48422_1_)
    {
        super(p_i48422_1_);
    }

    public @NotNull ActionResultType use(BlockState blockstate, World level, BlockPos blockpos, PlayerEntity player, Hand hand, BlockRayTraceResult result)
    {
        if (level.isClientSide)
        {
            return ActionResultType.SUCCESS;
        }
        else
        {
            NetworkHooks.openGui((ServerPlayerEntity) player, this.getMenuProvider(blockstate, level, blockpos));
            return ActionResultType.CONSUME;
        }
    }

    public INamedContainerProvider getMenuProvider(BlockState blockstate, World level, BlockPos blockpos)
    {
        return new SimpleNamedContainerProvider((p_220270_2_, p_220270_3_, p_220270_4_) ->
                new SiegeWorkbenchContainer(p_220270_2_, p_220270_3_, IWorldPosCallable.create(level, blockpos)), CONTAINER_TITLE);
    }


}
