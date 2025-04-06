package ru.magistu.siegemachines.fabric.init;

import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import ru.magistu.siegemachines.fabric.CountIngredientFabric;

public class IngredientTypes {

    public static void register() {
        CustomIngredientSerializer.register(CountIngredientFabric.SERIALIZER);
    }

}
