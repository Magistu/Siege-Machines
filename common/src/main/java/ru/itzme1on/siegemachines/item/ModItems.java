package ru.itzme1on.siegemachines.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.itzme1on.siegemachines.SiegeMachines;
import ru.itzme1on.siegemachines.client.render.Renderers;
import ru.itzme1on.siegemachines.entity.machine.*;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(SiegeMachines.MOD_ID, Registry.ITEM_KEY);

    public static final ItemGroup SIEGEMACHINES_GROUP = CreativeTabRegistry.create(new Identifier(SiegeMachines.MOD_ID, "medieval_siege_machines"),
            () -> new ItemStack(ModItems.BEAM.get()));

    public static final RegistrySupplier<Item> CANNONBALL = ITEMS.register("cannonball",
            () -> new Item(new Item.Settings()
                    .maxCount(16)
                    .group(ModItems.SIEGEMACHINES_GROUP)));

    public static final RegistrySupplier<Item> STONE = ITEMS.register("stone",
            () -> new Item(new Item.Settings()
                    .maxCount(16)));

    
    
    public static final RegistrySupplier<Item> GIANT_ARROW = ITEMS.register("giant_arrow",
            () -> new Item(new Item.Settings()
                    .maxCount(16)
                    .group(ModItems.SIEGEMACHINES_GROUP)));

    public static final RegistrySupplier<Item> TURRET_BASE = ITEMS.register("turret_base",
            () -> new Item(new Item.Settings()
                    .maxCount(16)
                    .group(ModItems.SIEGEMACHINES_GROUP)));

    public static final RegistrySupplier<Item> BEAM = ITEMS.register("beam",
            () -> new Item(new Item.Settings()
                    .maxCount(16)
                    .group(ModItems.SIEGEMACHINES_GROUP)));

    public static final RegistrySupplier<Item> COUNTERWEIGHT = ITEMS.register("counterweight",
            () -> new Item(new Item.Settings()
                    .maxCount(1)
                    .group(ModItems.SIEGEMACHINES_GROUP)));

    public static final RegistrySupplier<Item> BARREL = ITEMS.register("barrel",
            () -> new Item(new Item.Settings()
                    .maxCount(16)
                    .group(ModItems.SIEGEMACHINES_GROUP)));

    public static final RegistrySupplier<Item> WHEEL = ITEMS.register("wheel",
            () -> new Item(new Item.Settings()
                    .maxCount(16)
                    .group(ModItems.SIEGEMACHINES_GROUP)));

    
    public static final RegistrySupplier<MachineItem<Ballista>> BALLISTA = ItemRegistryHelper.registerMachineItem("ballista", new Item.Settings().group(ModItems.SIEGEMACHINES_GROUP), Ballista.TYPE, () -> MachineType.BALLISTA, Renderers.ItemEnum.BALLISTA);
    public static final RegistrySupplier<MachineItem<BatteringRam>> BATTERING_RAM = ItemRegistryHelper.registerMachineItem("battering_ram", new Item.Settings().group(ModItems.SIEGEMACHINES_GROUP), BatteringRam.TYPE, () -> MachineType.BATTERING_RAM, Renderers.ItemEnum.BATTERING_RAM);
    public static final RegistrySupplier<MachineItem<Catapult>> CATAPULT = ItemRegistryHelper.registerMachineItem("catapult", new Item.Settings().group(ModItems.SIEGEMACHINES_GROUP), Catapult.TYPE, () -> MachineType.CATAPULT, Renderers.ItemEnum.CATAPULT);
    public static final RegistrySupplier<MachineItem<Culverin>> CULVERIN = ItemRegistryHelper.registerMachineItem("culverin", new Item.Settings().group(ModItems.SIEGEMACHINES_GROUP), Culverin.TYPE, () -> MachineType.CULVERIN, Renderers.ItemEnum.CULVERIN);
    public static final RegistrySupplier<MachineItem<Mortar>> MORTAR = ItemRegistryHelper.registerMachineItem("mortar", new Item.Settings().group(ModItems.SIEGEMACHINES_GROUP), Mortar.TYPE, () -> MachineType.MORTAR, Renderers.ItemEnum.MORTAR);
    public static final RegistrySupplier<MachineItem<Trebuchet>> TREBUCHET = ItemRegistryHelper.registerMachineItem("trebuchet", new Item.Settings().group(ModItems.SIEGEMACHINES_GROUP), Trebuchet.TYPE, () -> MachineType.TREBUCHET, Renderers.ItemEnum.TREBUCHET);

    public static void init() {
        ITEMS.register();
    }
}
