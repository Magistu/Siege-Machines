package ru.magistu.siegemachines.config;

/**
 * Fabric-compatible config with values EXACTLY matching original NeoForge mod.
 */
public class SpecsConfig {
    // Global settings
    public static final SiegeMachineSpecs.ConfigValue<Integer> PREVENT_PICKUP_COOLDOWN =
            new SiegeMachineSpecs.ConfigValue<>(300);
    public static final SiegeMachineSpecs.ConfigValue<Integer> DEPLOYMENT_SICKNESS_COOLDOWN =
            new SiegeMachineSpecs.ConfigValue<>(300);
    public static final SiegeMachineSpecs.ConfigValue<Double> FIRE_DAMAGE_MULTIPLIER =
            new SiegeMachineSpecs.ConfigValue<>(3.0);
    public static final SiegeMachineSpecs.ConfigValue<Double> EXPLOSION_DAMAGE_MULTIPLIER =
            new SiegeMachineSpecs.ConfigValue<>(2.0);
    public static final SiegeMachineSpecs.ConfigValue<Double> ARROW_DAMAGE_MULTIPLIER =
            new SiegeMachineSpecs.ConfigValue<>(0.5);
    public static final SiegeMachineSpecs.ConfigValue<Boolean> ALLOW_FRIENDLY_FIRE =
            new SiegeMachineSpecs.ConfigValue<>(false);

    // Machine specs - constructor: (durability, delaytime, projectilespeed, inaccuracy, knockbackresistance)
    // EXACT VALUES FROM NEOFORGE:
    public static final SiegeMachineSpecs MORTAR =
            new SiegeMachineSpecs(80, 200, 2.5, 0.2, 0.5);
    public static final SiegeMachineSpecs CULVERIN =
            new SiegeMachineSpecs(150, 260, 3.5, 0.03, 0.6);
    public static final SiegeMachineSpecs TREBUCHET =
            new SiegeMachineSpecs(350, 400, 2.8, 0.2, 1.0);
    public static final SiegeMachineSpecs CATAPULT =
            new SiegeMachineSpecs(150, 200, 2.0, 0.2, 0.8);
    public static final SiegeMachineSpecs BALLISTA =
            new SiegeMachineSpecs(70, 120, 4.5, 0.04, 0.4);
    public static final SiegeMachineSpecs BATTERING_RAM =
            new SiegeMachineSpecs(500, 100, 0.0, 0.5, 1.0);
    public static final SiegeMachineSpecs SIEGE_LADDER =
            new SiegeMachineSpecs(400, 0, 0.0, 0.0, 1.0);

    // Missile specs - constructor: (mass, armorPiercing, explosive, knockback, explosionPower, explosionDamageMultiplier, fired)
    // EXACT VALUES FROM NEOFORGE:
    // NeoForge order: mass, explosionpower, armorpiercing, knockback, explosiondamagemultiplier, explosive, fired
    public static final MissileSpecs CANNONBALL =
            new MissileSpecs(15.0, 1.0, true, 1.5, 3.0, 1.5, false);
    public static final MissileSpecs STONE =
            new MissileSpecs(50.0, 1.0, true, 1.5, 2.5, 1.8, false);
    public static final MissileSpecs GIANT_STONE =
            new MissileSpecs(70.0, 1.0, true, 3.0, 5.0, 3.0, false);
    public static final MissileSpecs GIANT_ARROW =
            new MissileSpecs(5.0, 0.5, false, 1.0, 0.0, 1.0, false);

    public static void init() {
        // Config is static
    }

    public static void register() {
        // Config is static
    }
}
