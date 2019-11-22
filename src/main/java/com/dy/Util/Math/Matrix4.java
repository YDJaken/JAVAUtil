package com.dy.Util.Math;

public class Matrix4 {
	private double[] entries = new double[16];
	
	public double[] getEntries() {
		return entries;
	}

	public Matrix4() {

	}

	public Matrix4(double[] entries) {
		if (entries.length == 16) {
			this.entries = entries;
		}
	}

	public Matrix4(double column0Row0, double column1Row0, double column2Row0, double column3Row0, double column0Row1,
			double column1Row1, double column2Row1, double column3Row1, double column0Row2, double column1Row2,
			double column2Row2, double column3Row2, double column0Row3, double column1Row3, double column2Row3,
			double column3Row3) {
		this.entries[0] = column0Row0;
		this.entries[1] = column0Row1;
		this.entries[2] = column0Row2;
		this.entries[3] = column0Row3;
		this.entries[4] = column1Row0;
		this.entries[5] = column1Row1;
		this.entries[6] = column1Row2;
		this.entries[7] = column1Row3;
		this.entries[8] = column2Row0;
		this.entries[9] = column2Row1;
		this.entries[10] = column2Row2;
		this.entries[11] = column2Row3;
		this.entries[12] = column3Row0;
		this.entries[13] = column3Row1;
		this.entries[14] = column3Row2;
		this.entries[15] = column3Row3;
	}

	public Matrix4 clone() {
		Matrix4 ret = new Matrix4();
		for (int i = 0; i < this.entries.length; i++) {
			ret.entries[i] = this.entries[i];
		}
		return ret;
	}

	public double getEntry(int col, int row) {
		return entries[col * 4 + row];
	}

	public void setEntry(int col, int row, double number) {
		entries[col * 4 + row] = number;
	}

	public static Matrix4 inverseTransformation(Matrix4 matrix) {
		double matrix0 = matrix.entries[0];
		double matrix1 = matrix.entries[1];
		double matrix2 = matrix.entries[2];
		double matrix4 = matrix.entries[4];
		double matrix5 = matrix.entries[5];
		double matrix6 = matrix.entries[6];
		double matrix8 = matrix.entries[8];
		double matrix9 = matrix.entries[9];
		double matrix10 = matrix.entries[10];

		double vX = matrix.entries[12];
		double vY = matrix.entries[13];
		double vZ = matrix.entries[14];

		double x = -matrix0 * vX - matrix1 * vY - matrix2 * vZ;
		double y = -matrix4 * vX - matrix5 * vY - matrix6 * vZ;
		double z = -matrix8 * vX - matrix9 * vY - matrix10 * vZ;

		Matrix4 ret = new Matrix4();

		double[] result = ret.entries;

		result[0] = matrix0;
		result[1] = matrix4;
		result[2] = matrix8;
		result[3] = 0.0;
		result[4] = matrix1;
		result[5] = matrix5;
		result[6] = matrix9;
		result[7] = 0.0;
		result[8] = matrix2;
		result[9] = matrix6;
		result[10] = matrix10;
		result[11] = 0.0;
		result[12] = x;
		result[13] = y;
		result[14] = z;
		result[15] = 1.0;
		return ret;
	}

	public static Point3D multiplyByPoint(Matrix4 matrix, Point3D cartesian) {
		double vX = cartesian.getX();
		double vY = cartesian.getY();
		double vZ = cartesian.getZ();

		double x = matrix.entries[0] * vX + matrix.entries[4] * vY + matrix.entries[8] * vZ + matrix.entries[12];
		double y = matrix.entries[1] * vX + matrix.entries[5] * vY + matrix.entries[9] * vZ + matrix.entries[13];
		double z = matrix.entries[2] * vX + matrix.entries[6] * vY + matrix.entries[10] * vZ + matrix.entries[14];

		return new Point3D(x, y, z);
	}

	public static Matrix3 getRotation(Matrix4 matrix) {
		Matrix3 ret = new Matrix3();
		double[] result = ret.getEntries();
		result[0] = matrix.entries[0];
		result[1] = matrix.entries[1];
		result[2] = matrix.entries[2];
		result[3] = matrix.entries[4];
		result[4] = matrix.entries[5];
		result[5] = matrix.entries[6];
		result[6] = matrix.entries[8];
		result[7] = matrix.entries[9];
		result[8] = matrix.entries[10];

		return ret;
	}

	public static Matrix4 computeView(Point3D position, Point3D direction, Point3D up, Point3D right) {
		Matrix4 ret = new Matrix4();

		double[] result = ret.entries;

//		result[0] = right.getX();
//		result[1] = up.getX();
//		result[2] = -direction.getX();
//		result[3] = 0.0;
//		result[4] = right.getY();
//		result[5] = up.getY();
//		result[6] = -direction.getY();
//		result[7] = 0.0;
//		result[8] = right.getZ();
//		result[9] = up.getZ();
//		result[10] = -direction.getZ();
//		result[11] = 0.0;
//		result[12] = -right.dotProduct(position);
//		result[13] = -up.dotProduct(position);
//		result[14] = direction.dotProduct(position);
//		result[15] = 1.0;
		
		result[0] = right.getX();
		result[1] = right.getY();
		result[2] = right.getZ();
		result[3] = 0.0;
		
		result[4] = -up.getX();
		result[5] = -up.getY();
		result[6] = -up.getZ();
		result[7] = 0.0;
		
		result[8] = direction.getX();
		result[9] = direction.getY();
		result[10] = direction.getZ();
		result[11] = 0.0;
		
		result[12] = -right.dotProduct(position);
		result[13] = -up.dotProduct(position);
		result[14] = direction.dotProduct(position);
		result[15] = 1.0;
		
		return ret;
	}

	public static Point3D multiplyByPointAsVector(Matrix4 matrix, Point3D cartesian) {
		double vX = cartesian.getX();
		double vY = cartesian.getY();
		double vZ = cartesian.getZ();

		double x = matrix.entries[0] * vX + matrix.entries[4] * vY + matrix.entries[8] * vZ;
		double y = matrix.entries[1] * vX + matrix.entries[5] * vY + matrix.entries[9] * vZ;
		double z = matrix.entries[2] * vX + matrix.entries[6] * vY + matrix.entries[10] * vZ;

		return new Point3D(x, y, z);
	}
}
