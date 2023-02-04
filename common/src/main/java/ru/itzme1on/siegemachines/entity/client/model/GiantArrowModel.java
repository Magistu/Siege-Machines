package ru.itzme1on.siegemachines.entity.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class GiantArrowModel extends Model {
    protected final ModelPart cube_r0;
    protected final ModelPart cube_r1;
    protected final ModelPart cube_r2;

    public GiantArrowModel(ModelPart part) {
        super(RenderLayer::getEntityTranslucent);
        this.cube_r0 = part;
        this.cube_r1 = part.getChild("cube_r1");
        this.cube_r2 = part.getChild("cube_r2");
    }

    public static ModelPart createModel() {
        ModelData modelData = new ModelData();
        ModelPartData partData = modelData.getRoot();
        partData.addChild("cube_r1", ModelPartBuilder.create()
                        .uv(15, 0)
                        .cuboid(-2.0F, 0.0F, -10.0F, 4.0F, 0.0F, 4.0F)
                        .uv(-34, 0)
                        .cuboid(-2.5F, 0.0F, -7.0F, 5.0F, 0.0F, 34.0F),
                ModelTransform.rotation(0.0F, 0.0F, -0.7854F));
        partData.addChild("cube_r2", ModelPartBuilder.create()
                        .uv(7, 0)
                        .cuboid(-2.0F, 0.0F, -10.0F, 4.0F, 0.0F, 4.0F)
                        .uv(-34, 0)
                        .cuboid(-2.5F, 0.0F, -7.0F, 5.0F, 0.0F, 34.0F),
                ModelTransform.rotation(0.0F, 0.0F, 0.7854F));

        return modelData.getRoot().createPart(64, 64);
    }

    public void setupAnim(float a, float b, float c) {
        this.cube_r0.yaw = b * ((float) Math.PI / 180F);
        this.cube_r0.pitch = c * ((float) Math.PI / 180F);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.cube_r0.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
