package ru.magistu.siegemachines.config;

/**
 * Fabric-compatible missile specs with public field access.
 */
public class MissileSpecs {
    public final SiegeMachineSpecs.ConfigValue<Double> mass;
    public final SiegeMachineSpecs.ConfigValue<Double> armorpiercing;
    public final SiegeMachineSpecs.ConfigValue<Boolean> explosive;
    public final SiegeMachineSpecs.ConfigValue<Double> knockback;
    public final SiegeMachineSpecs.ConfigValue<Double> explosionpower;
    public final SiegeMachineSpecs.ConfigValue<Double> explosiondamagemultiplier;
    public final SiegeMachineSpecs.ConfigValue<Boolean> fired;

    public MissileSpecs(double mass, double armorPiercing, boolean explosive,
                        double knockback, double explosionPower, double explosionDamageMultiplier, boolean fired) {
        this.mass = new SiegeMachineSpecs.ConfigValue<>(mass);
        this.armorpiercing = new SiegeMachineSpecs.ConfigValue<>(armorPiercing);
        this.explosive = new SiegeMachineSpecs.ConfigValue<>(explosive);
        this.knockback = new SiegeMachineSpecs.ConfigValue<>(knockback);
        this.explosionpower = new SiegeMachineSpecs.ConfigValue<>(explosionPower);
        this.explosiondamagemultiplier = new SiegeMachineSpecs.ConfigValue<>(explosionDamageMultiplier);
        this.fired = new SiegeMachineSpecs.ConfigValue<>(fired);
    }
}
