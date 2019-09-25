package com.dy.Util.Math;

import java.util.HashMap;

public class Transforms {

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

	public static Matrix4 localFrameToFixedFrameGenerator(String firstAxis, String secondAxis, Point3D origin) {

		String thirdAxis = vectorProductLocalFrame.get(firstAxis).get(secondAxis);

		Ellipsoid ellipsoid = Ellipsoid.WGS84;
		
		if (MathUtil.equalsEpsilon(origin.getX(), 0.0, MathUtil.EPSILON14)
				&& MathUtil.equalsEpsilon(origin.getY(), 0.0, MathUtil.EPSILON14)) {
			int sign = MathUtil.sign(origin.getZ());

			Point3D scratchFirstCartesian = degeneratePositionLocalFrame.get(firstAxis);

			if (!firstAxis.equals("east") && !firstAxis.equals("west")) {
				scratchFirstCartesian.applyScaler((double) sign);
			}

			Point3D scratchSecondCartesian = degeneratePositionLocalFrame.get(secondAxis);
			if (!secondAxis.equals("east") && !secondAxis.equals("west")) {
				scratchSecondCartesian.applyScaler((double) sign);
			}

			Point3D scratchThirdCartesian = degeneratePositionLocalFrame.get(thirdAxis);
			if (!thirdAxis.equals("east") && !thirdAxis.equals("west")) {
				scratchThirdCartesian.applyScaler((double) sign);
			}
		} else {
			Point3D up = ellipsoid.geodeticSurfaceNormal(origin);
			Point3D east = new Point3D();
			east.x = -origin.y;
			east.y = origin.x;
			east.z = 0.0;
			Cartesian3.normalize(east, scratchCalculateCartesian.east);
			Cartesian3.cross(up, east, scratchCalculateCartesian.north);

			Cartesian3.multiplyByScalar(scratchCalculateCartesian.up, -1, scratchCalculateCartesian.down);
			Cartesian3.multiplyByScalar(scratchCalculateCartesian.east, -1, scratchCalculateCartesian.west);
			Cartesian3.multiplyByScalar(scratchCalculateCartesian.north, -1, scratchCalculateCartesian.south);

			scratchFirstCartesian = scratchCalculateCartesian[firstAxis];
			scratchSecondCartesian = scratchCalculateCartesian[secondAxis];
			scratchThirdCartesian = scratchCalculateCartesian[thirdAxis];
		}
		result[0] = scratchFirstCartesian.x;
		result[1] = scratchFirstCartesian.y;
		result[2] = scratchFirstCartesian.z;
		result[3] = 0.0;
		result[4] = scratchSecondCartesian.x;
		result[5] = scratchSecondCartesian.y;
		result[6] = scratchSecondCartesian.z;
		result[7] = 0.0;
		result[8] = scratchThirdCartesian.x;
		result[9] = scratchThirdCartesian.y;
		result[10] = scratchThirdCartesian.z;
		result[11] = 0.0;
		result[12] = origin.x;
		result[13] = origin.y;
		result[14] = origin.z;
		result[15] = 1.0;
		return result;
	}
}
