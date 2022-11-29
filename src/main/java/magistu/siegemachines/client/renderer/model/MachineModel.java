package magistu.siegemachines.client.renderer.model;

import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.entity.machine.Machine;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;


public class MachineModel<T extends Machine & IAnimatable> extends AnimatedGeoModel<T>
{
    public final String name;
	public final ResourceLocation animationlocation;
	public final ResourceLocation modellocation;
	public final ResourceLocation texturelocation;

	public MachineModel(String name)
	{
		this.name = name;
		this.animationlocation = new ResourceLocation(SiegeMachines.ID, "animations/" + this.name + ".animation.json");
		this.modellocation = new ResourceLocation(SiegeMachines.ID, "geo/" + this.name + ".geo.json");
		this.texturelocation = new ResourceLocation(SiegeMachines.ID, "textures/entity/" + this.name + ".png");
	}

    @Override
	public ResourceLocation getAnimationFileLocation(T entity)
	{
		return this.animationlocation;
	}

	@Override
	public ResourceLocation getModelLocation(T entity)
	{
		return this.modellocation;
	}

	@Override
	public ResourceLocation getTextureLocation(T entity)
	{
		return this.texturelocation;
	}
}
