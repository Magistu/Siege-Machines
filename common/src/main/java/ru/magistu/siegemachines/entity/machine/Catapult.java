package ru.magistu.siegemachines.entity.machine;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;


public class Catapult extends ShootingMachine implements ShootingGeoEntity {
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public Catapult(EntityType<? extends Mob> entitytype, Level level, MachineType type) {
        super(entitytype, level, type);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {

        if (super.mobInteract(player, hand) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }
        if (!this.level().isClientSide() && !this.isVehicle()) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public void startShooting(LivingEntity entity) {
        if (getDelayTicks() <= 0 && getUseTicks() <= 0 && this.shootingticks <= 0) {
            usesoundplayer.run();
            setUseTicks(type.usetime);
            this.shootingticks = this.type.usereleasetime;
        }
    }

    @Override
    public boolean isStationary() {
        return true;
    }

    public float getReloadProgress() {
        return ((float) this.type.specs.delaytime.get() - getDelayTicks()) / type.specs.delaytime.get();
    }
}