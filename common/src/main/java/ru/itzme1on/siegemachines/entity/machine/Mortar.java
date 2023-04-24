package ru.itzme1on.siegemachines.entity.machine;

import com.google.common.base.Suppliers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.itzme1on.siegemachines.SiegeMachines;
import ru.itzme1on.siegemachines.entity.IReloading;
import ru.itzme1on.siegemachines.client.gui.machine.crosshair.Crosshair;
import ru.itzme1on.siegemachines.client.gui.machine.crosshair.ReloadingCrosshair;
import ru.itzme1on.siegemachines.item.ModItems;
import ru.itzme1on.siegemachines.audio.ModSounds;
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


public class Mortar extends ShootingMachine implements IAnimatable, IReloading {
    public static final Supplier<EntityType<Mortar>> TYPE = Suppliers.memoize(() -> EntityType.Builder.create(Mortar::new, SpawnGroup.MISC)
            .setDimensions(0.5F, 0.5F)
            .build("mortar"));

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    
    static AnimationBuilder MOVING_ANIMATION = new AnimationBuilder().addAnimation("Moving", ILoopType.EDefaultLoopTypes.LOOP);

    public int shootingTicks = 0;
    private double wheelsPitch = 0.0;
    private double wheelsSpeed = 0.0;
    private int wheelsSoundTicks = 10;
    
    public Mortar(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world, MachineType.MORTAR);
    }
    
    private <E extends IAnimatable> PlayState wheels_predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(MOVING_ANIMATION);
        
        return PlayState.CONTINUE;
    }

    @Override
    public void tick() {
        if (this.useTicks != 0 && --this.useTicks <= 0) {
            this.useTicks = 0;
            this.delayTicks = this.type.delayTime;
        }

        if (this.shootingTicks != 0 && --this.shootingTicks <= 0) {
            if (this.inventory.containsItem(Items.GUNPOWDER)) this.useRealise();

            else if (!this.world.isClient) {
                Entity passenger = this.getPrimaryPassenger();
                if (passenger instanceof PlayerEntity player)
                    player.sendMessage(new TranslatableText(SiegeMachines.MOD_ID + ".no_gunpowder").formatted(Formatting.RED), true);
            }

            this.shootingTicks = 0;
        }

        if (!world.isClient() && this.isOnGround())
            this.setVelocity(this.getWheelsDeltaMovement());

        if (this.delayTicks > 0 && this.hasPassengers())
            --this.delayTicks;

        if (this.renderUpdateTicks-- <= 0) {
            this.updateMachineRender();
            this.renderUpdateTicks = SiegeMachines.RENDER_UPDATE_TIME;
        }

        if (this.getWheelsSpeed() > 0.0081 && this.wheelsSoundTicks-- <= 0) {
            this.world.playSound(this.getX(), this.getY(), this.getZ(), ModSounds.CANNON_WHEELS.get(), SoundCategory.NEUTRAL, 0.3f, 1.0f, true);
            this.wheelsSoundTicks = 20;
        }

        super.tick();
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (stack.getItem().equals(Items.FLINT_AND_STEEL)) {
            if (this.useTicks <= 0 && this.shootingTicks <= 0) {
                stack.damage(1, player, (entity) -> {
                    entity.sendToolBreakStatus(hand);
//                    net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, this.useItem, hand);
                });
                this.startShooting(player);
            }
            
            return ActionResult.SUCCESS;
        }
        
        if (stack.getItem().equals(Items.GUNPOWDER)) {
            if (!this.inventory.containsItem(Items.GUNPOWDER)) {
                if (!player.isCreative()) stack.decrement(1);
                
                this.inventory.putItem(Items.GUNPOWDER);
            }
            
            return ActionResult.SUCCESS;
        }
        
        if (super.interactMob(player, hand) == ActionResult.SUCCESS) return ActionResult.SUCCESS;

        if (!this.world.isClient() && !this.hasPassengers()) {
            player.startRiding(this);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Crosshair createCrosshair() {
        return new ReloadingCrosshair();
    }

    @Override
    public Item getMachineItem() {
        return ModItems.MORTAR.get();
    }

    @Override
    public void travel(Vec3d pos)  {
        if (this.isAlive()) {
            if (this.hasPassengers()) {
                LivingEntity livingentity = (LivingEntity) this.getPrimaryPassenger();

                this.setTurretRotationsDest(livingentity.getPitch(), livingentity.getYaw() - this.getYaw());
                this.setYawDest(livingentity.getYaw());

                this.updateYaw();
                this.updateTurretRotations();

                float f0 = livingentity.sidewaysSpeed * 0.2f;
                float f1 = livingentity.forwardSpeed;
                if (f1 <= 0.0f) f1 *= 0.5f;

                this.setMovementSpeed(0.04f);

                pos = new Vec3d(f0, pos.y, f1);
            }

            super.travel(pos);
        }
    }

    @Override
    public void startShooting(PlayerEntity player) {
        if (this.delayTicks <= 0 && this.useTicks <= 0 && this.shootingTicks <= 0) {
            if (!this.world.isClient)
                this.world.playSound(
                        null, this.getX(), this.getY(), this.getZ(),
                        ModSounds.FUSE.get(), SoundCategory.BLOCKS, this.getVolumeFromDist(this.distanceTo(player)), 0.8f);

            this.useTicks = this.type.useTime;
            this.shootingTicks = this.type.useRealiseTime;
        }
    }

    @Override
    public void shoot() {
        if (!world.isClient()) {
            super.shoot();
            this.setVelocity(this.getVelocity().subtract(this.getShotView().multiply(0.25)));
            this.velocityDirty = true;
            this.inventory.shrinkItem(Items.GUNPOWDER);
        }

        else {
            this.blowParticles(ParticleTypes.FLAME, 0.035, 25);
            this.blowParticles(ParticleTypes.CLOUD, 0.2, 60);
            Vec3d pos = this.getPos();
            this.world.playSound(pos.x, pos.y, pos.z, ModSounds.MORTAR_SHOOTING.get(), SoundCategory.BLOCKS, 1.5f, 0.85f + this.world.random.nextFloat() * 0.3f, false);
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<?> wheels_controller = new AnimationController<>(this, "wheels_controller", 1, (t) -> {
            double d = this.getWheelsSpeed();
            this.wheelsSpeed = d > 0 ? Math.min(d, 1.0) : Math.max(d, -1.0);
            return wheelsPitch += 0.013 * this.wheelsSpeed;
        }, this::wheels_predicate);
        data.addAnimationController(wheels_controller);
    }

    public double getWheelsSpeed() {
        if (this.onGround)
            return this.getRotationVec(5.0f)
                    .multiply(1, 0, 1)
                    .dotProduct(this.getVelocity());

        return 0.0;
    }

    public Vec3d getWheelsDeltaMovement() {
        if (this.isOnGround()) {
            Vec3d view = this.getRotationVec(1.0f);
            Vec3d movement = this.getVelocity();

            double d0 = movement.x * view.x + movement.z * view.z;

            double d1 = d0 * view.x;
            double d2 = 0.0;
            double d3 = d0 * view.z;

            return new Vec3d(d1, d2, d3);
        }

        return Vec3d.ZERO;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
