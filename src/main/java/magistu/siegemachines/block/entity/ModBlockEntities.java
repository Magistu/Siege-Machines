package magistu.siegemachines.block.entity;

import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.block.ModBlocks;
import magistu.siegemachines.block.entity.custom.SiegeWorkbenchBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, SiegeMachines.ID);

    public static final RegistryObject<BlockEntityType<SiegeWorkbenchBlockEntity>> SIEGE_WORKBENCH_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("siege_workbench", () ->
                    BlockEntityType.Builder.of(SiegeWorkbenchBlockEntity::new, ModBlocks.SIEGE_WORKBENCH.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
