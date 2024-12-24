package ru.magistu.siegemachines.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public final class SiegeMachineSpecs
{
    private final String name;

    public final ModConfigSpec.IntValue durability;
    public final ModConfigSpec.IntValue delaytime;
    public final ModConfigSpec.DoubleValue projectilespeed;
    public final ModConfigSpec.DoubleValue inaccuracy;
    public final ModConfigSpec.DoubleValue damagemultiplier;

    public SiegeMachineSpecs(ModConfigSpec.Builder builder, String name, int durability, int delaytime, float projectilespeed, float inaccuracy, float damagemultiplier)
    {
        this.name = name;

        builder.push(name);

        this.durability = builder.defineInRange("durability", durability,0,Integer.MAX_VALUE);
        this.delaytime = builder.defineInRange("reloadTime", delaytime,0,Integer.MAX_VALUE);
        this.projectilespeed = builder.defineInRange("projectileSpeed", projectilespeed,0,Integer.MAX_VALUE);
        this.inaccuracy = builder.defineInRange("inaccuracy", inaccuracy,0,Integer.MAX_VALUE);
        this.damagemultiplier = builder.defineInRange("damageMultiplier", damagemultiplier,0,Float.MAX_VALUE);

        builder.pop();
    }
}
