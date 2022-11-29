package magistu.siegemachines.client.renderer;

import magistu.siegemachines.client.renderer.model.MachineItemModel;
import magistu.siegemachines.item.MachineItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class MachineItemGeoRenderer extends GeoItemRenderer<MachineItem> {
	public MachineItemGeoRenderer(MachineItemModel<MachineItem> model)
	{
		super(model);
	}
}
