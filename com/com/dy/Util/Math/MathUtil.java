package com.dy.Util.Math;

public class MathUtil {

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

	public static boolean equalsEpsilon(double a, double b, double epsilon) {
		double differ = Math.abs(a - b);
		return differ <= epsilon;
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
