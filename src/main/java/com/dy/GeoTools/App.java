package com.dy.GeoTools;

import java.io.File;
import java.io.IOException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.util.factory.Hints;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.dy.Util.Memory;

import it.geosolutions.jaiext.JAIExt;

/**
 * Hello world!
 *
 */
public class App {
	static {
		JAIExt.initJAIEXT();
	}

	public static void main(String[] args) {
		File rasterFile = new File("E:\\dem90.tif");
		AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
		Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
		GridCoverage2DReader reader = format.getReader(rasterFile, hints);
		GridCoverage2D cov = null;
		try {
			System.out.println("加载前" + Memory.logMemoryStatus());
			cov = reader.read(null);
			System.out.println("加载后" + Memory.logMemoryStatus());
			CoordinateReferenceSystem crs = cov.getCoordinateReferenceSystem();
			crs.getIdentifiers().forEach(a->System.out.println(a.toString()));
			System.out.println(crs.getName());
			System.out.println(crs.toWKT());
			System.out.println(cov.getProperties().toString());
			System.out.println(cov.getEnvelope().toString());
		} catch (IOException giveUp) {
			throw new RuntimeException(giveUp);
		}
	}
}
