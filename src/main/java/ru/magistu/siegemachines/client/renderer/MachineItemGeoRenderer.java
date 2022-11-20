package ru.magistu.siegemachines.client.renderer;

import ru.magistu.siegemachines.client.renderer.model.MachineItemModel;
import ru.magistu.siegemachines.item.MachineItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class MachineItemGeoRenderer extends GeoItemRenderer<MachineItem>
{
	public MachineItemGeoRenderer(MachineItemModel<MachineItem> model)
	{
		super(model);
	}
}
