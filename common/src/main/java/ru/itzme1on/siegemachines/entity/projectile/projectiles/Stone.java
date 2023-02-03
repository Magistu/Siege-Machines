package ru.itzme1on.siegemachines.entity.projectile.projectiles;

import com.mojang.math.Vector3d;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import ru.itzme1on.siegemachines.entity.projectile.Missile;
import ru.itzme1on.siegemachines.entity.projectile.MissileType;
import ru.itzme1on.siegemachines.registry.ItemRegistry;

public class Stone extends Missile {
    public Stone(EntityType<Stone> entityType, Level level) {
        super(entityType, level);
        this.item = ItemRegistry.STONE.get();
    }

    public Stone(EntityType<Stone> entityType, Level level, Vector3d pos, LivingEntity entity, Item item) {
        super(entityType, level, pos, entity, MissileType.STONE, item);
    }
}
