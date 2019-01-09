package com.dy.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLRoutingUtil {
	private static String[] commonFileExtends = { "zip", "shp", "txt", "mp3", "pbf", "pdf", "md5", "bz2", "kml", "html",
			"poly","jpg","jpeg","png","svn","gz","osc"};

	public static boolean isCommonFile(String url) {
		for (int i = 0; i < commonFileExtends.length; i++) {
			if (url.lastIndexOf(commonFileExtends[i]) != -1)
				return true;
			if (url.lastIndexOf(commonFileExtends[i].toUpperCase()) != -1)
				return true;
		}
		return false;
	}
	
	public static String encodeURL(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
