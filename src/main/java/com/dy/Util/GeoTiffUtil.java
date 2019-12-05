package com.dy.Util;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.SAXParser;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.util.factory.Hints;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import com.dy.Util.XMLUtil;
import com.dy.Util.Sup.Config;

import it.geosolutions.imageio.plugins.tiff.TIFFField;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageMetadata;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;

public class GeoTiffUtil {
	public static GridCoverage2D loadGeoTiff(String fileurl) throws IOException {
		return loadGeoTiff(fileurl, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
	}

	public static GridCoverage2D loadGeoTiff(String fileurl, Hints hints) throws IOException {
		File rasterFile = new File("E:\\dem90.tif");
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

	public static Config loadIMGConfig(GridCoverage2D cov) throws SAXException, IOException {
		return loadIMGConfig(cov, new Config());
	}

	public static Config loadIMGConfig(GridCoverage2D cov, Config config) throws SAXException, IOException {
		loadMetadata(cov, config);
		config.setConfig("CRS", "EPSG:4326");
		config.setConfig("saveDate", new Date());
		Envelope env = cov.getEnvelope();
		GeneralDirectPosition lower = (GeneralDirectPosition) env.getLowerCorner();
		GeneralDirectPosition upper = (GeneralDirectPosition) env.getUpperCorner();
		config.setConfig("MinLON", lower.getOrdinate(0));
		config.setConfig("MinLAT", lower.getOrdinate(1));

		config.setConfig("MaxLON", upper.getOrdinate(0));
		config.setConfig("MaxLAT", upper.getOrdinate(1));

		config.setConfig("StartLON", lower.getOrdinate(0));
		config.setConfig("StartLAT", upper.getOrdinate(1));

		RenderedImage img = cov.getRenderedImage();
		int imgWidth = img.getWidth();
		int imgHeight = img.getHeight();
		double differLON = (upper.getOrdinate(0) - lower.getOrdinate(0)) / imgWidth;
		double differLAT = (upper.getOrdinate(1) - lower.getOrdinate(1)) / imgHeight;
		config.setConfig("imgWidth", imgWidth);
		config.setConfig("imgHeight", imgHeight);
		config.setConfig("differLON", differLON);
		config.setConfig("differLAT", differLAT);
//		Rectangle rec = loadRegion(80.6231689, -89, 82.6231689, 29.973449, config);
//		rec.setSize(1, 1);
//		Raster a = img.getData(rec);
//		int[] data = new int[1];
//		a.getPixel(rec.x, rec.y, data);
//		Object obj = cov.evaluate(new DirectPosition2D(80.6231689, 29.973449));
		return config;
	}

	public static Rectangle loadRegion(final double minLon, final double minLat, final double maxLon,
			final double maxLat, final Config config) {
		double StartLON = (double) config.getConfig("StartLON");
		double StartLAT = (double) config.getConfig("StartLAT");
		double differLON = (double) config.getConfig("differLON");
		double differLAT = (double) config.getConfig("differLAT");
		int imgWidth = (int) config.getConfig("imgWidth");
		int imgHeight = (int) config.getConfig("imgHeight");

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
		String GDALMetadata = field.getValueAsString(0);

		if (field != null) {
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
