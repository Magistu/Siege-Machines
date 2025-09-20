package ru.magistu.siegemachines;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.LoggerFactory;
import ru.magistu.siegemachines.block.ModBlocks;
import ru.magistu.siegemachines.client.ClientProxy;
import ru.magistu.siegemachines.client.ModSoundTypes;
import ru.magistu.siegemachines.config.SpecsConfig;
import ru.magistu.siegemachines.entity.EntityDataSerializers;
import ru.magistu.siegemachines.item.recipes.ModRecipes;
import ru.magistu.siegemachines.entity.ModEntityTypes;
import ru.magistu.siegemachines.client.gui.ModMenuTypes;
import ru.magistu.siegemachines.item.ModItems;
import ru.magistu.siegemachines.network.ModNetwork;
import ru.magistu.siegemachines.server.ServerProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SiegeMachines.ID)
public class SiegeMachines {
    public static final String ID = "siegemachines";
    public static final String MOD_NAME = "SiegeMachines";
    public static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final Proxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static final int RENDER_UPDATE_RANGE = 128;
    public static final int RENDER_UPDATE_RANGE_SQR = RENDER_UPDATE_RANGE * RENDER_UPDATE_RANGE;

    public SiegeMachines() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        PROXY.setup(eventBus);

        EntityDataSerializers.register(eventBus);
        ModEntityTypes.register(eventBus);
        ModSoundTypes.register(eventBus);
        ModMenuTypes.register(eventBus);
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        ModRecipes.register(eventBus);
        SpecsConfig.register();
        ModNetwork.register();

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(ID, path);
    }
}
