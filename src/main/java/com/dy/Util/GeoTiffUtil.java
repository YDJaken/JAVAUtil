package com.dy.Util;

import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.IIOException;
import javax.xml.parsers.SAXParser;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.util.factory.Hints;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

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
		boolean finded = false;
		CoordinateReferenceSystem crs = cov.getCoordinateReferenceSystem();
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

	public static Config loadIMGConfig(GridCoverage2D cov) throws IIOException {
		return loadIMGConfig(cov, new Config());
	}

	public static Config loadIMGConfig(GridCoverage2D cov, Config config) throws IIOException {
		loadMetadata(cov, config);
		config.setConfig("CRS", "EPSG:4326");
		Envelope env = cov.getEnvelope();
		GeneralDirectPosition lower = (GeneralDirectPosition) env.getLowerCorner();
		GeneralDirectPosition upper = (GeneralDirectPosition) env.getUpperCorner();
		config.setConfig("MinLON", lower.getOrdinate(0));
		config.setConfig("MinLAT", lower.getOrdinate(1));

		config.setConfig("MaxLON", upper.getOrdinate(0));
		config.setConfig("MaxLAT", upper.getOrdinate(1));

		config.setConfig("StartLON", lower.getOrdinate(0));
		config.setConfig("StartLAT", lower.getOrdinate(1));

		RenderedImage img = cov.getRenderedImage();
		int imgWidth = img.getWidth();
		int imgHeight = img.getHeight();
		double differLON = (upper.getOrdinate(0) - lower.getOrdinate(0)) / imgWidth;
		double differLAT = (upper.getOrdinate(1) - lower.getOrdinate(1)) / imgHeight;
		config.setConfig("imgWidth", imgWidth);
		config.setConfig("imgHeight", imgHeight);
		config.setConfig("differLON", differLON);
		config.setConfig("differLAT", differLAT);
		return config;
	}

	private static void loadMetadata(GridCoverage2D cov, Config config) throws IIOException {
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
			try {
				SAXParser builder = XMLUtil.getNewSAXParser();
				GDALMetaHandler handler = new GDALMetaHandler(config);
				builder.parse(new ByteArrayInputStream(GDALMetadata.getBytes()), handler);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class GDALMetaHandler extends DefaultHandler2 {

	private Config config;
	private static final HashMap<String, String> GDALMetaConfig = new HashMap<String, String>() {
		/**
		 * 
		 */
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
