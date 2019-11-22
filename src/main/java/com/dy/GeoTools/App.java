package com.dy.GeoTools;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.util.factory.Hints;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

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
			cov = reader.read(null);
			Runtime current = Runtime.getRuntime();
			System.out.println("JVM使用内存：" + (current.totalMemory() - current.freeMemory()) / (1024 * 1024) + "M");
			MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
			MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
			System.out.println("堆内存使用情况：" + (memoryUsage.getUsed() / (1024 * 1024) + "M"));

			CoordinateReferenceSystem crs = cov.getCoordinateReferenceSystem();
			System.out.println(crs.toWKT());
			System.out.println(cov.getProperties().toString());
			System.out.println(cov.getEnvelope().toString());
		} catch (IOException giveUp) {
			throw new RuntimeException(giveUp);
		}
	}
}
