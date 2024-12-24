package ru.magistu.siegemachines.item.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import ru.magistu.siegemachines.SiegeMachines;
import org.jetbrains.annotations.NotNull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import ru.magistu.siegemachines.block.ModBlocks;


public class SiegeWorkbenchRecipe extends ShapedRecipe {
    static ResourceLocation TYPE_ID = SiegeMachines.id("siege_workbench");

    static int MAX_WIDTH = 3;
    static int MAX_HEIGHT = 3;

    public SiegeWorkbenchRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification) {
        super(group, category, pattern, result, showNotification);
    }

    public SiegeWorkbenchRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result) {
        super(group, category, pattern, result);
    }

    public SiegeWorkbenchRecipe(ShapedRecipe recipe) {
        this(SiegeMachines.ID,recipe.category(), recipe.pattern, recipe.getResultItem(null));
    }


    @NotNull
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.SIEGE_WORKBENCH_SERIALIZER.get()  ;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return super.assemble(input, registries);
    }

    /* @Override
    @NotNull
    public RecipeType<?> getType() 
    {
        return Type.INSTANCE;
    }*/

    @Override
    @NotNull
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.SIEGE_WORKBENCH.get());
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class CustomSerializer extends Serializer {

        public static final MapCodec<SiegeWorkbenchRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(ShapedRecipe.Serializer.CODEC.forGetter(upgradeRecipe -> upgradeRecipe))
                        .apply(instance,SiegeWorkbenchRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, SiegeWorkbenchRecipe> STREAM_CODEC = StreamCodec.of(
                ShapedRecipe.Serializer.STREAM_CODEC::encode, pBuffer -> new SiegeWorkbenchRecipe(ShapedRecipe.Serializer.STREAM_CODEC.decode(pBuffer)));


        @Override
        public MapCodec<ShapedRecipe> codec() {
            return (MapCodec<ShapedRecipe>)(Object) CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShapedRecipe> streamCodec() {
            return  (StreamCodec<RegistryFriendlyByteBuf, ShapedRecipe>) (Object) STREAM_CODEC;
        }

    }
}