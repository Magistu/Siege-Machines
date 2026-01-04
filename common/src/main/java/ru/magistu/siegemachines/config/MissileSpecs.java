package ru.magistu.siegemachines.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public class MissileSpecs {
    public final ModConfigSpec.DoubleValue mass;
    public final ModConfigSpec.DoubleValue explosionpower;
    public final ModConfigSpec.DoubleValue armorpiercing;
    public final ModConfigSpec.DoubleValue knockback;
    public final ModConfigSpec.DoubleValue explosiondamagemultiplier;
    public final ModConfigSpec.BooleanValue explosive;
    public final ModConfigSpec.BooleanValue fired;

    public MissileSpecs(ModConfigSpec.Builder builder, String name, double mass, double explosionpower, double armorpiercing, double knockback, double explosiondamagemultiplier, boolean explosive, boolean fired) {
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
