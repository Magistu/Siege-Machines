package ru.magistu.siegemachines.entity;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.entity.machine.ItemStackListSerializer;

import java.util.List;

public class EntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, SiegeMachines.ID);

    private static final RegistryObject<EntityDataSerializer<List<ItemStack>>> ITEM_STACKS = ENTITY_DATA_SERIALIZERS.register("inventory", () -> ItemStackListSerializer.INSTANCE);

    public static void register(IEventBus bus) {
        ENTITY_DATA_SERIALIZERS.register(bus);
    }
}
