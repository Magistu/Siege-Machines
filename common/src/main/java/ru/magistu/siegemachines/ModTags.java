package ru.magistu.siegemachines;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class DamageTypes {
        public static final TagKey<DamageType> MACHINE_IMMUNE_TO = mod("machine_immune_to");

        static TagKey<DamageType> mod(String path) {
            return TagKey.create(Registries.DAMAGE_TYPE,SiegeMachines.id(path));
        }
    }

    public static class Blocks {


        public static final TagKey<Block> SMOOTH_IMPACT = mod("smooth_impact");

        static TagKey<Block> mod(String path) {
            return TagKey.create(Registries.BLOCK,SiegeMachines.id(path));
        }
    }

}
