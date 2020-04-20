package com.dy.Util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ShapeGeoJSONUtil {

	public static String shpToGeoJSON(final String shpFilePath) throws IOException {
		return shpToGeoJSON(shpFilePath, "ISO-8859-1", 0, -1);
	}

	public static String shpToGeoJSON(final String shpFilePath, final long offSet, final long Count)
			throws IOException {
		return shpToGeoJSON(shpFilePath, "ISO-8859-1", offSet, Count);
	}

	public static String shpToGeoJSON(final String shpFilePath, final String charset, final long offSet,
			final long Count) throws IOException {
		File shp = new File(shpFilePath);
		if (!shp.exists()) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("url", shp.toURI().toURL());

		ShapefileDataStore dataStore = (ShapefileDataStore) DataStoreFinder.getDataStore(map);
		dataStore.setCharset(Charset.forName(charset));
		String typeName = dataStore.getTypeNames()[0];

		FeatureJSON fJSON = new FeatureJSON();
		FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
		Filter filter = Filter.INCLUDE;

		FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
		SimpleFeature[] features = changeCRS(dataStore, collection, charset);
		StringWriter writer = null;
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("{\"type\": \"FeatureCollection\",\"features\": [");
			long i = 0;
			long count = 0;
			for (int j = 0; j < features.length; j++) {
				if (i < offSet) {
					i++;
					continue;
				}
				SimpleFeature feature = features[j];
				if (++count < Count || Count < 0) {
					writer = new StringWriter();
					fJSON.writeFeature(feature, writer);
					sb.append(writer.toString());
					if (j != features.length - 1)
						sb.append(',');
				} else {
					writer = new StringWriter();
					fJSON.writeFeature(feature, writer);
					sb.append(writer.toString());
					break;
				}
			}
			sb.append("]}");
		} finally {
			dataStore.dispose();
		}
		return sb.toString();
	}

	private static SimpleFeature[] changeCRS(final ShapefileDataStore dataStore,
			final FeatureCollection<SimpleFeatureType, SimpleFeature> collection, final String charset)
			throws IOException {
		CoordinateReferenceSystem origin = dataStore.getSchema().getCoordinateReferenceSystem();
		SimpleFeature[] features = new SimpleFeature[collection.size()];
		collection.toArray(features);
		if (CRSUtil.CheckCRS(origin, "EPSG:4326")) {
			return features;
		}
		for (int i = 0; i < features.length; i++) {
			SimpleFeature feature = features[i];
			List<Object> list = feature.getAttributes();
			Object obj = list.get(0);
			Geometry geo = (Geometry) obj;
			Geometry out = CRSUtil.changeCRS(origin, geo);
			if (out != null) {
				feature.setAttribute(0, out);
			}
		}
		return features;
	}

	public static String shpToGeoJSONConverted(final String shpFilePath) throws IOException {
		return shpToGeoJSONConverted(shpFilePath, "ISO-8859-1", 0, -1);
	}

	public static String shpToGeoJSONConverted(final String shpFilePath, final long offSet, final long Count)
			throws IOException {
		return shpToGeoJSONConverted(shpFilePath, "ISO-8859-1", offSet, Count);
	}

	public static String shpToGeoJSONConverted(final String shpFilePath, final String charset, final long offSet,
			final long Count) throws IOException {
		File shp = new File(shpFilePath);
		if (!shp.exists()) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("url", shp.toURI().toURL());

		ShapefileDataStore dataStore = (ShapefileDataStore) DataStoreFinder.getDataStore(map);
		dataStore.setCharset(Charset.forName(charset));
		String typeName = dataStore.getTypeNames()[0];

		FeatureJSON fJSON = new FeatureJSON();
		FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
		Filter filter = Filter.INCLUDE;

		FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
		SimpleFeature[] features = changeCRS(dataStore, collection, charset);
		StringWriter writer = null;
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("{\"type\": \"FeatureCollection\",\"features\": [");
			long i = 0;
			long count = 0;
			for (int j = 0; j < features.length; j++) {
				if (i < offSet) {
					i++;
					continue;
				}
				SimpleFeature feature = features[j];
				if (++count < Count || Count < 0) {
					writer = new StringWriter();
					fJSON.writeFeature(feature, writer);
					String output = writer.toString();
					JSONObject obj = JSON.parseObject(output);
					JSONObjectUtil.convertJSON(obj);
					output = obj.toJSONString();
					sb.append(output);
					if (j != features.length - 1)
						sb.append(',');
				} else {
					writer = new StringWriter();
					fJSON.writeFeature(feature, writer);
					String output = writer.toString();
					JSONObject obj = JSON.parseObject(output);
					JSONObjectUtil.convertJSON(obj);
					output = obj.toJSONString();
					sb.append(output);
					break;
				}
			}
			sb.append("]}");
		} finally {
			dataStore.dispose();
		}
		return sb.toString();

	}

	public static void main(String[] args) {
		try {
			String tmp = shpToGeoJSON("D:" + File.separatorChar + "ExplorData" + File.separatorChar +"Subbasin.shp");
			File f = new File("D:" + File.separatorChar + "ExplorData" + File.separatorChar +"Subbasin.json");
			if (!f.exists())
				f.createNewFile();
			FileUtil.writeString(f, tmp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
