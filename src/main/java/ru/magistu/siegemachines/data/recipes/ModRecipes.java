package ru.magistu.siegemachines.data.recipes;

import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes
{
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SiegeMachines.ID);

    public static final RegistryObject<RecipeSerializer<SiegeWorkbenchRecipe>> SIEGE_WORKBENCH_SERIALIZER =
            SERIALIZERS.register("siege_workbench", () -> SiegeWorkbenchRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
