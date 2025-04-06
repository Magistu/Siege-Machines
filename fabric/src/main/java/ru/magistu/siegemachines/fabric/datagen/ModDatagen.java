package ru.magistu.siegemachines.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import ru.magistu.siegemachines.datagen.ModBlockTagsProvider;
import ru.magistu.siegemachines.datagen.ModDamageTypeTagsProvider;

public class ModDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(ModDamageTypeTagsProvider::new);
        pack.addProvider(ModBlockTagsProvider::new);
    }
}
