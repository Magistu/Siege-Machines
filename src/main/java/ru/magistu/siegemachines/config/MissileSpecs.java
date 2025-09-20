package ru.magistu.siegemachines.config;


import net.minecraftforge.common.ForgeConfigSpec;

public class MissileSpecs {
    public final ForgeConfigSpec.DoubleValue mass;
    public final ForgeConfigSpec.DoubleValue explosionpower;
    public final ForgeConfigSpec.BooleanValue destroysground;

    public MissileSpecs(ForgeConfigSpec.Builder builder, String name, float mass, float explosionpower, boolean destroysground) {
        builder.push(name);

        this.mass = builder.defineInRange("mass", mass, 0, Double.MAX_VALUE);
        this.explosionpower = builder.defineInRange("explosionRadius", explosionpower, 0, Double.MAX_VALUE);
        this.destroysground = builder.define("destroysGround", destroysground);

        builder.pop();
    }
}
