package ru.magistu.siegemachines.block;

import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SiegeMachines.MOD_ID);
    public static final RegistryObject<SiegeWorkbench> SIEGE_WORKBENCH = registerBlock(() -> new SiegeWorkbench(Block.Properties.of(Material.WOOD).strength(2.5f).sound(SoundType.WOOD)));

    private static <T extends Block> RegistryObject<T> registerBlock(Supplier<T> block)
    {
        RegistryObject<T> toReturn = BLOCKS.register("siege_workbench", block);
        registerBlockItem(toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(RegistryObject<T> block)
    {
        ModItems.ITEMS.register("siege_workbench", () -> new BlockItem(block.get(), new Item.Properties().tab(ModItems.GROUP_SM)));
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
