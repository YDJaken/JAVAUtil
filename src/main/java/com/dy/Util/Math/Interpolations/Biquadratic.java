package com.dy.Util.Math.Interpolations;

import com.dy.Util.Math.Interpolation;

public class Biquadratic extends Interpolation {

	private static Biquadratic interpolation = null;

	public static Interpolation getInstance() {
		if (interpolation == null) {
			synchronized (Biquadratic.class) {
				if (interpolation == null) {
					interpolation = new Biquadratic();
				}
			}
		}
		return interpolation;
	}

	private Object readResolve() {
		return interpolation;
	}

	public static String getInterpolationMethod() {
		return "Biquadratic";
	}

	@Override
	public double interpolation(double[] input) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float interpolation(float[] input) {
		// TODO Auto-generated method stub
		return 0;
	}

}
