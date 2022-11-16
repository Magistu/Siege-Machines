package magistu.siegemachines.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import magistu.siegemachines.client.renderer.model.MachineModel;
import magistu.siegemachines.entity.machine.Culverin;
import magistu.siegemachines.entity.machine.Mortar;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

import java.util.Optional;

public class CulverinGeoRenderer extends MachineGeoRenderer<Culverin>
{
	public CulverinGeoRenderer(EntityRendererManager renderManager)
	{
		super(renderManager, new MachineModel<>("culverin"));
	}

	@Override
	public RenderType getRenderType(Culverin animatable, float partialTicks, MatrixStack stack,
			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation)
	{
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
