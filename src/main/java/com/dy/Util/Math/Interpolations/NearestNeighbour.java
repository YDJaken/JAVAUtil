package com.dy.Util.Math.Interpolations;

import com.dy.Util.Math.Interpolation;

public class NearestNeighbour extends Interpolation {
	private static NearestNeighbour interpolation = null;

	public static Interpolation getInstance() {
		if (interpolation == null) {
			synchronized (NearestNeighbour.class) {
				if (interpolation == null) {
					interpolation = new NearestNeighbour();
				}
			}
		}
		return interpolation;
	}

	private Object readResolve() {
		return interpolation;
	}

	public static String getInterpolationMethod() {
		return "NearestNeighbour";
	}

	@Override
	public double interpolation(double[] input) {
		if (input.length < 6)
			return 0.0;
		return interpolation(input[0], input[1], input[2], input[3], input[4], input[5]);
	}

	public double interpolation(final double g00, final double g10, final double g01, final double g11, final double x,
			final double y) {
		if (x > 0.5) {
			if (y > 0.5) {
				return g11;
			} else {
				return g10;
			}
		} else {
			if (y > 0.5) {
				return g01;
			} else {
				return g00;
			}
		}
	}

	@Override
	public float interpolation(float[] input) {
		if (input.length < 6)
			return 0.0f;
		return interpolation(input[0], input[1], input[2], input[3], input[4], input[5]);
	}

	public float interpolation(final float g00, final float g10, final float g01, final float g11, final float x,
			final float y) {
		if (x > 0.5) {
			if (y > 0.5) {
				return g11;
			} else {
				return g10;
			}
		} else {
			if (y > 0.5) {
				return g01;
			} else {
				return g00;
			}
		}
	}

}
