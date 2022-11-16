package magistu.siegemachines.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import magistu.siegemachines.client.renderer.model.MachineModel;
import magistu.siegemachines.entity.machine.Catapult;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class CatapultGeoRenderer extends MachineGeoRenderer<Catapult>
{
	public CatapultGeoRenderer(EntityRendererManager renderManager)
	{
		super(renderManager, new MachineModel<>("catapult"));
	}

	@Override
	public RenderType getRenderType(Catapult animatable, float partialTicks, MatrixStack stack,
			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation)
	{
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
