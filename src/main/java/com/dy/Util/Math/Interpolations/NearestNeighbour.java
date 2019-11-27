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
		return 0;
	}

	@Override
	public float interpolation(float[] input) {
		return 0;
	}

}
