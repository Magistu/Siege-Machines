package ru.itzme1on.siegemachines.entity.client.model;

import net.minecraft.util.Identifier;
import ru.itzme1on.siegemachines.SiegeMachines;
import ru.itzme1on.siegemachines.entity.machine.Machine;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MachineModel <T extends Machine & IAnimatable> extends AnimatedGeoModel<T> {
    public final String name;
    public final Identifier animationLocation;
    public final Identifier modelLocation;
    public final Identifier textureLocation;

    public MachineModel(String name) {
        this.name = name;
        this.animationLocation = new Identifier(SiegeMachines.MOD_ID, "animations/" + this.name + ".animation.json");
        this.modelLocation = new Identifier(SiegeMachines.MOD_ID, "geo/" + this.name + ".geo.json");
        this.textureLocation = new Identifier(SiegeMachines.MOD_ID, "textures/entity/" + this.name + ".png");
    }

    @Override
    public Identifier getModelLocation(T object) {
        return this.modelLocation;
    }

    @Override
    public Identifier getTextureLocation(T object) {
        return this.textureLocation;
    }

    @Override
    public Identifier getAnimationFileLocation(T animatable) {
        return this.animationLocation;
    }
}
