package ru.magistu.siegemachines;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import ru.magistu.siegemachines.init.IngredientTypes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public record CountIngredientForge(Ingredient base,int count) implements ICustomIngredient {

    public static final MapCodec<CountIngredientForge> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("base").forGetter(CountIngredientForge::base),
                    ExtraCodecs.POSITIVE_INT.fieldOf("count").forGetter(CountIngredientForge::count)
            ).apply(instance, CountIngredientForge::new));


    @Override
    public boolean test(ItemStack stack) {
        return base.test(stack) && stack.getCount() >= count;
    }

    @Override
    public Stream<ItemStack> getItems() {
        List<ItemStack> items = Arrays.stream(base.getItems()).toList();
        for (ItemStack stack : items) {
            stack.setCount(count);
        }
        return items.stream();
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
