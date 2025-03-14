package ru.magistu.siegemachines.entity.machine;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import ru.magistu.siegemachines.util.CartesianGeometry;

public class Trebuchet extends Catapult implements ShootingGeoEntity {
    private final MachinePartEntity[] subentities;
    private final MachinePartEntity backside;

    private final Vec3 backsidepos;

    public Trebuchet(EntityType<? extends Mob> entitytype, Level level) {
        super(entitytype, level, MachineType.TREBUCHET);
        this.backside = new MachinePartEntity(this, "backside", 5.0F, 2.0F);
        this.backsidepos = new Vec3(0.0, 0.0, -85.0).scale(1.0 / 16.0);
        this.subentities = new MachinePartEntity[]{this.backside};
    }

    private void tickPart(MachinePartEntity subentity, double p_226526_2_, double p_226526_4_, double p_226526_6_) {
        subentity.setPos(this.getX() + p_226526_2_, this.getY() + p_226526_4_, this.getZ() + p_226526_6_);
    }

    @Override
    public void aiStep() {
        Vec3[] avector3d = new Vec3[this.subentities.length];

        Vec3 pos = this.position().add(CartesianGeometry.applyRotations(this.backsidepos, 0.0, this.getYaw()));
        this.tickPart(this.backside, pos.x, pos.y, pos.z);

        for (int i = 0; i < this.subentities.length; ++i) {
            avector3d[i] = new Vec3(this.subentities[i].getX(), this.subentities[i].getY(), this.subentities[i].getZ());
        }

        for (int i = 0; i < this.subentities.length; ++i) {
            this.subentities[i].xo = avector3d[i].x;
            this.subentities[i].yo = avector3d[i].y;
            this.subentities[i].zo = avector3d[i].z;
            this.subentities[i].xOld = avector3d[i].x;
            this.subentities[i].yOld = avector3d[i].y;
            this.subentities[i].zOld = avector3d[i].z;
        }

        super.aiStep();
    }
}