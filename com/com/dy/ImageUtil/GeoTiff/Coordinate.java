package com.dy.ImageUtil.GeoTiff;

public class Coordinate {

	static final double pi = Math.PI;
	static final double two_pi = pi * 2;
	static final double halfPI = pi / 2.0;

	static final double WGS84_X = 6378137.0;
	static final double WGS84_Z = 6356752.3142451793;

	static final double epsilon10 = 0.0000000001;
	static final double epsilon12 = 0.000000000001;
	static final double epsilon14 = 0.00000000000001;

	/**
	 * 角度转弧度
	 * 
	 * @param angle
	 */
	static double angleToRadian(double angle) {
		return Math.toRadians(angle);
	}

	/**
	 * 弧度转角度
	 * 
	 * @param radian
	 */
	static double radianToAngle(double radian) {
		return Math.toDegrees(radian);
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

	static private double computeC(double f, double cosineSquaredAlpha) {
		return f * cosineSquaredAlpha * (4.0 + f * (4.0 - 3.0 * cosineSquaredAlpha)) / 16.0;
	}

	static private double computeDeltaLambda(double f, double sineAlpha, double cosineSquaredAlpha, double sigma,
			double sineSigma, double cosineSigma, double cosineTwiceSigmaMidpoint) {
		double C = Coordinate.computeC(f, cosineSquaredAlpha);
		return (1.0 - C) * f * sineAlpha * (sigma + C * sineSigma * (cosineTwiceSigmaMidpoint
				+ C * cosineSigma * (2.0 * cosineTwiceSigmaMidpoint * cosineTwiceSigmaMidpoint - 1.0)));
	}

	/**
	 * 大圆距离计算
	 * 
	 * @param firstLongitude
	 * @param firstLatitude
	 * @param secondLongitude
	 * @param secondLatitude
	 * @param major
	 * @param minor
	 * @return
	 */
	static double[] computeDistance(double firstLongitude, double firstLatitude, double secondLongitude,
			double secondLatitude, double major, double minor) {
		double eff = (major - minor) / major;
		double l = secondLongitude - firstLongitude;

		double u1 = Math.atan((1 - eff) * Math.tan(firstLatitude));
		double u2 = Math.atan((1 - eff) * Math.tan(secondLatitude));

		double cosineU1 = Math.cos(u1);
		double sineU1 = Math.sin(u1);
		double cosineU2 = Math.cos(u2);
		double sineU2 = Math.sin(u2);

		double cc = cosineU1 * cosineU2;
		double cs = cosineU1 * sineU2;
		double ss = sineU1 * sineU2;
		double sc = sineU1 * cosineU2;

		double lambda = l;
		double lambdaDot = pi * 2;

		double cosineLambda = Math.cos(lambda);
		double sineLambda = Math.sin(lambda);

		double sigma;
		double cosineSigma;
		double sineSigma;
		double cosineSquaredAlpha;
		double cosineTwiceSigmaMidpoint;

		do {
			cosineLambda = Math.cos(lambda);
			sineLambda = Math.sin(lambda);

			double temp = cs - sc * cosineLambda;
			sineSigma = Math.sqrt(cosineU2 * cosineU2 * sineLambda * sineLambda + temp * temp);
			cosineSigma = ss + cc * cosineLambda;

			sigma = Math.atan2(sineSigma, cosineSigma);

			double sineAlpha;

			if (sineSigma == 0.0) {
				sineAlpha = 0.0;
				cosineSquaredAlpha = 1.0;
			} else {
				sineAlpha = cc * sineLambda / sineSigma;
				cosineSquaredAlpha = 1.0 - sineAlpha * sineAlpha;
			}

			lambdaDot = lambda;

			cosineTwiceSigmaMidpoint = cosineSigma - 2.0 * ss / cosineSquaredAlpha;

			if (Double.isNaN(cosineTwiceSigmaMidpoint)) {
				cosineTwiceSigmaMidpoint = 0.0;
			}

			lambda = l + Coordinate.computeDeltaLambda(eff, sineAlpha, cosineSquaredAlpha, sigma, sineSigma,
					cosineSigma, cosineTwiceSigmaMidpoint);
		} while (Math.abs(lambda - lambdaDot) > epsilon12);

		double uSquared = cosineSquaredAlpha * (major * major - minor * minor) / (minor * minor);
		double A = 1.0 + uSquared * (4096.0 + uSquared * (uSquared * (320.0 - 175.0 * uSquared) - 768.0)) / 16384.0;
		double B = uSquared * (256.0 + uSquared * (uSquared * (74.0 - 47.0 * uSquared) - 128.0)) / 1024.0;

		double cosineSquaredTwiceSigmaMidpoint = cosineTwiceSigmaMidpoint * cosineTwiceSigmaMidpoint;
		double deltaSigma = B * sineSigma * (cosineTwiceSigmaMidpoint + B
				* (cosineSigma * (2.0 * cosineSquaredTwiceSigmaMidpoint - 1.0) - B * cosineTwiceSigmaMidpoint
						* (4.0 * sineSigma * sineSigma - 3.0) * (4.0 * cosineSquaredTwiceSigmaMidpoint - 3.0) / 6.0)
				/ 4.0);

		double startHeading = Math.atan2(cosineU2 * sineLambda, cs - sc * cosineLambda);
		double endHeading = Math.atan2(cosineU1 * sineLambda, cs * cosineLambda - sc);

		// {distance,startHeading,endHeading,uSquared}
		return new double[] { minor * A * (sigma - deltaSigma), startHeading, endHeading, uSquared };
	}

	static double[] computeDistance(double firstLongitude, double firstLatitude, double secondLongitude,
			double secondLatitude) {
		return Coordinate.computeDistance(firstLongitude, firstLatitude, secondLongitude, secondLatitude, WGS84_X,
				WGS84_Z);
	}
}
