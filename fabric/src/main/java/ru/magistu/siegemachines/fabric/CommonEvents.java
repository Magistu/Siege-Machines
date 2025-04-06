//package ru.magistu.siegemachines.fabric;
//
//import dev.architectury.event.events.common.TickEvent;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.Pose;
//import net.minecraft.world.entity.player.Player;
//import ru.magistu.siegemachines.entity.machine.LadderSeat;
//import ru.magistu.siegemachines.entity.machine.Machine;
//
//public class CommonEvents {
//
//    public static void register() {
//        TickEvent.PLAYER_POST.register(CommonEvents::onPlayerTick);
//    }
//
//    private static void onPlayerTick(Player player) {
//        Entity vehicle = player.getVehicle();
//        if (vehicle instanceof Machine || vehicle instanceof LadderSeat) {
//            player.setPose(Pose.STANDING);
//        }
//    }
//}
