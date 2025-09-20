package ru.magistu.siegemachines.entity.machine;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemStackListSerializer implements EntityDataSerializer<List<ItemStack>> {
    
    public static final ItemStackListSerializer INSTANCE = new ItemStackListSerializer();
    
    @Override
    public void write(FriendlyByteBuf buffer, List<ItemStack> itemStacks) {
        buffer.writeInt(itemStacks.size());
        for (ItemStack stack : itemStacks) {
            buffer.writeItem(stack);
        }
    }
    
    @Override
    public List<ItemStack> read(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        List<ItemStack> itemStacks = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            itemStacks.add(buffer.readItem());
        }
        return itemStacks;
    }
    
    @Override
    public List<ItemStack> copy(List<ItemStack> itemStacks) {
        List<ItemStack> copy = new ArrayList<>(itemStacks.size());
        for (ItemStack stack : itemStacks) {
            copy.add(stack.copy());
        }
        return copy;
    }
}