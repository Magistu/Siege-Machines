package ru.magistu.siegemachines.item;

import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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

    public static final RegistryObject<MachineItem> MORTAR = ITEMS.register("mortar", MortarItem::new);
    //public static final RegistryObject<MachineItem> CULVERIN = ITEMS.register("culverin", CulverinItem::new);
    public static final RegistryObject<MachineItem> CATAPULT = ITEMS.register("catapult", CatapultItem::new);
    public static final RegistryObject<MachineItem> TREBUCHET = ITEMS.register("trebuchet", TrebuchetItem::new);
    public static final RegistryObject<MachineItem> BALLISTA = ITEMS.register("ballista", BallistaItem::new);
    public static final RegistryObject<MachineItem> BATTERING_RAM = ITEMS.register("battering_ram", BatteringRamItem::new);

    public static final RegistryObject<Item> CANNONBALL = ITEMS.register("cannonball", () -> new Item(new Item.Properties().stacksTo(16).tab(ModItems.GROUP_SM)));
    public static final RegistryObject<Item> STONE = ITEMS.register("stone", () -> new Item(new Item.Properties().stacksTo(16)));
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
