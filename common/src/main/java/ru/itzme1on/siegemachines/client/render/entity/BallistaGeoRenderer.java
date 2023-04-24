package ru.itzme1on.siegemachines.client.render.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.itzme1on.siegemachines.client.render.model.MachineModel;
import ru.itzme1on.siegemachines.entity.machine.Ballista;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

import java.util.Optional;

public class BallistaGeoRenderer extends MachineGeoRenderer<Ballista> 
{
	public BallistaGeoRenderer(EntityRendererFactory.Context renderManager) 
	{
		super(renderManager, new MachineModel<>("ballista"));
	}

	@Override
	public RenderLayer getRenderType(Ballista animatable, float partialTick, MatrixStack MatrixStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, Identifier texture) 
	{
		return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void renderEarly(Ballista animatable, MatrixStack stackIn, float ticks, VertexConsumerProvider RenderLayerBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
		GeoModelProvider<Ballista> modelProvider = this.getGeoModelProvider();
		GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(animatable));

		Optional<GeoBone> turret = model.getBone("Balliste");

		turret.ifPresent(bone -> bone.setRotationX(-animatable.getTurretPitch() * (float) Math.PI / 180.0f));
		turret.ifPresent(bone -> bone.setRotationY(-animatable.getTurretYaw() * (float) Math.PI / 180.0f));

		Optional<GeoBone> projectile = model.getBone("BallistaArrow");

		projectile.ifPresent(bone -> bone.setRotationX(-animatable.getTurretPitch() * (float) Math.PI / 180.0f));
		projectile.ifPresent(bone -> bone.setRotationY(-animatable.getTurretYaw() * (float) Math.PI / 180.0f));

		boolean shouldRender = ((animatable.useTicks <= 0 && animatable.shootingTicks <= 0) || (animatable.useTicks > 0 && animatable.shootingTicks > 0)) && animatable.delayTicks <= 0;
		int projectileSize = shouldRender && animatable.hasAmmo() ? 1 : 0;

		projectile.ifPresent(bone -> bone.setScaleX(projectileSize));
		projectile.ifPresent(bone -> bone.setScaleY(projectileSize));
		projectile.ifPresent(bone -> bone.setScaleZ(projectileSize));

		super.renderEarly(animatable, stackIn, ticks, RenderLayerBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
	}
}
