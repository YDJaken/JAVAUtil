package com.dy.ImageUtil.GeoTiff;

public class Polygon extends Config {
	public Point[] position;

	public Polygon(double[] position) {
		this(Point.fromArray(position));
	}

	public Polygon(Point[] position) {
		this.position = position;
	}

	public double area() {
		double area = 0.0;
		for (int i = 0; i < this.position.length - 1; i++) {
			Point p1 = this.position[i];
			Point p2 = this.position[i + 1];
			area += (p2.longitude - p1.longitude) * (2 + Math.sin(p1.latitude) + Math.sin(p2.latitude));
		}
		return Math.abs(area * Coordinate.WGS84_X * Coordinate.WGS84_Z / 2.0);
	}

	/**
	 * 判定点是否在此多边形内
	 * 
	 * @param {Point} otherPoint
	 * @return {boolean}
	 */
	public boolean contains(Point otherPoint) {
		if (this.isVertix(otherPoint))
			return true;
		boolean flag = false;
		for (int i = 0, l = this.position.length, j = l - 1; i < l; j = i, i++) {
			if ((this.position[i].latitude < otherPoint.latitude && this.position[j].latitude >= otherPoint.latitude)
					|| (this.position[i].latitude >= otherPoint.latitude
							&& this.position[j].latitude < otherPoint.latitude)) {
				double longitude = this.position[i].longitude + (otherPoint.latitude - this.position[i].latitude)
						* (this.position[j].longitude - this.position[i].longitude)
						/ (this.position[j].latitude - this.position[i].latitude);

				if (longitude == otherPoint.longitude) {
					return true;
				}

				if (longitude > otherPoint.longitude) {
					flag = !flag;
				}
			}
		}

		return flag;
	}

	/**
	 * 判断点是否在边上
	 * 
	 * @param otherPoint
	 * @return {boolean}
	 */
	public boolean inLines(Point otherPoint) {
		for (int i = 0, l = this.position.length, j = l - 1; i < l; j = i, i++) {
			if ((this.position[i].latitude < otherPoint.latitude && this.position[j].latitude >= otherPoint.latitude)
					|| (this.position[i].latitude >= otherPoint.latitude
							&& this.position[j].latitude < otherPoint.latitude)) {
				double longitude = this.position[i].longitude + (otherPoint.latitude - this.position[i].latitude)
						* (this.position[j].longitude - this.position[i].longitude)
						/ (this.position[j].latitude - this.position[i].latitude);

				if (longitude == otherPoint.longitude) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判定传入点是否为顶点
	 * 
	 * @param otherPoint
	 * @return {boolean}
	 */
	public boolean isVertix(Point otherPoint) {
		for (int i = 0; i < this.position.length; i++) {
			if (otherPoint.equals(this.position[i]))
				return true;
		}
		return false;
	}

	/**
	 * 判断两个多边形包围型是否相交
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	public static boolean simpleIntersection(Polygon one, Polygon two) {
		Rectangle rec1 = Polygon.loadRectangle(one), rec2 = Polygon.loadRectangle(two);
		return Rectangle.simpleIntersection(rec1, rec2) != null ? true : rec1.equals(rec2);
	}

	/**
	 * 计算此多边形的包围矩形
	 * 
	 * @return {Rectangle}
	 */
	public static Rectangle loadRectangle(Polygon target) {
		return Polygon.loadRectangle(target, true);
	}

	public static Rectangle loadRectangle(Polygon target, boolean isImage) {
		double maxLon = Double.NEGATIVE_INFINITY, maxLat = Double.NEGATIVE_INFINITY, minLon = Double.POSITIVE_INFINITY,
				minLat = Double.POSITIVE_INFINITY;
		for (int i = 0; i < target.position.length; i++) {
			maxLon = Math.max(target.position[i].longitude, maxLon);
			maxLat = Math.max(target.position[i].latitude, maxLat);
			minLon = Math.min(target.position[i].longitude, minLon);
			minLat = Math.min(target.position[i].latitude, minLat);
		}
		return isImage ? new Rectangle((int) minLon, (int) minLat, (int) maxLon, (int) maxLat)
				: new Rectangle(minLon, minLat, maxLon, maxLat);
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("Polygon:{");
		for (int i = 0; i < position.length; i++) {
			ret.append(position[i].toString()).append(",");
		}
		ret.append("}");
		return ret.toString();
	}
}
