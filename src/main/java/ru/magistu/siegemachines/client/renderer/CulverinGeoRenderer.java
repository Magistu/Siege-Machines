package ru.magistu.siegemachines.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import ru.magistu.siegemachines.client.renderer.model.MachineModel;
import ru.magistu.siegemachines.entity.machine.Culverin;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CulverinGeoRenderer extends MachineGeoRenderer<Culverin>
{
	public CulverinGeoRenderer(EntityRendererProvider.Context renderManager)
	{
		super(renderManager, new MachineModel<>("culverin"));
	}

	@Override
	public RenderType getRenderType(Culverin animatable, float partialTicks, PoseStack stack,
									MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation)
	{
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
