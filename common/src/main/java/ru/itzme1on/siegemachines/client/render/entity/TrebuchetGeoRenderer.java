package ru.itzme1on.siegemachines.client.render.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import ru.itzme1on.siegemachines.client.render.model.MachineModel;
import ru.itzme1on.siegemachines.entity.machine.machines.Trebuchet;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

import java.util.Optional;

public class TrebuchetGeoRenderer extends MachineGeoRenderer<Trebuchet> {
	public TrebuchetGeoRenderer(EntityRendererFactory.Context renderManager) {
		super(renderManager, new MachineModel<>("trebuchet"));
	}

	@Override
	public RenderLayer getRenderType(Trebuchet animatable, float partialTicks, MatrixStack stack,
									 VertexConsumerProvider RenderLayerBuffer, VertexConsumer vertexBuilder, int packedLightIn,
									 Identifier textureLocation)
	{
		return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void renderEarly(Trebuchet animatable, MatrixStack stackIn, float ticks,
							VertexConsumerProvider RenderLayerBuffer, VertexConsumer vertexBuilder,
							int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {

		GeoModelProvider<Trebuchet> modelProvider = this.getGeoModelProvider();
		GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(animatable));

		Optional<GeoBone> projectile = model.getBone("Cobblestone");
		int projectileSize = (animatable.state == Trebuchet.State.IDLE_RELOADED || animatable.shootingTicks > 0) && animatable.hasAmmo() ? 1 : 0;
		projectile.ifPresent(bone -> bone.setScaleX(projectileSize));
		projectile.ifPresent(bone -> bone.setScaleY(projectileSize));
		projectile.ifPresent(bone -> bone.setScaleZ(projectileSize));

		super.renderEarly(animatable, stackIn, ticks, RenderLayerBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
	}
}
