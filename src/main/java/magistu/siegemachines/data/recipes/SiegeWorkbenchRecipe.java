package magistu.siegemachines.data.recipes;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.block.ModBlocks;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class SiegeWorkbenchRecipe implements ISiegeWorkbenchRecipe, net.minecraftforge.common.crafting.IShapedRecipe<CraftingInventory>
{
    static int MAX_WIDTH = 3;
    static int MAX_HEIGHT = 3;
    /**
     * Expand the max width and height allowed in the deserializer.
     * This should be called by modders who add custom crafting tables that are larger than the vanilla 3x3.
     * @param width your max recipe width
     * @param height your max recipe height
     */
    public static void setCraftingSize(int width, int height)
    {
        if (MAX_WIDTH < width) MAX_WIDTH = width;
        if (MAX_HEIGHT < height) MAX_HEIGHT = height;
    }

    private final int width;
    private final int height;
    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack result;
    private final ResourceLocation id;
    private final String group;

    public SiegeWorkbenchRecipe(ResourceLocation p_i48162_1_, String p_i48162_2_, int p_i48162_3_, int p_i48162_4_, NonNullList<Ingredient> p_i48162_5_, ItemStack p_i48162_6_)
    {
        this.id = p_i48162_1_;
        this.group = p_i48162_2_;
        this.width = p_i48162_3_;
        this.height = p_i48162_4_;
        this.recipeItems = p_i48162_5_;
        this.result = p_i48162_6_;
    }

    public static class SiegeWorkbenchRecipeType implements IRecipeType<SiegeWorkbenchRecipe>
    {
        @Override
        public String toString()
        {
            return SiegeWorkbenchRecipe.TYPE_ID.toString();
        }
    }

    public ResourceLocation getId()
    {
        return this.id;
    }

    public IRecipeSerializer<?> getSerializer()
    {
        return ModRecipes.SIEGE_WORKBENCH_SERIALIZER.get();
    }

    public String getGroup()
    {
        return this.group;
    }

    public ItemStack getResultItem()
    {
        return this.result;
    }

    public @NotNull NonNullList<Ingredient> getIngredients()
    {
        return this.recipeItems;
    }

    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_)
    {
        return p_194133_1_ >= this.width && p_194133_2_ >= this.height;
    }

    public boolean testIngredient(Ingredient ingredient, @Nullable ItemStack p_test_1_)
    {
        if (p_test_1_ == null)
        {
           return false;
        }
        else
        {
            if (ingredient.getItems().length == 0)
            {
               return p_test_1_.isEmpty();
            }
            else
            {
                for (ItemStack itemstack : ingredient.getItems())
                {
                    if (itemstack.getItem() == p_test_1_.getItem() && itemstack.getCount() == p_test_1_.getCount())
                    {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    public boolean matches(CraftingInventory p_77569_1_, World p_77569_2_)
    {
        for (int i = 0; i <= p_77569_1_.getWidth() - this.width; ++i)
        {
            for (int j = 0; j <= p_77569_1_.getHeight() - this.height; ++j)
            {
                if (this.matches(p_77569_1_, i, j, true))
                {
                    return true;
                }

                if (this.matches(p_77569_1_, i, j, false))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matches(CraftingInventory p_77573_1_, int p_77573_2_, int p_77573_3_, boolean p_77573_4_)
    {
        for (int i = 0; i < p_77573_1_.getWidth(); ++i)
        {
            for (int j = 0; j < p_77573_1_.getHeight(); ++j)
            {
                int k = i - p_77573_2_;
                int l = j - p_77573_3_;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height)
                {
                    if (p_77573_4_)
                    {
                        ingredient = this.recipeItems.get(this.width - k - 1 + l * this.width);
                    }
                    else
                    {
                        ingredient = this.recipeItems.get(k + l * this.width);
                    }
                }
                ItemStack itemstack = p_77573_1_.getItem(i + j * p_77573_1_.getWidth());

                if (!this.testIngredient(ingredient, itemstack))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public ItemStack assemble(CraftingInventory p_77572_1_)
    {
        return this.getResultItem().copy();
    }

    public int getWidth()
    {
        return this.width;
    }

    @Override
    public int getRecipeWidth()
    {
        return getWidth();
    }

    public int getHeight()
    {
        return this.height;
    }

    @Override
    public int getRecipeHeight()
    {
        return getHeight();
    }

    @Override
    public @NotNull ItemStack getToastSymbol()
    {
        return new ItemStack(ModBlocks.SIEGE_WORKBENCH.get());
    }

    private static NonNullList<Ingredient> dissolvePattern(String[] p_192402_0_, Map<String, Ingredient> p_192402_1_, int p_192402_2_, int p_192402_3_)
    {
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(p_192402_2_ * p_192402_3_, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(p_192402_1_.keySet());
        set.remove(" ");

        for (int i = 0; i < p_192402_0_.length; ++i)
        {
            for (int j = 0; j < p_192402_0_[i].length(); ++j)
            {
                String s = p_192402_0_[i].substring(j, j + 1);
                Ingredient ingredient = p_192402_1_.get(s);
                if (ingredient == null)
                {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                nonnulllist.set(j + p_192402_2_ * i, ingredient);
            }
        }

        if (!set.isEmpty())
        {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        }
        else
        {
            return nonnulllist;
        }
    }

    @VisibleForTesting
    static String[] shrink(String... p_194134_0_)
    {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int i1 = 0; i1 < p_194134_0_.length; ++i1)
        {
            String s = p_194134_0_[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0)
            {
                if (k == i1)
                {
                    ++k;
                }

                ++l;
            }
            else
            {
                l = 0;
            }
        }

        if (p_194134_0_.length == l)
        {
            return new String[0];
        }
        else
        {
            String[] astring = new String[p_194134_0_.length - l - k];

            for (int k1 = 0; k1 < astring.length; ++k1)
            {
                astring[k1] = p_194134_0_[k1 + k].substring(i, j + 1);
            }

            return astring;
        }
    }

    private static int firstNonSpace(String p_194135_0_)
    {
        int i;
        for (i = 0; i < p_194135_0_.length() && p_194135_0_.charAt(i) == ' '; ++i)
        {
        }

        return i;
    }

    private static int lastNonSpace(String p_194136_0_)
    {
        int i;
        for (i = p_194136_0_.length() - 1; i >= 0 && p_194136_0_.charAt(i) == ' '; --i)
        {
        }

        return i;
    }

    private static String[] patternFromJson(JsonArray p_192407_0_)
    {
        String[] astring = new String[p_192407_0_.size()];
        if (astring.length > MAX_HEIGHT)
        {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
        } else if (astring.length == 0)
        {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        }
        else
        {
            for (int i = 0; i < astring.length; ++i)
            {
                String s = JSONUtils.convertToString(p_192407_0_.get(i), "pattern[" + i + "]");
                if (s.length() > MAX_WIDTH)
                {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
                }

                if (i > 0 && astring[0].length() != s.length())
                {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }
    }

    public static class TagStackList extends Ingredient.TagList
    {
        private final int count;

        public TagStackList(ITag<Item> p_i48193_1_, int count)
        {
            super(p_i48193_1_);
            this.count = count;
        }

        public Collection<ItemStack> getItems()
        {
            Collection<ItemStack> list = super.getItems();
            list.forEach(ele -> ele.setCount(this.count));
            return list;
        }

        public @NotNull JsonObject serialize()
        {
           JsonObject jsonobject = super.serialize();
           return jsonobject;
        }
    }


    public static Ingredient.IItemList valueFromJson(JsonObject p_199803_0_)
    {
        int count = JSONUtils.getAsInt(p_199803_0_, "count", 1);
        if (p_199803_0_.has("item") && p_199803_0_.has("tag"))
        {
            throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
        }
        else if (p_199803_0_.has("item"))
        {
            ResourceLocation resourcelocation1 = new ResourceLocation(JSONUtils.getAsString(p_199803_0_, "item"));
            Item item = Registry.ITEM.getOptional(resourcelocation1).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + resourcelocation1 + "'"));
            return new Ingredient.SingleItemList(new ItemStack(item, count));
        }
        else if (p_199803_0_.has("tag"))
        {
            ResourceLocation resourcelocation = new ResourceLocation(JSONUtils.getAsString(p_199803_0_, "tag"));
            ITag<Item> itag = TagCollectionManager.getInstance().getItems().getTag(resourcelocation);
            if (itag == null)
            {
                throw new JsonSyntaxException("Unknown item tag '" + resourcelocation + "'");
            }
            else
            {
                return new TagStackList(itag, count);
            }
        }
        else
        {
            throw new JsonParseException("An ingredient entry needs either a tag or an item");
        }
    }


//    public static class CompoundIngredientE extends CompoundIngredient
//    {
//        protected CompoundIngredientE(List<Ingredient> children)
//        {
//            super(children);
//        }
//    }
//
//
//    public static Ingredient getIngredient(JsonElement json)
//    {
//        if (json == null || json.isJsonNull())
//            throw new JsonSyntaxException("Json cannot be null");
//
//        if (json.isJsonArray())
//        {
//            List<Ingredient> ingredients = Lists.newArrayList();
//            List<Ingredient> vanilla = Lists.newArrayList();
//            json.getAsJsonArray().forEach((ele) ->
//            {
//                Ingredient ing = getIngredient(ele);
//
//                if (ing.getClass() == Ingredient.class) //Vanilla, Due to how we read it splits each itemstack, so we pull out to re-merge later
//                    vanilla.add(ing);
//                else
//                    ingredients.add(ing);
//            });
//
//            if (!vanilla.isEmpty())
//                ingredients.add(Ingredient.merge(vanilla));
//
//            if (ingredients.size() == 0)
//                throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
//
//            if (ingredients.size() == 1)
//                return ingredients.get(0);
//
//            return new CompoundIngredientE(ingredients);
//        }
//
//        if (!json.isJsonObject())
//            throw new JsonSyntaxException("Expcted ingredient to be a object or array of objects");
//
//        JsonObject obj = (JsonObject)json;
//
//        String type = JSONUtils.getAsString(obj, "type", "minecraft:item");
//        if (type.isEmpty())
//            throw new JsonSyntaxException("Ingredient type can not be an empty string");
//
//        IIngredientSerializer<?> serializer = ingredients.get(new ResourceLocation(type));
//        if (serializer == null)
//            throw new JsonSyntaxException("Unknown ingredient type: " + type);
//
//        return serializer.parse(obj);
//    }


    public static Ingredient ingredientFromJson(@Nullable JsonElement p_199802_0_)
    {
        if (p_199802_0_ != null && !p_199802_0_.isJsonNull())
        {
//            Ingredient ret = CraftingHelper.getIngredient(p_199802_0_);
//            if (ret != null) return ret;

            if (p_199802_0_.isJsonObject())
            {
                return Ingredient.fromValues(Stream.of(valueFromJson(p_199802_0_.getAsJsonObject())));
            }
            else if (p_199802_0_.isJsonArray())
            {
                JsonArray jsonarray = p_199802_0_.getAsJsonArray();
                if (jsonarray.size() == 0)
                {
                    throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
                }
                else
                {
                    return Ingredient.fromValues(StreamSupport.stream(jsonarray.spliterator(), false).map((p_209355_0_) -> valueFromJson(JSONUtils.convertToJsonObject(p_209355_0_, "item"))));
                }
            }
            else
            {
               throw new JsonSyntaxException("Expected item to be object or array of objects");
            }
        }
        else
        {
            throw new JsonSyntaxException("Item cannot be null");
        }
    }


    private static Map<String, Ingredient> keyFromJson(JsonObject p_192408_0_)
    {
        Map<String, Ingredient> map = Maps.newHashMap();

        for (Map.Entry<String, JsonElement> entry : p_192408_0_.entrySet())
        {
            if (entry.getKey().length() != 1)
            {
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey()))
            {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            Ingredient ingredient = ingredientFromJson(entry.getValue());

            map.put(entry.getKey(), ingredient);
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    public static ItemStack itemFromJson(JsonObject p_199798_0_)
    {
        String s = JSONUtils.getAsString(p_199798_0_, "item");
        Item item = Registry.ITEM.getOptional(new ResourceLocation(s)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + s + "'"));
        if (p_199798_0_.has("data"))
        {
            throw new JsonParseException("Disallowed data tag found");
        }
        else
        {
            int i = JSONUtils.getAsInt(p_199798_0_, "count", 1);
            return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(p_199798_0_, true);
        }
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>  implements IRecipeSerializer<SiegeWorkbenchRecipe>
    {
        private static final ResourceLocation NAME = new ResourceLocation(SiegeMachines.ID, "siege_workbench");

        public SiegeWorkbenchRecipe fromJson(ResourceLocation p_199425_1_, JsonObject p_199425_2_)
        {
            String s = JSONUtils.getAsString(p_199425_2_, "group", "");
            Map<String, Ingredient> map = SiegeWorkbenchRecipe.keyFromJson(JSONUtils.getAsJsonObject(p_199425_2_, "key"));
            String[] astring = SiegeWorkbenchRecipe.shrink(SiegeWorkbenchRecipe.patternFromJson(JSONUtils.getAsJsonArray(p_199425_2_, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = SiegeWorkbenchRecipe.dissolvePattern(astring, map, i, j);
            ItemStack itemstack = SiegeWorkbenchRecipe.itemFromJson(JSONUtils.getAsJsonObject(p_199425_2_, "result"));
            return new SiegeWorkbenchRecipe(p_199425_1_, s, i, j, nonnulllist, itemstack);
        }

        public SiegeWorkbenchRecipe fromNetwork(ResourceLocation p_199426_1_, PacketBuffer p_199426_2_)
        {
            int i = p_199426_2_.readVarInt();
            int j = p_199426_2_.readVarInt();
            String s = p_199426_2_.readUtf(32767);
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

            for (int k = 0; k < nonnulllist.size(); ++k)
            {
                nonnulllist.set(k, Ingredient.fromNetwork(p_199426_2_));
            }

            ItemStack itemstack = p_199426_2_.readItem();
            return new SiegeWorkbenchRecipe(p_199426_1_, s, i, j, nonnulllist, itemstack);
        }

        public void toNetwork(PacketBuffer p_199427_1_, SiegeWorkbenchRecipe p_199427_2_)
        {
            p_199427_1_.writeVarInt(p_199427_2_.width);
            p_199427_1_.writeVarInt(p_199427_2_.height);
            p_199427_1_.writeUtf(p_199427_2_.group);

            for (Ingredient ingredient : p_199427_2_.recipeItems)
            {
                ingredient.toNetwork(p_199427_1_);
            }

            p_199427_1_.writeItem(p_199427_2_.result);
        }
    }
}
