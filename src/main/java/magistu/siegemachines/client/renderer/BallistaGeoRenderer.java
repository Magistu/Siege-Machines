package magistu.siegemachines.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import magistu.siegemachines.client.renderer.model.MachineModel;
import magistu.siegemachines.entity.machine.Ballista;
import magistu.siegemachines.entity.projectile.ProjectileBuilder;
import magistu.siegemachines.item.ModItems;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

import java.util.Optional;

public class BallistaGeoRenderer extends MachineGeoRenderer<Ballista>
{
	public BallistaGeoRenderer(EntityRendererManager renderManager)
	{
		super(renderManager, new MachineModel<>("ballista"));
	}

	@Override
	public RenderType getRenderType(Ballista animatable, float partialTicks, MatrixStack stack,
									IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation)
	{
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void renderEarly(Ballista animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		GeoModelProvider<Ballista> modelProvider = this.getGeoModelProvider();
		GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(animatable));

		Optional<GeoBone> turret = model.getBone("Balliste");
		turret.ifPresent(bone -> bone.setRotationX(-animatable.getTurretPitch() * (float) Math.PI / 180.0f));
		turret.ifPresent(bone -> bone.setRotationY(-animatable.getTurretYaw() * (float) Math.PI / 180.0f));

		Optional<GeoBone> projectile = model.getBone("BallistaArrow");
		projectile.ifPresent(bone -> bone.setRotationX(-animatable.getTurretPitch() * (float) Math.PI / 180.0f));
		projectile.ifPresent(bone -> bone.setRotationY(-animatable.getTurretYaw() * (float) Math.PI / 180.0f));
		boolean shouldrender = ((animatable.useticks <= 0 && animatable.shootingticks <= 0) || (animatable.useticks > 0 && animatable.shootingticks > 0)) && animatable.delayticks <= 0;
		int projectilesize = shouldrender && animatable.hasAmmo() ? 1 : 0;
		projectile.ifPresent(bone -> bone.setScaleX(projectilesize));
		projectile.ifPresent(bone -> bone.setScaleY(projectilesize));
		projectile.ifPresent(bone -> bone.setScaleZ(projectilesize));

		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
	}
}
