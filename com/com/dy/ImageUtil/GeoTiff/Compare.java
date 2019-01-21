package com.dy.ImageUtil.GeoTiff;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.dy.ImageUtil.TiffUtil;

/**
 * 全局单位 m m^2 m^3 GeoTiff 影像对比
 * 
 * @author dy
 *
 */
public class Compare extends Config {

	// 对比模式
	static final boolean DEFAULT = true, ALL = true, SOME = false;

	private Rectangle rectangle = new Rectangle(
			new double[] { -Math.PI, -Coordinate.halfPI, Math.PI, Coordinate.halfPI });
	BufferedImage[] img1;
	BufferedImage[] img2;
	private Rectangle[] imgBound;
	private Thread[] subThreads;
	Integer threadCount = 0;
	private ProtectThread pt;
	

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
		setConfig("startWidth1", 0);
		// 图片2起始对比宽度位置
		setConfig("startWidth2", 0);
		// 图片1起始对比高度位置
		setConfig("startHeight1", 0);
		// 图片2起始对比高度位置
		setConfig("startHeight2", 0);
		// 每次对比的像素宽度
		setConfig("pixelWidth", 25);
		// 每次对比的像素高度
		setConfig("pixelHeight", 25);
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
		// 忽略面积 单位 m^2
		setConfig("ignoreArea", 5000);
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
		setConfig("imgWidth1", width);
		// 图片1高度
		setConfig("imgHeight1", height);
		// 图片2宽度
		setConfig("imgWidth2", width);
		// 图片2高度
		setConfig("imgHeight2", height);
		// 宽度像素变化对应的经度变化
		setConfig("longtitudeDelta", (rectangle.east - rectangle.west) / width);
		// 高度像素变化对应的纬度变化
		setConfig("latitudeDelta", (rectangle.north - rectangle.south) / height);
		double realWorldWidth = Coordinate.computeDistance(rectangle.west, rectangle.south, rectangle.west,
				rectangle.north)[0];
		double realWorldHeight = Coordinate.computeDistance(rectangle.west, rectangle.south, rectangle.east,
				rectangle.south)[0];
		// 宽度像素变化对应的现实世界的米数
		setConfig("realWorldWidthDelta", realWorldWidth / width);
		// 宽度像素变化对应的现实世界的米数
		setConfig("realWorldHeightDelta", realWorldHeight / height);
	}

	public String toString() {
		if (imgBound == null) {
			return "";
		} else {
			StringBuilder a = new StringBuilder();
			a.append("{\"data\":[\n");
			for (int i = 0; i < imgBound.length; i++) {
				if (imgBound[i] == null)
					continue;
				a.append("[");
				double[] target = Rectangle.pack(imgBound[i]);
				for (int j = 0; j < target.length; j++) {
					a.append(target[j]);
					if (j != target.length - 1) {
						a.append(",");
					}
				}
				if (i == imgBound.length - 1) {
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
		int pixelWidth = (int) getConfig("pixelWidth");
		int pixelHeight = (int) getConfig("pixelHeight");
		int width1 = (int) Math.ceil(((int) getConfig("imgWidth1") - (int) getConfig("startWidth1")) / pixelWidth);
		int width2 = (int) Math.ceil(((int) getConfig("imgWidth2") - (int) getConfig("startWidth2")) / pixelWidth);
		if (width1 != width2) {
			throw new IllegalStateException("图片参数无法对齐");
		}
		int height1 = (int) Math.ceil(((int) getConfig("imgHeight1") - (int) getConfig("startHeight1")) / pixelHeight);
		int height2 = (int) Math.ceil(((int) getConfig("imgHeight2") - (int) getConfig("startHeight2")) / pixelHeight);
		if (height1 != height2) {
			throw new IllegalStateException("图片参数无法对齐");
		}
		// 生成切分矩阵的列数
		setConfig("compareColumn", width1);
		// 生成切分矩阵的行数
		setConfig("compareRow", height1);
		double areaPerElement = ((double) getConfig("realWorldWidthDelta") * pixelWidth)
				* ((double) getConfig("realWorldHeightDelta") * pixelHeight);
		// 忽略的单元格子数
		setConfig("ignoreElementCount",(int)Math.round((int)getConfig("ignoreArea")/areaPerElement));
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
			Process p = new Process(imgBound,(int)getConfig("compareRow"),(int)getConfig("compareColumn"),this);
			p.merge();
			if ((boolean) getConfig("needTransform")) {
				transformCoordinate();
			} else {
				return imgBound;
			}
			return imgBound;
		}
	}

	// 再次激活守护线程
	public void reProtect() {
		if (threadCount >= 0) {
			pt = new ProtectThread(subThreads, this);
			pt.start();
		}
	}

	public Compare() {
	}
	
	public Compare(double[] rectangle, String imgURL1, String imgURL2) {
		this.rectangle = new Rectangle(reform(rectangle), true);
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
//			Rectangle[] bs = 
					a.compare();
//			if (bs[0] == null) {
//				System.out.println("null");
//			}
//			System.out.println(a.toString());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
