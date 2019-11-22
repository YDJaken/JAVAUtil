package com.dy.ImageUtil.GeoTiff;

import java.util.HashMap;

public class Config {
	private HashMap<String, Object> config = new HashMap<String, Object>();

	public void setConfig(String name, Object value) {
		config.put(name, value);
	}

	public Object getConfig(String name) {
		return config.get(name);
	}
	
	public Object removeConfig(String name) {
		return config.remove(name);
	}
}
