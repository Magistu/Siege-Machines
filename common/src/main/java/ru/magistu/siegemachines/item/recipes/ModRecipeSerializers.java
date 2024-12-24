package ru.magistu.siegemachines.item.recipes;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;


public class ModRecipeSerializers
{
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(SiegeMachines.ID, Registries.RECIPE_SERIALIZER);
    public static final RegistrySupplier<SiegeWorkbenchRecipe.CustomSerializer> SIEGE_WORKBENCH_SERIALIZER = SERIALIZERS.register("siege_workbench", SiegeWorkbenchRecipe.CustomSerializer::new);

    public static RecipeType<SiegeWorkbenchRecipe> TYPE = new RecipeType<>() {
        @Override
        public String toString() {
            return SiegeWorkbenchRecipe.TYPE_ID.toString();
        }
    };
    
    public static void register() {
        SERIALIZERS.register();
    }
}
