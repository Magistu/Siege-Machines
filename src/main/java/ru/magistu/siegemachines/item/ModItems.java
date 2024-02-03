package ru.magistu.siegemachines.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.client.renderer.*;
import ru.magistu.siegemachines.client.renderer.model.MachineItemModel;
import ru.magistu.siegemachines.entity.EntityTypes;
import ru.magistu.siegemachines.entity.machine.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems
{
    public static final CreativeModeTab GROUP_SM = new CreativeModeTab(SiegeMachines.ID + ".medieval_siege_machines") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MORTAR.get());
        }
    };

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SiegeMachines.ID);

    public static final RegistryObject<Item> MORTAR = ITEMS.register("mortar", () -> new MachineItem<>(new Item.Properties().tab(ModItems.GROUP_SM), EntityTypes.MORTAR, () -> MachineType.MORTAR)
    {@Override @OnlyIn(Dist.CLIENT) public MachineItemGeoRenderer<Mortar> getRenderer() {return new MachineItemGeoRenderer<>(new MachineItemModel<>("mortar"));}});
    public static final RegistryObject<Item> CULVERIN = ITEMS.register("culverin", () -> new MachineItem<>(new Item.Properties().tab(ModItems.GROUP_SM), EntityTypes.CULVERIN, () -> MachineType.CULVERIN)
    {@Override @OnlyIn(Dist.CLIENT) public MachineItemGeoRenderer<Culverin> getRenderer() {return new MachineItemGeoRenderer<>(new MachineItemModel<>("culverin"));}});
    public static final RegistryObject<Item> CATAPULT = ITEMS.register("catapult", () -> new MachineItem<>(new Item.Properties().tab(ModItems.GROUP_SM), EntityTypes.CATAPULT, () -> MachineType.CATAPULT)
    {@Override @OnlyIn(Dist.CLIENT) public MachineItemGeoRenderer<Catapult> getRenderer() {return new MachineItemGeoRenderer<>(new MachineItemModel<>("catapult"));}});
    public static final RegistryObject<Item> TREBUCHET = ITEMS.register("trebuchet", () -> new MachineItem<>(new Item.Properties().tab(ModItems.GROUP_SM), EntityTypes.TREBUCHET, () -> MachineType.TREBUCHET)
    {@Override @OnlyIn(Dist.CLIENT) public MachineItemGeoRenderer<Trebuchet> getRenderer() {return new MachineItemGeoRenderer<>(new MachineItemModel<>("trebuchet"));}});
    public static final RegistryObject<Item> BALLISTA = ITEMS.register("ballista", () -> new MachineItem<>(new Item.Properties().tab(ModItems.GROUP_SM), EntityTypes.BALLISTA, () -> MachineType.BALLISTA)
    {@Override @OnlyIn(Dist.CLIENT) public MachineItemGeoRenderer<Ballista> getRenderer() {return new MachineItemGeoRenderer<>(new MachineItemModel<>("ballista"));}});
    public static final RegistryObject<Item> BATTERING_RAM = ITEMS.register("battering_ram", () -> new MachineItem<>(new Item.Properties().tab(ModItems.GROUP_SM), EntityTypes.BATTERING_RAM, () -> MachineType.BATTERING_RAM)
    {@Override @OnlyIn(Dist.CLIENT) public MachineItemGeoRenderer<BatteringRam> getRenderer() {return new MachineItemGeoRenderer<>(new MachineItemModel<>("battering_ram"));}});
    public static final RegistryObject<Item> SIEGE_LADDER = ITEMS.register("siege_ladder", () -> new MachineItem<>(new Item.Properties().tab(ModItems.GROUP_SM), EntityTypes.SIEGE_LADDER, () -> MachineType.SIEGE_LADDER)
    {@Override @OnlyIn(Dist.CLIENT) public MachineItemGeoRenderer<SiegeLadder> getRenderer() {return new MachineItemGeoRenderer<>(new MachineItemModel<>("siege_ladder"));}});

    public static final RegistryObject<Item> CANNONBALL = ITEMS.register("cannonball", () -> new Item(new Item.Properties().stacksTo(16).tab(ModItems.GROUP_SM)));
    public static final RegistryObject<Item> STONE = ITEMS.register("stone", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> GIANT_STONE = ITEMS.register("giant_stone", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> GIANT_ARROW = ITEMS.register("giant_arrow", () -> new Item(new Item.Properties().stacksTo(16).tab(ModItems.GROUP_SM)));

    public static final RegistryObject<Item> TURRET_BASE = ITEMS.register("turret_base", () -> new Item(new Item.Properties().tab(ModItems.GROUP_SM)));
    public static final RegistryObject<Item> BEAM = ITEMS.register("beam", () -> new Item(new Item.Properties().tab(ModItems.GROUP_SM)));
    public static final RegistryObject<Item> COUNTERWEIGHT = ITEMS.register("counterweight", () -> new Item(new Item.Properties().tab(ModItems.GROUP_SM)));
    public static final RegistryObject<Item> BARREL = ITEMS.register("barrel", () -> new Item(new Item.Properties().tab(ModItems.GROUP_SM)));
    public static final RegistryObject<Item> WHEEL = ITEMS.register("wheel", () -> new Item(new Item.Properties().tab(ModItems.GROUP_SM)));

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
