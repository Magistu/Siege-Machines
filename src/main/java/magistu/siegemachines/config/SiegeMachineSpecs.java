package magistu.siegemachines.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class SiegeMachineSpecs
{
    private final String name;

    public final ForgeConfigSpec.ConfigValue<Integer> durability;
    public final ForgeConfigSpec.ConfigValue<Integer> delaytime;
    public final ForgeConfigSpec.ConfigValue<Float> projectilespeed;
    public final ForgeConfigSpec.ConfigValue<Float> inaccuracy;

    public SiegeMachineSpecs(ForgeConfigSpec.Builder builder, String name, int durability, int delaytime, float projectilespeed, float inaccuracy)
    {
        this.name = name;

        builder.push(name);

        this.durability = builder.define("durability", durability);
        this.delaytime = builder.define("reloadTime", delaytime);
        this.projectilespeed = builder.define("projectileSpeed", projectilespeed);
        this.inaccuracy = builder.define("inaccuracy", inaccuracy);

        builder.pop();
    }
}
