package ru.magistu.siegemachines.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import ru.magistu.siegemachines.item.ModItems;

public class GiantStone extends Missile {
    public GiantStone(EntityType<GiantStone> entitytype, Level level) {
        super(entitytype, level);
    }

    public GiantStone(EntityType<GiantStone> entitytype, Level level, Vector3d pos, LivingEntity entity, Entity engine) {
        super(entitytype, level, pos, entity, engine, MissileType.GIANT_STONE);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ModItems.GIANT_STONE.get();
    }
}
