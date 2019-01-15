package com.dy.ImageUtil.GeoTiff;

/**
 * 二维坐标点
 * 
 * @author dy
 *
 */
public class Point {
	double x, longitude;
	double y, latitude;

	public Point(Point point) {
		this(point.x, point.y);
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
            return Coordinate.inRange(this.longitude - epsilon, this.longitude + epsilon, target.longitude) &&
                Coordinate.inRange(this.latitude - epsilon, this.latitude + epsilon, target.latitude);
        } else {
            return (this.longitude > 0 ? Coordinate.inRange(this.longitude * (1 - epsilon / 10000), this.longitude * (1 + epsilon / 10000), target.x) : Coordinate.inRange(this.longitude * (1 + epsilon / 10000), this.longitude * (1 - epsilon / 10000), target.longitude)) &&
                (this.latitude > 0 ? Coordinate.inRange(this.latitude * (1 - epsilon / 10000), this.latitude * (1 + epsilon / 10000), target.latitude) : Coordinate.inRange(this.latitude * (1 + epsilon / 10000), this.latitude * (1 - epsilon / 10000), target.latitude));
        }
	}
}
