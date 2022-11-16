package magistu.siegemachines.item;

import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.block.ModBlocks;
import magistu.siegemachines.client.renderer.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems
{
    public static final ItemGroup GROUP_SM = new ItemGroup(SiegeMachines.ID + ".medieval_siege_machines")
    {
        public @NotNull ItemStack makeIcon()
        {
            return new ItemStack(ModItems.MORTAR.get());
        }
    };

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SiegeMachines.ID);

    public static final RegistryObject<MachineItem> MORTAR = ITEMS.register("mortar", () -> new MachineItem(new Item.Properties().tab(ModItems.GROUP_SM).setISTER(() -> MortarItemGeoRenderer::new), "mortar", "MORTAR"));
    //public static final RegistryObject<MachineItem> CULVERIN = ITEMS.register("culverin", () -> new MachineItem(new Item.Properties().tab(ModItems.GROUP_SM).setISTER(() -> CulverinItemGeoRenderer::new), "culverin", "CULVERIN"));
    public static final RegistryObject<MachineItem> CATAPULT = ITEMS.register("catapult", () -> new MachineItem(new Item.Properties().tab(ModItems.GROUP_SM).setISTER(() -> CatapultItemGeoRenderer::new), "catapult", "CATAPULT"));
    public static final RegistryObject<MachineItem> TREBUCHET = ITEMS.register("trebuchet", () -> new MachineItem(new Item.Properties().tab(ModItems.GROUP_SM).setISTER(() -> TrebuchetItemGeoRenderer::new), "trebuchet", "TREBUCHET"));
    public static final RegistryObject<MachineItem> BALLISTA = ITEMS.register("ballista", () -> new MachineItem(new Item.Properties().tab(ModItems.GROUP_SM).setISTER(() -> BallistaItemGeoRenderer::new), "ballista", "BALLISTA"));
    public static final RegistryObject<MachineItem> BATTERING_RAM = ITEMS.register("battering_ram", () -> new MachineItem(new Item.Properties().tab(ModItems.GROUP_SM).setISTER(() -> BatteringRamItemGeoRenderer::new), "battering_ram", "BATTERING_RAM"));

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
