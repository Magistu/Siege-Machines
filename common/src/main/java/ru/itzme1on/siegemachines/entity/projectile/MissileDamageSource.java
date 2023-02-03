package ru.itzme1on.siegemachines.entity.projectile;

import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class MissileDamageSource extends IndirectEntityDamageSource {
    public MissileDamageSource(Entity entity, @Nullable Entity entity2) {
        super("thrown", entity, entity2);
    }

    public static MissileDamageSource thrown(Entity entity, @Nullable Entity entity2, boolean bypassArmor) {
        MissileDamageSource damage = new MissileDamageSource(entity, entity2);
        if (bypassArmor) damage.bypassArmor();
        return damage;
    }
}