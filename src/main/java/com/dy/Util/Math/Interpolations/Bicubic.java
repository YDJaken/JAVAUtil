package com.dy.Util.Math.Interpolations;

import com.dy.Util.Math.Interpolation;

public class Bicubic extends Interpolation {

	private static Bicubic interpolation = null;

	public static Interpolation getInstance() {
		if (interpolation == null) {
			synchronized (Bicubic.class) {
				if (interpolation == null) {
					interpolation = new Bicubic();
				}
			}
		}
		return interpolation;
	}

	private Object readResolve() {
		return interpolation;
	}

	public static String getInterpolationMethod() {
		return "Bicubic";
	}

	@Override
	public double interpolation(double[] input) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double interpolation(final double g00, final double g01, final double g02, final double g03,
			final double g10, final double g11, final double g12, final double g13, final double g20, final double g21,
			final double g22, final double g23, final double g30, final double g31, final double g32, final double g33,
			final double x, final double y) {
		double u0, u1, u2, u3;
		double v0, v1, v2, v3;

		u0 = 1.0 + x;
		u1 = x;
		u2 = 1.0 - x;
		u3 = 2.0 - x;

		v0 = 1.0 + y;
		v1 = y;
		v2 = 1.0 - y;
		v3 = 2.0 - y;

		u0 = 4.0 - 8.0 * u0 + 5.0 * u0 * u0 - u0 * u0 * u0;
		u1 = 1.0 - 2.0 * u1 * u1 + u1 * u1 * u1;
		u2 = 1.0 - 2.0 * u2 * u2 + u2 * u2 * u2;
		u3 = 4.0 - 8.0 * u3 + 5.0 * u3 * u3 - u3 * u3 * u3;

		v0 = 4.0 - 8.0 * v0 + 5.0 * v0 * v0 - v0 * v0 * v0;
		v1 = 1.0 - 2.0 * v1 * v1 + v1 * v1 * v1;
		v2 = 1.0 - 2.0 * v2 * v2 + v2 * v2 * v2;
		v3 = 4.0 - 8.0 * v3 + 5 * v3 * v3 - v3 * v3 * v3;

		double g0, g1, g2, g3;
		g0 = v0 * g00 + v1 * g10 + v2 * g20 + v3 * g30;
		g1 = v0 * g01 + v1 * g11 + v2 * g21 + v3 * g31;
		g2 = v0 * g02 + v1 * g12 + v2 * g22 + v3 * g32;
		g3 = v0 * g03 + v1 * g13 + v2 * g23 + v3 * g33;

		return g0 * u0 + g1 * u1 + g2 * u2 + g3 * u3;
	}

	@Override
	public float interpolation(float[] input) {
		// TODO Auto-generated method stub
		return 0;
	}

	public float interpolation(final float g00, final float g01, final float g02, final float g03, final float g10,
			final float g11, final float g12, final float g13, final float g20, final float g21, final float g22,
			final float g23, final float g30, final float g31, final float g32, final float g33, final float x,
			final float y) {
		float u0, u1, u2, u3;
		float v0, v1, v2, v3;

		u0 = 1.0f + x;
		u1 = x;
		u2 = 1.0f - x;
		u3 = 2.0f - x;

		v0 = 1.0f + y;
		v1 = y;
		v2 = 1.0f - y;
		v3 = 2.0f - y;

		u0 = 4.0f - 8.0f * u0 + 5.0f * u0 * u0 - u0 * u0 * u0;
		u1 = 1.0f - 2.0f * u1 * u1 + u1 * u1 * u1;
		u2 = 1.0f - 2.0f * u2 * u2 + u2 * u2 * u2;
		u3 = 4.0f - 8.0f * u3 + 5.0f * u3 * u3 - u3 * u3 * u3;

		v0 = 4.0f - 8.0f * v0 + 5.0f * v0 * v0 - v0 * v0 * v0;
		v1 = 1.0f - 2.0f * v1 * v1 + v1 * v1 * v1;
		v2 = 1.0f - 2.0f * v2 * v2 + v2 * v2 * v2;
		v3 = 4.0f - 8.0f * v3 + 5 * v3 * v3 - v3 * v3 * v3;

		float g0, g1, g2, g3;
		g0 = v0 * g00 + v1 * g10 + v2 * g20 + v3 * g30;
		g1 = v0 * g01 + v1 * g11 + v2 * g21 + v3 * g31;
		g2 = v0 * g02 + v1 * g12 + v2 * g22 + v3 * g32;
		g3 = v0 * g03 + v1 * g13 + v2 * g23 + v3 * g33;

		return g0 * u0 + g1 * u1 + g2 * u2 + g3 * u3;
	}

}
