package ru.magistu.siegemachines.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;
import ru.magistu.siegemachines.item.ModItems;

public class Stone extends Missile {
    public Stone(EntityType<Stone> entitytype, Level level) {
        super(entitytype, level);
    }

    public Stone(EntityType<Stone> entitytype, Level level, Vector3d pos, LivingEntity entity, Entity engine) {
        super(entitytype, level, pos, entity, engine, MissileType.STONE);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.STONE.get();
    }
}
