package magistu.siegemachines.entity.machine;

import magistu.siegemachines.config.SiegeMachineSpecs;
import magistu.siegemachines.config.SpecsConfig;
import magistu.siegemachines.entity.projectile.ProjectileBuilder;
import magistu.siegemachines.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3d;

public enum MachineType
{
    MORTAR(SpecsConfig.MORTAR, 18, 0.0f, 85.0f, 0.0f, 0.0f, 0.5f, 0.5f, true, 10, 10,
            new Vector3d(-0.5, 0.0, -20.0).scale(1 / 16.0), new Vector3d(0.0, 17.0, 7.0).scale(1 / 16.0), new Vector3d(0.0, 0.0, 12.0).scale(1 / 16.0),
            ProjectileBuilder.CANNON_AMMO, true,
            new ItemStack[] {new ItemStack(Items.OAK_PLANKS, 2), new ItemStack(ModItems.BEAM.get(), 1), new ItemStack(Items.STICK, 5), new ItemStack(Items.IRON_NUGGET, 21)}),
    CULVERIN(SpecsConfig.CULVERIN, 9, 0.0f, 85.0f, 0.0f, 0.0f, 0.5f, 0.5f, true, 10, 10,
            new Vector3d(-0.5, 0.0, -20.0).scale(1 / 16.0), new Vector3d(0.0, 17.0, 7.0).scale(1 / 16.0), new Vector3d(0.0, 0.0, 12.0).scale(1 / 16.0),
            ProjectileBuilder.CANNON_AMMO, true,
            new ItemStack[] {new ItemStack(Items.OAK_PLANKS, 2), new ItemStack(ModItems.BEAM.get(), 1), new ItemStack(Items.STICK, 5), new ItemStack(Items.IRON_NUGGET, 21)}),
    TREBUCHET(SpecsConfig.TREBUCHET, 9, -45.0f, 75.0f, 0.0f, 0.0f, 0.05f, 0.5f, true, 38, 137,
            new Vector3d(-34.0, 0.0, -94.0).scale(1 / 16.0), new Vector3d(0.0, 19.0, -3.0), new Vector3d(0.0, 10.0, -1.0),
            ProjectileBuilder.GIANT_THROWING_AMMO, false,
            new ItemStack[] {new ItemStack(Items.OAK_PLANKS, 8), new ItemStack(ModItems.BEAM.get(), 12), new ItemStack(Items.STICK, 20), new ItemStack(Items.IRON_NUGGET, 36), new ItemStack(Items.COBBLESTONE, 2)}),
    CATAPULT(SpecsConfig.CATAPULT, 9, 0.0f, 75.0f, 0.0f, 0.0f, 0.2f, 1.0f, true, 2, 10,
            new Vector3d(30.0, 0.0, -40.0).scale(1 / 16.0), new Vector3d(0.0, 51.0, -5.0).scale(1 / 16.0), new Vector3d(0.0, 0.0, 0.0),
            ProjectileBuilder.THROWING_AMMO, false,
            new ItemStack[] {new ItemStack(Items.OAK_PLANKS, 5), new ItemStack(ModItems.BEAM.get(), 6), new ItemStack(Items.STICK, 10), new ItemStack(Items.IRON_NUGGET, 14)}),
    BALLISTA(SpecsConfig.BALLISTA, 9, -30.0f, 60.0f, -180.0f, 180.0f, 0.0f, 3.0f, false, 1, 20,
            new Vector3d(0.0, 0.0, -30.0).scale(1 / 16.0), new Vector3d(0.0, 22.5, 0.0).scale(1 / 16.0), new Vector3d(0.0, 0.0, 17.0).scale(1 / 16.0),
            ProjectileBuilder.BALLISTA_AMMO, false,
            new ItemStack[] {new ItemStack(Items.OAK_PLANKS, 2), new ItemStack(ModItems.BEAM.get(), 1), new ItemStack(Items.STICK, 5), new ItemStack(Items.IRON_NUGGET, 8)}),
    BATTERING_RAM(SpecsConfig.BATTERING_RAM, 9, 0.0f, 0.0f, 0.0f, 0.0f, 0.1f, 0.0f, false, 5, 5,
            new Vector3d(12.0, 0.0, -48.0).scale(1 / 16.0), new Vector3d(0.0, 26.0, 36.0).scale(1 / 16.0), new Vector3d(0.0, 0.0, 32.0).scale(1 / 16.0),
            ProjectileBuilder.NO_AMMO, false,
            new ItemStack[] {new ItemStack(Items.OAK_PLANKS, 6), new ItemStack(ModItems.BEAM.get(), 8), new ItemStack(Items.STICK, 12), new ItemStack(Items.IRON_NUGGET, 8)});

    public final SiegeMachineSpecs specs;
    public final int containersize;
    public final float turretminpitch;
    public final float turretmaxpitch;
    public final float turretminyaw;
    public final float turretmaxyaw;
    public final float rotationspeed;
    public final float turretspeed;
    public final boolean yawfirst;
    public final int userealisetime;
    public final int usetime;

    public final Vector3d passengerpos;
    public final Vector3d turretpivot;
    public final Vector3d turretvector;

    public final ProjectileBuilder[] ammo;
    public final boolean usesgunpowder;
    
    public final ItemStack[] wreckage;

    MachineType(
            SiegeMachineSpecs specs,
            int containersize,
            float turretminpitch,
            float turretmaxpitch,
            float turretminyaw,
            float turretmaxyaw,
            float rotationspeed,
            float turretspeed,
            boolean yawfirst,
            int shootingtime,
            int usetime,
            Vector3d passengerpos,
            Vector3d turretpivot,
            Vector3d turretvector,
            ProjectileBuilder[] ammo,
            boolean usesgunpowder,
            ItemStack[] wreckage)
    {
        this.specs = specs;
        this.containersize = containersize;
        this.turretminpitch = turretminpitch;
        this.turretmaxpitch = turretmaxpitch;
        this.turretminyaw = turretminyaw;
        this.turretmaxyaw = turretmaxyaw;
        this.rotationspeed = rotationspeed;
        this.turretspeed = turretspeed;
        this.yawfirst = yawfirst;
        this.userealisetime = shootingtime;
        this.usetime = usetime;
        this.passengerpos = passengerpos;
        this.turretpivot = turretpivot;
        this.turretvector = turretvector;
        this.ammo = ammo;
        this.usesgunpowder = usesgunpowder;
        this.wreckage = wreckage;
    }
}