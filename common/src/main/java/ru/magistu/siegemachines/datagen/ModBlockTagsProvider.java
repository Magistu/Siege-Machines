package ru.magistu.siegemachines.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import ru.magistu.siegemachines.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends TagsProvider<Block> {

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, Registries.BLOCK, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        HolderGetter<Block> blockLookup = provider.asGetterLookup().lookupOrThrow(Registries.BLOCK);

        tag(ModTags.Blocks.SMOOTH_IMPACT)
                .addTag(BlockTags.SAND).addTag(BlockTags.DIRT)
                .add(blockLookup.getOrThrow(ResourceKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("dirt"))).key(),
                        blockLookup.getOrThrow(ResourceKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("snow"))).key(),
                        blockLookup.getOrThrow(ResourceKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("snow_block"))).key(),
                        blockLookup.getOrThrow(ResourceKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("grass_block"))).key());
    }
}
