package ru.itzme1on.siegemachines.entity.machine.machines;

import com.google.common.base.Suppliers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import ru.itzme1on.siegemachines.SiegeMachinesCore;
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

import java.util.function.Supplier;

public class Ballista extends ShootingMachine implements IAnimatable {
    public static final Supplier<EntityType<Ballista>> TYPE = Suppliers.memoize(() -> EntityType.Builder.create(Ballista::new, SpawnGroup.MISC)
            .setDimensions(0.5F, 0.5F)
            .build("giant_arrow"));

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    static AnimationBuilder SHOOTING_ANIM = new AnimationBuilder()
            .addAnimation("Shooting", ILoopType.EDefaultLoopTypes.LOOP);
    static AnimationBuilder RELOADING_ANIM = new AnimationBuilder()
            .addAnimation("Reloading", ILoopType.EDefaultLoopTypes.LOOP);

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return null;
    }

    public enum State {
        SHOOTING,
        RELOADING
    }
    public State state = State.RELOADING;

    public Ballista(EntityType<Ballista> entityType, World world) {
        super(entityType, world, MachineType.BALLISTA);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        switch (state) {
            case SHOOTING -> {
                event.getController().setAnimation(SHOOTING_ANIM);
                return PlayState.CONTINUE;
            }
            case RELOADING -> {
                event.getController().setAnimation(RELOADING_ANIM);
                return PlayState.CONTINUE;
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 1, (t) -> {
            if (this.state.equals(State.RELOADING))
                return (double) (this.type.delayTime - this.delayTicks) / this.type.delayTime;

            return t;
        }, this::predicate));
    }

    @Override
    public boolean canBeControlledByRider() {
        return true;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.hasPassengers()) {
            player.startRiding(this);
            return super.interactMob(player, hand);
        }

        return super.interactMob(player, hand);
    }

    @Override
    public Crosshair createCrosshair() {
        return new ReloadingCrosshair();
    }

    @Override
    public Item getMachineItem() {
        return ItemRegistry.BALLISTA.get();
    }

    @Override
    public void startShooting(PlayerEntity player) {
        if (this.delayTicks <= 0 && this.useTicks <= 0 && this.shootingTicks <= 0) {
            this.state = State.SHOOTING;
            this.useTicks = this.type.useTime;
            this.shootingTicks = this.type.useRealiseTime;

            Vec3d pos = this.getPos();
            world.playSound(
                    pos.x,
                    pos.y,
                    pos.z,
                    (SoundEvent) SoundRegistry.BALLISTA_SHOOTING,
                    SoundCategory.BLOCKS,
                    1.0f,
                    1.0f,
                    false);
        }
    }

    @Override
    public void shoot() {
        if (!world.isClient)
            super.shoot();
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.isAlive()) {
            if (this.hasPassengers() && this.useTicks <= 0 && this.delayTicks <= 0) {
                LivingEntity entity = (LivingEntity) this.getPrimaryPassenger();

                assert entity != null;
                this.setTurretRotations(entity.getPitch(), this.getTurretYaw());
                this.updateTurretRotations();

                this.setYawDest(entity.getYaw());
                this.updateYaw();
            }

            super.travel(movementInput);
        }
    }

    @Override
    public void tick() {
        if (this.useTicks != 0 && --this.useTicks <= 0) {
            this.state = State.RELOADING;
            this.useTicks = 0;
            this.delayTicks = this.type.delayTime;
        }

        if (this.shootingTicks != 0 && --this.shootingTicks <= 0) {
            this.useRealise();
            this.shootingTicks = 0;
        }

        if (!world.isClient && this.isOnGround())
            this.setVelocity(this.getVelocity().multiply(0.0, 1.0, 0.0));

        if (this.delayTicks > 0 && this.hasPassengers()) {
            if (this.delayTicks % 21 == 0) {
//                Vec3d pos = this.getPos();
                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                player.playSound((SoundEvent) SoundRegistry.BALLISTA_RELOADING, 1, 1);
            }
            --this.delayTicks;
        }

        if (this.renderUpdateTicks-- <= 0) {
            this.updateMachineRender();
            this.renderUpdateTicks = SiegeMachinesCore.RENDER_UPDATE_TIME;
        }

        super.tick();
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
