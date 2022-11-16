package magistu.siegemachines.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.client.renderer.model.GiantArrowModel;
import magistu.siegemachines.entity.projectile.GiantArrow;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class GiantArrowRenderer extends EntityRenderer<GiantArrow>
{
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(SiegeMachines.ID, "textures/entity/giant_arrow.png");
//    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/projectiles/arrow.png");

    public GiantArrowModel model = new GiantArrowModel();

    public GiantArrowRenderer(EntityRendererManager p_i46547_1_)
    {
        super(p_i46547_1_);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GiantArrow p_110775_1_)
    {
        return TEXTURE_LOCATION;
    }

    public void render(@NotNull GiantArrow giantarrow, float p_225623_2_, float p_225623_3_, @NotNull MatrixStack matrixstack, @NotNull IRenderTypeBuffer buffer, int n)
    {
        matrixstack.pushPose();
        matrixstack.scale(-1.0F, -1.0F, -1.0F);
        float f = MathHelper.lerp(p_225623_3_, giantarrow.yRotO, giantarrow.yRot);
        float f1 = -MathHelper.lerp(p_225623_3_, giantarrow.xRotO, giantarrow.xRot);
        float f2 = (float) giantarrow.shakeTime - p_225623_3_;
        if (f2 > 0.0F)
        {
            f1 -= MathHelper.sin(f2 * 3.0F) * f2;
        }
        IVertexBuilder ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(giantarrow)));
        this.model.setupAnim(0.0F, f, f1);
        this.model.renderToBuffer(matrixstack, ivertexbuilder, n, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixstack.popPose();
        super.render(giantarrow, p_225623_2_, p_225623_3_, matrixstack, buffer, n);
    }
}
