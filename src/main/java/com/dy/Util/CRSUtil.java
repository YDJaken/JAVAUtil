package com.dy.Util;

import java.io.IOException;
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

	public static Geometry changeCRS(final CoordinateReferenceSystem origin, final Geometry sourcePoint)
			throws IOException {
		if (EPSG4326 == null) {
			try {
				EPSG4326 = CRS.decode("EPSG:4326");
			} catch (FactoryException e) {
				e.printStackTrace();
				EPSG4326 = null;
			}
		}
		try {
			MathTransform transform = CRS.findMathTransform(origin, EPSG4326,true);
			return JTS.transform(sourcePoint, transform);
		} catch (FactoryException | MismatchedDimensionException | TransformException e) {
			e.printStackTrace();
			return null;
		}
	}
}
