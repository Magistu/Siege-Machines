package ru.magistu.siegemachines.util;

import net.minecraft.world.phys.Vec3;

public class CartesianGeometry
{
	public static Vec3 applyRotations(Vec3 vec, double pitch, double yaw) {
		double x = vec.x * Math.cos(yaw) - vec.y * Math.sin(pitch) * Math.sin(yaw) - vec.z * Math.sin(yaw) * Math.cos(pitch);
		double y = vec.y * Math.cos(pitch) - vec.z * Math.sin(pitch);
		double z = vec.x * Math.sin(yaw) + vec.y * Math.sin(pitch) * Math.cos(yaw) + vec.z * Math.cos(yaw) * Math.cos(pitch);
		return new Vec3(x, y, z);
	}
}
