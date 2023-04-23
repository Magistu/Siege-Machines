package ru.itzme1on.siegemachines.entity.client.model;

import net.minecraft.util.Identifier;
import ru.itzme1on.siegemachines.SiegeMachinesCore;
import ru.itzme1on.siegemachines.item.MachineItem;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MachineItemModel <T extends MachineItem & IAnimatable> extends AnimatedGeoModel<T> {
    public final String name;
    public final Identifier animationLocation;
    public final Identifier modelLocation;
    public final Identifier textureLocation;

    public MachineItemModel(String name) {
        this.name = name;
        this.animationLocation = new Identifier(SiegeMachinesCore.MOD_ID, "animations/none.animation.json");
        this.modelLocation = new Identifier(SiegeMachinesCore.MOD_ID, "geo/" + this.name + "_item.geo.json");
        this.textureLocation = new Identifier(SiegeMachinesCore.MOD_ID, "textures/entity/" + this.name + ".png");
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
