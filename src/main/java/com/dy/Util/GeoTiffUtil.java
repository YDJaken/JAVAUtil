package com.dy.Util;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.media.jai.PlanarImage;
import javax.xml.parsers.SAXParser;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.util.factory.Hints;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import com.dy.Util.XMLUtil;
import com.dy.Util.Sup.Config;
import it.geosolutions.imageio.plugins.tiff.TIFFField;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageMetadata;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;

public class GeoTiffUtil {

	public static Object loadImgData(PlanarImage img, Rectangle rec) throws IllegalArgumentException {
		SampleModel model = img.getSampleModel();
		int type = model.getDataType();
		switch (type) {
		case DataBuffer.TYPE_BYTE:
			return null;
		case DataBuffer.TYPE_DOUBLE:
			return processData(model, img, rec, (double[]) null);
		case DataBuffer.TYPE_FLOAT:
			return processData(model, img, rec, (float[]) null);
		case DataBuffer.TYPE_INT:
		case DataBuffer.TYPE_SHORT:
		case DataBuffer.TYPE_USHORT:
			return processData(model, img, rec, (int[]) null);
		case DataBuffer.TYPE_UNDEFINED:
			return null;
		default:
			return null;
		}
	}

	private static double[] processData(final SampleModel model, final PlanarImage img, final Rectangle rec,
			double[] ddata) {
		int bandNum = img.getNumBands();
		if (ddata == null) {
			long length = (long) rec.height * (long) rec.width * (long) bandNum;
			if (length > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("Request Region too big.");
			}
			ddata = new double[(int) length];
		}
		Point[] indexs = img.getTileIndices(rec);
		for (int i = 0; i < indexs.length; i++) {
			Point target = indexs[i];
			Raster start = img.getTile(target.x, target.y);
			Rectangle bounds = rec.intersection(start.getBounds());
			for (int j = 0; j < bounds.height; j++) {
				double[] tmpData = new double[bounds.width * bandNum];
				start.getPixels(bounds.x, bounds.y + j, bounds.width, 1, tmpData);
				int topIndex = ((j + bounds.y - rec.y) * rec.width + (bounds.x - rec.x)) * bandNum;
				for (int k = 0; k < tmpData.length; k += bandNum) {
					for (int l = 0; l < bandNum; l++) {
						ddata[topIndex + k + l] = tmpData[k + l];
					}
				}
			}
		}
		return ddata;
	}

	private static float[] processData(final SampleModel model, final PlanarImage img, final Rectangle rec,
			float[] fdata) {
		int bandNum = img.getNumBands();
		if (fdata == null) {
			long length = (long) rec.height * (long) rec.width * (long) bandNum;
			if (length > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("Request Region too big.");
			}
			fdata = new float[(int) length];
		}
		Point[] indexs = img.getTileIndices(rec);
		for (int i = 0; i < indexs.length; i++) {
			Point target = indexs[i];
			Raster start = img.getTile(target.x, target.y);
			Rectangle bounds = rec.intersection(start.getBounds());
			for (int j = 0; j < bounds.height; j++) {
				float[] tmpData = new float[bounds.width * bandNum];
				start.getPixels(bounds.x, bounds.y + j, bounds.width, 1, tmpData);
				int topIndex = ((j + bounds.y - rec.y) * rec.width + (bounds.x - rec.x)) * bandNum;
				for (int k = 0; k < tmpData.length; k += bandNum) {
					for (int l = 0; l < bandNum; l++) {
						fdata[topIndex + k + l] = tmpData[k + l];
					}
				}
			}
		}
		return fdata;
	}

	private static int[] processData(final SampleModel model, final PlanarImage img, final Rectangle rec, int[] idata) {
		int bandNum = img.getNumBands();
		if (idata == null) {
			long length = (long) rec.height * (long) rec.width * (long) bandNum;
			if (length > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("Request Region too big.");
			}
			idata = new int[(int) length];
		}
		Point[] indexs = img.getTileIndices(rec);
		for (int i = 0; i < indexs.length; i++) {
			Point target = indexs[i];
			Raster start = img.getTile(target.x, target.y);
			Rectangle bounds = rec.intersection(start.getBounds());
			for (int j = 0; j < bounds.height; j++) {
				int[] tmpData = new int[bounds.width * bandNum];
				start.getPixels(bounds.x, bounds.y + j, bounds.width, 1, tmpData);
				int topIndex = ((j + bounds.y - rec.y) * rec.width + (bounds.x - rec.x)) * bandNum;
				for (int k = 0; k < tmpData.length; k += bandNum) {
					for (int l = 0; l < bandNum; l++) {
						idata[topIndex + k + l] = tmpData[k + l];
					}
				}
			}
		}
		return idata;
	}

	public static GridCoverage2D loadGeoTiff(String fileurl) throws IOException {
		return loadGeoTiff(fileurl, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
	}

	public static GridCoverage2D loadGeoTiff(String fileurl, Hints hints) throws IOException {
		File rasterFile = new File(fileurl);
		if (!rasterFile.exists() || !rasterFile.canRead()) {
			return null;
		}
		AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
		GridCoverage2DReader reader = format.getReader(rasterFile, hints);
		GridCoverage2D cov = reader.read(null);
		return cov;
	}

	public static boolean CheckCRS(GridCoverage2D cov) {
		return CheckCRS(cov, "EPSG:4326");
	}

	public static boolean CheckCRS(GridCoverage2D cov, String targetCRS) {
		CoordinateReferenceSystem crs = cov.getCoordinateReferenceSystem();
		return CRSUtil.CheckCRS(crs, targetCRS);
	}

	public static Config loadIMGConfig(GridCoverage2D cov)
			throws SAXException, IOException, MismatchedDimensionException, TransformException {
		return loadIMGConfig(cov, new Config());
	}

	public static Config loadIMGConfig(GridCoverage2D cov, Config config)
			throws SAXException, IOException, MismatchedDimensionException, TransformException {
		loadMetadata(cov, config);
		config.setConfig("CRS", cov.getCoordinateReferenceSystem().toWKT());
		config.setConfig("saveDate", new Date(new java.util.Date().getTime()));
		Envelope env = cov.getEnvelope();
		GeneralDirectPosition lower = (GeneralDirectPosition) env.getLowerCorner();
		GeneralDirectPosition upper = (GeneralDirectPosition) env.getUpperCorner();
		
		config.setConfig("MinLON", lower.getOrdinate(0));
		config.setConfig("MinLAT", lower.getOrdinate(1));
		config.setConfig("MaxLON", upper.getOrdinate(0));
		config.setConfig("MaxLAT", upper.getOrdinate(1));
		config.setConfig("StartLON", config.getConfig("MinLON"));
		config.setConfig("StartLAT", config.getConfig("MaxLAT"));

		PlanarImage img = (PlanarImage) cov.getRenderedImage();
		int imgWidth = img.getWidth();
		int imgHeight = img.getHeight();
		double differLON = ((double) config.getConfig("MaxLON") - (double) config.getConfig("MinLON")) / imgWidth;
		double differLAT = ((double) config.getConfig("MaxLAT") - (double) config.getConfig("MinLAT")) / imgHeight;
		config.setConfig("imgWidth", imgWidth);
		config.setConfig("imgHeight", imgHeight);
		config.setConfig("imgBrandNumber", img.getNumBands());
		config.setConfig("differLON", differLON);
		config.setConfig("differLAT", differLAT);
//		testSame(config, img, cov);
		return config;
	}

//	private static void testSame(Config config, RenderedImage img, GridCoverage2D cov) {
//		Rectangle rec = loadRegion(80.6231689, -89, 82.6231689, 29.973449, config);
//		rec.setSize(1, 1);
//		Raster a = img.getData(rec);
//		int[] data = new int[1];
//		a.getPixel(rec.x, rec.y, data);
//		System.out.println(data[0]);
//		Object obj = cov.evaluate(new DirectPosition2D(80.6231689, 29.973449));
//		System.out.println(Array.get(obj, 0));
//	}

	public static Rectangle loadRegion(final double minLon, final double minLat, final double maxLon,
			final double maxLat, final Config config) {
		double StartLON = (double) config.getConfig("StartLON");
		double StartLAT = (double) config.getConfig("StartLAT");
		double differLON = (double) config.getConfig("differLON");
		double differLAT = (double) config.getConfig("differLAT");
		int imgWidth = (int) config.getConfig("imgWidth");
		int imgHeight = (int) config.getConfig("imgHeight");
		return loadRegion(minLon, minLat, maxLon, maxLat, StartLON, StartLAT, differLON, differLAT, imgWidth,
				imgHeight);
	}

	public static Rectangle loadRegion(final double minLon, final double minLat, final double maxLon,
			final double maxLat, final double StartLON, final double StartLAT, final double differLON,
			final double differLAT, final int imgWidth, final int imgHeight) {
		int startX = (int) Math.floor((minLon - StartLON) / differLON);
		if (startX < 0 || startX > imgWidth) {
			return null;
		}

		int startY = (int) Math.floor((StartLAT - maxLat) / differLAT);
		if (startY < 0 || startY > imgHeight) {
			return null;
		}

		int endX = (int) Math.ceil((maxLon - StartLON) / differLON);
		if (endX < 0 || endX > imgWidth || endX < startX) {
			return null;
		}
		int endY = (int) Math.ceil((StartLAT - minLat) / differLAT);
		if (endY < 0 || endY > imgHeight || endY < startY) {
			return null;
		}

		return new Rectangle(startX, startY, endX - startX, endY - startY);
	}

	private static void loadMetadata(GridCoverage2D cov, Config config) throws SAXException, IOException {
		Map<?, ?> map = cov.getProperties();
		TIFFImageReader tifReader = (TIFFImageReader) map.get("JAI.ImageReader");
		TIFFImageMetadata metaData = (TIFFImageMetadata) tifReader.getImageMetadata(0);
		// DateTime
		TIFFField field = metaData.getTIFFField(306);
		config.setConfig("modifyDate", field == null ? null : field.getData());

		// GDALMetadata
		field = metaData.getTIFFField(42112);

		if (field != null) {
			String GDALMetadata = field.getValueAsString(0);
			SAXParser builder = XMLUtil.getNewSAXParser();
			GDALMetaHandler handler = new GDALMetaHandler(config);
			builder.parse(new ByteArrayInputStream(GDALMetadata.getBytes()), handler);
		}
	}

}

class GDALMetaHandler extends DefaultHandler2 {

	private Config config;
	private static final HashMap<String, String> GDALMetaConfig = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			this.put("STATISTICS_MAXIMUM", "MAX");
			this.put("STATISTICS_MINIMUM", "MIN");
			this.put("STATISTICS_MEAN", "MEAN");
			this.put("STATISTICS_STDDEV", "STDDEV");
		}
	};

	public GDALMetaHandler(Config config) {
		this.config = config;
	}

	private String sample = null;
	private String name = null;

	/**
	 * 用来遍历xml文件的开始标签
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (qName.equals("Item")) {
			sample = "sample" + attributes.getValue("sample");
			@SuppressWarnings("unchecked")
			HashMap<String, Object> map = (HashMap<String, Object>) config.getConfig(sample);
			if (map == null) {
				map = new HashMap<String, Object>();
				config.setConfig(sample, map);
			}
			String tmpName = attributes.getValue("name");
			tmpName = GDALMetaConfig.get(tmpName);
			if (tmpName != null) {
				name = tmpName;
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		String value = new String(ch, start, length);
		value = value.trim();
		if (sample != null && name != null) {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> map = (HashMap<String, Object>) config.getConfig(sample);
			map.put(name, value);
		}
	}

	/**
	 * 用来遍历xml文件的结束标签
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		sample = null;
		name = null;
	}
}
