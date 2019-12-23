package com.dy.Util;

import java.util.Set;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class CRSUtil {

	private static CoordinateReferenceSystem EPSG4326 = null;

	public static boolean CheckCRS(CoordinateReferenceSystem crs, String targetCRS) {
		boolean finded = false;
		Set<ReferenceIdentifier> indentifiers = crs.getIdentifiers();
		int size = indentifiers.size();
		ReferenceIdentifier[] indentifiersA = indentifiers.toArray(new ReferenceIdentifier[size]);
		for (int i = 0; i < size; i++) {
			if (indentifiersA[i].toString().equals(targetCRS)) {
				finded = true;
				break;
			}
		}
		return finded;
	}
	
	public static MathTransform findTransform(final CoordinateReferenceSystem origin) {
		if (EPSG4326 == null) {
			try {
				EPSG4326 = CRS.decode("EPSG:4326");
			} catch (FactoryException e) {
				e.printStackTrace();
				EPSG4326 = null;
				return null;
			}
		}
		return findTransform(origin,EPSG4326);
	}
	
	public static MathTransform findTransform(final CoordinateReferenceSystem origin,final CoordinateReferenceSystem dest) {
		try {
			return CRS.findMathTransform(origin, dest,true);
		} catch (FactoryException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Geometry changeCRS(final CoordinateReferenceSystem origin,final CoordinateReferenceSystem dest, final Geometry sourcePoint) {
		try {
			MathTransform transform = CRS.findMathTransform(origin, dest,true);
			return JTS.transform(sourcePoint, transform);
		} catch (FactoryException | MismatchedDimensionException | TransformException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public static Geometry changeCRS(final CoordinateReferenceSystem origin, final Geometry sourcePoint) {
		if (EPSG4326 == null) {
			try {
				EPSG4326 = CRS.decode("EPSG:4326");
			} catch (FactoryException e) {
				e.printStackTrace();
				EPSG4326 = null;
				return null;
			}
		}
		return changeCRS(origin,EPSG4326,sourcePoint);
	}
}
