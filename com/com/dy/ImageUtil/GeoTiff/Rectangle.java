package com.dy.ImageUtil.GeoTiff;

public class Rectangle extends Config {

	double west;
	double south;
	double east;
	double north;
	double width;
	double height;
	boolean isImage = false;

	public Rectangle() {
		this(0, 0, 0, 0);
	}

	public Rectangle(int[] array) {
		this(array[0], array[1], array[2], array[3], false);
		this.isImage = true;
	}

	public Rectangle(double[] array) {
		this(array, false);
	}

	public Rectangle(double[] array, boolean degree) {
		this(array[0], array[1], array[2], array[3], degree);
	}

	public Rectangle(int west, int south, int east, int north) {
		this(west, south, east, north, false);
		this.isImage = true;
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
		this.height = north - south;
		this.width = east - west;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("Rectangle:{").append(west).append(",").append(south).append(",").append(east)
				.append(",").append(north).append('}');
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
	 * 返回西南角经纬度 弧度制 左下角点
	 * 
	 * @param rec
	 * @return
	 */
	Point southwest() {
		return isImage ? new Point(this.west, this.north) : new Point(this.west, this.south);
	}

	/**
	 * 返回西北角经纬度 弧度制 左上角点
	 * 
	 * @param rec
	 * @return
	 */
	Point northwest() {
		return isImage ? new Point(this.west, this.south) : new Point(this.west, this.north);
	}

	/**
	 * 返回东北角经纬度 弧度制 右上角点
	 * 
	 * @param rec
	 * @return
	 */
	Point northeast() {
		return isImage ? new Point(this.east, this.south) : new Point(this.east, this.north);
	}

	/**
	 * 返回东南角经纬度 弧度制 右下角点
	 * 
	 * @param rec
	 * @return
	 */
	Point southeast() {
		return isImage ? new Point(this.east, this.north) : new Point(this.east, this.south);
	}

	/**
	 * 计算此长方形的中心点
	 * 
	 * @param rec
	 * @return
	 */
	Point center() {
		double east = this.east, west = this.west;
		if (east < west) {
			east += Coordinate.two_pi;
		}
		return new Point(Coordinate.negativePIToPI((west + east) * 0.5), (south + north) * 0.5);
	}

	/**
	 * 返回点数组 内为{左上角点,右上角点,右下角点,左下角点}
	 * 
	 * @return
	 */
	Point[] toPointArray() {
		return new Point[] { northwest(), northeast(), southeast(), southwest() };
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
	 * 判断宽相等的传入矩形是否右相邻
	 * 
	 * @param otherRectangle
	 * @return
	 */
	public boolean rightTo(Rectangle otherRectangle) {
		return this.north == otherRectangle.north && this.south == otherRectangle.south
				&& this.east == otherRectangle.west;
	}

	/**
	 * 得到两个右接壤矩形的接壤边
	 * 
	 * @param otherRectangle (在本矩形右边)
	 * @return
	 */
	public Point[] rightIntersection(Rectangle otherRectangle) {
		if (this.east != otherRectangle.west || this.south > otherRectangle.north || this.north < otherRectangle.south)
			return null;
		return isImage
				? new Point[] { this.south > otherRectangle.south ? northeast() : otherRectangle.northwest(),
						this.north > otherRectangle.north ? otherRectangle.southwest() : southeast() }
				: new Point[] { this.north > otherRectangle.north ? otherRectangle.northwest() : northeast(),
						this.south > otherRectangle.south ? southeast() : otherRectangle.southwest() };
	}

	/**
	 * 判断宽相等的传入矩形是否左相邻
	 * 
	 * @param otherRectangle
	 * @return
	 */
	public boolean leftTo(Rectangle otherRectangle) {
		return this.north == otherRectangle.north && this.south == otherRectangle.south
				&& this.west == otherRectangle.east;
	}

	/**
	 * 得到两个左接壤矩形的接壤边
	 * 
	 * @param otherRectangle (在本矩形左边)
	 * @return
	 */
	public Point[] leftIntersection(Rectangle otherRectangle) {
		if (this.west != otherRectangle.east || this.south > otherRectangle.north || this.north < otherRectangle.south)
			return null;
		return isImage
				? new Point[] { this.south < otherRectangle.south ? otherRectangle.northeast() : northwest(),
						this.north < otherRectangle.north ? southwest() : otherRectangle.southeast() }
				: new Point[] { this.north > otherRectangle.north ? otherRectangle.northeast() : northwest(),
						this.south > otherRectangle.south ? southwest() : otherRectangle.southeast() };
	}

	/**
	 * 判断高相等的传入矩形是否上相邻
	 * 
	 * @param otherRectangle
	 * @return
	 */
	public boolean topTo(Rectangle otherRectangle) {
		return this.west == otherRectangle.west && this.east == otherRectangle.east
				&& this.north == otherRectangle.south;
	}

	/**
	 * 得到两个上接壤矩形的接壤边
	 * 
	 * @param otherRectangle (在本矩形上边)
	 * @return
	 */
	public Point[] topIntersection(Rectangle otherRectangle) {
		if (isImage) {
			if (this.south != otherRectangle.north)
				return null;
		} else {
			if (this.north != otherRectangle.south)
				return null;
		}
		if (this.west > otherRectangle.east || this.east < otherRectangle.west)
			return null;
		return new Point[] { this.west > otherRectangle.west ? northwest() : otherRectangle.southwest(),
				this.east > otherRectangle.east ? otherRectangle.southeast() : northeast() };
	}

	/**
	 * 判断高相等的传入矩形是否下相邻
	 * 
	 * @param otherRectangle
	 * @return
	 */
	public boolean bottomTo(Rectangle otherRectangle) {
		return this.west == otherRectangle.west && this.east == otherRectangle.east
				&& this.south == otherRectangle.north;
	}

	/**
	 * 得到两个下接壤矩形的接壤边
	 * 
	 * @param otherRectangle (在本矩形下边)
	 * @return
	 */
	public Point[] bottomIntersection(Rectangle otherRectangle) {
		if (isImage) {
			if (this.north != otherRectangle.south)
				return null;
		} else {
			if (this.south != otherRectangle.north)
				return null;
		}
		if (this.east < otherRectangle.west || this.west > otherRectangle.east)
			return null;
		return new Point[] { this.west > otherRectangle.west ? southwest() : otherRectangle.northwest(),
				this.east > otherRectangle.east ? otherRectangle.northeast() : southeast() };
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
		boolean img = rectangle.isImage && otherRectangle.isImage;
		double west = Math.min(rectangle.west, otherRectangle.west),
				south = Math.min(rectangle.south, otherRectangle.south),
				east = Math.max(rectangle.east, otherRectangle.east),
				north = Math.max(rectangle.north, otherRectangle.north);
		return img ? new Rectangle((int) west, (int) south, (int) east, (int) north)
				: new Rectangle(west, south, east, north);
	}

	/**
	 * 判断一个长方形是否与另一个相邻或相交
	 * 
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
	 * 合并两个长方形 经纬度
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