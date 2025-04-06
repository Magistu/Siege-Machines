package ru.magistu.siegemachines.neoforge.entity;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.entity.machine.Machine;

import java.util.List;

public class ModEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, SiegeMachines.ID);

    private static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<List<ItemStack>>> ITEM_STACKS = ENTITY_DATA_SERIALIZERS.register("inventory", () -> Machine.ITEM_STACKS_SERIALIZER);

    public static void register(IEventBus bus) {
        ENTITY_DATA_SERIALIZERS.register(bus);
    }
}
