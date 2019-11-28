package com.dy.Util.Math;

import com.dy.Util.Math.Interpolations.Bicubic;
import com.dy.Util.Math.Interpolations.Bilinear;
import com.dy.Util.Math.Interpolations.SimpleKriging;
import com.dy.Util.Math.Interpolations.Linear;
import com.dy.Util.Math.Interpolations.NearestNeighbour;

public abstract class Interpolation {

	public static Interpolation getInterpolationInstance(String interpolationMethodType) {
		Interpolation ret = null;
		if (interpolationMethodType.equals(NearestNeighbour.getInterpolationMethod())) {
			ret = NearestNeighbour.getInstance();
		} else if (interpolationMethodType.equals(Bilinear.getInterpolationMethod())) {
			ret = Bilinear.getInstance();
		} else if (interpolationMethodType.equals(Linear.getInterpolationMethod())) {
			ret = Linear.getInstance();
		} else if (interpolationMethodType.equals(SimpleKriging.getInterpolationMethod())) {
			ret = SimpleKriging.getInstance();
		} else if (interpolationMethodType.equals(Bicubic.getInterpolationMethod())) {
			ret = Bicubic.getInstance();
		}
		return ret;
	}

	public abstract double interpolation(double[] input);

	public abstract float interpolation(float[] input);
}
