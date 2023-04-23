package ru.itzme1on.siegemachines.entity.machine.machines;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.itzme1on.siegemachines.SiegeMachines;
import ru.itzme1on.siegemachines.entity.machine.MachineType;
import ru.itzme1on.siegemachines.entity.machine.ShootingMachine;
import ru.itzme1on.siegemachines.gui.machine.crosshair.Crosshair;
import ru.itzme1on.siegemachines.gui.machine.crosshair.ReloadingCrosshair;
import ru.itzme1on.siegemachines.registry.ItemRegistry;
import ru.itzme1on.siegemachines.registry.SoundRegistry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;


public class Catapult extends ShootingMachine implements IAnimatable
{
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    static AnimationBuilder SHOOTING_ANIM = new AnimationBuilder().addAnimation("Shooting", ILoopType.EDefaultLoopTypes.LOOP);
    static AnimationBuilder RELOADING_ANIM = new AnimationBuilder().addAnimation("Reloading", ILoopType.EDefaultLoopTypes.LOOP);
    static AnimationBuilder IDLE_RELOADED_ANIM = new AnimationBuilder().addAnimation("IdleReloaded", ILoopType.EDefaultLoopTypes.LOOP);
    static AnimationBuilder IDLE_NOT_RELOADED_ANIM = new AnimationBuilder().addAnimation("IdleNotReloaded", ILoopType.EDefaultLoopTypes.LOOP);

    public enum State
    {
        SHOOTING,
        RELOADING,
        IDLE_RELOADED,
        IDLE_NOT_RELOADED
    }
    public State state = State.RELOADING;

    public Catapult(EntityType<? extends MobEntity> entityType, World level) {
        super(entityType, level, MachineType.CATAPULT);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        switch (state) {
            case SHOOTING -> {
                event.getController().setAnimation(SHOOTING_ANIM);
                return PlayState.CONTINUE;
            }
            case IDLE_RELOADED -> {
                event.getController().setAnimation(IDLE_RELOADED_ANIM);
                return PlayState.CONTINUE;
            }
            case RELOADING -> {
                event.getController().setAnimation(RELOADING_ANIM);
                return PlayState.CONTINUE;
            }
            case IDLE_NOT_RELOADED -> {
                event.getController().setAnimation(IDLE_NOT_RELOADED_ANIM);
                return PlayState.CONTINUE;
            }
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<?> controller = new AnimationController<>(this, "controller", 1, (t) -> {
            if (this.state.equals(State.RELOADING))
                return (double) (this.type.delayTime - this.delayTicks) / this.type.delayTime;

            return t;
        }, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (super.interactMob(player, hand) == ActionResult.SUCCESS)
            return ActionResult.SUCCESS;

        if (!this.world.isClient && !this.hasPassengers()) {
            player.startRiding(this);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    public void startShooting(PlayerEntity player) {
        if (this.delayTicks <= 0 && this.useTicks <= 0 && this.shootingTicks <= 0) {
            this.state = State.SHOOTING;
            this.useTicks = this.type.useTime;
            this.shootingTicks = this.type.useRealiseTime;

            Vec3d pos = this.getPos();
            this.world.playSound(pos.x, pos.y, pos.z, SoundRegistry.CATAPULT_SHOOTING.get(), SoundCategory.BLOCKS, 1.5f, 1.0f, false);
        }
    }

    @Override
    public void shoot() {
        if (!world.isClient) super.shoot();
    }

    @Override
    public void travel(Vec3d pos) {
        if (this.isAlive()) {
            if (this.hasPassengers() && this.useTicks <= 0 && this.delayTicks <= 0) {
                LivingEntity livingentity = (LivingEntity) this.getPrimaryPassenger();

                this.setTurretRotations(livingentity.getPitch(), this.getTurretYaw());
                this.updateTurretRotations();

                this.setYawDest(livingentity.getYaw());
                this.updateYaw();
            }
            super.travel(pos);
        }
    }

    @Override
    public void tick() {
        if (this.useTicks != 0 && --this.useTicks <= 0) {
            this.state = State.RELOADING;
            this.useTicks = 0;
            this.delayTicks = this.type.delayTime;
        }

        if (this.shootingTicks != 0 && --this.shootingTicks <= 0)
        {
            this.useRealise();
            this.shootingTicks = 0;
        }

        if (!world.isClient && this.isOnGround())
            this.setVelocity(this.getVelocity().multiply(0.0, 1.0, 0.0));

        if (this.delayTicks > 0 && this.hasPassengers()) {
            if (this.delayTicks % 20 == 0) {
                Vec3d pos = this.getPos();
                this.world.playSound(pos.x, pos.y, pos.z, SoundRegistry.CATAPULT_RELOADING.get(), SoundCategory.BLOCKS, 1.0f, 1.0f, false);
            }

            if (--this.delayTicks <= 0) this.state = State.IDLE_RELOADED;
        }

        if (this.renderUpdateTicks-- <= 0) {
            this.updateMachineRender();
            this.renderUpdateTicks = SiegeMachines.RENDER_UPDATE_TIME;
        }

        super.tick();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Crosshair createCrosshair()
    {
        return new ReloadingCrosshair();
    }

    @Override
    public Item getMachineItem() {
        return ItemRegistry.CATAPULT.get();
    }
}