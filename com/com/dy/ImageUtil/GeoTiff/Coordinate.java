package com.dy.ImageUtil.GeoTiff;

public class Coordinate {

	static final double pi = Math.PI;
	static final double two_pi = pi * 2;
	static final double halfPI = pi / 2.0;
	static final double epsilon10 = 0.0000000001;
	static final double epsilon14 = 0.00000000000001;

	/**
	 * 角度转弧度
	 * 
	 * @param angle
	 */
	static double angleToRadian(double angle) {
		return angle * pi / 180;
	}

	/**
	 * 弧度转角度
	 * 
	 * @param radian
	 */
	static double radianToAngle(double radian) {
		return radian * 180 / pi;
	}

	/**
	 * 在[-PI,PI]内找出一个和输入x相同的角
	 * 
	 * @param x
	 * @return {number}
	 */
	static double negativePIToPI(double x) {
		while (x < -(pi + epsilon10)) {
			x += two_pi;
		}
		if (x < -pi) {
			return -pi;
		}
		while (x > pi + epsilon10) {
			x -= two_pi;
		}
		return x > pi ? pi : x;
	}

	/**
	 * 转换经度使其满足[-PI,PI]
	 * 
	 * @param angle
	 * @return {number}
	 */
	static double convertLongitudeRange(double angle) {
		double simplified = angle - Math.floor(angle / two_pi) * two_pi;
		if (simplified < -pi) {
			return simplified + two_pi;
		}
		if (simplified >= pi) {
			return simplified - two_pi;
		}
		return simplified;
	}
}
