package ru.magistu.siegemachines.config;

/**
 * Fabric-compatible specs matching NeoForge API.
 * Fields: durability, delaytime, projectilespeed, inaccuracy, knockbackresistance
 */
public final class SiegeMachineSpecs {
    public final ConfigValue<Integer> durability;
    public final ConfigValue<Integer> delaytime;
    public final ConfigValue<Double> projectilespeed;
    public final ConfigValue<Double> inaccuracy;
    public final ConfigValue<Double> knockbackresistance;

    public SiegeMachineSpecs(int durability, int delaytime, double projectilespeed,
                             double inaccuracy, double knockbackresistance) {
        this.durability = new ConfigValue<>(durability);
        this.delaytime = new ConfigValue<>(delaytime);
        this.projectilespeed = new ConfigValue<>(projectilespeed);
        this.inaccuracy = new ConfigValue<>(inaccuracy);
        this.knockbackresistance = new ConfigValue<>(knockbackresistance);
    }

    public static class ConfigValue<T> {
        private final T value;

        public ConfigValue(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }

        public T getDefault() {
            return value;
        }
    }
}
