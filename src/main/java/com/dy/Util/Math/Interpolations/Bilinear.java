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
		return 0;
	}

	@Override
	public float interpolation(float[] input) {
		return 0;
	}

}
