package ru.itzme1on.siegemachines.events;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import ru.itzme1on.siegemachines.entity.EntityTypes;
import ru.itzme1on.siegemachines.entity.machine.*;

public class CommonEvents
{
    public static void init()
    {
        LifecycleEvent.SETUP.register(CommonEvents::onSetup);
    }

    public static void onSetup()
    {
        
    }
}
