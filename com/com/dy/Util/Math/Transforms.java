package com.dy.Util.Math;

import java.util.HashMap;

public class Transforms {

	@SuppressWarnings("serial")
	public static final HashMap<String, Point3D> degeneratePositionLocalFrame = new HashMap<String, Point3D>() {
		{
			put("north", new Point3D(-1, 0, 0));
			put("east", new Point3D(0, 1, 0));
			put("up", new Point3D(0, 0, 1));
			put("south", new Point3D(1, 0, 0));
			put("west", new Point3D(0, -1, 0));
			put("down", new Point3D(0, 0, -1));
		}
	};

	@SuppressWarnings("serial")
	public static final HashMap<String, HashMap<String, String>> vectorProductLocalFrame = new HashMap<String, HashMap<String, String>>() {
		{
			put("up", new HashMap<String, String>() {
				{
					put("south", "east");
					put("north", "west");
					put("west", "south");
					put("east", "north");
				}
			});
			put("down", new HashMap<String, String>() {
				{
					put("south", "west");
					put("north", "east");
					put("west", "north");
					put("east", "south");
				}
			});
			put("south", new HashMap<String, String>() {
				{
					put("up", "west");
					put("down", "east");
					put("west", "down");
					put("east", "up");
				}
			});
			put("north", new HashMap<String, String>() {
				{
					put("up", "east");
					put("down", "west");
					put("west", "up");
					put("east", "down");
				}
			});
			put("west", new HashMap<String, String>() {
				{
					put("up", "north");
					put("down", "south");
					put("north", "down");
					put("south", "up");
				}
			});
			put("east", new HashMap<String, String>() {
				{
					put("up", "south");
					put("down", "north");
					put("north", "up");
					put("south", "down");
				}
			});
		}
	};

	public static final HashMap<String, Point3D> scratchCalculateCartesian = new HashMap<String, Point3D>();

	public static Matrix4 localFrameToFixedFrameGenerator(String firstAxis, String secondAxis, Point3D origin) {

		String thirdAxis = vectorProductLocalFrame.get(firstAxis).get(secondAxis);

		Ellipsoid ellipsoid = Ellipsoid.WGS84;

		Point3D scratchFirstCartesian, scratchSecondCartesian, scratchThirdCartesian = null;

		if (MathUtil.equalsEpsilon(origin.getX(), 0.0, MathUtil.EPSILON14)
				&& MathUtil.equalsEpsilon(origin.getY(), 0.0, MathUtil.EPSILON14)) {
			int sign = MathUtil.sign(origin.getZ());

			scratchFirstCartesian = degeneratePositionLocalFrame.get(firstAxis);

			if (!firstAxis.equals("east") && !firstAxis.equals("west")) {
				scratchFirstCartesian.applyScaler((double) sign);
			}

			scratchSecondCartesian = degeneratePositionLocalFrame.get(secondAxis);
			if (!secondAxis.equals("east") && !secondAxis.equals("west")) {
				scratchSecondCartesian.applyScaler((double) sign);
			}

			scratchThirdCartesian = degeneratePositionLocalFrame.get(thirdAxis);
			if (!thirdAxis.equals("east") && !thirdAxis.equals("west")) {
				scratchThirdCartesian.applyScaler((double) sign);
			}
		} else {
			Point3D up = ellipsoid.geodeticSurfaceNormal(origin);
			Point3D east = new Point3D();
			east.setX(-origin.getY());
			east.setY(origin.getX());
			east.setZ(0.0);
			east = east.normalize();
			Point3D north = up.crossProduct(east);
			Point3D down = up.copy();
			down.applyScaler(-1.0);
			Point3D west = east.copy();
			west.applyScaler(-1.0);
			Point3D south = north.copy();
			south.applyScaler(-1.0);

			scratchCalculateCartesian.put("up", up);
			scratchCalculateCartesian.put("east", east);
			scratchCalculateCartesian.put("north", north);
			scratchCalculateCartesian.put("down", down);
			scratchCalculateCartesian.put("west", west);
			scratchCalculateCartesian.put("south", south);

			scratchFirstCartesian = scratchCalculateCartesian.get(firstAxis);
			scratchSecondCartesian = scratchCalculateCartesian.get(secondAxis);
			scratchThirdCartesian = scratchCalculateCartesian.get(thirdAxis);
			scratchCalculateCartesian.clear();
		}

		return new Matrix4(scratchFirstCartesian.getX(), scratchFirstCartesian.getY(), scratchFirstCartesian.getZ(),
				0.0, scratchSecondCartesian.getX(), scratchSecondCartesian.getY(), scratchSecondCartesian.getZ(), 0.0,
				scratchThirdCartesian.getX(), scratchThirdCartesian.getY(), scratchThirdCartesian.getZ(), 0.0,
				origin.getX(), origin.getY(), origin.getZ(), 1.0);
	}
}
