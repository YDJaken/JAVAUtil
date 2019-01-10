package com.dy.ImageUtil.GeoTiff;

public class Rectangle {

	double west;
	double south;
	double east;
	double north;

	public Rectangle() {
		this(0, 0, 0, 0);
	}

	public Rectangle(double[] array) {
		this(array, false);
	}

	public Rectangle(double[] array, boolean degree) {
		this(array[0], array[1], array[2], array[3], degree);
	}

	public Rectangle(double west, double south, double east, double north) {
		this(west, south, east, north, false);
	}

	/**
	 * 用经纬度确定的长方形区域或一般长方形
	 * 
	 * @param west   最西边的经度点 取值弧度,范围[-PI,PI] 或者 minX
	 * @param south  最南边的纬度点 取值弧度,范围[-PI/2,PI/2] 或者 minY
	 * @param east   最东边的经度点 取值弧度,范围[-PI,PI] 或者 maxX
	 * @param north  最北边的纬度点 取值弧度,范围[-PI/2,PI/2] 或者 maxY
	 * @param degree 是否使用角度制
	 */
	public Rectangle(double west, double south, double east, double north, boolean degree) {
		if (degree) {
			west = Coordinate.angleToRadian(west);
			south = Coordinate.angleToRadian(south);
			east = Coordinate.angleToRadian(east);
			north = Coordinate.angleToRadian(north);
		}
		this.west = west;
		this.south = south;
		this.east = east;
		this.north = north;
	}

	@Override
	public String toString(){
		StringBuilder b = new StringBuilder();
		b.append("Rectangle:{west:").append(west).append(",south:").append(south).append(",east:").append(east).append(",north:").append(north).append('}');
		return b.toString();
	}
	
	/**
	 * 将一个长方形的数据放入数组
	 * 
	 * @param value
	 * @return
	 */
	static double[] pack(Rectangle value) {
		double[] ret = new double[4];
		ret[0] = value.west;
		ret[1] = value.south;
		ret[2] = value.east;
		ret[3] = value.north;
		return ret;
	}

	/**
	 * 检查两个长方形是否在一个范围内相等
	 * 
	 * @param left
	 * @param right
	 * @param absoluteEpsilon
	 * @return
	 */
	static boolean equalsEpsilon(Rectangle left, Rectangle right, double absoluteEpsilon) {
		return (left == right) || ((Math.abs(left.west - right.west) <= absoluteEpsilon)
				&& (Math.abs(left.south - right.south) <= absoluteEpsilon)
				&& (Math.abs(left.east - right.east) <= absoluteEpsilon)
				&& (Math.abs(left.north - right.north) <= absoluteEpsilon));
	}

	/**
	 * 检查两个长方形是否相等
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	static boolean equals(Rectangle left, Rectangle right) {
		return (left == right) || ((left.west == right.west) && (left.south == right.south) && (left.east == right.east)
				&& (left.north == right.north));
	}

	/**
	 * 返回西南角经纬度 弧度制
	 * 
	 * @param rec
	 * @return
	 */
	double[] southwest() {
		return new double[] { this.west, this.south, 0 };
	}

	/**
	 * 返回西北角经纬度 弧度制
	 * 
	 * @param rec
	 * @return
	 */
	double[] northwest() {
		return new double[] { this.west, this.north, 0 };
	}

	/**
	 * 返回东北角经纬度 弧度制
	 * 
	 * @param rec
	 * @return
	 */
	double[] northeast() {
		return new double[] { this.east, this.north, 0 };
	}

	/**
	 * 返回东南角经纬度 弧度制
	 * 
	 * @param rec
	 * @return
	 */
	double[] southeast() {
		return new double[] { this.east, this.south, 0 };
	}

	/**
	 * 计算此长方形的中心点
	 * 
	 * @param rec
	 * @return
	 */
	double[] center() {
		double east = this.east, west = this.west;
		if (east < west) {
			east += Coordinate.two_pi;
		}
		return new double[] { Coordinate.negativePIToPI((west + east) * 0.5), (south + north) * 0.5, 0 };
	}

	/**
	 * 检查两个长方形是否相交 并返回相交的长方形
	 * 
	 * @param rectangle
	 * @param otherRectangle
	 * @return
	 */
	static Rectangle intersection(Rectangle rectangle, Rectangle otherRectangle) {
		double rectangleEast = rectangle.east;
		double rectangleWest = rectangle.west;
		double otherRectangleEast = otherRectangle.east;
		double otherRectangleWest = otherRectangle.west;
		if (rectangleEast < rectangleWest && otherRectangleEast > 0) {
			rectangleEast += Coordinate.two_pi;
		} else if (otherRectangleEast < otherRectangleWest && rectangleEast > 0) {
			otherRectangleEast += Coordinate.two_pi;
		}
		if (rectangleEast < rectangleWest && otherRectangleWest < 0) {
			otherRectangleWest += Coordinate.two_pi;
		} else if (otherRectangleEast < otherRectangleWest && rectangleWest < 0) {
			rectangleWest += Coordinate.two_pi;
		}
		double west = Coordinate.negativePIToPI(Math.max(rectangleWest, otherRectangleWest));
		double east = Coordinate.negativePIToPI(Math.min(rectangleEast, otherRectangleEast));
		if ((rectangle.west < rectangle.east || otherRectangle.west < otherRectangle.east) && east <= west) {
			return null;
		}
		double south = Math.max(rectangle.south, otherRectangle.south);
		double north = Math.min(rectangle.north, otherRectangle.north);
		if (south >= north) {
			return null;
		}
		return new Rectangle(west, south, east, north);
	}

	/**
	 * 不考虑经纬度可能反应的角的多样性，适用于非经纬度坐标系的长方形
	 * 
	 * @param rectangle
	 * @param otherRectangle
	 * @return
	 */
	static Rectangle simpleIntersection(Rectangle rectangle, Rectangle otherRectangle) {
		double west = Math.max(rectangle.west, otherRectangle.west);
		double south = Math.max(rectangle.south, otherRectangle.south);
		double east = Math.min(rectangle.east, otherRectangle.east);
		double north = Math.min(rectangle.north, otherRectangle.north);
		if (south >= north || west >= east) {
			return null;
		}
		return new Rectangle(west, south, east, north);
	}

	/**
	 * 合并两个一般长方形
	 * 
	 * @param rectangle
	 * @param otherRectangle
	 * @return
	 */
	static Rectangle simpleUnion(Rectangle rectangle, Rectangle otherRectangle) {
		if (!Rectangle.nextTo(rectangle, otherRectangle))
			return null;
		double west = Math.min(rectangle.west, otherRectangle.west),
				south = Math.min(rectangle.south, otherRectangle.south),
				east = Math.max(rectangle.east, otherRectangle.east),
				north = Math.max(rectangle.north, otherRectangle.north);
		return new Rectangle(west, south, east, north);
	}

	/**
	 * 判断一个长方形是否与另一个相邻或相交
	 * @param rectangle
	 * @param otherRectangle
	 * @return
	 */
	private static boolean nextTo(Rectangle rectangle, Rectangle otherRectangle) {
		return (Rectangle.between(rectangle.east, rectangle.west, otherRectangle.east)
				&& Rectangle.between(rectangle.east, rectangle.west, otherRectangle.west)
				&& (Rectangle.between(rectangle.north, rectangle.south, otherRectangle.south)
						|| Rectangle.between(rectangle.north, rectangle.south, otherRectangle.north)))
				|| (Rectangle.between(rectangle.north, rectangle.south, otherRectangle.north)
						&& Rectangle.between(rectangle.north, rectangle.south, otherRectangle.south)
						&& (Rectangle.between(rectangle.east, rectangle.west, otherRectangle.west)
								|| Rectangle.between(rectangle.east, rectangle.west, otherRectangle.east)));
	}

	private static boolean between(double max, double min, double input) {
		return input <= max || input >= min;
	}

	/**
	 * 合并两个长方形
	 * 
	 * @param rectangle
	 * @param otherRectangle
	 * @return
	 */
	static Rectangle union(Rectangle rectangle, Rectangle otherRectangle) {
		double rectangleEast = rectangle.east;
		double rectangleWest = rectangle.west;
		double otherRectangleEast = otherRectangle.east;
		double otherRectangleWest = otherRectangle.west;
		if (rectangleEast < rectangleWest && otherRectangleEast > 0) {
			rectangleEast += Coordinate.two_pi;
		} else if (otherRectangleEast < otherRectangleWest && rectangleEast > 0) {
			otherRectangleEast += Coordinate.two_pi;
		}
		if (rectangleEast < rectangleWest && otherRectangleWest < 0) {
			otherRectangleWest += Coordinate.two_pi;
		} else if (otherRectangleEast < otherRectangleWest && rectangleWest < 0) {
			rectangleWest += Coordinate.two_pi;
		}
		double west = Coordinate.convertLongitudeRange(Math.min(rectangleWest, otherRectangleWest));
		double east = Coordinate.convertLongitudeRange(Math.max(rectangleEast, otherRectangleEast));
		return new Rectangle(west, Math.min(rectangle.south, otherRectangle.south), east,
				Math.max(rectangle.north, otherRectangle.north));
	}

	/**
	 * 扩大一个长方形,使其可以容纳传入点
	 * 
	 * @param cartographic
	 */
	void expand(double[] cartographic) {
		this.west = Math.min(this.west, cartographic[0]);
		this.south = Math.min(this.south, cartographic[1]);
		this.east = Math.max(this.east, cartographic[0]);
		this.north = Math.max(this.north, cartographic[1]);
	}

	/**
	 * 判断一个点是否存在于此长方形
	 * 
	 * @param cartographic
	 * @return
	 */
	boolean contains(double[] cartographic) {
		double longitude = cartographic[0];
		double latitude = cartographic[1];
		double west = this.west;
		double east = this.east;
		if (east < west) {
			east += Coordinate.two_pi;
			if (longitude < 0.0) {
				longitude += Coordinate.two_pi;
			}
		}
		return (longitude > west || Math.abs(longitude - west) <= Coordinate.epsilon14)
				&& (longitude < east || Math.abs(longitude - east) <= Coordinate.epsilon14) && latitude >= this.south
				&& latitude <= this.north;
	}
}