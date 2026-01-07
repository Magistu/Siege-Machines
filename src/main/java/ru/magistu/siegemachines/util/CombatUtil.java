package ru.magistu.siegemachines.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import ru.magistu.siegemachines.config.SpecsConfig;

public class CombatUtil {

    public static boolean canHurt(Entity attacker, Entity victim) {
        if (attacker == null || SpecsConfig.ALLOW_FRIENDLY_FIRE.get()) {
            return true;
        }
        if (attacker instanceof LivingEntity livingowner && victim instanceof TamableAnimal animal && animal.isOwnedBy(livingowner)) {
            return false;
        }
        return victim.getTeam() == null || !attacker.isAlliedTo(victim);
    }
}
