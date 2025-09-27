package ru.magistu.siegemachines.config;


import net.minecraftforge.common.ForgeConfigSpec;

public class MissileSpecs {
    public final ForgeConfigSpec.DoubleValue mass;
    public final ForgeConfigSpec.DoubleValue explosionpower;
    public final ForgeConfigSpec.DoubleValue armorpiercing;
    public final ForgeConfigSpec.DoubleValue knockback;
    public final ForgeConfigSpec.DoubleValue explosiondamagemultiplier;
    public final ForgeConfigSpec.BooleanValue explosive;
    public final ForgeConfigSpec.BooleanValue fired;

    public MissileSpecs(ForgeConfigSpec.Builder builder, String name, double mass, double explosionpower, double armorpiercing, double knockback, double explosiondamagemultiplier, boolean explosive, boolean fired) {
        builder.push(name);

        this.mass = builder.defineInRange("mass", mass, 0, Double.MAX_VALUE);
        this.explosionpower = builder.defineInRange("explosionRadius", explosionpower, 0, Double.MAX_VALUE);
        this.armorpiercing = builder.defineInRange("armorPiercing", armorpiercing, 0, 1.0);
        this.knockback = builder.defineInRange("knockback", knockback, 0, Double.MAX_VALUE);
        this.explosiondamagemultiplier = builder.defineInRange("explosionDamageMultiplier", explosiondamagemultiplier, 0, Float.MAX_VALUE);
        this.explosive = builder.define("explosive", explosive);
        this.fired = builder.define("fired", fired);

        builder.pop();
    }
}
