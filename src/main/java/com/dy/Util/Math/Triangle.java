package com.dy.Util.Math;

public class Triangle {

	public static Double computeArea(Point3D A, Point3D B, Point3D C) {
		Point3D AB = new Point3D(B.getX()-A.getX(),B.getY()-A.getY(),B.getZ()-A.getZ());
		Point3D AC = new Point3D(C.getX() - A.getX(), C.getY() - A.getY(), C.getZ() - A.getZ());
		return Point3D.computeMagenitude(AB.crossProduct(AC)) / 2;
	}

}
