package ru.magistu.siegemachines.item;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import ru.magistu.siegemachines.client.renderer.MachineItemGeoRenderer;
import ru.magistu.siegemachines.client.renderer.model.MachineItemModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import ru.magistu.siegemachines.entity.EntityTypes;

import java.util.function.Consumer;

public class BatteringRamItem extends MachineItem
{
    public BatteringRamItem()
    {
        super(new Properties().tab(ModItems.GROUP_SM), "battering_ram", "BATTERING_RAM");
    }

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IClientItemExtensions() {
			private final BlockEntityWithoutLevelRenderer renderer = new MachineItemGeoRenderer(new MachineItemModel<>("battering_ram"));

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer;
			}
		});
	}

	@Override
	public EntityType<?> getType() {
		return EntityTypes.BATTERING_RAM.get();
	}
}
