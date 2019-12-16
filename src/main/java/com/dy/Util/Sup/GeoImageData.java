package com.dy.Util.Sup;

public class GeoImageData {
	private double longtitude;
	private double latitude;
	private Object data;

	public GeoImageData() {
		
	}
	
	public GeoImageData(double longtitude, double latitude) {
		this(longtitude, latitude, null);
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public GeoImageData(double longtitude, double latitude, Object data) {
		this.longtitude = longtitude;
		this.latitude = latitude;
		this.data = data;
	}
	
	public void destory() {
		this.data = null;
	}

	@Override
	public String toString() {
		return "GeoImageData [longtitude=" + longtitude + ", latitude=" + latitude + ", data=" + data + "]";
	}
}
