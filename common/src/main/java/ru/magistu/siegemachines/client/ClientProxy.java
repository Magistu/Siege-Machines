package ru.magistu.siegemachines.client;

import net.minecraft.world.entity.EntityType;
import ru.magistu.siegemachines.entity.ModEntityTypes;
import ru.magistu.siegemachines.gui.machine.crosshair.Crosshair;
import ru.magistu.siegemachines.gui.machine.crosshair.ReloadingCrosshair;

import java.util.HashMap;
import java.util.Map;


public class ClientProxy {

    public static final Map<EntityType<?>, Crosshair> CROSSHAIR_FACTORIES = new HashMap<>();

    public static void setup(){

        CROSSHAIR_FACTORIES.put(ModEntityTypes.BALLISTA.get(),new ReloadingCrosshair());
        CROSSHAIR_FACTORIES.put(ModEntityTypes.CULVERIN.get(),new ReloadingCrosshair());
        CROSSHAIR_FACTORIES.put(ModEntityTypes.MORTAR.get(),new ReloadingCrosshair());
        CROSSHAIR_FACTORIES.put(ModEntityTypes.CATAPULT.get(),new ReloadingCrosshair());
        CROSSHAIR_FACTORIES.put(ModEntityTypes.TREBUCHET.get(),new ReloadingCrosshair());
    }
}
