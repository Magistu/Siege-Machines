package ru.magistu.siegemachines.client;

import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import ru.magistu.siegemachines.entity.machine.MachineType;

import java.util.function.Supplier;

public class KeyBindings
{
    public static KeyMapping MACHINE_USE = new KeyMapping(SiegeMachines.ID + ".machine_use", 70, SiegeMachines.ID + ".category");
    public static KeyMapping LADDER_CLIMB = new KeyMapping(SiegeMachines.ID + ".ladder_climb", 32, SiegeMachines.ID + ".category");
    public static KeyMapping MACHINE_INVENTORY = new KeyMapping(SiegeMachines.ID + ".machine_inventory", 73, SiegeMachines.ID + ".category");

    public static void register()
    {
        ClientRegistry.registerKeyBinding(MACHINE_USE);
        ClientRegistry.registerKeyBinding(MACHINE_INVENTORY);
    }
    
    public static KeyMapping getUseKey(MachineType type)
    {
        if (type == MachineType.SIEGE_LADDER)
            return LADDER_CLIMB;
        return MACHINE_USE;
    }
}
