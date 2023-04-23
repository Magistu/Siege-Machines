package ru.itzme1on.siegemachines.client.render.item;

import ru.itzme1on.siegemachines.client.render.model.MachineItemModel;
import ru.itzme1on.siegemachines.entity.machine.Machine;
import ru.itzme1on.siegemachines.item.MachineItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class MachineItemGeoRenderer <T extends Machine> extends GeoItemRenderer<MachineItem<T>>
{
	public MachineItemGeoRenderer(MachineItemModel<MachineItem<T>> model)
	{
		super(model);
	}
}
