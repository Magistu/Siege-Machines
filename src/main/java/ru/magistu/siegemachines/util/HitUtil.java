package ru.magistu.siegemachines.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class HitUtil {
    public static HitResult getBlockHitResult(Vec3 from, Vec3 delta, Level level, ClipContext.Block clipcontext) {
        Vec3 to = from.add(delta);
        return level.clip(new ClipContext(from, to, clipcontext, ClipContext.Fluid.NONE, null));
    }

    public static HitResult getHitResult(Vec3 from, Entity entity, Predicate<Entity> filter, Vec3 delta, Level level, float margin, ClipContext.Block clipcontext) {
        Vec3 to = from.add(delta);
        HitResult blockhit = level.clip(new ClipContext(from, to, clipcontext, ClipContext.Fluid.NONE, null));
        if (blockhit.getType() != HitResult.Type.MISS) {
            to = blockhit.getLocation();
        }

        HitResult entityhit = ProjectileUtil.getEntityHitResult(level, entity, from, to, new AABB(from, to).inflate(1.0), filter, margin);
        if (entityhit != null) {
            return entityhit;
        }

        return blockhit;
    }
}
