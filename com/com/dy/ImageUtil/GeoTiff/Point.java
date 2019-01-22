package com.dy.ImageUtil.GeoTiff;

/**
 * 二维坐标点
 * 
 * @author dy
 *
 */
public class Point extends Config {
	double x, longitude;
	double y, latitude;

	public Point(Point point) {
		this(point.x, point.y);
	}

	public Point(double[] arr) {
		this(arr[0], arr[1]);
	}

	public Point(double x, double y) {
		this.x = this.longitude = x;
		this.y = this.latitude = y;
	}

	public Point copy() {
		return new Point(this);
	}
	
	public boolean equals(Point target) {
		return equals(target, Coordinate.epsilon10);
	}

	public boolean equals(Point target, double epsilon) {
		if (epsilon < 1) {
			return Coordinate.inRange(this.longitude - epsilon, this.longitude + epsilon, target.longitude)
					&& Coordinate.inRange(this.latitude - epsilon, this.latitude + epsilon, target.latitude);
		} else {
			return (this.longitude > 0
					? Coordinate.inRange(this.longitude * (1 - epsilon / 10000), this.longitude * (1 + epsilon / 10000),
							target.x)
					: Coordinate.inRange(this.longitude * (1 + epsilon / 10000), this.longitude * (1 - epsilon / 10000),
							target.longitude))
					&& (this.latitude > 0
							? Coordinate.inRange(this.latitude * (1 - epsilon / 10000),
									this.latitude * (1 + epsilon / 10000), target.latitude)
							: Coordinate.inRange(this.latitude * (1 + epsilon / 10000),
									this.latitude * (1 - epsilon / 10000), target.latitude));
		}
	}

	public static double computeMagenitude(Point point) {
		return Math.sqrt(point.longitude * point.longitude + point.latitude * point.latitude);
	}

	public static Point[] fromArray(double[] input) {
		Point[] ret = new Point[input.length / 2];
		int position = 0;
		for (int i = 0; i < input.length; i++) {
			ret[position++] = new Point(input[i], input[++i]);
		}
		return ret;
	}
}
