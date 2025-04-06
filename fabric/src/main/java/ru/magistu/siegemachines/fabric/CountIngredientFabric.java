package ru.magistu.siegemachines.fabric;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import ru.magistu.siegemachines.SiegeMachines;

import java.util.Arrays;
import java.util.List;


public record CountIngredientFabric(Ingredient base, int count) implements CustomIngredient {
    public static final CountIngredientSerializer SERIALIZER = new CountIngredientSerializer();


    @Override
    public boolean test(ItemStack stack) {
        return base.test(stack) && stack.getCount() >= count;
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        return Arrays.stream(base.getItems()).peek(s -> s.setCount(count)).toList();
    }

    @Override
    public boolean requiresTesting() {
        return false;
    }

    @Override
    public CustomIngredientSerializer<CountIngredientFabric> getSerializer() {
        return SERIALIZER;
    }

    public static class CountIngredientSerializer implements CustomIngredientSerializer<CountIngredientFabric> {
        public static final ResourceLocation LOCATION = SiegeMachines.id("count");

        public static final MapCodec<CountIngredientFabric> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Ingredient.CODEC.fieldOf("base").forGetter(CountIngredientFabric::base),
                        ExtraCodecs.POSITIVE_INT.fieldOf("count").forGetter(CountIngredientFabric::count)
                ).apply(instance, CountIngredientFabric::new));

        @Override
        public ResourceLocation getIdentifier() {
            return LOCATION;
        }

        @Override
        public MapCodec<CountIngredientFabric> getCodec(boolean allowEmpty) {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CountIngredientFabric> getPacketCodec() {
            return StreamCodec.of(
                (buf, ingredient) -> {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient.base());
                    buf.writeVarInt(ingredient.count());
                },
                buf -> {
                    Ingredient base = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                    int count = buf.readVarInt();
                    return new CountIngredientFabric(base, count);
                }
            );
        }
    }
}
