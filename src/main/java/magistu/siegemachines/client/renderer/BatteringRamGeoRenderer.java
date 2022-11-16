package magistu.siegemachines.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import magistu.siegemachines.client.renderer.model.MachineModel;
import magistu.siegemachines.entity.machine.Ballista;
import magistu.siegemachines.entity.machine.BatteringRam;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

import java.util.Optional;

public class BatteringRamGeoRenderer extends MachineGeoRenderer<BatteringRam>
{
	public BatteringRamGeoRenderer(EntityRendererManager renderManager)
	{
		super(renderManager, new MachineModel<>("battering_ram"));
	}

	@Override
	public RenderType getRenderType(BatteringRam animatable, float partialTicks, MatrixStack stack,
									IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation)
	{
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
