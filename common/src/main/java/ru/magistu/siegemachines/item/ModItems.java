package ru.magistu.siegemachines.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.world.item.Item;

import ru.magistu.siegemachines.client.renderer.MachineItemGeoRenderer;
import ru.magistu.siegemachines.client.renderer.model.MachineItemModel;
import ru.magistu.siegemachines.entity.ModEntityTypes;
import ru.magistu.siegemachines.entity.machine.*;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(SiegeMachines.ID, Registries.ITEM);

    public static final RegistrySupplier<Item> MORTAR = ITEMS.register("mortar", () -> new MachineItem<>(new Item.Properties(), ModEntityTypes.MORTAR, () -> MachineType.MORTAR)
    {@Override public MachineItemGeoRenderer<Mortar> getRenderer() {return new MachineItemGeoRenderer<>(new MachineItemModel<>("mortar"));}});
    public static final RegistrySupplier<Item> CULVERIN = ITEMS.register("culverin", () -> new MachineItem<>(new Item.Properties(), ModEntityTypes.CULVERIN, () -> MachineType.CULVERIN)
    {@Override public MachineItemGeoRenderer<Culverin> getRenderer() {return new MachineItemGeoRenderer<>(new MachineItemModel<>("culverin"));}});
    public static final RegistrySupplier<Item> CATAPULT = ITEMS.register("catapult", () -> new MachineItem<>(new Item.Properties(), ModEntityTypes.CATAPULT, () -> MachineType.CATAPULT)
    {@Override public MachineItemGeoRenderer<Catapult> getRenderer() {return new MachineItemGeoRenderer<>(new MachineItemModel<>("catapult"));}});
    public static final RegistrySupplier<Item> TREBUCHET = ITEMS.register("trebuchet", () -> new MachineItem<>(new Item.Properties(), ModEntityTypes.TREBUCHET, () -> MachineType.TREBUCHET)
    {@Override public MachineItemGeoRenderer<Trebuchet> getRenderer() {return new MachineItemGeoRenderer<>(new MachineItemModel<>("trebuchet"));}});
    public static final RegistrySupplier<Item> BALLISTA = ITEMS.register("ballista", () -> new MachineItem<>(new Item.Properties(), ModEntityTypes.BALLISTA, () -> MachineType.BALLISTA)
    {@Override public MachineItemGeoRenderer<Ballista> getRenderer() {return new MachineItemGeoRenderer<>(new MachineItemModel<>("ballista"));}});
    public static final RegistrySupplier<Item> BATTERING_RAM = ITEMS.register("battering_ram", () -> new MachineItem<>(new Item.Properties(), ModEntityTypes.BATTERING_RAM, () -> MachineType.BATTERING_RAM)
    {@Override public MachineItemGeoRenderer<BatteringRam> getRenderer() {return new MachineItemGeoRenderer<>(new MachineItemModel<>("battering_ram"));}});
    public static final RegistrySupplier<Item> SIEGE_LADDER = ITEMS.register("siege_ladder", () -> new MachineItem<>(new Item.Properties(), ModEntityTypes.SIEGE_LADDER, () -> MachineType.SIEGE_LADDER)
    {@Override public MachineItemGeoRenderer<SiegeLadder> getRenderer() {return new MachineItemGeoRenderer<>(new MachineItemModel<>("siege_ladder"));}});

    public static final RegistrySupplier<Item> CANNONBALL = ITEMS.register("cannonball", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistrySupplier<Item> STONE = ITEMS.register("stone", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistrySupplier<Item> GIANT_STONE = ITEMS.register("giant_stone", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistrySupplier<Item> GIANT_ARROW = ITEMS.register("giant_arrow", () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistrySupplier<Item> TURRET_BASE = ITEMS.register("turret_base", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> BEAM = ITEMS.register("beam", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> COUNTERWEIGHT = ITEMS.register("counterweight", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> BARREL = ITEMS.register("barrel", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WHEEL = ITEMS.register("wheel", () -> new Item(new Item.Properties()));

    public static void register() {
        ITEMS.register();
    }
}
