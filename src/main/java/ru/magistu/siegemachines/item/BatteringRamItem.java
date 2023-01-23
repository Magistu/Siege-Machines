package ru.magistu.siegemachines.item;

import ru.magistu.siegemachines.client.renderer.MachineItemGeoRenderer;
import ru.magistu.siegemachines.client.renderer.model.MachineItemModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class BatteringRamItem extends MachineItem
{
    public BatteringRamItem()
    {
        super(new Properties().tab(ModItems.GROUP_SM), "battering_ram", "BATTERING_RAM");
    }

    @Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IItemRenderProperties() {
			private final BlockEntityWithoutLevelRenderer renderer = new MachineItemGeoRenderer(new MachineItemModel<>("battering_ram"));

			@Override
			public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
				return renderer;
			}
		});
	}
}
