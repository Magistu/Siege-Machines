package ru.magistu.siegemachines.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.world.damagesource.DamageTypes;
import ru.magistu.siegemachines.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public ModDamageTypeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(pOutput, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.DamageTypes.MACHINE_IMMUNE_TO).add(DamageTypes.CACTUS, DamageTypes.WITHER, DamageTypes.MAGIC, DamageTypes.DROWN, DamageTypes.STARVE);
    }
}
