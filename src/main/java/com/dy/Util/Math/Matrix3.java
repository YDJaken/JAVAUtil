package com.dy.Util.Math;

import com.dy.Util.MathUtil;

public class Matrix3 {
	private double[] entries = new double[9];

	public double[] getEntries() {
		return entries;
	}

	public Matrix3() {

	}

	public Matrix3(double[] entries) {
		if (entries.length == 9) {
			this.entries = entries;
		}
	}

	public Matrix3(double column0Row0, double column1Row0, double column2Row0, double column0Row1, double column1Row1,
			double column2Row1, double column0Row2, double column1Row2, double column2Row2) {
		this.entries[0] = column0Row0;
		this.entries[1] = column0Row1;
		this.entries[2] = column0Row2;
		this.entries[3] = column1Row0;
		this.entries[4] = column1Row1;
		this.entries[5] = column1Row2;
		this.entries[6] = column2Row0;
		this.entries[7] = column2Row1;
		this.entries[8] = column2Row2;
	}

	public Matrix3 clone() {
		Matrix3 ret = new Matrix3();
		for (int i = 0; i < this.entries.length; i++) {
			ret.entries[i] = this.entries[i];
		}
		return ret;
	}

	public double getEntry(int col, int row) {
		return entries[col * 3 + row];
	}

	public void setEntry(int col, int row, double number) {
		entries[col * 3 + row] = number;
	}

	public static double determinant(Matrix3 matrix) {
		double m11 = matrix.entries[0];
		double m21 = matrix.entries[1];
		double m31 = matrix.entries[2];
		double m12 = matrix.entries[3];
		double m22 = matrix.entries[4];
		double m32 = matrix.entries[5];
		double m13 = matrix.entries[6];
		double m23 = matrix.entries[7];
		double m33 = matrix.entries[8];

		return m11 * (m22 * m33 - m23 * m32) + m12 * (m23 * m31 - m21 * m33) + m13 * (m21 * m32 - m22 * m31);
	}

	public static Matrix3 inverse(Matrix3 matrix) {
		double m11 = matrix.entries[0];
		double m21 = matrix.entries[1];
		double m31 = matrix.entries[2];
		double m12 = matrix.entries[3];
		double m22 = matrix.entries[4];
		double m32 = matrix.entries[5];
		double m13 = matrix.entries[6];
		double m23 = matrix.entries[7];
		double m33 = matrix.entries[8];

		double determinant = Matrix3.determinant(matrix);

		if (Math.abs(determinant) <= MathUtil.EPSILON15) {
			throw new Error("matrix is not invertible");
		}

		Matrix3 ret = new Matrix3();
		double[] result = ret.getEntries();

		result[0] = m22 * m33 - m23 * m32;
		result[1] = m23 * m31 - m21 * m33;
		result[2] = m21 * m32 - m22 * m31;
		result[3] = m13 * m32 - m12 * m33;
		result[4] = m11 * m33 - m13 * m31;
		result[5] = m12 * m31 - m11 * m32;
		result[6] = m12 * m23 - m13 * m22;
		result[7] = m13 * m21 - m11 * m23;
		result[8] = m11 * m22 - m12 * m21;

		double scale = 1.0 / determinant;
		return Matrix3.multiplyByScalar(ret, scale);
	}

	private static Matrix3 multiplyByScalar(Matrix3 matrix, double scalar) {
		Matrix3 ret = matrix.clone();
		double[] result = ret.getEntries();

		result[0] = result[0] * scalar;
		result[1] = result[1] * scalar;
		result[2] = result[2] * scalar;
		result[3] = result[3] * scalar;
		result[4] = result[4] * scalar;
		result[5] = result[5] * scalar;
		result[6] = result[6] * scalar;
		result[7] = result[7] * scalar;
		result[8] = result[8] * scalar;
		
		return ret;
	}
	
	public static Point3D multiplyByPointAsVector(Matrix3 matrix, Point3D cartesian) {
		double vX = cartesian.getX();
		double vY = cartesian.getY();
		double vZ = cartesian.getZ();

		double x = matrix.entries[0] * vX + matrix.entries[3] * vY + matrix.entries[6] * vZ;
		double y = matrix.entries[1] * vX + matrix.entries[4] * vY + matrix.entries[7] * vZ;
		double z = matrix.entries[2] * vX + matrix.entries[5] * vY + matrix.entries[8] * vZ;

		return new Point3D(x, y, z);
	}

	public static Matrix3 fromQuaternion(Quaternion quaternion) {
		double x2 = quaternion.getX() * quaternion.getX();
		double xy = quaternion.getX() * quaternion.getY();
		double xz = quaternion.getX() * quaternion.getZ();
		double xw = quaternion.getX() * quaternion.getW();
		double y2 = quaternion.getY() * quaternion.getY();
		double yz = quaternion.getY() * quaternion.getZ();
		double yw = quaternion.getY() * quaternion.getW();
		double z2 = quaternion.getZ() * quaternion.getZ();
		double zw = quaternion.getZ() * quaternion.getW();
		double w2 = quaternion.getW() * quaternion.getW();

		double m00 = x2 - y2 - z2 + w2;
		double m01 = 2.0 * (xy - zw);
		double m02 = 2.0 * (xz + yw);

		double m10 = 2.0 * (xy + zw);
		double m11 = -x2 + y2 - z2 + w2;
		double m12 = 2.0 * (yz - xw);

		double m20 = 2.0 * (xz - yw);
		double m21 = 2.0 * (yz + xw);
		double m22 = -x2 - y2 + z2 + w2;

		return new Matrix3(m00, m01, m02, m10, m11, m12, m20, m21, m22);
	}

	public static Point3D getColumn(Matrix3 matrix, int index) {
		int startIndex = index * 3;
		return new Point3D(matrix.entries[startIndex++], matrix.entries[startIndex++], matrix.entries[startIndex]);
	}
}
