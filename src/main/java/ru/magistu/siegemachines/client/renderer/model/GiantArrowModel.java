package ru.magistu.siegemachines.client.renderer.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class GiantArrowModel extends Model
{
	protected final ModelPart cube_r0;
	protected final ModelPart cube_r1;
	protected final ModelPart cube_r2;

	public GiantArrowModel(ModelPart p_170945_)
	{
		super(RenderType::entityTranslucent);
		this.cube_r0 = p_170945_;
		this.cube_r1 = p_170945_.getChild("cube_r1");
		this.cube_r2 = p_170945_.getChild("cube_r2");
	}

	public static ModelPart createModel()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(15, 0).addBox(-2.0F, 0.0F, -10.0F, 4.0F, 0.0F, 4.0F).texOffs(-34, 0).addBox(-2.5F, 0.0F, -7.0F, 5.0F, 0.0F, 34.0F), PartPose.rotation(0.0F, 0.0F, -0.7854F));
		partdefinition.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(7, 0).addBox(-2.0F, 0.0F, -10.0F, 4.0F, 0.0F, 4.0F).texOffs(-34, 0).addBox(-2.5F, 0.0F, -7.0F, 5.0F, 0.0F, 34.0F), PartPose.rotation(0.0F, 0.0F, 0.7854F));

		return meshdefinition.getRoot().bake(64, 64);
	}

	public void setupAnim(float p_103811_, float p_103812_, float p_103813_)
	{
		this.cube_r0.yRot = p_103812_ * ((float)Math.PI / 180F);
		this.cube_r0.xRot = p_103813_ * ((float)Math.PI / 180F);
	}

	public void renderToBuffer(PoseStack p_103815_, VertexConsumer p_103816_, int p_103817_, int p_103818_, float p_103819_, float p_103820_, float p_103821_, float p_103822_)
	{
		this.cube_r0.render(p_103815_, p_103816_, p_103817_, p_103818_, p_103819_, p_103820_, p_103821_, p_103822_);
	}
}
