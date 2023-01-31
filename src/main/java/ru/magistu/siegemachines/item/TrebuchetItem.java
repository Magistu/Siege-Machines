package ru.magistu.siegemachines.item;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import ru.magistu.siegemachines.client.renderer.MachineItemGeoRenderer;
import ru.magistu.siegemachines.client.renderer.model.MachineItemModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import ru.magistu.siegemachines.entity.EntityTypes;

import java.util.function.Consumer;

public class TrebuchetItem extends MachineItem
{
    public TrebuchetItem()
    {
        super(new Properties().tab(ModItems.GROUP_SM), "trebuchet", "TREBUCHET");
    }

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IClientItemExtensions() {
			private final BlockEntityWithoutLevelRenderer renderer = new MachineItemGeoRenderer(new MachineItemModel<>("trebuchet"));

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer;
			}
		});
	}

	@Override
	public EntityType<?> getType() {
		return EntityTypes.TREBUCHET.get();
	}
}
