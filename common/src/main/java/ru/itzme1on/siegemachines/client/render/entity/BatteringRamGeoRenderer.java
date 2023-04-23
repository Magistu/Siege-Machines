package ru.itzme1on.siegemachines.client.render.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import ru.itzme1on.siegemachines.client.render.model.MachineModel;
import ru.itzme1on.siegemachines.entity.machine.machines.BatteringRam;

public class BatteringRamGeoRenderer extends MachineGeoRenderer<BatteringRam>
{
	public BatteringRamGeoRenderer(EntityRendererFactory.Context renderManager)
	{
		super(renderManager, new MachineModel<>("battering_ram"));
	}

	@Override
	public RenderLayer getRenderType(BatteringRam animatable, float partialTicks, MatrixStack stack,
									 VertexConsumerProvider RenderLayerBuffer, VertexConsumer vertexBuilder, int packedLightIn,
									 Identifier textureLocation)
	{
		return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
	}
}
