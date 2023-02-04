package ru.itzme1on.siegemachines.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.itzme1on.siegemachines.SiegeMachines;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(SiegeMachines.MOD_ID, Registry.ITEM_KEY);

    public static final ItemGroup SIEGEMACHINES_GROUP = CreativeTabRegistry.create(new Identifier(SiegeMachines.MOD_ID, "medieval_siege_machines"),
            () -> new ItemStack(ItemRegistry.BEAM.get()));

    public static final RegistrySupplier<Item> CANNONBALL = ITEMS.register("cannonball",
            () -> new Item(new Item.Settings()
                    .maxCount(16)
                    .group(ItemRegistry.SIEGEMACHINES_GROUP)));

    public static final RegistrySupplier<Item> STONE = ITEMS.register("stone",
            () -> new Item(new Item.Settings()
                    .maxCount(16)));

    public static final RegistrySupplier<Item> GIANT_ARROW = ITEMS.register("giant_arrow",
            () -> new Item(new Item.Settings()
                    .maxCount(16)
                    .group(ItemRegistry.SIEGEMACHINES_GROUP)));

    public static final RegistrySupplier<Item> TURRET_BASE = ITEMS.register("turret_base",
            () -> new Item(new Item.Settings()
                    .maxCount(16)
                    .group(ItemRegistry.SIEGEMACHINES_GROUP)));

    public static final RegistrySupplier<Item> BEAM = ITEMS.register("beam",
            () -> new Item(new Item.Settings()
                    .maxCount(16)
                    .group(ItemRegistry.SIEGEMACHINES_GROUP)));

    public static final RegistrySupplier<Item> COUNTERWEIGHT = ITEMS.register("counterweight",
            () -> new Item(new Item.Settings()
                    .maxCount(1)
                    .group(ItemRegistry.SIEGEMACHINES_GROUP)));

    public static final RegistrySupplier<Item> BARREL = ITEMS.register("barrel",
            () -> new Item(new Item.Settings()
                    .maxCount(16)
                    .group(ItemRegistry.SIEGEMACHINES_GROUP)));

    public static final RegistrySupplier<Item> WHEEL = ITEMS.register("wheel",
            () -> new Item(new Item.Settings()
                    .maxCount(16)
                    .group(ItemRegistry.SIEGEMACHINES_GROUP)));
}
