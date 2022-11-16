package magistu.siegemachines.client;

import magistu.siegemachines.SiegeMachines;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings
{
    public static KeyBinding MACHINE_USE = new KeyBinding(SiegeMachines.ID + ".machine_use", 70, SiegeMachines.ID + ".category");
    public static KeyBinding MACHINE_INVENTORY = new KeyBinding(SiegeMachines.ID + ".machine_inventory", 73, SiegeMachines.ID + ".category");

    public static void register()
    {
        ClientRegistry.registerKeyBinding(MACHINE_USE);
        ClientRegistry.registerKeyBinding(MACHINE_INVENTORY);
    }
}
