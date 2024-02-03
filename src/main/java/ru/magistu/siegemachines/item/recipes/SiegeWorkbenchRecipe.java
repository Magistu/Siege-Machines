package ru.magistu.siegemachines.item.recipes;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IShapedRecipe;
import org.jetbrains.annotations.NotNull;
import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import ru.magistu.siegemachines.block.ModBlocks;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class SiegeWorkbenchRecipe implements IShapedRecipe<CraftingContainer> 
{
    static ResourceLocation TYPE_ID = new ResourceLocation(SiegeMachines.ID, "siege_workbench");

    static int MAX_WIDTH = 3;
    static int MAX_HEIGHT = 3;
    
    private final ResourceLocation id;
    private final ItemStack result;
    private final List<CountIngredient> recipeitems;
    
    final int width;
    final int height;
    
    public SiegeWorkbenchRecipe(ResourceLocation id, int width, int height, List<CountIngredient> recipeitems, ItemStack result) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.recipeitems = recipeitems;
        this.result = result;
    }

    public static class Type implements RecipeType<SiegeWorkbenchRecipe>
    {
        public static final Type INSTANCE = new Type();
        
        @Override
        public String toString()
        {
            return SiegeWorkbenchRecipe.TYPE_ID.toString();
        }
    }

    @NotNull
    public ResourceLocation getId() {
        return this.id;
    }

    @NotNull
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    @NotNull
    public RecipeType<?> getType() 
    {
        return Type.INSTANCE;
    }

    @Override
    @NotNull
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.SIEGE_WORKBENCH.get());
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
    
    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
     * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
     */
    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    public List<CountIngredient> getRecipeItems()
    {
        return this.recipeitems;
    }

    @Override
    public NonNullList<Ingredient> getIngredients()
    {
        NonNullList<Ingredient> list = NonNullList.create();
        for (CountIngredient ingredient : this.recipeitems)
            list.add(ingredient.get());
        return list;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.width && height >= this.height;
    }
    
    @NotNull
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        for(int i = 0; i <= container.getWidth() - this.width; ++i) {
            for(int j = 0; j <= container.getHeight() - this.height; ++j) {
                if (this.matches(container, i, j, true)) {
                    return getRemainingItems(container, i, j, true);
                }

                if (this.matches(container, i, j, false)) {
                    return getRemainingItems(container, i, j, true);
                }
            }
        }

        return NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
    }
    
    private NonNullList<ItemStack> getRemainingItems(CraftingContainer container, int width, int height, boolean mirrored) 
    {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

        for(int i = 0; i < container.getWidth(); ++i) {
            for(int j = 0; j < container.getHeight(); ++j) {
                int k = i - width;
                int l = j - height;
                CountIngredient ingredient = CountIngredient.of(Ingredient.EMPTY);
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (mirrored) {
                        ingredient = this.recipeitems.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.recipeitems.get(k + l * this.width);
                    }
                }
                
                int index = i + j * container.getWidth();
                ItemStack stack = container.getItem(index);
                stack.setCount(ingredient.getCount());
                nonnulllist.set(index, stack);
            }
        }

        return nonnulllist;
    }
    
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(CraftingContainer container, Level level) {
        for(int i = 0; i <= container.getWidth() - this.width; ++i) {
            for(int j = 0; j <= container.getHeight() - this.height; ++j) {
                if (this.matches(container, i, j, true)) {
                    return true;
                }

                if (this.matches(container, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the region of a crafting inventory is match for the recipe.
     */
    private boolean matches(CraftingContainer container, int width, int height, boolean mirrored) {
        for(int i = 0; i < container.getWidth(); ++i) {
            for(int j = 0; j < container.getHeight(); ++j) {
                int k = i - width;
                int l = j - height;
                CountIngredient ingredient = CountIngredient.of(Ingredient.EMPTY);
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (mirrored) {
                        ingredient = this.recipeitems.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.recipeitems.get(k + l * this.width);
                    }
                }

                if (!ingredient.test(container.getItem(i + j * container.getWidth()))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack assemble(CraftingContainer container) {
        return this.getResultItem().copy();
    }

    public int getWidth() {
        return this.width;
    }

    @Override
    public int getRecipeWidth() {
        return getWidth();
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public int getRecipeHeight() {
        return getHeight();
    }

    static NonNullList<CountIngredient> dissolvePattern(String[] pattern, Map<String, CountIngredient> keys, int patternwidth, int patternHeight) {
        NonNullList<CountIngredient> list = NonNullList.withSize(patternwidth * patternHeight, CountIngredient.of(Ingredient.EMPTY));
        Set<String> set = Sets.newHashSet(keys.keySet());
        set.remove(" ");

        for(int i = 0; i < pattern.length; ++i) {
            for(int j = 0; j < pattern[i].length(); ++j) {
                String s = pattern[i].substring(j, j + 1);
                CountIngredient ingredient = keys.get(s);
                if (ingredient == null)
                {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                list.set(j + patternwidth * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return list;
        }
    }

    @VisibleForTesting
    static String[] shrink(String... toshrink) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for(int i1 = 0; i1 < toshrink.length; ++i1) {
            String s = toshrink[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (toshrink.length == l) {
            return new String[0];
        } else {
            String[] astring = new String[toshrink.length - l - k];

            for(int k1 = 0; k1 < astring.length; ++k1) {
                astring[k1] = toshrink[k1 + k].substring(i, j + 1);
            }

            return astring;
        }
    }

    public boolean isIncomplete() {
        NonNullList<Ingredient> list = this.getIngredients();
        return list.isEmpty() || list.stream().filter((ingredient) -> {
            return !ingredient.isEmpty();
        }).anyMatch(ForgeHooks::hasNoElements);
    }

    private static int firstNonSpace(String entry) {
        int i;
        for(i = 0; i < entry.length() && entry.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int lastNonSpace(String entry) {
        int i;
        for(i = entry.length() - 1; i >= 0 && entry.charAt(i) == ' '; --i) {}

        return i;
    }

    static String[] patternFromJson(JsonArray patternarray) {
        String[] astring = new String[patternarray.size()];
        if (astring.length > MAX_HEIGHT) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < astring.length; ++i) {
                String s = GsonHelper.convertToString(patternarray.get(i), "pattern[" + i + "]");
                if (s.length() > MAX_WIDTH) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
                }

                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }
    }

    /**
     * Returns a key json object as a Java HashMap.
     */
    static Map<String, CountIngredient> keyFromJson(JsonObject keyentry) {
        Map<String, CountIngredient> map = Maps.newHashMap();

        for(Map.Entry<String, JsonElement> entry : keyentry.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }
            
            map.put(entry.getKey(), CountIngredient.fromJson(entry.getValue().getAsJsonObject()));
        }

        map.put(" ", CountIngredient.of(Ingredient.EMPTY));
        return map;
    }

    public static ItemStack itemStackFromJson(JsonObject stackobject)
    {
        return CraftingHelper.getItemStack(stackobject, true, true);
    }
    
    public static class Serializer implements RecipeSerializer<SiegeWorkbenchRecipe>
    {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(SiegeMachines.ID,"siege_workbench");
    
        @Override
        public SiegeWorkbenchRecipe fromJson(ResourceLocation recipeid, JsonObject json) 
        {
            Map<String, CountIngredient> map = SiegeWorkbenchRecipe.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            String[] astring = SiegeWorkbenchRecipe.shrink(SiegeWorkbenchRecipe.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            
            int i = astring[0].length();
            int j = astring.length;
            
            List<CountIngredient> list = SiegeWorkbenchRecipe.dissolvePattern(astring, map, i, j);
            ItemStack result = SiegeWorkbenchRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            
            return new SiegeWorkbenchRecipe(recipeid, i, j, list, result);
        }
    
        @Override
        public SiegeWorkbenchRecipe fromNetwork(ResourceLocation recipeid, FriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            int j = buffer.readVarInt();
            NonNullList<CountIngredient> list = NonNullList.withSize(i * j, CountIngredient.of(Ingredient.EMPTY));

            for(int k = 0; k < list.size(); ++k) {
                list.set(k, CountIngredient.fromNetwork(buffer));
            }

            ItemStack itemstack = buffer.readItem();
            return new SiegeWorkbenchRecipe(recipeid, i, j, list, itemstack);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SiegeWorkbenchRecipe pRecipe) {
            buffer.writeVarInt(pRecipe.width);
            buffer.writeVarInt(pRecipe.height);

            for(CountIngredient ingredient : pRecipe.recipeitems) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(pRecipe.result);
        }
    
//        @Override
//        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
//            return INSTANCE;
//        }
//
//        @Nullable
//        @Override
//        public ResourceLocation getRegistryName() {
//            return ID;
//        }
//
//        @Override
//        public Class<RecipeSerializer<?>> getRegistryType() {
//            return Serializer.castClass(RecipeSerializer.class);
//        }
//
//        @SuppressWarnings("unchecked") // Need this wrapper, because generics
//        private static <G> Class<G> castClass(Class<?> cls) {
//            return (Class<G>)cls;
//        }
    }
}