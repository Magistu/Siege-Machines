package ru.magistu.siegemachines.entity.projectile;

import com.mojang.math.Vector3d;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import ru.magistu.siegemachines.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class Stone extends Missile {
	public Stone(EntityType<Stone> entityType, Level level) {
		super(entityType, level);
		this.item = ModItems.STONE.get();
	}

	public Stone(EntityType<Stone> entityType, Level level, Vector3d pos, LivingEntity entity, Item item) {
		super(entityType, level, pos, entity, MissileType.STONE, item);
	}
}
