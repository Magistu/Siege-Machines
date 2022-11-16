package magistu.siegemachines.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import magistu.siegemachines.client.renderer.model.MachineModel;
import magistu.siegemachines.entity.machine.Mortar;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

import java.util.Optional;

public class MortarGeoRenderer extends MachineGeoRenderer<Mortar>
{
	public MortarGeoRenderer(EntityRendererManager renderManager)
	{
		super(renderManager, new MachineModel<>("mortar"));
	}

	@Override
	public RenderType getRenderType(Mortar animatable, float partialTicks, MatrixStack stack,
			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation)
	{
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void renderEarly(Mortar animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		GeoModelProvider<Mortar> modelProvider = this.getGeoModelProvider();
		GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(animatable));

		Optional<GeoBone> bone0 = model.getBone("Barrel");
		bone0.ifPresent(bone -> bone.setRotationX(-animatable.getTurretPitch() * (float) Math.PI / 180.0f));
		bone0.ifPresent(bone -> bone.setRotationY(-animatable.getTurretYaw() * (float) Math.PI / 180.0f));

		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
	}
}
