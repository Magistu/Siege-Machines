package ru.magistu.siegemachines;

import net.minecraftforge.fml.event.lifecycle.*;
import ru.magistu.siegemachines.block.ModBlocks;
import ru.magistu.siegemachines.client.ClientProxy;
import ru.magistu.siegemachines.client.SoundTypes;
import ru.magistu.siegemachines.config.SpecsConfig;
import ru.magistu.siegemachines.item.recipes.ModRecipes;
import ru.magistu.siegemachines.entity.EntityTypes;
import ru.magistu.siegemachines.client.gui.ModMenuTypes;
import ru.magistu.siegemachines.item.ModItems;
import ru.magistu.siegemachines.network.PacketHandler;
import ru.magistu.siegemachines.proxy.IProxy;
import ru.magistu.siegemachines.server.ServerProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SiegeMachines.ID)
public class SiegeMachines {
    public static final String ID = "siegemachines";

    public static final IProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static final int RENDER_UPDATE_RANGE = 128;
    public static final int RENDER_UPDATE_TIME = 20;
    public static final int RENDER_UPDATE_RANGE_SQR = RENDER_UPDATE_RANGE * RENDER_UPDATE_RANGE;
    
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    IEventBus eventBus;


    public SiegeMachines()
    {
        eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        EntityTypes.register(eventBus);
        SoundTypes.register(eventBus);
        ModMenuTypes.register(eventBus);
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        ModRecipes.register(eventBus);
        SpecsConfig.register();

        PacketHandler.init();

        MinecraftForge.EVENT_BUS.register(this);
    }


    private void setup(final FMLCommonSetupEvent event)
    {
        
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
        PROXY.clientSetup(event);
	}

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        
    }

    private void processIMC(final InterModProcessEvent event)
    {
        
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        
    }
}
