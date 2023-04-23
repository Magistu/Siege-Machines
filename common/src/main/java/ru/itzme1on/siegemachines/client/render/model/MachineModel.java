package ru.itzme1on.siegemachines.client.render.model;

import net.minecraft.util.Identifier;
import ru.itzme1on.siegemachines.SiegeMachines;
import ru.itzme1on.siegemachines.entity.machine.Machine;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;


public class MachineModel<T extends Machine & IAnimatable> extends AnimatedGeoModel<T>
{
    public final String name;
	public final Identifier animationlocation;
	public final Identifier modellocation;
	public final Identifier texturelocation;

	public MachineModel(String name)
	{
		this.name = name;
		this.animationlocation = new Identifier(SiegeMachines.MOD_ID, "animations/" + this.name + ".animation.json");
		this.modellocation = new Identifier(SiegeMachines.MOD_ID, "geo/" + this.name + ".geo.json");
		this.texturelocation = new Identifier(SiegeMachines.MOD_ID, "textures/entity/" + this.name + ".png");
	}

    @Override
	public Identifier getAnimationFileLocation(T entity)
	{
		return this.animationlocation;
	}

	@Override
	public Identifier getModelLocation(T entity)
	{
		return this.modellocation;
	}

	@Override
	public Identifier getTextureLocation(T entity)
	{
		return this.texturelocation;
	}
}
