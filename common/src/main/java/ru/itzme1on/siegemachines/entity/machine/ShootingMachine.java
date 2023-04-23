package ru.itzme1on.siegemachines.entity.machine;

import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.itzme1on.siegemachines.SiegeMachines;
import ru.itzme1on.siegemachines.entity.IReloading;
import ru.itzme1on.siegemachines.entity.projectile.Missile;
import ru.itzme1on.siegemachines.entity.projectile.ProjectileBuilder;
import ru.itzme1on.siegemachines.network.PacketHandler;
import ru.itzme1on.siegemachines.network.PacketMachineInventorySlot;

import java.util.Arrays;
import java.util.Random;

public abstract class ShootingMachine extends Machine implements IReloading {
    public int shootingTicks = 0;

    protected ShootingMachine(EntityType<? extends MobEntity> entityType, World world, MachineType type) {
        super(entityType, world, type);
    }

    public abstract void startShooting(PlayerEntity player);

    public void shoot() {
        if (this.type.ammo.length == 0)
            return;

        ProjectileBuilder projectileBuilder = this.getProjectileBuilder();

//        if (projectileBuilder.equals(ProjectileBuilder.NONE)) {
//            Entity passenger = this.getPrimaryPassenger();
//            if (passenger instanceof PlayerEntity)
//                passenger.sendSystemMessage(new TranslatableText(SiegeMachines.MOD_ID + ".no_ammo").formatted(Formatting.RED), SiegeMachines.CHAT_UUID);
//            return;
//        }

        LivingEntity livingEntity = (LivingEntity) this.getPrimaryPassenger();
        ProjectileEntity projectile = projectileBuilder.factory.create(
                projectileBuilder.entityType,
                this.world,
                new Vector3d(
                        this.getShotPos().x,
                        this.getShotPos().y,
                        this.getShotPos().z),
                livingEntity == null ? this : livingEntity,
                projectileBuilder.projectileItem
        );

        if (projectile instanceof Missile missile)
            missile.setItem(new ItemStack(missile.getDefaultItem()));

        projectile.setVelocity(
                this,
                this.getTurretPitch(),
                this.getGlobalTurretYaw(),
                0.0f,
                this.type.projectileSpeed,
                this.type.inaccuracy);

        this.world.spawnEntity(projectile);
        this.inventory.shrinkItem(projectileBuilder.item);
    }

    @Override
    public void use(PlayerEntity player) {
//        if (!this.world.isClient) {}
        this.startShooting(player);
    }

    @Override
    public void useRealise() {
//        if (!this.world.isClient) {}
        this.shoot();
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (this.isValidAmmo(stack)) {
            if (!this.hasAmmo()) {
                if (!player.isCreative())
                    stack.decrement(1);
                this.inventory.putItem(stack.getItem());
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    protected Vec3d getShotPos() {
        double pitch = this.getTurretPitch() * Math.PI / 180.0;
        double yaw = (this.getYaw(0.5f) + this.getTurretYaw()) * Math.PI / 180.0;

        return this.getPos()
                .add(applyRotation(this.type.turretPivot, 0.0, yaw)
                        .add(applyRotation(this.type.turretVector, pitch, yaw)));
    }

    protected Vec3d getShotView() {
        double pitch = this.getTurretPitch() * Math.PI / 180.0;
        double yaw = this.getGlobalTurretYaw() * Math.PI / 180.0;

        double d0 = -Math.sin(yaw) * Math.cos(pitch);
        double d1 = -Math.sin(pitch);
        double d2 = Math.cos(yaw) * Math.cos(pitch);

        return new Vec3d(d0, d1, d2).normalize();
    }

    protected void blowParticles(ParticleEffect particle, double speed, int amount) {
        for (int i = 0; i < amount; ++i) {
            Vec3d pos = this.getShotPos();
            Vec3d inaccuracy = new Vec3d(new Random().nextGaussian() * 0.2,
                    new Random().nextGaussian() * 0.2,
                    new Random().nextGaussian() * 0.2);
            Vec3d velocity = this.getShotView().add(inaccuracy).multiply(speed);

            this.world.addParticle(particle, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z);
        }
    }

    @Override
    public void updateMachineRender() {
        if (!this.world.isClient) {
            for (int i = 0; i < rows * 9; ++i) {
                if (this.isValidAmmo(this.inventory.getStack(i)))
                    PacketMachineInventorySlot.sendToAllAround(this, i);
            }
        }
    }

    public boolean isValidAmmo(Item entry) {
        return Arrays.stream(this.type.ammo).anyMatch(builder -> builder.item.equals(entry));
    }

    public boolean isValidAmmo(ItemStack stack) {
        return this.isValidAmmo(stack.getItem());
    }

    public ItemStack getAmmo() {
        return this.inventory.items.stream().filter(this::isValidAmmo).findFirst().orElse(ItemStack.EMPTY);
    }

    public boolean hasAmmo() {
        return this.inventory.items.stream().anyMatch(this::isValidAmmo);
    }

    public ProjectileBuilder<?> getProjectileBuilder() {
        ItemStack ammo = this.getAmmo();
        if (ammo.equals(ItemStack.EMPTY))
            return ProjectileBuilder.NONE;

        return Arrays.stream(this.type.ammo).filter(builder ->
                builder.item.equals(ammo.getItem())).findFirst().orElse(ProjectileBuilder.NONE);
    }
}
