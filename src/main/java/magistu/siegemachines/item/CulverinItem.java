package magistu.siegemachines.item;

import magistu.siegemachines.client.renderer.MachineItemGeoRenderer;
import magistu.siegemachines.client.renderer.model.MachineItemModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class CulverinItem extends MachineItem
{
    public CulverinItem()
    {
        super(new Properties().tab(ModItems.GROUP_SM), "culverin", "CULVERIN");
    }

    @Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IItemRenderProperties() {
			private final BlockEntityWithoutLevelRenderer renderer = new MachineItemGeoRenderer(new MachineItemModel<>("culverin"));

			@Override
			public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
				return renderer;
			}
		});
	}
}
