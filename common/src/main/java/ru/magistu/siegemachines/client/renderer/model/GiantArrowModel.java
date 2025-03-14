package ru.magistu.siegemachines.client.renderer.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class GiantArrowModel extends Model {
    protected final ModelPart cube_r0;
    protected final ModelPart cube_r1;
    protected final ModelPart cube_r2;

    public GiantArrowModel(ModelPart modelpart) {
        super(RenderType::entityTranslucent);
        this.cube_r0 = modelpart;
        this.cube_r1 = modelpart.getChild("cube_r1");
        this.cube_r2 = modelpart.getChild("cube_r2");
    }

    public static ModelPart createModel() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(15, 0).addBox(-2.0F, 0.0F, -10.0F, 4.0F, 0.0F, 4.0F).texOffs(-34, 0).addBox(-2.5F, 0.0F, -7.0F, 5.0F, 0.0F, 34.0F), PartPose.rotation(0.0F, 0.0F, -0.7854F));
        partdefinition.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(7, 0).addBox(-2.0F, 0.0F, -10.0F, 4.0F, 0.0F, 4.0F).texOffs(-34, 0).addBox(-2.5F, 0.0F, -7.0F, 5.0F, 0.0F, 34.0F), PartPose.rotation(0.0F, 0.0F, 0.7854F));

        return meshdefinition.getRoot().bake(64, 64);
    }

    public void setupAnim(float p_103811_, float p_103812_, float p_103813_) {
        this.cube_r0.yRot = p_103812_ * ((float) Math.PI / 180.0f);
        this.cube_r0.xRot = p_103813_ * ((float) Math.PI / 180.0f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.cube_r0.render(poseStack, buffer, packedLight, packedOverlay, color);

    }
}
