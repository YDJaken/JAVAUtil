package com.dy.GeoTools;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Set;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.util.factory.Hints;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.dy.Util.Memory;

import it.geosolutions.imageio.plugins.tiff.TIFFField;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFIFD;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageMetadata;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;
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
			System.out.println(Memory.logMemoryStatus());
			System.out.println("----------------------------------------");
			RenderedImage img = cov.getRenderedImage();
			System.out.println(Memory.logMemoryStatus());
			System.out.println("----------------------------------------");
			Object ret = cov.evaluate(new DirectPosition2D(10.0, 10.0));
			System.out.println(ret.getClass().isArray() ? Array.get(ret, 0) : ret);
			Class<? extends Object> asd = ret.getClass();
			ret = cov.evaluate(new DirectPosition2D(10.01, 10.0));
			System.out.println(ret.getClass().isArray() ? Array.get(ret, 0) : ret);
			ret = cov.evaluate(new DirectPosition2D(10.02, 10.0));
			System.out.println(ret.getClass().isArray() ? Array.get(ret, 0) : ret);
			CoordinateReferenceSystem crs = cov.getCoordinateReferenceSystem();
			boolean finded = false;
			Set<ReferenceIdentifier> indentifiers =crs.getIdentifiers();
			int size = indentifiers.size();
			ReferenceIdentifier[] indentifiersA = indentifiers.toArray(new ReferenceIdentifier[size]);
			for (int i = 0; i < size; i++) {
				if(indentifiersA[i].toString().equals("EPSG:4326")) {
					finded = true;
					break;
				}
			}
			if(!finded) return;
			System.out.println("----------------------------------------");
			Map<Object, Object> map = cov.getProperties();
			TIFFImageReader tifReader = (TIFFImageReader)map.get("JAI.ImageReader");
			TIFFImageMetadata metaData = (TIFFImageMetadata)tifReader.getImageMetadata(0);
			TIFFIFD rootfield =metaData.getRootIFD();
			// GDALMetadata
			TIFFField field = rootfield.getTIFFField(42112);
			System.out.println(field.getValueAsString(0));
			System.out.println("----------------------------------------");
			System.out.println(cov.getEnvelope().toString());
		} catch (IOException giveUp) {
			throw new RuntimeException(giveUp);
		}
	}
}
