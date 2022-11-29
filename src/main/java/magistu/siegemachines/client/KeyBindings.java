package magistu.siegemachines.client;

import magistu.siegemachines.SiegeMachines;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public class KeyBindings
{
    public static KeyMapping MACHINE_USE = new KeyMapping(SiegeMachines.ID + ".machine_use", 70, SiegeMachines.ID + ".category");
    public static KeyMapping MACHINE_INVENTORY = new KeyMapping(SiegeMachines.ID + ".machine_inventory", 73, SiegeMachines.ID + ".category");

    public static void register()
    {
        ClientRegistry.registerKeyBinding(MACHINE_USE);
        ClientRegistry.registerKeyBinding(MACHINE_INVENTORY);
    }
}
