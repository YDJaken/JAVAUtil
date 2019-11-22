package com.dy.Util.Math;

public class Point3D {
	private Double x, y, z;
	public static final Point3D UNIT_X = new Point3D(1, 0, 0);
	public static final Point3D UNIT_Y = new Point3D(0, 1, 0);
	public static final Point3D UNIT_Z = new Point3D(0, 0, 1);

	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D() {
		this(0, 0, 0);
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Double getZ() {
		return z;
	}

	public void setZ(Double z) {
		this.z = z;
	}

	public Point3D clone() {
		return this.copy();
	}

	public Point3D copy() {
		return new Point3D(this.x, this.y, this.z);
	}

	/**
	 * 标量乘
	 * 
	 * @param scale
	 */
	public void applyScaler(Double scale) {
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;
	}

	public String toString() {
		return "x: " + this.x + ", y:" + this.y + " ,z:" + this.z;
	}

	/**
	 * 获得-V
	 */
	public void ngate() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}

	/**
	 * 向量加
	 * 
	 * @param otherPoint
	 */
	public void add(Point3D otherPoint) {
		this.x = this.x + otherPoint.x;
		this.y = this.y + otherPoint.y;
		this.z = this.z + otherPoint.z;
	}

	/**
	 * 向量减
	 * 
	 * @param otherPoint
	 */
	public void subtraction(Point3D otherPoint) {
		Point3D tmp = otherPoint.copy();
		tmp.ngate();
		this.add(tmp);
	}

	/**
	 * 外积 叉乘
	 * 
	 * @param otherPoint
	 */
	public Point3D crossProduct(Point3D otherPoint) {
		Point3D tmp = this.copy();
		tmp.x = this.y * otherPoint.z - this.z * otherPoint.y;
		tmp.y = this.z * otherPoint.x - this.x * otherPoint.z;
		tmp.z = this.x * otherPoint.y - this.y * otherPoint.x;
		return tmp;
	}

	/**
	 * 归一化
	 * 
	 * @return
	 */
	public Point3D normalize() {
		double mag = Point3D.computeMagenitude(this);
		Point3D ret = this.copy();
		ret.applyScaler(1.0 / mag);
		return ret;
	}

	public static Point3D multiplyComponents(Point3D left, Point3D right) {
		return new Point3D(left.x * right.x, left.y * right.y, left.z * right.z);
	}

	/**
	 * 内积 点乘
	 * 
	 * @param otherPoint
	 * @return {number}
	 */
	public Double dotProduct(Point3D otherPoint) {
		return this.x * otherPoint.x + this.y * otherPoint.y + this.z * otherPoint.z;
	}

	/**
	 * 计算点的大小
	 * 
	 * @param point
	 * @return {*}
	 */
	public static Double computeMagenitude(Point3D point) {
		return Math.sqrt(point.x * point.x + point.y * point.y + point.z * point.z);
	}

	public static Point3D fromString(String position) {
		return Point3D.fromString(position, " ");
	}

	public static Point3D fromString(String position, String spliter) throws Error {
		position = position.trim();
		String[] aim = position.split(spliter);
		if (aim.length != 3) {
			throw new Error("Input String is not valid!");
		}
		return new Point3D(Double.parseDouble(aim[0]), Double.parseDouble(aim[1]), Double.parseDouble(aim[2]));
	}
}
