package com.dy.Util.Math.Interpolations;

import com.dy.Util.Math.Interpolation;

public class SimpleKriging extends Interpolation {

	private static SimpleKriging interpolation = null;

	public static Interpolation getInstance() {
		if (interpolation == null) {
			synchronized (SimpleKriging.class) {
				if (interpolation == null) {
					interpolation = new SimpleKriging();
				}
			}
		}
		return interpolation;
	}

	private Object readResolve() {
		return interpolation;
	}

	public static String getInterpolationMethod() {
		return "SimpleKriging";
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
