package ru.magistu.siegemachines.item.recipes;

import net.minecraft.world.item.crafting.RecipeType;
import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes
{
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SiegeMachines.ID);
    public static final RegistryObject<RecipeSerializer<SiegeWorkbenchRecipe>> SIEGE_WORKBENCH_SERIALIZER = SERIALIZERS.register("siege_workbench", () -> SiegeWorkbenchRecipe.Serializer.INSTANCE);

    public static RecipeType<SiegeWorkbenchRecipe> SIEGE_WORKBENCH_RECIPE = SiegeWorkbenchRecipe.Type.INSTANCE;
    
    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
