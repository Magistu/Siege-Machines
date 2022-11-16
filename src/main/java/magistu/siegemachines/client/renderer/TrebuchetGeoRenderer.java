package magistu.siegemachines.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import magistu.siegemachines.client.renderer.model.MachineModel;
import magistu.siegemachines.entity.machine.Trebuchet;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

import java.util.Optional;

public class TrebuchetGeoRenderer extends MachineGeoRenderer<Trebuchet>
{
	public TrebuchetGeoRenderer(EntityRendererManager renderManager)
	{
		super(renderManager, new MachineModel<>("trebuchet"));
	}

	@Override
	public RenderType getRenderType(Trebuchet animatable, float partialTicks, MatrixStack stack,
			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation)
	{
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void renderEarly(Trebuchet animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		GeoModelProvider<Trebuchet> modelProvider = this.getGeoModelProvider();
		GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(animatable));

		Optional<GeoBone> projectile = model.getBone("Cobblestone");
		int projectilesize = (animatable.state == Trebuchet.State.IDLE_RELOADED || animatable.shootingticks > 0) && animatable.hasAmmo() ? 1 : 0;
		projectile.ifPresent(bone -> bone.setScaleX(projectilesize));
		projectile.ifPresent(bone -> bone.setScaleY(projectilesize));
		projectile.ifPresent(bone -> bone.setScaleZ(projectilesize));

		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
	}
}
