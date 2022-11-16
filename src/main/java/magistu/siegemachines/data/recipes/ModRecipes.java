package magistu.siegemachines.data.recipes;

import magistu.siegemachines.SiegeMachines;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes
{
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SiegeMachines.ID);
    public static final RegistryObject<SiegeWorkbenchRecipe.Serializer> SIEGE_WORKBENCH_SERIALIZER = RECIPE_SERIALIZER.register("siege_workbench", SiegeWorkbenchRecipe.Serializer::new);

    public static IRecipeType<SiegeWorkbenchRecipe> SIEGE_WORKBENCH_RECIPE = new SiegeWorkbenchRecipe.SiegeWorkbenchRecipeType();

    public static void register(IEventBus eventBus)
    {
        RECIPE_SERIALIZER.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, SiegeWorkbenchRecipe.TYPE_ID, SIEGE_WORKBENCH_RECIPE);
    }
}
