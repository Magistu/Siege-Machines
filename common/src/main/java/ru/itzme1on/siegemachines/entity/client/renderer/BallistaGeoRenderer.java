package ru.itzme1on.siegemachines.entity.client.renderer;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.itzme1on.siegemachines.entity.client.model.MachineModel;
import ru.itzme1on.siegemachines.entity.machine.machines.Ballista;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BallistaGeoRenderer extends GeoEntityRenderer<Ballista> {
    public BallistaGeoRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new MachineModel<>("ballista"));
    }

    @Override
    public RenderLayer getRenderType(
            Ballista animatable,
            float partialTick,
            MatrixStack poseStack,
            @Nullable VertexConsumerProvider bufferSource,
            @Nullable VertexConsumer buffer,
            int packedLight,
            Identifier texture) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }
}
