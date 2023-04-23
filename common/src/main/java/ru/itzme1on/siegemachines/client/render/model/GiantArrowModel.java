package ru.itzme1on.siegemachines.client.render.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class GiantArrowModel extends Model {
	protected final ModelPart cube_r0;
	protected final ModelPart cube_r1;
	protected final ModelPart cube_r2;

	public GiantArrowModel(ModelPart p_170945_) {
		super(RenderLayer::getEntityTranslucent);
		this.cube_r0 = p_170945_;
		this.cube_r1 = p_170945_.getChild("cube_r1");
		this.cube_r2 = p_170945_.getChild("cube_r2");
	}

	public static ModelPart createModel() {
		ModelData meshDefinition = new ModelData();
		ModelPartData partDefinition = meshDefinition.getRoot();
		partDefinition.addChild("cube_r1", ModelPartBuilder.create().uv(15, 0).cuboid(-2.0F, 0.0F, -10.0F, 4.0F, 0.0F, 4.0F).uv(-34, 0).cuboid(-2.5F, 0.0F, -7.0F, 5.0F, 0.0F, 34.0F), ModelTransform.rotation(0.0F, 0.0F, -0.7854F));
		partDefinition.addChild("cube_r2", ModelPartBuilder.create().uv(7, 0).cuboid(-2.0F, 0.0F, -10.0F, 4.0F, 0.0F, 4.0F).uv(-34, 0).cuboid(-2.5F, 0.0F, -7.0F, 5.0F, 0.0F, 34.0F), ModelTransform.rotation(0.0F, 0.0F, 0.7854F));

		return meshDefinition.getRoot().createPart(64, 64);
	}

	public void setupAnim(float p_103811_, float p_103812_, float p_103813_) {
		this.cube_r0.yaw = p_103812_ * ((float)Math.PI / 180F);
		this.cube_r0.pitch = p_103813_ * ((float)Math.PI / 180F);
	}

	@Override
	public void render(MatrixStack p_103815_, VertexConsumer p_103816_, int p_103817_, int p_103818_, float p_103819_, float p_103820_, float p_103821_, float p_103822_) {
		this.cube_r0.render(p_103815_, p_103816_, p_103817_, p_103818_, p_103819_, p_103820_, p_103821_, p_103822_);
	}
}
