package ru.magistu.siegemachines.client.renderer.model;

import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MachineModel<T extends Machine & IAnimatable> extends AnimatedGeoModel<T>
{
    public final String name;
	public final ResourceLocation animationLocation;
	public final ResourceLocation modelLocation;
	public final ResourceLocation textureLocation;

	public MachineModel(String name)
	{
		this.name = name;
		this.animationLocation = new ResourceLocation(SiegeMachines.MOD_ID, "animations/" + this.name + ".animation.json");
		this.modelLocation = new ResourceLocation(SiegeMachines.MOD_ID, "geo/" + this.name + ".geo.json");
		this.textureLocation = new ResourceLocation(SiegeMachines.MOD_ID, "textures/entity/" + this.name + ".png");
	}

    @Override
	public ResourceLocation getAnimationFileLocation(T entity) {
		return this.animationLocation;
	}

	@Override
	public ResourceLocation getModelLocation(T entity) {
		return this.modelLocation;
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return this.textureLocation;
	}
}
