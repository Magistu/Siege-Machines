package ru.itzme1on.siegemachines.client.render.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.itzme1on.siegemachines.client.render.model.MachineModel;
import ru.itzme1on.siegemachines.entity.machine.machines.Mortar;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

import java.util.Optional;

public class MortarGeoRenderer extends MachineGeoRenderer<Mortar> {

	public MortarGeoRenderer(EntityRendererFactory.Context renderManager) {
		super(renderManager, new MachineModel<>("mortar"));
	}

	@Override
	public RenderLayer getRenderType(Mortar animatable, float partialTick, MatrixStack MatrixStack,
									 @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, 
									 int packedLight, Identifier texture) {
		return super.getRenderType(animatable, partialTick, MatrixStack, bufferSource, buffer, packedLight, texture);
	}

	@Override
	public void renderEarly(Mortar animatable, MatrixStack stackIn, float ticks, VertexConsumerProvider RenderLayerBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		GeoModelProvider<Mortar> modelProvider = this.getGeoModelProvider();
		GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(animatable));

		Optional<GeoBone> bone0 = model.getBone("Barrel");
		bone0.ifPresent(bone -> bone.setRotationX(-animatable.getTurretPitch() * (float) Math.PI / 180.0f));
		bone0.ifPresent(bone -> bone.setRotationY(-animatable.getTurretYaw() * (float) Math.PI / 180.0f));

		super.renderEarly(animatable, stackIn, ticks, RenderLayerBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
	}
}
