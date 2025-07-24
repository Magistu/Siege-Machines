package ru.magistu.siegemachines.fabric.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import ru.magistu.siegemachines.entity.machine.SiegeLadder;

public class CommonEvents {

    public static void register() {
        EntityEvent.ADD.register(CommonEvents::onEntityAdded);
    }

    public static EventResult onEntityAdded(Entity entity, Level world) {
        if (entity instanceof SiegeLadder ladder) {
            ladder.onAddedToLevel();
        }
        return EventResult.pass();
    }
}
