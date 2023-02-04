package ru.itzme1on.siegemachines.entity.projectile.projectiles;

import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import ru.itzme1on.siegemachines.entity.projectile.Missile;
import ru.itzme1on.siegemachines.entity.projectile.MissileType;
import ru.itzme1on.siegemachines.registry.ItemRegistry;

public class Stone extends Missile {

    public Stone(EntityType<Stone> entityType, World world) {
        super(entityType, world);
        this.item = ItemRegistry.STONE.get();
    }

    public Stone(EntityType<Stone> entityType, World world, Vector3d pos, LivingEntity entity, Item item) {
        super(entityType, world, pos, entity, MissileType.STONE, item);
    }
}
