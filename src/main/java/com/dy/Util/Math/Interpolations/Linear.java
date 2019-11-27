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
	public double interpolation(double[] input) {
		return 0;
	}

	@Override
	public float interpolation(float[] input) {
		return 0;
	}

}
