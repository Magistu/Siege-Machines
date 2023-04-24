package ru.itzme1on.siegemachines.entity.machine;

import com.google.common.base.Suppliers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import ru.itzme1on.siegemachines.client.gui.machine.crosshair.Crosshair;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Supplier;

public class Trebuchet extends ShootingMachine implements IAnimatable {
    public static final Supplier<EntityType<Trebuchet>> TYPE = Suppliers.memoize(() -> EntityType.Builder.create(Trebuchet::new, SpawnGroup.MISC)
            .setDimensions(0.5F, 0.5F)
            .build("mortar"));

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    static AnimationBuilder SHOOTING_ANIM = new AnimationBuilder().addAnimation("Shooting", ILoopType.EDefaultLoopTypes.LOOP);
    static AnimationBuilder RELOADING_ANIM = new AnimationBuilder().addAnimation("Reloading", ILoopType.EDefaultLoopTypes.LOOP);
    static AnimationBuilder IDLE_RELOADED_ANIM = new AnimationBuilder().addAnimation("IdleReloaded", ILoopType.EDefaultLoopTypes.LOOP);
    static AnimationBuilder IDLE_NOT_RELOADED_ANIM = new AnimationBuilder().addAnimation("IdleNotReloaded", ILoopType.EDefaultLoopTypes.LOOP);

    public enum State {
        SHOOTING,
        RELOADING,
        IDLE_RELOADED,
        IDLE_NOT_RELOADED
    }
    public State state = State.RELOADING;

    public Trebuchet(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world, MachineType.TREBUCHET);
    }

    @Override
    public Crosshair createCrosshair() {
        return null;
    }

    @Override
    public Item getMachineItem() {
        return null;
    }

    @Override
    public void startShooting(PlayerEntity player) {

    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return null;
    }
}
