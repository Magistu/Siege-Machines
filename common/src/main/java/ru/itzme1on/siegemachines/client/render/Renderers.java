package ru.itzme1on.siegemachines.client.render;

import ru.itzme1on.siegemachines.client.render.item.*;
import ru.itzme1on.siegemachines.client.render.model.MachineItemModel;
import ru.itzme1on.siegemachines.entity.machine.*;

import java.util.Map;

import static java.util.Map.entry;

public class Renderers
{
    public enum ItemEnum
    {
        BALLISTA,
        BATTERING_RAM,
        CATAPULT,
        CULVERIN,
        MORTAR,
        TREBUCHET
    }

    public static final MachineItemGeoRenderer<Ballista> BALLISTA = new MachineItemGeoRenderer<>(new MachineItemModel<>("ballista"));
    public static final MachineItemGeoRenderer<BatteringRam> BATTERING_RAM = new MachineItemGeoRenderer<>(new MachineItemModel<>("battering_ram"));
    public static final MachineItemGeoRenderer<Catapult> CATAPULT = new MachineItemGeoRenderer<>(new MachineItemModel<>("catapult"));
    public static final MachineItemGeoRenderer<Culverin> CULVERIN = new MachineItemGeoRenderer<>(new MachineItemModel<>("culverin"));
    public static final MachineItemGeoRenderer<Mortar> MORTAR = new MachineItemGeoRenderer<>(new MachineItemModel<>("mortar"));
    public static final MachineItemGeoRenderer<Trebuchet> TREBUCHET = new MachineItemGeoRenderer<>(new MachineItemModel<>("trebuchet"));

    public static final Map<ItemEnum, MachineItemGeoRenderer<? extends Machine>> ITEM_MAP = Map.ofEntries(
            entry(ItemEnum.BALLISTA, BALLISTA),
            entry(ItemEnum.BATTERING_RAM, BATTERING_RAM),
            entry(ItemEnum.CATAPULT, CATAPULT),
            entry(ItemEnum.CULVERIN, CULVERIN),
            entry(ItemEnum.MORTAR, MORTAR),
            entry(ItemEnum.TREBUCHET, TREBUCHET)
    );
}
