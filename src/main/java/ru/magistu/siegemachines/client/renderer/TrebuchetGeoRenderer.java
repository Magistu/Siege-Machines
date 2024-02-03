package ru.magistu.siegemachines.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import ru.magistu.siegemachines.client.renderer.model.MachineModel;
import ru.magistu.siegemachines.entity.machine.SiegeLadder;
import ru.magistu.siegemachines.entity.machine.Trebuchet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

import java.util.Optional;

public class TrebuchetGeoRenderer extends MachineGeoRenderer<Trebuchet>
{
	public TrebuchetGeoRenderer(EntityRendererProvider.Context renderManager)
	{
		super(renderManager, new MachineModel<>("trebuchet"));
	}

	@Override
	public RenderType getRenderType(Trebuchet animatable, float partialTicks, PoseStack stack,
									MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation)
	{
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void renderEarly(Trebuchet animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		GeoModelProvider<Trebuchet> modelProvider = this.getGeoModelProvider();
		GeoModel model = modelProvider.getModel(modelProvider.getModelResource(animatable));

		Optional<GeoBone> projectile = model.getBone("Cobblestone");
		int projectilesize = (animatable.state == Trebuchet.State.IDLE_RELOADED || animatable.shootingticks > 0) && animatable.hasAmmo() ? 1 : 0;
		projectile.ifPresent(bone -> bone.setScaleX(projectilesize));
		projectile.ifPresent(bone -> bone.setScaleY(projectilesize));
		projectile.ifPresent(bone -> bone.setScaleZ(projectilesize));

		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
	}

	@Override
	public boolean shouldRender(Trebuchet animatable, Frustum camera, double camx, double camy, double camz)
	{
		if (!animatable.shouldRender(camx, camy, camz)) {
			return false;
		} else if (animatable.noCulling) {
			return true;
		} else {
			AABB aabb = animatable.getBoundingBoxForCulling().inflate(10.0D);
			if (aabb.hasNaN() || aabb.getSize() == 0.0D) {
				aabb = new AABB(animatable.getX() - 2.0D, animatable.getY() - 2.0D, animatable.getZ() - 2.0D, animatable.getX() + 2.0D, animatable.getY() + 2.0D, animatable.getZ() + 2.0D);
			}

			return camera.isVisible(aabb);
		}
	}
}
