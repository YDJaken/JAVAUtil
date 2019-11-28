package com.dy.Util.Math.Interpolations;

import com.dy.Util.Math.Interpolation;

public class Bilinear extends Interpolation {
	private static Bilinear interpolation = null;

	public static Interpolation getInstance() {
		if (interpolation == null) {
			synchronized (Bilinear.class) {
				if (interpolation == null) {
					interpolation = new Bilinear();
				}
			}
		}
		return interpolation;
	}

	private Object readResolve() {
		return interpolation;
	}

	public static String getInterpolationMethod() {
		return "Bilinear";
	}

	@Override
	public double interpolation(double[] input) {
		if (input.length != 6)
			return 0.0;
		return interpolation(input[0], input[1], input[2], input[3], input[4], input[5]);
	}

	public double interpolation(final double g00, final double g10, final double g01, final double g11, final double x,
			final double y) {
		double rx = (1.0 - x);
		double ry = (1.0 - y);
		double a = rx * ry, b = x * ry, c = rx * y, d = x * y;
		return g00 * a + g10 * b + g01 * c + g11 * d;
	}

	@Override
	public float interpolation(float[] input) {
		if (input.length != 6)
			return 0.0f;
		return interpolation(input[0], input[1], input[2], input[3], input[4], input[5]);
	}

	public float interpolation(final float g00, final float g10, final float g01, final float g11, final float x,
			final float y) {
		float rx = (1.0f - x);
		float ry = (1.0f - y);
		float a = rx * ry, b = x * ry, c = rx * y, d = x * y;
		return g00 * a + g10 * b + g01 * c + g11 * d;
	}

}
