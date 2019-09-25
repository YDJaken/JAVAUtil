package com.dy.Util.Math;

public class Matrix3 {
	private double[] entries = new double[9];

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
}
