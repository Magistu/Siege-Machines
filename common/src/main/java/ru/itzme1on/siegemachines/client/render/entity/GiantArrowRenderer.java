package ru.itzme1on.siegemachines.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import ru.itzme1on.siegemachines.SiegeMachines;
import ru.itzme1on.siegemachines.client.render.model.GiantArrowModel;
import ru.itzme1on.siegemachines.entity.projectile.projectiles.GiantArrow;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class GiantArrowRenderer extends EntityRenderer<GiantArrow> {
    public static final Identifier TEXTURE_LOCATION = new Identifier(SiegeMachines.MOD_ID, "textures/entity/giant_arrow.png");

    public GiantArrowModel model = new GiantArrowModel(GiantArrowModel.createModel());

    public GiantArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(GiantArrow entity) {
        return TEXTURE_LOCATION;
    }

    public void render(@NotNull GiantArrow giantarrow, float p_225623_2_, float p_225623_3_, @NotNull MatrixStack matrixstack, @NotNull VertexConsumerProvider buffer, int n) {
        matrixstack.push();
        matrixstack.scale(-1.0F, -1.0F, -1.0F);
        float f = MathHelper.lerp(p_225623_3_, giantarrow.prevYaw, giantarrow.getYaw());
        float f1 = -MathHelper.lerp(p_225623_3_, giantarrow.prevPitch, giantarrow.getPitch());
        float f2 = (float) giantarrow.shake - p_225623_3_;
        if (f2 > 0.0F) f1 -= MathHelper.sin(f2 * 3.0F) * f2;

        VertexConsumer IVertexBuilder = buffer.getBuffer(this.model.getLayer(this.getTexture(giantarrow)));

        this.model.setupAnim(0.0F, f, f1);
        this.model.render(matrixstack, IVertexBuilder, n, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixstack.pop();

        super.render(giantarrow, p_225623_2_, p_225623_3_, matrixstack, buffer, n);
    }
}
