package ru.magistu.siegemachines.client.renderer;

import ru.magistu.siegemachines.client.renderer.model.MachineItemModel;
import ru.magistu.siegemachines.entity.machine.Machine;
import ru.magistu.siegemachines.item.MachineItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MachineItemGeoRenderer<T extends Machine> extends GeoItemRenderer<MachineItem<T>> {
	public MachineItemGeoRenderer(MachineItemModel<MachineItem<T>> model)
	{
		super(model);
	}
}