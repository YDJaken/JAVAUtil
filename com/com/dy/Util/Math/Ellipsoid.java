package com.dy.Util.Math;

public class Ellipsoid {

	public static final Ellipsoid WGS84 = new Ellipsoid(6378137.0, 6378137.0, 6356752.3142451793);

	public Point3D getRadii() {
		return radii;
	}

	public void setRadii(Point3D radii) {
		this.radii = radii;
	}

	public Point3D getRadiiSquared() {
		return radiiSquared;
	}

	public void setRadiiSquared(Point3D radiiSquared) {
		this.radiiSquared = radiiSquared;
	}

	public Point3D getRadiiToTheFourth() {
		return radiiToTheFourth;
	}

	public void setRadiiToTheFourth(Point3D radiiToTheFourth) {
		this.radiiToTheFourth = radiiToTheFourth;
	}

	public Point3D getOneOverRadii() {
		return oneOverRadii;
	}

	public void setOneOverRadii(Point3D oneOverRadii) {
		this.oneOverRadii = oneOverRadii;
	}

	public Point3D getOneOverRadiiSquared() {
		return oneOverRadiiSquared;
	}

	public void setOneOverRadiiSquared(Point3D oneOverRadiiSquared) {
		this.oneOverRadiiSquared = oneOverRadiiSquared;
	}

	public double getMinimumRadius() {
		return minimumRadius;
	}

	public void setMinimumRadius(double minimumRadius) {
		this.minimumRadius = minimumRadius;
	}

	public double getMaximumRadius() {
		return maximumRadius;
	}

	public void setMaximumRadius(double maximumRadius) {
		this.maximumRadius = maximumRadius;
	}

	public double getCenterToleranceSquared() {
		return centerToleranceSquared;
	}

	public void setCenterToleranceSquared(double centerToleranceSquared) {
		this.centerToleranceSquared = centerToleranceSquared;
	}

	public double getSquaredXOverSquaredZ() {
		return squaredXOverSquaredZ;
	}

	public void setSquaredXOverSquaredZ(double squaredXOverSquaredZ) {
		this.squaredXOverSquaredZ = squaredXOverSquaredZ;
	}

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

	public Point3D geodeticSurfaceNormal(Point3D cartesian) {
		return Point3D.multiplyComponents(cartesian, this.oneOverRadiiSquared).normalize();
	}
}
