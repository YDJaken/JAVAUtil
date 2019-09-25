package com.dy.Util.Math;

public class Ellipsoid {
	private Point3D radii;
	private Point3D radiiSquared;
	private Point3D radiiToTheFourth;
	private Point3D oneOverRadii;
	private Point3D oneOverRadiiSquared;
	private double minimumRadius;
	private double maximumRadius;
	private double centerToleranceSquared = MathUtil.EPSILON1;
	private double squaredXOverSquaredZ;

	public Ellipsoid(double x, double y, double z) {
		this.radii = new Point3D(x, y, z);
		this.radiiSquared = new Point3D(x * x, y * y, z * z);
		this.radiiToTheFourth = new Point3D(x * x * x * x, y * y * y * y, z * z * z * z);
		this.oneOverRadii = new Point3D(x == 0 ? 0 : 1 / x, y == 0 ? 0 : 1 / y, z == 0 ? 0 : 1 / z);
		this.oneOverRadiiSquared = new Point3D(x == 0 ? 0 : 1 / (x * x), y == 0 ? 0 : 1 / (y * y),
				z == 0 ? 0 : 1 / (z * z));
		this.minimumRadius = Math.min(z, Math.min(x, y));
		this.maximumRadius = Math.max(z, Math.max(x, y));
		if (radiiSquared.getZ() != 0) {
			this.squaredXOverSquaredZ = radiiSquared.getX() / radiiSquared.getZ();
		}
	}
}
