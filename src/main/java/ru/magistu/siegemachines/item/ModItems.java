package ru.magistu.siegemachines.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
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
import ru.magistu.siegemachines.block.ModBlocks;
import ru.magistu.siegemachines.client.renderer.*;
import ru.magistu.siegemachines.client.renderer.model.MachineItemModel;
import ru.magistu.siegemachines.entity.ModEntityTypes;
import ru.magistu.siegemachines.entity.machine.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SiegeMachines.ID);

    public static final RegistryObject<CreativeModeTab> GROUP_SM = CREATIVE_TABS.register("siegemachines", ()->CreativeModeTab.builder().title(Component.translatable(SiegeMachines.ID + ".medieval_siege_machines")).icon(() -> new ItemStack(ModItems.MORTAR.get())).displayItems(new CreativeModeTab.DisplayItemsGenerator() {
        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters param1ItemDisplayParameters, CreativeModeTab.Output param1Output) {
            param1Output.accept(MORTAR.get().getDefaultInstance());
            param1Output.accept(CULVERIN.get().getDefaultInstance());
            param1Output.accept(CATAPULT.get().getDefaultInstance());
            param1Output.accept(TREBUCHET.get().getDefaultInstance());
            param1Output.accept(BALLISTA.get().getDefaultInstance());
            param1Output.accept(BATTERING_RAM.get().getDefaultInstance());
            param1Output.accept(SIEGE_LADDER.get().getDefaultInstance());
            param1Output.accept(CANNONBALL.get().getDefaultInstance());
            param1Output.accept(GIANT_ARROW.get().getDefaultInstance());
            param1Output.accept(TURRET_BASE.get().getDefaultInstance());
            param1Output.accept(BEAM.get().getDefaultInstance());
            param1Output.accept(BARREL.get().getDefaultInstance());
            param1Output.accept(WHEEL.get().getDefaultInstance());
            param1Output.accept(ModBlocks.SIEGE_WORKBENCH_ITEM.get());
            
        }
    }).withTabsBefore(CreativeModeTabs.SPAWN_EGGS).build());

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SiegeMachines.ID);

    public static final RegistryObject<Item> MORTAR = ITEMS.register("mortar", () -> new MachineItem<>(new Item.Properties(), ModEntityTypes.MORTAR, () -> MachineType.MORTAR) {
        @Override
        @OnlyIn(Dist.CLIENT)
        public MachineItemGeoRenderer<Cannon> getRenderer() {
            return new MachineItemGeoRenderer<>(new MachineItemModel<>("mortar"));
        }
    });
    public static final RegistryObject<Item> CULVERIN = ITEMS.register("culverin", () -> new MachineItem<>(new Item.Properties(), ModEntityTypes.CULVERIN, () -> MachineType.CULVERIN) {
        @Override
        @OnlyIn(Dist.CLIENT)
        public MachineItemGeoRenderer<Cannon> getRenderer() {
            return new MachineItemGeoRenderer<>(new MachineItemModel<>("culverin"));
        }
    });
    public static final RegistryObject<Item> CATAPULT = ITEMS.register("catapult", () -> new MachineItem<>(new Item.Properties(), ModEntityTypes.CATAPULT, () -> MachineType.CATAPULT) {
        @Override
        @OnlyIn(Dist.CLIENT)
        public MachineItemGeoRenderer<Catapult> getRenderer() {
            return new MachineItemGeoRenderer<>(new MachineItemModel<>("catapult"));
        }
    });
    public static final RegistryObject<Item> TREBUCHET = ITEMS.register("trebuchet", () -> new MachineItem<>(new Item.Properties(), ModEntityTypes.TREBUCHET, () -> MachineType.TREBUCHET) {
        @Override
        @OnlyIn(Dist.CLIENT)
        public MachineItemGeoRenderer<Catapult> getRenderer() {
            return new MachineItemGeoRenderer<>(new MachineItemModel<>("trebuchet"));
        }
    });
    public static final RegistryObject<Item> BALLISTA = ITEMS.register("ballista", () -> new MachineItem<>(new Item.Properties(), ModEntityTypes.BALLISTA, () -> MachineType.BALLISTA) {
        @Override
        @OnlyIn(Dist.CLIENT)
        public MachineItemGeoRenderer<Catapult> getRenderer() {
            return new MachineItemGeoRenderer<>(new MachineItemModel<>("ballista"));
        }
    });
    public static final RegistryObject<Item> BATTERING_RAM = ITEMS.register("battering_ram", () -> new MachineItem<>(new Item.Properties(), ModEntityTypes.BATTERING_RAM, () -> MachineType.BATTERING_RAM) {
        @Override
        @OnlyIn(Dist.CLIENT)
        public MachineItemGeoRenderer<BatteringRam> getRenderer() {
            return new MachineItemGeoRenderer<>(new MachineItemModel<>("battering_ram"));
        }
    });
    public static final RegistryObject<Item> SIEGE_LADDER = ITEMS.register("siege_ladder", () -> new MachineItem<>(new Item.Properties(), ModEntityTypes.SIEGE_LADDER, () -> MachineType.SIEGE_LADDER) {
        @Override
        @OnlyIn(Dist.CLIENT)
        public MachineItemGeoRenderer<SiegeLadder> getRenderer() {
            return new MachineItemGeoRenderer<>(new MachineItemModel<>("siege_ladder"));
        }
    });

    public static final RegistryObject<Item> CANNONBALL = ITEMS.register("cannonball", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> STONE = ITEMS.register("stone", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> GIANT_STONE = ITEMS.register("giant_stone", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> GIANT_ARROW = ITEMS.register("giant_arrow", () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> TURRET_BASE = ITEMS.register("turret_base", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BEAM = ITEMS.register("beam", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COUNTERWEIGHT = ITEMS.register("counterweight", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BARREL = ITEMS.register("barrel", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WHEEL = ITEMS.register("wheel", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
