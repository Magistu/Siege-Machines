package ru.magistu.siegemachines.datagen;

import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.codehaus.plexus.util.StringUtils;
import ru.magistu.siegemachines.SiegeMachines;

import java.util.function.Supplier;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, SiegeMachines.ID, "en_us");
    }

    @Override
    protected void addTranslations() {


    }

    protected void addPotion(Holder<Potion> potion, String name) {
        ItemStack stack = PotionContents.createItemStack(Items.POTION,potion);
        add(stack.getDescriptionId(), name);
        
        ItemStack splashStack = PotionContents.createItemStack(Items.SPLASH_POTION,potion);
        add(splashStack.getDescriptionId(), "Splash "+name);

        ItemStack lingeringStack = PotionContents.createItemStack(Items.LINGERING_POTION,potion);
        add(lingeringStack.getDescriptionId(), "Lingering "+name);
    }

    protected void addDefaultMobEffect(Supplier<? extends MobEffect> supplier) {
        addEffect(supplier,getNameFromEffect(supplier.get()));
    }

    protected void addDefaultItem(Supplier<? extends Item> supplier) {
        addItem(supplier,getNameFromItem(supplier.get()));
    }

    protected void addDefaultBlock(Supplier<? extends Block> supplier) {
        addBlock(supplier,getNameFromBlock(supplier.get()));
    }

    protected void addDefaultEntityType(Supplier<EntityType<?>> supplier) {
        addEntityType(supplier,getNameFromEntity(supplier.get()));
    }

    public static String getNameFromItem(Item item) {
        return StringUtils.capitaliseAllWords(item.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromBlock(Block block) {
        return StringUtils.capitaliseAllWords(block.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromEffect(MobEffect effect) {
        return StringUtils.capitaliseAllWords(effect.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromEntity(EntityType<?> entity) {
        return StringUtils.capitaliseAllWords(entity.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

}
