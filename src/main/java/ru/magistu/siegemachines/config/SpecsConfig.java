package ru.magistu.siegemachines.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public final class SpecsConfig
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final SiegeMachineSpecs MORTAR;
    public static final SiegeMachineSpecs CULVERIN;
    public static final SiegeMachineSpecs TREBUCHET;
    public static final SiegeMachineSpecs CATAPULT;
    public static final SiegeMachineSpecs BALLISTA;
    public static final SiegeMachineSpecs BATTERING_RAM;
    public static final SiegeMachineSpecs SIEGE_LADDER;

    public static final MissileSpecs CANNONBALL;
    public static final MissileSpecs STONE;
    public static final MissileSpecs GIANT_STONE;

    static
    {
        BUILDER.push("siege_machines");
        
        MORTAR = new SiegeMachineSpecs(BUILDER, "mortar", 80, 200, 2.5f, 0.2f, 1.5f);
        CULVERIN = new SiegeMachineSpecs(BUILDER, "culverin", 150, 260, 3.5f, 0.03f, 3.0f);
        TREBUCHET = new SiegeMachineSpecs(BUILDER, "trebuchet", 350, 400, 2.8f, 0.2f, 2.0f);
        CATAPULT = new SiegeMachineSpecs(BUILDER, "catapult", 150, 200, 2.0f, 0.2f, 1.0f);
        BALLISTA = new SiegeMachineSpecs(BUILDER, "ballista", 70, 120, 4.5f, 0.04f, 1.2f);
        BATTERING_RAM = new SiegeMachineSpecs(BUILDER, "battering_ram", 500, 100, 0.0f, 0.5f, 0.0f);
        SIEGE_LADDER = new SiegeMachineSpecs(BUILDER, "siege_ladder", 400, 0, 0.0f, 0.0f, 0.0f);

        BUILDER.pop();

        BUILDER.push("missiles");

        CANNONBALL = new MissileSpecs(BUILDER, "cannonball", 15.0f, 3.0f, false);
        STONE = new MissileSpecs(BUILDER, "stone", 50.0f, 2.0f, false);
        GIANT_STONE = new MissileSpecs(BUILDER, "giant_stone", 70.0f, 5.0f, false);

        BUILDER.pop();
        
        SPEC = BUILDER.build();
    }
    
    public static void register()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "siege-machines-specs.toml");
    }
}
