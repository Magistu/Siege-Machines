package magistu.siegemachines.client.renderer.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class GiantArrowModel extends Model
{
	private final ModelRenderer cube_r0;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;

	public GiantArrowModel()
	{
		super(RenderType::entityCutout);
		texWidth = 64;
		texHeight = 64;

		cube_r0 = new ModelRenderer(this);
		cube_r0.setPos(0.0F, 0.0F, 0.0F);
		setRotationAngle(cube_r0, 0.0F, 0.0F, 0.0F);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(0.0F, 0.0F, 3.0F);
		cube_r0.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, -0.7854F);
		cube_r1.texOffs(15, 0).addBox(-2.0F, 0.0F, -10.0F, 4.0F, 0.0F, 4.0F, 0.0F, false);
		cube_r1.texOffs(-34, 0).addBox(-2.5F, 0.0F, -7.0F, 5.0F, 0.0F, 34.0F, 0.0F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setPos(0.0F, 0.0F, 3.0F);
		cube_r0.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, 0.7854F);
		cube_r2.texOffs(7, 0).addBox(-2.0F, 0.0F, -10.0F, 4.0F, 0.0F, 4.0F, 0.0F, false);
		cube_r2.texOffs(-34, 0).addBox(-2.5F, 0.0F, -7.0F, 5.0F, 0.0F, 34.0F, 0.0F, false);
	}

	public void setupAnim(float p_225603_1_, float p_225603_2_, float p_225603_3_)
	{
		this.cube_r0.yRot = p_225603_2_ * ((float)Math.PI / 180F);
		this.cube_r0.xRot = p_225603_3_ * ((float)Math.PI / 180F);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_)
	{
		cube_r0.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
	}
}
