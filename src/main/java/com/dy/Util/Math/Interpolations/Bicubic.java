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

	@Override
	public float interpolation(float[] input) {
		// TODO Auto-generated method stub
		return 0;
	}

}
