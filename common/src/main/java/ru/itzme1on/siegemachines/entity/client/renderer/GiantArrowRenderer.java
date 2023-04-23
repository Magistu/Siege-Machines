package ru.itzme1on.siegemachines.entity.client.renderer;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import ru.itzme1on.siegemachines.SiegeMachinesCore;
import ru.itzme1on.siegemachines.entity.client.model.GiantArrowModel;
import ru.itzme1on.siegemachines.entity.projectile.projectiles.GiantArrow;

public class GiantArrowRenderer extends EntityRenderer<GiantArrow> {
    public static final Identifier TEXTURE_LOCATION = new Identifier(SiegeMachinesCore.MOD_ID, "textures/entity/giant_arrow.png");

    public GiantArrowModel model = new GiantArrowModel(GiantArrowModel.createModel());

    public GiantArrowRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(GiantArrow entity) {
        return TEXTURE_LOCATION;
    }

    @Override
    public void render(GiantArrow entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.scale(-1.0F, -1.0F, -1.0F);
        float f = MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw());
        float f1 = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());
        float f2 = (float) entity.shake - tickDelta;
        if (f2 > 0.0F)
            f1 -= Math.sin(f2 * 3.0F) * f2;
        VertexConsumer iVertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(this.getTexture(entity)));
        this.model.setupAnim(0.0F, f, f1);
        this.model.render(
                matrices,
                iVertexConsumer,
                light,
                OverlayTexture.DEFAULT_UV,
                1.0F,
                1.0F,
                1.0F,
                1.0F);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
