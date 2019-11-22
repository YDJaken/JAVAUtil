package com.dy.Util.Math;

public class Quaternion {
	private double x, y, z, w;

	public Quaternion(double x, double y, double z, double w) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Quaternion() {
		this(0, 0, 0, 0);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	@Override
	public String toString() {
		return "Quaternion [x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
	}

	public static Quaternion fromHeadingPitchRoll(double heading, double pitch, double roll) {
		Quaternion rollScratch = Quaternion.fromAxisAngle(Point3D.UNIT_X, roll);
		Quaternion pitchScratch = Quaternion.fromAxisAngle(Point3D.UNIT_Y, -pitch);
		Quaternion result = Quaternion.multiply(pitchScratch, rollScratch);
		Quaternion headingScratch = Quaternion.fromAxisAngle(Point3D.UNIT_Z, -heading);
		return Quaternion.multiply(headingScratch, result);
	}

	public static Quaternion multiply(Quaternion left, Quaternion right) {
		double leftX = left.x;
		double leftY = left.y;
		double leftZ = left.z;
		double leftW = left.w;

		double rightX = right.x;
		double rightY = right.y;
		double rightZ = right.z;
		double rightW = right.w;

		double x = leftW * rightX + leftX * rightW + leftY * rightZ - leftZ * rightY;
		double y = leftW * rightY - leftX * rightZ + leftY * rightW + leftZ * rightX;
		double z = leftW * rightZ + leftX * rightY - leftY * rightX + leftZ * rightW;
		double w = leftW * rightW - leftX * rightX - leftY * rightY - leftZ * rightZ;

		return new Quaternion(x, y, z, w);
	}

	public static Quaternion fromAxisAngle(Point3D axis, double angle) {
		double halfAngle = angle * 0.5;
		double s = Math.sin(halfAngle);
		axis = axis.normalize();
		return new Quaternion(axis.getX() * s, axis.getY() * s, axis.getZ() * s, Math.cos(halfAngle));
	}
}
