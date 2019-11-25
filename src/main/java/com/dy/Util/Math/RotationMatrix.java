package com.dy.Util.Math;

import com.dy.Util.MathUtil;

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
		return RotationMatrix.generate3DMatrix(Math.toRadians(pitch), Math.toRadians(yaw), Math.toRadians(roll));
	}

	public static double[] generate3DMatrix(final double pitch, final double yaw, final double roll) {
		return RotationMatrix.generate3DMatrix(new Point3D(), pitch, yaw, roll);
	}

	public static double[] generate3DMatrixDegree(final double longitude, final double latitude, final double height,
			final double pitch, final double yaw, final double roll) {
		return RotationMatrix.generate3DMatrix(
				Ellipsoid.WGS84.cartographicToCartesian(Math.toRadians(longitude), Math.toRadians(latitude), height),
				Math.toRadians(pitch), Math.toRadians(yaw), Math.toRadians(roll));
	}

	public static double[] generate3DMatrix(final double longitude, final double latitude, final double height,
			final double pitch, final double yaw, final double roll) {
		return RotationMatrix.generate3DMatrix(Ellipsoid.WGS84.cartographicToCartesian(longitude, latitude, height),
				pitch, yaw, roll);
	}

	public static double[] generate3DMatrix(final Point3D position, final double pitch, final double yaw,
			final double roll) {
		Matrix4 transform = Transforms.localFrameToFixedFrameGenerator("east", "north", position);
		double heading = yaw - MathUtil.PI_OVER_TWO;
		Quaternion rotQuat = Quaternion.fromHeadingPitchRoll(heading, pitch, roll);
		Matrix3 rotMat = Matrix3.fromQuaternion(rotQuat);
		Point3D direction = Matrix3.getColumn(rotMat, 0);
		Point3D up = Matrix3.getColumn(rotMat, 2);
		Point3D right = direction.crossProduct(up);
		direction = Matrix4.multiplyByPointAsVector(transform, direction);
		up = Matrix4.multiplyByPointAsVector(transform, up);
		right = Matrix4.multiplyByPointAsVector(transform, right);
		Matrix3 tmp = Matrix4.getRotation(Matrix4.computeView(position, direction, up, right));
		return tmp.getEntries();
	}

	public static Point3D computeOrientationFromMatrixDegree(final double[] matrix, final Point3D position)
			throws Exception {
		Point3D ret = RotationMatrix.computeOrientationFromMatrix(matrix, Ellipsoid.WGS84.cartographicToCartesian(
				Math.toRadians(position.getX()), Math.toRadians(position.getY()), position.getZ()));
		ret.setX(Math.toDegrees(ret.getX()));
		ret.setY(Math.toDegrees(ret.getY()));
		ret.setZ(Math.toDegrees(ret.getZ()));
		return ret;
	}

	public static Point3D computeOrientationFromMatrix(final double[] matrix, final Point3D position) throws Exception {
		if (matrix.length != 9) {
			throw new Exception("invalid Matrix");
		}
		Matrix4 transform = Transforms.localFrameToFixedFrameGenerator("east", "north", position);
		Point3D up = new Point3D(-matrix[3], -matrix[4], -matrix[5]);
		Point3D direction = new Point3D(matrix[6], matrix[7], matrix[8]);
		Matrix4 inverceMatrix = Matrix4.inverseTransformation(transform);
		up = Matrix4.multiplyByPointAsVector(inverceMatrix, up);
		direction = Matrix4.multiplyByPointAsVector(inverceMatrix, direction);
		Point3D right = direction.crossProduct(up);
		return new Point3D(RotationMatrix.getYaw(direction, up), RotationMatrix.getPitch(direction),
				RotationMatrix.getRoll(direction, up, right));
	}

	public static double getHeading(Point3D direction, Point3D up) {
		double heading;
		if (!MathUtil.equalsEpsilon(Math.abs(direction.getZ()), 1.0, MathUtil.EPSILON3)) {
			heading = Math.atan2(direction.getY(), direction.getX()) - MathUtil.PI_OVER_TWO;
		} else {
			heading = Math.atan2(up.getY(), up.getX()) - MathUtil.PI_OVER_TWO;
		}

		heading = MathUtil.TWO_PI - MathUtil.zeroToTwoPi(heading);
		if (heading > Math.PI) {
			heading -= MathUtil.TWO_PI;
		}
		return heading;
	}

	public static double getYaw(Point3D direction, Point3D up) {
		return RotationMatrix.getHeading(direction, up);
	}

	public static double getPitch(Point3D direction) {
		return MathUtil.PI_OVER_TWO - MathUtil.acosClamped(direction.getZ());
	}

	public static double getRoll(Point3D direction, Point3D up, Point3D right) {
		double roll = 0.0;
		if (!MathUtil.equalsEpsilon(Math.abs(direction.getZ()), 1.0, MathUtil.EPSILON3)) {
			roll = Math.atan2(-right.getZ(), up.getZ());
			roll = MathUtil.zeroToTwoPi(roll + MathUtil.TWO_PI);
		}

		if (roll == MathUtil.TWO_PI) {
			roll = 0.0;
		}

		return roll;
	}

	public static void main(String[] args) {
		double[] a = generate3DMatrix(Math.toRadians(113.558330), Math.toRadians(25.063773), 29.1, -0.8569566627292158,
				-2.7139869868511823, 0.0);
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (int i = 0; i < a.length; i++) {
			builder.append(a[i]);
			builder.append(",");
		}
		builder.append("]");
		System.out.println(builder.toString());
		try {
			Point3D hpr = computeOrientationFromMatrixDegree(a,
					new Point3D(113.558169120253, 25.0632353814444, 27.7765723755583));
			System.out.println(hpr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
