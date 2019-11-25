package com.dy.Util;

import com.dy.Util.MathUtil;

public class MathUtil {
	
	public static final double pi = Math.PI;
	public static final double two_pi = pi * 2;
	public static final double halfPI = pi / 2.0;

	public static final double WGS84_X = 6378137.0;
	public static final double WGS84_Z = 6356752.3142451793;

	public static final double EPSILON1 = 0.1;
	public static final double EPSILON2 = 0.01;
	public static final double EPSILON3 = 0.001;
	public static final double EPSILON4 = 0.0001;
	public static final double EPSILON5 = 0.00001;
	public static final double EPSILON6 = 0.000001;
	public static final double EPSILON7 = 0.0000001;
	public static final double EPSILON8 = 0.00000001;
	public static final double EPSILON9 = 0.000000001;
	public static final double EPSILON10 = 0.0000000001;
	public static final double EPSILON11 = 0.00000000001;
	public static final double EPSILON12 = 0.000000000001;
	public static final double EPSILON13 = 0.0000000000001;
	public static final double EPSILON14 = 0.00000000000001;
	public static final double EPSILON15 = 0.000000000000001;
	public static final double EPSILON16 = 0.0000000000000001;
	public static final double EPSILON17 = 0.00000000000000001;
	public static final double EPSILON18 = 0.000000000000000001;
	public static final double EPSILON19 = 0.0000000000000000001;
	public static final double EPSILON20 = 0.00000000000000000001;
	public static final double EPSILON21 = 0.000000000000000000001;

	public static final double PI_OVER_TWO = Math.PI * 0.5;
	public static final double TWO_PI = Math.PI * 2.0;

	public static double zeroToTwoPi(double angle) {
		double mod = MathUtil.mod(angle, TWO_PI);
		if (Math.abs(mod) < EPSILON14 && Math.abs(angle) > EPSILON14) {
			return TWO_PI;
		}
		return mod;
	}

	public static double mod(double m, double n) {
		return ((m % n) + n) % n;
	}

	public static double acosClamped(double value) {
		return Math.acos(MathUtil.clamp(value, -1.0, 1.0));
	}

	public static double clamp(double value, double min, double max) {
		return value < min ? min : value > max ? max : value;
	}

	public static boolean equalsEpsilon(double left, double right, double epsilon) {
		return MathUtil.equalsEpsilon(left, right, epsilon, epsilon);
	}

	public static boolean equalsEpsilon(double left, double right, double relativeEpsilon, double absoluteEpsilon) {
		double absDiff = Math.abs(left - right);
		return absDiff <= absoluteEpsilon || absDiff <= relativeEpsilon * Math.max(Math.abs(left), Math.abs(right));
	}

	public static int sign(double number) {
		if (number > 0) {
			return 1;
		} else if (number == 0) {
			return 0;
		} else {
			return -1;
		}
	}
}
