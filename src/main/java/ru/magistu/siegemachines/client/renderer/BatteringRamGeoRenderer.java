package ru.magistu.siegemachines.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.Nullable;
import ru.magistu.siegemachines.client.renderer.model.MachineModel;
import ru.magistu.siegemachines.entity.machine.BatteringRam;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BatteringRamGeoRenderer extends MachineGeoRenderer<BatteringRam>
{
	public BatteringRamGeoRenderer(EntityRendererProvider.Context renderManager)
	{
		super(renderManager, new MachineModel<>("battering_ram"));
	}

	@Override
	public RenderType getRenderType(BatteringRam animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
