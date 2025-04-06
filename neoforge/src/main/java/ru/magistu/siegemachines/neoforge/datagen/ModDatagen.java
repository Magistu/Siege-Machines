package ru.magistu.siegemachines.neoforge.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import ru.magistu.siegemachines.datagen.ModBlockTagsProvider;
import ru.magistu.siegemachines.datagen.ModDamageTypeTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModDatagen {

    public static void gather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> completableFuture = event.getLookupProvider();
        generator.addProvider(true, new ModLangProvider(output));
        generator.addProvider(true, new ModDamageTypeTagsProvider(output,completableFuture));
        generator.addProvider(true, new ModBlockTagsProvider(output, completableFuture));
    }

}
