package com.dy.Util;

public class SystemStatus {

	public static String loadSystemOS() {
		return System.getProperties().getProperty("os.name");
	}
}
