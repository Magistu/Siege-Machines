package ru.magistu.siegemachines.data.recipes;

import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes
{
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SiegeMachines.MOD_ID);
    public static final RegistryObject<SiegeWorkbenchRecipe.Serializer> SIEGE_WORKBENCH_SERIALIZER = RECIPE_SERIALIZER.register("siege_workbench", SiegeWorkbenchRecipe.Serializer::new);

    public static RecipeType<SiegeWorkbenchRecipe> SIEGE_WORKBENCH_RECIPE = new SiegeWorkbenchRecipe.SiegeWorkbenchRecipeType();

    public static void register(IEventBus eventBus)
    {
        RECIPE_SERIALIZER.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, SiegeWorkbenchRecipe.TYPE_ID, SIEGE_WORKBENCH_RECIPE);
    }
}
