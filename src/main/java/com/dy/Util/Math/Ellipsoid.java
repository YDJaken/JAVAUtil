package com.dy.Util.Math;

public class Ellipsoid {

	public static final Ellipsoid WGS84 = new Ellipsoid(6378137.0, 6378137.0, 6356752.3142451793);

	public Point3D getRadii() {
		return radii;
	}

	public void setRadii(Point3D radii) {
		this.radii = radii;
	}

	public Point3D getRadiiSquared() {
		return radiiSquared;
	}

	public void setRadiiSquared(Point3D radiiSquared) {
		this.radiiSquared = radiiSquared;
	}

	public Point3D getRadiiToTheFourth() {
		return radiiToTheFourth;
	}

	public void setRadiiToTheFourth(Point3D radiiToTheFourth) {
		this.radiiToTheFourth = radiiToTheFourth;
	}

	public Point3D getOneOverRadii() {
		return oneOverRadii;
	}

	public void setOneOverRadii(Point3D oneOverRadii) {
		this.oneOverRadii = oneOverRadii;
	}

	public Point3D getOneOverRadiiSquared() {
		return oneOverRadiiSquared;
	}

	public void setOneOverRadiiSquared(Point3D oneOverRadiiSquared) {
		this.oneOverRadiiSquared = oneOverRadiiSquared;
	}

	public double getMinimumRadius() {
		return minimumRadius;
	}

	public void setMinimumRadius(double minimumRadius) {
		this.minimumRadius = minimumRadius;
	}

	public double getMaximumRadius() {
		return maximumRadius;
	}

	public void setMaximumRadius(double maximumRadius) {
		this.maximumRadius = maximumRadius;
	}

	public double getCenterToleranceSquared() {
		return centerToleranceSquared;
	}

	public void setCenterToleranceSquared(double centerToleranceSquared) {
		this.centerToleranceSquared = centerToleranceSquared;
	}

	public double getSquaredXOverSquaredZ() {
		return squaredXOverSquaredZ;
	}

	public void setSquaredXOverSquaredZ(double squaredXOverSquaredZ) {
		this.squaredXOverSquaredZ = squaredXOverSquaredZ;
	}

	private Point3D radii;
	private Point3D radiiSquared;
	private Point3D radiiToTheFourth;
	private Point3D oneOverRadii;
	private Point3D oneOverRadiiSquared;
	private double minimumRadius;
	private double maximumRadius;
	private double centerToleranceSquared = MathUtil.EPSILON1;
	private double squaredXOverSquaredZ;

	public Ellipsoid(double x, double y, double z) {
		this.radii = new Point3D(x, y, z);
		this.radiiSquared = new Point3D(x * x, y * y, z * z);
		this.radiiToTheFourth = new Point3D(x * x * x * x, y * y * y * y, z * z * z * z);
		this.oneOverRadii = new Point3D(x == 0 ? 0 : 1 / x, y == 0 ? 0 : 1 / y, z == 0 ? 0 : 1 / z);
		this.oneOverRadiiSquared = new Point3D(x == 0 ? 0 : 1 / (x * x), y == 0 ? 0 : 1 / (y * y),
				z == 0 ? 0 : 1 / (z * z));
		this.minimumRadius = Math.min(z, Math.min(x, y));
		this.maximumRadius = Math.max(z, Math.max(x, y));
		if (radiiSquared.getZ() != 0) {
			this.squaredXOverSquaredZ = radiiSquared.getX() / radiiSquared.getZ();
		}
	}

	public Point3D geodeticSurfaceNormal(Point3D cartesian) {
		return Point3D.multiplyComponents(cartesian, this.oneOverRadiiSquared).normalize();
	}

	public Point3D geodeticSurfaceNormalCartographic(double longitude, double latitude) {
		double cosLatitude = Math.cos(latitude);
		double x = cosLatitude * Math.cos(longitude);
		double y = cosLatitude * Math.sin(longitude);
		double z = Math.sin(latitude);

		return new Point3D(x, y, z).normalize();
	}

	public Point3D cartographicToCartesian(double longitude, double latitude, double height) {
		Point3D n = this.geodeticSurfaceNormalCartographic(longitude, latitude);
		Point3D k = Point3D.multiplyComponents(this.radiiSquared, n);
		double gamma = Math.sqrt(n.dotProduct(k));
		k.applyScaler(1 / gamma);
		n.applyScaler(height);
		n.add(k);
		return n;
	}

	public Point3D scaleToGeodeticSurface(Point3D cartesian) {
		double positionX = cartesian.getX();
		double positionY = cartesian.getY();
		double positionZ = cartesian.getZ();

		double oneOverRadiiX = oneOverRadii.getX();
		double oneOverRadiiY = oneOverRadii.getY();
		double oneOverRadiiZ = oneOverRadii.getZ();

		double x2 = positionX * positionX * oneOverRadiiX * oneOverRadiiX;
		double y2 = positionY * positionY * oneOverRadiiY * oneOverRadiiY;
		double z2 = positionZ * positionZ * oneOverRadiiZ * oneOverRadiiZ;

		double squaredNorm = x2 + y2 + z2;
		double ratio = Math.sqrt(1.0 / squaredNorm);

		Point3D intersection = cartesian.copy();
		intersection.applyScaler(ratio);

		if (squaredNorm < centerToleranceSquared) {
			return !Double.isFinite(ratio) ? null : intersection;
		}

		double oneOverRadiiSquaredX = oneOverRadiiSquared.getX();
		double oneOverRadiiSquaredY = oneOverRadiiSquared.getY();
		double oneOverRadiiSquaredZ = oneOverRadiiSquared.getZ();

		Point3D gradient = new Point3D(intersection.getX() * oneOverRadiiSquaredX * 2.0,
				intersection.getY() * oneOverRadiiSquaredY * 2.0, intersection.getZ() * oneOverRadiiSquaredZ * 2.0);

		// Compute the initial guess at the normal vector multiplier, lambda.
		double lambda = (1.0 - ratio) * Point3D.computeMagenitude(cartesian)
				/ (0.5 * Point3D.computeMagenitude(gradient));
		double correction = 0.0;

		double func;
		double denominator;
		double xMultiplier;
		double yMultiplier;
		double zMultiplier;
		double xMultiplier2;
		double yMultiplier2;
		double zMultiplier2;
		double xMultiplier3;
		double yMultiplier3;
		double zMultiplier3;

		do {
			lambda -= correction;

			xMultiplier = 1.0 / (1.0 + lambda * oneOverRadiiSquaredX);
			yMultiplier = 1.0 / (1.0 + lambda * oneOverRadiiSquaredY);
			zMultiplier = 1.0 / (1.0 + lambda * oneOverRadiiSquaredZ);

			xMultiplier2 = xMultiplier * xMultiplier;
			yMultiplier2 = yMultiplier * yMultiplier;
			zMultiplier2 = zMultiplier * zMultiplier;

			xMultiplier3 = xMultiplier2 * xMultiplier;
			yMultiplier3 = yMultiplier2 * yMultiplier;
			zMultiplier3 = zMultiplier2 * zMultiplier;

			func = x2 * xMultiplier2 + y2 * yMultiplier2 + z2 * zMultiplier2 - 1.0;

			denominator = x2 * xMultiplier3 * oneOverRadiiSquaredX + y2 * yMultiplier3 * oneOverRadiiSquaredY
					+ z2 * zMultiplier3 * oneOverRadiiSquaredZ;

			double derivative = -2.0 * denominator;

			correction = func / derivative;
		} while (Math.abs(func) > MathUtil.EPSILON12);

		return new Point3D(positionX * xMultiplier, positionY * yMultiplier, positionZ * zMultiplier);
	}

	public double[] cartesianToCartographic(Point3D cartesian) {
		Point3D p = this.scaleToGeodeticSurface(cartesian);
		if (p == null) {
			return null;
		}

		Point3D n = this.geodeticSurfaceNormal(p);
		Point3D h = cartesian.clone();
		Point3D tmpP = p.clone();
		tmpP.applyScaler(-1.0);
		h.add(tmpP);

		double longitude = Math.atan2(n.getY(), n.getX());
		double latitude = Math.asin(n.getZ());
		double height = MathUtil.sign(h.dotProduct(cartesian)) * Point3D.computeMagenitude(h);

		return new double[] { longitude, latitude, height };
	}
}
