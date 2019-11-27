package com.dy.Util.Math.Interpolations;

import com.dy.Util.Math.Interpolation;

public class Kriging extends Interpolation {

	private static Kriging interpolation = null;

	public static Interpolation getInstance() {
		if (interpolation == null) {
			synchronized (Kriging.class) {
				if (interpolation == null) {
					interpolation = new Kriging();
				}
			}
		}
		return interpolation;
	}

	private Object readResolve() {
		return interpolation;
	}

	public static String getInterpolationMethod() {
		return "Kriging";
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
