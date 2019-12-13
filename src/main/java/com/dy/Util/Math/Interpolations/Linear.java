package com.dy.Util.Math.Interpolations;

import com.dy.Util.Math.Interpolation;

public class Linear extends Interpolation {

	private static Linear interpolation = null;

	public static Interpolation getInstance() {
		if (interpolation == null) {
			synchronized (Linear.class) {
				if (interpolation == null) {
					interpolation = new Linear();
				}
			}
		}
		return interpolation;
	}

	private Object readResolve() {
		return interpolation;
	}

	public static String getInterpolationMethod() {
		return "Linear";
	}

	@Override
	public double interpolation(final double[] input) {
		if (input.length < 3)
			return 0.0;
		return interpolation(input[0], input[1], input[2]);
	}

	public double interpolation(final double start, final double end, final double time) {
		return ((1.0 - time) * start) + (time * end);
	}

	@Override
	public float interpolation(float[] input) {
		if (input.length < 3)
			return 0.0f;
		return interpolation(input[0], input[1], input[2]);
	}

	public float interpolation(final float start, final float end, final float time) {
		return ((1.0f - time) * start) + (time * end);
	}

}
