package ru.magistu.siegemachines.item;

import org.jetbrains.annotations.NotNull;
import ru.magistu.siegemachines.client.renderer.MachineItemGeoRenderer;
import ru.magistu.siegemachines.client.renderer.model.MachineItemModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class CatapultItem extends MachineItem
{
    public CatapultItem()
    {
        super(new Properties().tab(ModItems.GROUP_SM), "catapult", "CATAPULT");
    }

    @Override
	public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IItemRenderProperties() {
			private final BlockEntityWithoutLevelRenderer renderer = new MachineItemGeoRenderer(new MachineItemModel<>("catapult"));

			@Override
			public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
				return renderer;
			}
		});
	}
}
