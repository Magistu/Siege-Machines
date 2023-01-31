package ru.magistu.siegemachines.client.renderer.model;

import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.item.MachineItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;


public class MachineItemModel<T extends MachineItem & IAnimatable> extends AnimatedGeoModel<T>
{
    public final String name;
	public final ResourceLocation animationlocation;
	public final ResourceLocation modellocation;
	public final ResourceLocation texturelocation;

	public MachineItemModel(String name)
	{
		this.name = name;
		this.animationlocation = new ResourceLocation(SiegeMachines.ID, "animations/none.animation.json");
		this.modellocation = new ResourceLocation(SiegeMachines.ID, "geo/" + this.name + "_item.geo.json");
		this.texturelocation = new ResourceLocation(SiegeMachines.ID, "textures/entity/" + this.name + ".png");
	}

    @Override
	public ResourceLocation getAnimationResource(T entity)
	{
		return this.animationlocation;
	}

	@Override
	public ResourceLocation getModelResource(T entity)
	{
		return this.modellocation;
	}

	@Override
	public ResourceLocation getTextureResource(T entity)
	{
		return this.texturelocation;
	}
}
