package ru.magistu.siegemachines.api.enitity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public interface Shootable extends Useable {
    void use(@Nullable LivingEntity entity);
    
    @Override
    default UsageType getUsage()
    {
        return UsageType.SHOOT;
    }

    boolean hasAmmo();
    
    float getProjectileInitSpeed();
    
    Vec3 getShotPos();
    
    Vec3 getShotView();

    boolean isValidAmmo(ItemStack stack);

    List<Item> getValidAmmo();
    
    boolean reload(ItemStack stack);
}