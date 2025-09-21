package ru.magistu.siegemachines.config;


import net.minecraftforge.common.ForgeConfigSpec;

public final class SiegeMachineSpecs {
    private final String name;

    public final ForgeConfigSpec.IntValue durability;
    public final ForgeConfigSpec.IntValue delaytime;
    public final ForgeConfigSpec.DoubleValue projectilespeed;
    public final ForgeConfigSpec.DoubleValue inaccuracy;
    public final ForgeConfigSpec.DoubleValue explosiondamagemultiplier;
    public final ForgeConfigSpec.DoubleValue knockbackresistance;

    public SiegeMachineSpecs(ForgeConfigSpec.Builder builder, String name, int durability, int delaytime, float projectilespeed, float inaccuracy, float explosiondamagemultiplier, float knockbackresistance) {
        this.name = name;

        builder.push(name);

        this.durability = builder.defineInRange("durability", durability, 0, Integer.MAX_VALUE);
        this.delaytime = builder.defineInRange("reloadTime", delaytime, 0, Integer.MAX_VALUE);
        this.projectilespeed = builder.defineInRange("projectileSpeed", projectilespeed, 0, Integer.MAX_VALUE);
        this.inaccuracy = builder.defineInRange("inaccuracy", inaccuracy, 0, Integer.MAX_VALUE);
        this.explosiondamagemultiplier = builder.defineInRange("explosionDamageMultiplier", explosiondamagemultiplier, 0, Float.MAX_VALUE);
        this.knockbackresistance = builder.defineInRange("knockbackResistance", knockbackresistance, 0, 1);

        builder.pop();
    }
}
