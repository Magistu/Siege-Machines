package ru.magistu.siegemachines.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.Nullable;
import ru.magistu.siegemachines.client.renderer.model.MachineModel;
import ru.magistu.siegemachines.entity.machine.BatteringRam;
import ru.magistu.siegemachines.entity.machine.Catapult;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CatapultGeoRenderer extends MachineGeoRenderer<Catapult>
{
	public CatapultGeoRenderer(EntityRendererProvider.Context renderManager)
	{
		super(renderManager, new MachineModel<>("catapult"));
	}

	@Override
	public RenderType getRenderType(Catapult animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
