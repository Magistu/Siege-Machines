package ru.magistu.siegemachines.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import ru.magistu.siegemachines.item.ModItems;

public class Cannonball extends Missile {
    public Cannonball(EntityType<Cannonball> type, Level level) {
        super(type, level);
    }

    public Cannonball(EntityType<Cannonball> entitytype, Level level, Vec3 pos, LivingEntity shooter, Entity engine) {
        super(entitytype, level, pos, shooter, engine, MissileType.CANNONBALL);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.CANNONBALL.get();
    }
}
