package ru.magistu.siegemachines.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import ru.magistu.siegemachines.SiegeMachines;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, SiegeMachines.ID, "en_us");
    }

    @Override
    protected void addTranslations() {}

}
