package ru.magistu.siegemachines.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MissileSpecs
{
    public final ForgeConfigSpec.ConfigValue<Float> mass;
    public final ForgeConfigSpec.ConfigValue<Float> explosionpower;
    public final ForgeConfigSpec.ConfigValue<Boolean> destroysground;

    public MissileSpecs(ForgeConfigSpec.Builder builder, String name, float mass, float explosionpower, boolean destroysground) 
    {
        builder.push(name);

        this.mass = builder.define("mass", mass);
        this.explosionpower = builder.define("explosionRadius", explosionpower);
        this.destroysground = builder.define("destroysGround", destroysground);

        builder.pop();
    }
}
