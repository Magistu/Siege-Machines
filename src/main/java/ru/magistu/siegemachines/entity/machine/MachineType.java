package ru.magistu.siegemachines.entity.machine;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import ru.magistu.siegemachines.client.ModSoundTypes;
import ru.magistu.siegemachines.config.SiegeMachineSpecs;
import ru.magistu.siegemachines.config.SpecsConfig;
import ru.magistu.siegemachines.entity.projectile.ProjectileBuilder;
import ru.magistu.siegemachines.item.ModItems;

import java.util.function.Supplier;

public class MachineType {

    public static MachineType MORTAR = new MachineType(
            SpecsConfig.MORTAR, 1, 18.5f, 0.0f, 85.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.5f, true, 10, 10,
            new Vec3(0.0, 0.0, -10.0).scale(1 / 16.0),
            new Vec3(0.0, 17.0, 7.0).scale(1 / 16.0),
            new Vec3(0.0, 0.0, 12.0).scale(1 / 16.0),
            ProjectileBuilder.CANNON_AMMO, true,
            () -> new ItemStack[]{new ItemStack(Items.OAK_PLANKS, 2), new ItemStack(ModItems.BEAM.get(), 1), new ItemStack(Items.STICK, 5), new ItemStack(Items.IRON_NUGGET, 21)}, ModItems.MORTAR,
            -1, -1, null, 1.0f, ModSoundTypes.FUSE, 0.3f, ModSoundTypes.MORTAR_SHOOTING);

    public static MachineType CULVERIN = new MachineType(
            SpecsConfig.CULVERIN, 1, 18.5f, 4.0f, 18.5f, 0.0f, 0.0f, 0.0f, 0.4f, 0.1f, true, 10, 10,
            new Vec3(15.0, 0.0, -35.0).scale(1 / 16.0),
            new Vec3(0.0, 26.0, 18.0).scale(1 / 16.0),
            new Vec3(0.0, 0.0, 12.0).scale(1 / 16.0),
            ProjectileBuilder.CANNON_AMMO, true,
            () -> new ItemStack[]{new ItemStack(Items.OAK_PLANKS, 3), new ItemStack(ModItems.BEAM.get(), 2), new ItemStack(Items.STICK, 10), new ItemStack(Items.IRON_NUGGET, 32)}, ModItems.CULVERIN,
            -1, -1, null, -1, ModSoundTypes.FUSE, 0.3f, ModSoundTypes.CULVERIN_SHOOTING);

    public static MachineType TREBUCHET = new MachineType(
            SpecsConfig.TREBUCHET, 1, 0.0f, -45.0f, 75.0f, 0.0f, 0.0f, 0.0f, 5.0f, 0.1f, true, 38, 137,
            new Vec3(40.0, 0.0, -60.0).scale(1 / 16.0),
            new Vec3(0.0, 19.0, 0.0),
            new Vec3(0.0, 0.0, 0.0),
            ProjectileBuilder.GIANT_THROWING_AMMO, false,
            () -> new ItemStack[]{new ItemStack(Items.OAK_PLANKS, 8), new ItemStack(ModItems.BEAM.get(), 12), new ItemStack(Items.STICK, 20), new ItemStack(Items.IRON_NUGGET, 36), new ItemStack(Items.COBBLESTONE, 2)}, ModItems.TREBUCHET,
            40, 1.0f, ModSoundTypes.TREBUCHET_RELOADING, 1.0f, ModSoundTypes.TREBUCHET_SHOOTING, -1, null);

    public static MachineType CATAPULT = new MachineType(
            SpecsConfig.CATAPULT, 1, 0.0f, 0.0f, 75.0f, 0.0f, 0.0f, 0.0f, 0.2f, 1.0f, true, 2, 10,
            new Vec3(30.0, 0.0, -40.0).scale(1 / 16.0),
            new Vec3(0.0, 51.0, -5.0).scale(1 / 16.0),
            new Vec3(0.0, 0.0, 0.0),
            ProjectileBuilder.THROWING_AMMO, false,
            () -> new ItemStack[]{new ItemStack(Items.OAK_PLANKS, 5), new ItemStack(ModItems.BEAM.get(), 6), new ItemStack(Items.STICK, 10), new ItemStack(Items.IRON_NUGGET, 14)}, ModItems.CATAPULT,
            20, 1.0f, ModSoundTypes.CATAPULT_RELOADING, 1.5f, ModSoundTypes.CATAPULT_SHOOTING, -1, null);

    public static MachineType BALLISTA = new MachineType(
            SpecsConfig.BALLISTA, 1, 0.0f, -30.0f, 60.0f, 0.0f, -180.0f, 180.0f, 8.0f, 0.0f, false, 1, 20,
            new Vec3(0.0, 0.0, -30.0).scale(1 / 16.0),
            new Vec3(0.0, 22.5, 0.0).scale(1 / 16.0),
            new Vec3(0.0, 0.0, 17.0).scale(1 / 16.0),
            ProjectileBuilder.BALLISTA_AMMO, false,
            () -> new ItemStack[]{new ItemStack(Items.OAK_PLANKS, 2), new ItemStack(ModItems.BEAM.get(), 1), new ItemStack(Items.STICK, 5), new ItemStack(Items.IRON_NUGGET, 8)}, ModItems.BALLISTA,
            21, 1.0f, ModSoundTypes.BALLISTA_RELOADING, 1.4f, ModSoundTypes.BALLISTA_SHOOTING, -1, null);

    public static MachineType BATTERING_RAM = new MachineType(
            SpecsConfig.BATTERING_RAM, 1, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.1f, 0.1f, false, 5, 5,
            new Vec3(12.0, 0.0, -48.0).scale(1 / 16.0),
            new Vec3(0.0, 26.0, 36.0).scale(1 / 16.0),
            new Vec3(0.0, 0.0, 32.0).scale(1 / 16.0),
            ProjectileBuilder.NO_AMMO, false,
            () -> new ItemStack[]{new ItemStack(Items.OAK_PLANKS, 6), new ItemStack(ModItems.BEAM.get(), 8), new ItemStack(Items.STICK, 12), new ItemStack(Items.IRON_NUGGET, 8)}, ModItems.BATTERING_RAM,
            -1, -1, null, 0.6f, ModSoundTypes.RAM_HITTING, -1, null);

    public static MachineType SIEGE_LADDER = new MachineType(
            SpecsConfig.SIEGE_LADDER, 1, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.1f, 0.1f, false, 5, 5,
            new Vec3(0.0, 0.0, -56.0).scale(1 / 16.0),
            new Vec3(0.0, 26.0, 36.0).scale(1 / 16.0),
            new Vec3(0.0, 0.0, 32.0).scale(1 / 16.0),
            ProjectileBuilder.NO_AMMO, false,
            () -> new ItemStack[]{new ItemStack(ModItems.BEAM.get(), 4), new ItemStack(Items.STICK, 20)}, ModItems.SIEGE_LADDER,
            -1, -1, null, -1, null, -1, null);

    public final SiegeMachineSpecs specs;
    public final int containerrows;
    public final float turretinitpitch;
    public final float turretminpitch;
    public final float turretmaxpitch;
    public final float turretinityaw;
    public final float turretminyaw;
    public final float turretmaxyaw;
    public final float turretspeed;
    public final float rotationspeed;
    public final boolean yawfirst;
    public final int usereleasetime;
    public final int usetime;

    public final Vec3 passengerpos;
    public final Vec3 turretpivot;
    public final Vec3 turretvector;

    public final ProjectileBuilder<?>[] ammo;
    public final boolean usesgunpowder;

    public final Supplier<ItemStack[]> wreckage;
    public final Supplier<Item> machineitem;

    int reloadsoundduration;
    float reloadvolume;
    Supplier<SoundEvent> reloadsound;
    float usevolume;
    Supplier<SoundEvent> usesound;
    float usereleasevolume;
    Supplier<SoundEvent> usereleasesound;

    MachineType(
            SiegeMachineSpecs specs,
            int containerrows,
            float turretinitpitch,
            float turretminpitch,
            float turretmaxpitch,
            float turretinityaw,
            float turretminyaw,
            float turretmaxyaw,
            float turretspeed,
            float rotationspeed,
            boolean yawfirst,
            int usereleasetime,
            int usetime,
            Vec3 passengerpos,
            Vec3 turretpivot,
            Vec3 turretvector,
            ProjectileBuilder<?>[] ammo,
            boolean usesgunpowder,
            Supplier<ItemStack[]> wreckage,
            Supplier<Item> machineitem,
            int reloadsoundduration,
            float reloadvolume,
            Supplier<SoundEvent> reloadsound,
            float usevolume,
            Supplier<SoundEvent> usesound,
            float usereleasevolume,
            Supplier<SoundEvent> usereleasesound) {
        this.specs = specs;
        this.containerrows = containerrows;
        this.turretinitpitch = turretinitpitch;
        this.turretminpitch = turretminpitch;
        this.turretmaxpitch = turretmaxpitch;
        this.turretinityaw = turretinityaw;
        this.turretminyaw = turretminyaw;
        this.turretmaxyaw = turretmaxyaw;
        this.rotationspeed = rotationspeed;
        this.turretspeed = turretspeed;
        this.yawfirst = yawfirst;
        this.usereleasetime = usereleasetime;
        this.usetime = usetime;
        this.passengerpos = passengerpos;
        this.turretpivot = turretpivot;
        this.turretvector = turretvector;
        this.ammo = ammo;
        this.usesgunpowder = usesgunpowder;
        this.wreckage = wreckage;
        this.machineitem = machineitem;
        this.reloadsoundduration = reloadsoundduration;
        this.reloadvolume = reloadvolume;
        this.reloadsound = reloadsound;
        this.usevolume = usevolume;
        this.usesound = usesound;
        this.usereleasevolume = usereleasevolume;
        this.usereleasesound = usereleasesound;
    }
}