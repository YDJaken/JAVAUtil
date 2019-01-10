package com.dy.ImageUtil.GeoTiff;

import java.awt.image.BufferedImage;

import com.dy.Util.MatrixUtil;

public class SubCompareThread extends Thread {
	SubCompareThread(BufferedImage img1, BufferedImage img2, Compare fatherThread) {
		this.img1 = img1;
		this.img2 = img2;
		this.fatherThread = fatherThread;
		this.matrix = generateMatrix();
	}

	private Rectangle[][] generateMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

	private final Rectangle[][] matrix;
	private final BufferedImage img1;
	private final BufferedImage img2;
	private final Compare fatherThread;
	private boolean stopFlag = false;
	private boolean stillRuning = false;

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

		fatherThread.processRectangle(reformatMatrix(matrix));
		fatherThread.threadCount = fatherThread.threadCount - 1;
		fatherThread.notify();
		stillRuning = false;
	}

	private Rectangle[] reformatMatrix(Rectangle[][] matrix2) {
		return (Rectangle[]) (MatrixUtil.matrixReshape(matrix2, 1, matrix2.length * matrix2[0].length)[0]);
	}
}
