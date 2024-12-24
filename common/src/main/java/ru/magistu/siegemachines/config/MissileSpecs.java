package ru.magistu.siegemachines.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public class MissileSpecs
{
    public final ModConfigSpec.DoubleValue mass;
    public final ModConfigSpec.DoubleValue explosionpower;
    public final ModConfigSpec.BooleanValue destroysground;

    public MissileSpecs(ModConfigSpec.Builder builder, String name, float mass, float explosionpower, boolean destroysground)
    {
        builder.push(name);

        this.mass = builder.defineInRange("mass", mass,0,Double.MAX_VALUE);
        this.explosionpower = builder.defineInRange("explosionRadius", explosionpower,0,Double.MAX_VALUE);
        this.destroysground = builder.define("destroysGround", destroysground);

        builder.pop();
    }
}
