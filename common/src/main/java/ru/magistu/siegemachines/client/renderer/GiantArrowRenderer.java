package ru.magistu.siegemachines.client.renderer;

import ru.magistu.siegemachines.SiegeMachines;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import ru.magistu.siegemachines.client.renderer.model.GiantArrowModel;
import ru.magistu.siegemachines.item.GiantArrow;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class GiantArrowRenderer extends EntityRenderer<GiantArrow>
{
    public static final ResourceLocation TEXTURE_LOCATION = SiegeMachines.id("textures/item/giant_arrow.png");
//    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/projectiles/arrow.png");

    public GiantArrowModel model = new GiantArrowModel(GiantArrowModel.createModel());

    public GiantArrowRenderer(EntityRendererProvider.Context p_i46547_1_)
    {
        super(p_i46547_1_);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GiantArrow p_110775_1_)
    {
        return TEXTURE_LOCATION;
    }

    public void render(@NotNull GiantArrow giantarrow, float p_225623_2_, float p_225623_3_, @NotNull PoseStack matrixstack, @NotNull MultiBufferSource buffer, int n)
    {
        matrixstack.pushPose();
        matrixstack.scale(-1.0F, -1.0F, -1.0F);
        float f = Mth.lerp(p_225623_3_, giantarrow.yRotO, giantarrow.getYRot());
        float f1 = -Mth.lerp(p_225623_3_, giantarrow.xRotO, giantarrow.getXRot());
        float f2 = (float) giantarrow.shakeTime - p_225623_3_;
        if (f2 > 0.0F)
        {
            f1 -= Mth.sin(f2 * 3.0F) * f2;
        }
        VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(giantarrow)));
        this.model.setupAnim(0.0F, f, f1);
        this.model.renderToBuffer(matrixstack, ivertexbuilder, n, OverlayTexture.NO_OVERLAY);
        matrixstack.popPose();
        super.render(giantarrow, p_225623_2_, p_225623_3_, matrixstack, buffer, n);
    }
}
