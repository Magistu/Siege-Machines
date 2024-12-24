package ru.magistu.siegemachines.client.renderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import ru.magistu.siegemachines.entity.machine.SiegeLadder;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MachineGeoRenderer<T extends Machine & GeoAnimatable> extends GeoEntityRenderer<T>
{
    public MachineGeoRenderer(EntityRendererProvider.Context rendermanager, GeoModel<T> model)
    {
        super(rendermanager, model);
    }

    @Override
    public @Nullable RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    protected float getDeathMaxRotation(T entity)
    {
		return 0.0F;
	}
}
