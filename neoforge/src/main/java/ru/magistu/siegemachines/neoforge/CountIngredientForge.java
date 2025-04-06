package ru.magistu.siegemachines.neoforge;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import ru.magistu.siegemachines.neoforge.init.IngredientTypes;
import java.util.Arrays;
import java.util.stream.Stream;

public record CountIngredientForge(Ingredient base, int count) implements ICustomIngredient {

    public static final MapCodec<CountIngredientForge> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Ingredient.CODEC.fieldOf("base").forGetter(CountIngredientForge::base),
                    ExtraCodecs.POSITIVE_INT.fieldOf("count").forGetter(CountIngredientForge::count)
            ).apply(instance, CountIngredientForge::new));


    @Override
    public boolean test(ItemStack stack) {
        return base.test(stack) && stack.getCount() >= count;
    }

    @Override
    public Stream<ItemStack> getItems() {
        return Arrays.stream(base.getItems()).peek(s -> s.setCount(count));
    }

    @Override
    public boolean isSimple() {
        return base.isSimple();
    }

    @Override
    public IngredientType<?> getType() {
        return IngredientTypes.COUNT_INGREDIENT_TYPE.get();
    }
}
