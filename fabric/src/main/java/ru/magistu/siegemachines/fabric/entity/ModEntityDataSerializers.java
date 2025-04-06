package ru.magistu.siegemachines.fabric.entity;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.ItemStack;
import ru.magistu.siegemachines.entity.machine.Machine;

import java.util.List;

public class ModEntityDataSerializers {
    public static final EntityDataSerializer<List<ItemStack>> ITEM_STACKS_SERIALIZER = Machine.ITEM_STACKS_SERIALIZER;

    public static void register() {
        EntityDataSerializers.registerSerializer(ITEM_STACKS_SERIALIZER);
    }
}