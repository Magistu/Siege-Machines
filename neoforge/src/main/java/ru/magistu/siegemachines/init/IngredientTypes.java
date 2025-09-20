package ru.magistu.siegemachines.init;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import ru.magistu.siegemachines.CountIngredientNeoForge;
import ru.magistu.siegemachines.SiegeMachines;

public class IngredientTypes {
    private static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, SiegeMachines.ID);

    public static final DeferredHolder<IngredientType<?>, IngredientType<CountIngredientNeoForge>> COUNT_INGREDIENT_TYPE = INGREDIENT_TYPES.register("count", () -> new IngredientType<>(CountIngredientNeoForge.CODEC));

    public static void register(IEventBus bus) {
        INGREDIENT_TYPES.register(bus);
    }

}
