package magistu.siegemachines.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import magistu.siegemachines.client.renderer.MachineGeoRenderer;
import magistu.siegemachines.client.renderer.model.MachineItemModel;
import magistu.siegemachines.entity.machine.Mortar;
import magistu.siegemachines.item.MachineItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import java.util.Optional;

public class MachineItemGeoRenderer extends GeoItemRenderer<MachineItem>
{
	public MachineItemGeoRenderer(MachineItemModel<MachineItem> model)
	{
		super(model);
	}
}
