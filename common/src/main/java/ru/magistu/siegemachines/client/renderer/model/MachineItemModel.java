package ru.magistu.siegemachines.client.renderer.model;

import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.item.MachineItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;


public class MachineItemModel<T extends MachineItem & GeoAnimatable> extends GeoModel<T>
{
    public final String name;
	public final ResourceLocation animationlocation;
	public final ResourceLocation modellocation;
	public final ResourceLocation texturelocation;

	public MachineItemModel(String name)
	{
		this.name = name;
		this.animationlocation = SiegeMachines.id("animations/none.animation.json");
		this.modellocation = SiegeMachines.id( "geo/" + this.name + "_item.geo.json");
		this.texturelocation = SiegeMachines.id( "textures/entity/" + this.name + ".png");
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
