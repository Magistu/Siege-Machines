package ru.magistu.siegemachines.item.recipes;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import ru.magistu.siegemachines.SiegeMachines;


public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(SiegeMachines.ID, Registries.RECIPE_SERIALIZER);
    public static final RegistrySupplier<SiegeWorkbenchRecipe.CustomSerializer> SIEGE_WORKBENCH_SERIALIZER = SERIALIZERS.register("siege_workbench", SiegeWorkbenchRecipe.CustomSerializer::new);

    public static RecipeType<SiegeWorkbenchRecipe> SIEGE_WORKBENCH_RECIPE = SiegeWorkbenchRecipe.Type.INSTANCE;

    public static void register() {
        SERIALIZERS.register();
    }
}
