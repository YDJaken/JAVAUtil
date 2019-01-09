package com.dy.ImageUtil.GeoTiff;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import com.dy.ImageUtil.TiffUtil;

public class Compare {

	private final double halfPI = Math.PI / 2.0;

	private double[] rectangle = { -Math.PI, -halfPI, Math.PI, halfPI };
	private BufferedImage[] img1;
	private BufferedImage[] img2;
	private double[][] result = null;
	private HashMap<String, Object> config = new HashMap<String, Object>();

	public void setConfig(String name, Object value) {
		config.put(name, value);
	}

	public Object getConfig(String name) {
		return config.get(name);
	}

	public double[] getRectangle() {
		return rectangle;
	}

	public void setRectangle(double[] rectangle) {
		this.rectangle = rectangle;
	}

	private double[] reform(double[] input) {
		if (input.length != 4) {
			return new double[] { -Math.PI, -halfPI, Math.PI, halfPI };
		}
		if (input[0] > input[2]) {
			double tmp = input[0];
			input[0] = input[2];
			input[2] = tmp;
		}
		if (input[1] > input[3]) {
			double tmp = input[1];
			input[1] = input[3];
			input[3] = tmp;
		}
		return input;
	}

	private void defaultSetting() {
		// 每次对比的像素数
		setConfig("pixelSize", 25);
		// 差异率阈值
		setConfig("flagRadio", 50.0);
		// 设置最大线程数
		setConfig("maxThreadPool", 10);
	}

	private void init(String imgURL1, String imgURL2) throws IOException {
		img1 = TiffUtil.loadTiff(imgURL1);
		img2 = TiffUtil.loadTiff(imgURL2);
		if (img1 == null || img2 == null|| img1.length != img2.length) {
			throw new IllegalStateException();
		}
		int width = Math.min(img1[0].getWidth(), img2[0].getWidth()),
				height = Math.min(img1[0].getHeight(), img2[0].getWidth());
		this.setConfig("imgWith", width);
		this.setConfig("imgHeight", height);
		this.setConfig("longtitudeDelta", (rectangle[2] - rectangle[0]) / width);
		this.setConfig("latitudeDelta", (rectangle[3] - rectangle[1]) / height);
	}

	public String toString() {
		if (result == null) {
			return "";
		} else {
			StringBuilder a = new StringBuilder();
			a.append("{\"data\":[\n");
			for (int i = 0; i < result.length; i++) {
				a.append("[");
				for (int j = 0; j < result[i].length; j++) {
					a.append(result[i][j]);
					if (j != result[i].length - 1) {
						a.append(",");
					}
				}
				if (i == result.length - 1) {
					a.append("]");
				} else {
					a.append("],\n");
				}
			}
			a.append("]}");
			return a.toString();
		}
	}

	private double[][] analysis() {
		return new double[1][1];
	}

	public double[][] compare() {
		result = analysis();
		return result;
	}

	public Compare(double[] rectangle, String imgURL1, String imgURL2) {
		this.rectangle = reform(rectangle);
		try {
			init(imgURL1, imgURL2);
		} catch (IOException e) {
			System.out.println(imgURL1 + "文件未找到");
		} catch (IllegalStateException e) {
			System.out.println("传入图片文件不匹配");
		}
	}

	public Compare(double longtitude1, double latitude1, double longtitude2, double latitude2, String imgURL1,
			String imgURL2) {
		this.rectangle = reform(new double[] { longtitude1, latitude1, longtitude2, latitude2 });
		try {
			init(imgURL1, imgURL2);
		} catch (IOException e) {
			System.out.println(imgURL1 + "文件未找到");
		} catch (IllegalStateException e) {
			System.out.println("传入图片文件不匹配");
		}
	}

	public static void main(String[] args) {
		Compare a = new Compare(45.0769349657265, 90.82141185464081, 47.032121099508096, 93.97137099423514,
				"/data/DownLoad/001.tif", "/data/DownLoad/002.tif");
		a.setConfig("pixelSize", 10);
		a.setConfig("pixelSize", 100);
		System.out.println(a.getConfig("pixelSize"));
	}
}
