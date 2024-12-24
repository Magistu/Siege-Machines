package ru.magistu.siegemachines.entity.machine;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.item.ModItems;
import ru.magistu.siegemachines.platform.Services;
import ru.magistu.siegemachines.util.CartesianGeometry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SiegeLadder extends Machine implements GeoEntity
{
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    
    private static final Vec3 CLIMB_VECTOR = new Vec3(0.0, 130.0, 130.0).scale(1.0 / 16.0);
    private static final Vec3 CLIMB_PIVOT_1 = new Vec3(-8.0, 0.0, -37.0).scale(1.0 / 16.0);
    private static final Vec3 CLIMB_PIVOT_2 = new Vec3(8.0, 0.0, -37.0).scale(1.0 / 16.0);

    private static final int NUMBER_OF_SEATS = 16;
    
    private final List<LadderSeat> leftseats;
    private final List<LadderSeat> rightseats;
    public final List<LadderSeat> seats;

    private final int wheelssoundticks = 10;
    private double lastwheelpitch;
    private double wheelspitch = 0.0;


    public SiegeLadder(EntityType<? extends Mob> entitytype, Level level)
    {
        super(entitytype, level, MachineType.SIEGE_LADDER);
        
        this.leftseats = Stream.generate(() -> new LadderSeat(this)).limit(NUMBER_OF_SEATS / 2).collect(Collectors.toList());
        this.rightseats = Stream.generate(() -> new LadderSeat(this)).limit(NUMBER_OF_SEATS / 2).collect(Collectors.toList());
        this.seats = Stream.of(this.leftseats, this.rightseats)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
	}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache()
    {
        return this.factory;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand)
    {
        if (player.level().isClientSide() || player.isPassenger())
            return InteractionResult.PASS;

        if (!this.isVehicle())
        {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }

        LadderSeat seat = this.getFreeSeat(player);
        if (seat != null)
        {
            player.startRiding(seat);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
	public void travel(Vec3 pos)
    {
		if (this.isAlive())
        {
            if (this.isVehicle())
            {
			    LivingEntity livingentity = this.getControllingPassenger();

                this.setYawDest(livingentity.getYRot());

                this.updateYaw();

				float f1 = livingentity.zza;
				if (f1 <= 0.0f)
					f1 *= 0.25f;
				this.setSpeed(0.04f);

                pos = new Vec3(0.0f, pos.y, f1);
			}
            
            super.travel(pos);
		}
	}

    @Override
    public void tick()
    {

        lastwheelpitch = wheelspitch;
        wheelspitch += this.getWheelsSpeed();

        if (this.renderupdateticks-- <= 0)
        {
            this.updateMachineRender();
            this.renderupdateticks = SiegeMachines.RENDER_UPDATE_TIME;
        }

//        if (this.getWheelsSpeed() > 0.0081 && this.wheelssoundticks-- <= 0)
//        {
//            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), ModSoundTypes.RAM_WHEELS.get(), SoundCategory.NEUTRAL, 0.6f, 1.0f, true);
//            this.wheelssoundticks = 20;
//        }

        this.seatsTick();

        super.tick();
    }

    public double getLerpedWheelPitch(float partialTick) {
        return Mth.lerp(partialTick,lastwheelpitch,wheelspitch);
    }


    public void seatsTick()
    {
        this.leftseats.forEach(seat -> this.updateSeatPosition(seat, true));
        this.rightseats.forEach(seat -> this.updateSeatPosition(seat, false));
    }

    public void updateSeatPosition(LadderSeat seat, boolean left)
    {
        double yaw = this.getYRot() * Math.PI / 180.0;

        float highness = seat.climb();
        
        Vec3 pos = this.getSeatPosititon(highness, yaw, left);
        Optional<Vec3> freepos = this.level().findFreePosition(seat, Shapes.create(AABB.ofSize(pos, 0.1, 0.1, 0.1)), pos, 0.0, 0.0, 0.0);
        if (freepos.isPresent() && pos.distanceTo(freepos.get()) < 0.5)
        {
            seat.setHighness(highness);
            pos = freepos.get();
        }
        else
            pos = this.getSeatPosititon(seat, yaw, left);
        seat.moveTo(pos);
    }

    @Override
    public void remove(RemovalReason reason) {
        for (LadderSeat seat : this.seats)
            seat.discard();
        super.remove(reason);
    }



    @Override
    public void use(Player player)
    {
        if (this.getControllingPassenger() == player)
        {
            LadderSeat seat = this.getFreeSeat(player);
            if (seat != null)
                player.startRiding(seat);
        }
    }

    @Override
    public void useRealise()
    {
        
    }

    public double getWheelsSpeed()
    {
        if (this.onGround())
        {
            return this.getViewVector(5.0f).multiply(1, 0, 1).dot(this.getDeltaMovement());
        }

        return 0.0;
    }

    @Override
    public Item getMachineItem()
    {
        return ModItems.SIEGE_LADDER.get();
    }

    protected Vec3 getSeatPosititon(LadderSeat seat, double yaw, boolean left)
    {
        return getSeatPosititon(seat.getHighness(), yaw, left);
    }
    
    protected Vec3 getSeatPosititon(float highness, double yaw, boolean left)
    {
        return this.position().add(CartesianGeometry.applyRotations((left ? CLIMB_PIVOT_1 : CLIMB_PIVOT_2).add(CLIMB_VECTOR.scale(highness)), 0.0, yaw));
    }
    
    protected @Nullable LadderSeat getFreeSeat(Player player)
    {
        AtomicReference<LadderSeat> left = new AtomicReference<>(null);
        AtomicReference<LadderSeat> right = new AtomicReference<>(null);
        
        long l1 = this.leftseats.stream().filter(seat -> {
            if (seat.isVehicle())
                return true;
            else
            {
                left.set(seat);
                return false;
            }}).count();

        long l2 = this.rightseats.stream().filter(seat -> {
            if (seat.isVehicle())
                return true;
            else
            {
                right.set(seat);
                return false;
            }}).count();
        
        if (l1 < l2)
            return left.get();
        else if (l1 == l2 && player != null)
        {
            Vec3 view = this.getViewVector(0.0f);
            return player.position().subtract(this.position()).dot(new Vec3(view.z, 0.0, -view.x).normalize()) > 0.0 ? right.get() : left.get();
        }
        
        return right.get();
    }
    
    @Override
    public void push(Entity entity)
    {
        
    }

    @Override
    public void push(double x, double y, double z)
    {

    }

    //Forge methods, do not remove
    @SuppressWarnings("unused")
    public void onAddedToLevel() {
        Services.PLATFORM.onAddedToLevel(this);
        this.seats.forEach(seat -> this.level().addFreshEntity(seat));
    }
}
