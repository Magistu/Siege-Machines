package ru.magistu.siegemachines.client;

import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.client.KeyMapping;

public class KeyBindings
{
    public static KeyMapping MACHINE_USE = new KeyMapping(SiegeMachines.ID + ".machine_use", 70, SiegeMachines.ID + ".category");
    public static KeyMapping MACHINE_INVENTORY = new KeyMapping(SiegeMachines.ID + ".machine_inventory", 73, SiegeMachines.ID + ".category");
}
