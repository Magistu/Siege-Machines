package ru.magistu.siegemachines;

import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.magistu.siegemachines.block.ModBlocks;
import ru.magistu.siegemachines.client.ClientEvents;
import ru.magistu.siegemachines.client.renderer.ModEntityRenderers;
import ru.magistu.siegemachines.entity.ModEntityAttributes;
import ru.magistu.siegemachines.entity.ModEntityTypes;
import ru.magistu.siegemachines.gui.ModMenuTypes;
import ru.magistu.siegemachines.init.ModCreativeTabs;
import ru.magistu.siegemachines.item.ModItems;
import ru.magistu.siegemachines.item.recipes.ModRecipeSerializers;
import ru.magistu.siegemachines.network.ModPacketHandler;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class SiegeMachines {

    public static final String ID = "siegemachines";
    public static final String MOD_NAME = "SiegeMachines";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final int RENDER_UPDATE_RANGE = 128;
    public static final int RENDER_UPDATE_RANGE_SQR = RENDER_UPDATE_RANGE * RENDER_UPDATE_RANGE;

    public static void init() {
        ModEntityTypes.register();
        ModSoundTypes.register();
        ModMenuTypes.register();
        ModBlocks.register();
        ModItems.register();
        ModRecipeSerializers.register();
        ModEntityAttributes.register();
        ModPacketHandler.register();
        ModCreativeTabs.register();
        if (Platform.getEnv() == EnvType.CLIENT) {
            ClientEvents.register();
            ModEntityRenderers.register();
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}