package com.dy.Util.Math;

public class RotationMatrix {

	public static double[] generate3DMatrixDegree(final Point3D orientation) {
		return RotationMatrix.generate3DMatrixDegree(orientation.getX(), orientation.getY(), orientation.getZ());
	}

	public static double[] generate3DMatrix(final Point3D orientation) {
		return RotationMatrix.generate3DMatrix(orientation.getX(), orientation.getY(), orientation.getZ());
	}

	public static double[] generate3DMatrixDegree(final double[] orientation) {
		return RotationMatrix.generate3DMatrixDegree(orientation[0], orientation[1], orientation[2]);
	}

	public static double[] generate3DMatrix(final double[] orientation) {
		return RotationMatrix.generate3DMatrix(orientation[0], orientation[1], orientation[2]);
	}

	public static double[] generate3DMatrixDegree(final double pitch, final double yaw, final double roll) {
		return RotationMatrix.generate3DMatrix(Math.toRadians(pitch), Math.toRadians(yaw),Math.toRadians(roll));
	}

	// X pitch Y yaw Z roll
	public static double[] generate3DMatrix(final double pitch, final double yaw,  final double roll) {
		double[] ret = new double[9];
		ret[0] = Math.cos(roll) * Math.cos(yaw);
		ret[1] = Math.cos(roll) * Math.cos(pitch);
		ret[2] = Math.cos(yaw) * Math.cos(pitch);
		ret[3] = Math.sin(roll) * Math.sin(yaw);
		ret[4] = Math.sin(roll) * Math.sin(pitch);
		ret[5] = Math.sin(yaw) * Math.sin(pitch);
		return ret;
	}

	public static Point3D computeOrientationFromMatrixDegree(final double[] matrix) throws Exception {
		Point3D ret = RotationMatrix.computeOrientationFromMatrix(matrix);
		ret.setX(Math.toDegrees(ret.getX()));
		ret.setY(Math.toDegrees(ret.getY()));
		ret.setZ(Math.toDegrees(ret.getZ()));
		return ret;
	}

	public static Point3D computeOrientationFromMatrix(final double[] matrix) throws Exception {
		if (matrix.length != 9) {
			throw new Exception("invalid Matrix");
		}
		double[] ret = new double[3];
		// TODO 添加从姿态矩阵反算姿态角方法
		return new Point3D(ret[0], ret[1], ret[2]);
	}

	public static void main(String[] args) {
		double[] a = generate3DMatrix(Math.toRadians(-35.29d),Math.toRadians(159.9d),Math.toRadians(0d));
		System.out.println(a[0]);
		System.out.println(a[1]);
		System.out.println(a[2]);
		System.out.println(a[3]);
		System.out.println(a[4]);
		System.out.println(a[5]);
	}
}
