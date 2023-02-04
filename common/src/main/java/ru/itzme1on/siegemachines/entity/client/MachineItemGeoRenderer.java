package ru.itzme1on.siegemachines.entity.client;

import ru.itzme1on.siegemachines.item.MachineItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class MachineItemGeoRenderer extends GeoItemRenderer<MachineItem> {
    public MachineItemGeoRenderer(AnimatedGeoModel<MachineItem> modelProvider) {
        super(modelProvider);
    }
}
