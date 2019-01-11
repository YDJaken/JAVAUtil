package com.dy.ImageUtil.GeoTiff;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import com.dy.ImageUtil.TiffUtil;

public class Compare {

	static final boolean DEFAULT = true, ALL = true, SOME = false;

	private Rectangle rectangle = new Rectangle(
			new double[] { -Math.PI, -Coordinate.halfPI, Math.PI, Coordinate.halfPI });
	private BufferedImage[] img1;
	private BufferedImage[] img2;
	private Rectangle[] imgBound;
	private Rectangle[] result;
	private HashMap<String, Object> config = new HashMap<String, Object>();
	private Thread[] subThreads;
	Integer threadCount = 0;
	private ProtectThread pt;

	public void setConfig(String name, Object value) {
		config.put(name, value);
	}

	public Object getConfig(String name) {
		return config.get(name);
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	private double[] reform(double[] input) {
		if (input.length != 4) {
			return new double[] { -Math.PI, -Coordinate.halfPI, Math.PI, Coordinate.halfPI };
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
		// 图片1起始对比宽度位置
		setConfig("startWith1", 0);
		// 图片2起始对比宽度位置
		setConfig("startWith2", 0);
		// 图片1起始对比高度位置
		setConfig("startHeight1", 0);
		// 图片2起始对比高度位置
		setConfig("startHeight2", 0);
		// 每次对比的像素数
		setConfig("pixelSize", 25);
		// 差异率阈值 (不同数大于此阈值算做有区别)
		setConfig("flagRadio", 50.0);
		// 设置最大线程数
		setConfig("maxThreadPool", 10);
		// 设置检查模式 (默认为全部检查，可以指定为Compare.SOME 用于检测某一页的异同)
		setConfig("compareMode", Compare.DEFAULT);
		// 设置对应页数 (只有在模式为Compare.SOME时启用) 传入页数index : 1,2,3,4
		setConfig("comparePage", "");
		// 当前线程达到的Index(如果总线程数大于页数时此值总为0)
		setConfig("currentIndex", 0);
		// 是否需要将图片坐标转为传入坐标
		setConfig("needTransform", false);
	}

	private void init(String imgURL1, String imgURL2) throws IOException {
		img1 = TiffUtil.loadTiff(imgURL1);
		img2 = TiffUtil.loadTiff(imgURL2);
		if (img1 == null || img2 == null || img1.length != img2.length || img1.length == 0) {
			throw new IllegalStateException();
		}
		int width = Math.min(img1[0].getWidth(), img2[0].getWidth()),
				height = Math.min(img1[0].getHeight(), img2[0].getWidth());
		// 图片1宽度
		setConfig("imgWith1", width);
		// 图片1高度
		setConfig("imgHeight1", height);
		// 图片2宽度
		setConfig("imgWith2", width);
		// 图片2高度
		setConfig("imgHeight2", height);
		setConfig("longtitudeDelta", (rectangle.east - rectangle.west) / width);
		setConfig("latitudeDelta", (rectangle.north - rectangle.south) / height);
	}

	public String toString() {
		if (result == null) {
			return "";
		} else {
			StringBuilder a = new StringBuilder();
			a.append("{\"data\":[\n");
			for (int i = 0; i < result.length; i++) {
				if (result[i] == null)
					continue;
				a.append("[");
				double[] target = Rectangle.pack(result[i]);
				for (int j = 0; j < target.length; j++) {
					a.append(target[j]);
					if (j != target.length - 1) {
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

	/**
	 * 子线程合并入口
	 */
	synchronized void processRectangle(Rectangle[] aim) {
		if (imgBound == null) {
			imgBound = aim;
		} else {
			imgBound = Process.join(imgBound, aim);
		}
	}

	// 开启线程和持续计算
	private void startProcess(int currentIndex) throws InterruptedException {
		boolean needGo = false;
		if ((boolean) getConfig("compareMode") == Compare.ALL) {
			for (int i = currentIndex; i < img1.length; i++) {
				if (threadCount == subThreads.length) {
					setConfig("currentIndex", i);
					needGo = true;
					break;
				}
				subThreads[threadCount] = new SubCompareThread(img1[i], img2[i], this);
				threadCount++;
			}
		} else {
			String[] target = ((String) getConfig("comparePage")).split(",");
			for (int i = currentIndex; i < target.length; i++) {
				if (threadCount == subThreads.length) {
					setConfig("currentIndex", i);
					needGo = true;
					break;
				}
				subThreads[threadCount] = new SubCompareThread(img1[Integer.parseInt(target[i])],
						img2[Integer.parseInt(target[i])], this);
				threadCount++;
			}
		}
		if (pt == null) {
			pt = new ProtectThread(subThreads, this);
			pt.start();
		}
		synchronized (this) {
			if (threadCount > 0) {
				wait();
				if (needGo) {
					startProcess((int) getConfig("currentIndex"));
				}
			}
		}

	}

	// 将图片坐标的矩阵转换为经纬度或者传入坐标系
	private void transformCoordinate() {

	}

	// 计算图片被切分的矩阵行列数
	private void computeMatrixSize() {
		int width1 = (int) Math
				.ceil(((int) getConfig("imgWith1") - (int) getConfig("startWith1")) / (int) getConfig("pixelSize"));
		int width2 = (int) Math
				.ceil(((int) getConfig("imgWith2") - (int) getConfig("startWith2")) / (int) getConfig("pixelSize"));
		if (width1 != width2) {
			throw new IllegalStateException("图片参数无法对齐");
		}
		int height1 = (int) getConfig("imgHeight1") - (int) getConfig("startHeight1");
		int height2 = (int) getConfig("imgHeight2") - (int) getConfig("startHeight2");
		if (height1 != height2) {
			throw new IllegalStateException("图片参数无法对齐");
		}
		// 生成切分矩阵的列数
		setConfig("compareColumn", width1);
		// 生成切分矩阵的行数
		setConfig("compareRow", height1);
	}

	/**
	 * 对比两张tiff
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public Rectangle[] compare() throws InterruptedException {
		computeMatrixSize();
		subThreads = new Thread[(int) getConfig("maxThreadPool")];
		startProcess((int) getConfig("currentIndex"));
		synchronized (this) {
			if (threadCount > 0) {
				wait();
			}
			if ((boolean) getConfig("needTransform")) {
				transformCoordinate();
			} else {
				result = imgBound;
				return imgBound;
			}
			return result;
		}
	}

	// 再次激活守护线程
	public void reProtect() {
		if (threadCount >= 0) {
			pt = new ProtectThread(subThreads, this);
			pt.start();
		}
	}

	public Compare(double[] rectangle, String imgURL1, String imgURL2) {
		this.rectangle = new Rectangle(reform(rectangle));
		defaultSetting();
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
		this(new double[] { longtitude1, latitude1, longtitude2, latitude2 }, imgURL1, imgURL2);
	}

	public static void main(String[] args) {
		Compare a = new Compare(45.0769349657265, 90.82141185464081, 47.032121099508096, 93.97137099423514,
				"/data/DownLoad/001.tif", "/data/DownLoad/002.tif");
		try {
			a.compare();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
