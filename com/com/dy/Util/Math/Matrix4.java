package com.dy.Util.Math;

public class Matrix4 {
	private double[] entries = new double[16];

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
}
