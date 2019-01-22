package com.dy.ImageUtil.GeoTiff;

import java.awt.image.BufferedImage;

import com.dy.Util.MatrixUtil;

public class SubCompareThread extends Thread {

	private final Rectangle[][] matrix;
	private final BufferedImage img1;
	private final BufferedImage img2;
	private final Compare fatherThread;
	private boolean stopFlag = false;
	private boolean stillRuning = true;
	private final int pixelWidth;
	private final int pixelHeight;

	SubCompareThread(BufferedImage img1, BufferedImage img2, Compare fatherThread) {
		this.img1 = img1;
		this.img2 = img2;
		this.fatherThread = fatherThread;
		this.pixelWidth = (int) fatherThread.getConfig("pixelWidth");
		this.pixelHeight = (int) fatherThread.getConfig("pixelHeight");
		this.matrix = generateMatrix();
	}

	private Rectangle[][] generateMatrix() {
		Rectangle[][] ret = new Rectangle[(int) fatherThread.getConfig("compareRow")][(int) fatherThread
				.getConfig("compareColumn")];
		for (int i = 0; i < ret.length - 1; i++) {
			for (int j = 0; j < ret[i].length - 1; j++) {
				ret[i][j] = new Rectangle((int)(j * pixelWidth), (int)(i * pixelHeight),(int)((j + 1) * pixelWidth), (int)((i + 1) * pixelHeight));
			}
		}
		return ret;
	}

	private boolean compareRec(Rectangle rec) {
		int pixsize = pixelHeight * pixelWidth;
		int[] ImageOneArray = new int[pixsize];
		ImageOneArray = img1.getRGB((int) rec.west, (int) rec.south, pixelWidth, pixelHeight, ImageOneArray, 0,
				pixelWidth);
		int[] ImageTwoArray = new int[pixsize];
		ImageTwoArray = img2.getRGB((int) rec.west, (int) rec.south, pixelWidth, pixelHeight, ImageTwoArray, 0,
				pixelWidth);
		int count = 0;
		for (int i = 0; i < pixsize; i++) {
			if (stopFlag)
				return false;
			if (ImageOneArray[i] != ImageTwoArray[i]) {
				count++;
			}
		}
		return (count / pixsize) * 100.0 > (double) fatherThread.getConfig("flagRadio");
	}

	public boolean stillRun() {
		return this.stillRuning;
	}

	public void setFlag(boolean input) {
		this.stopFlag = input;
	}

	@Override
	public void run() {
		super.run();
		stillRuning = true;
		for (int i = 0; i < matrix.length - 1; i++) {
			for (int j = 0; j < matrix[i].length - 1; j++) {
				if (stopFlag)
					return;
				if (!compareRec(matrix[i][j]))
					matrix[i][j] = null;
			}
		}
		synchronized (fatherThread) {
			fatherThread.processRectangle(reformatMatrix(matrix));
			stillRuning = false;
			fatherThread.threadCount = fatherThread.threadCount - 1;
			if (fatherThread.threadCount == 0)
				fatherThread.notify();
		}
	}

	private Rectangle[] reformatMatrix(Rectangle[][] matrix2) {
		return (Rectangle[]) (MatrixUtil.matrixReshape(matrix2, Rectangle.class, 1,
				matrix2.length * matrix2[0].length)[0]);
	}
}
