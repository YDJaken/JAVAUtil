package com.dy.ImageUtil.GeoTiff;

import com.dy.Util.MatrixUtil;

public class Process {

	private final Rectangle[][] matrix;

	Process(Rectangle[][] matrix) {
		this.matrix = matrix;
	}

	Process(Rectangle[] rec, int row, int column) {
		this((Rectangle[][]) MatrixUtil.matrixReshape(MatrixUtil.reformate(rec, Rectangle.class), Rectangle.class, row,
				column));
	}

	private void merge() {
		StringBuilder ass = new StringBuilder();
		ass.append("--------------合并前-----------------\n");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == null) {
					ass.append('*');
				} else {
					matrix[i][j] = new Rectangle();
					ass.append('1');
				}
				if (j != matrix[i].length - 1) {
					ass.append(',');
				}
			}
			ass.append("\n");
		}
		ass.append("--------------合并前-----------------\n");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == null) {
					continue;
				} else {
					findBorder(i, j);
				}
			}
		}
		ass.append("--------------合并中-----------------\n");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == null) {
					ass.append('*');
				} else {
					boolean bol = (boolean) (matrix[i][j].getConfig("bounder"));
					if (!bol)
						ass.append('-');
					else
						ass.append('1');
				}
				if (j != matrix[i].length - 1) {
					ass.append(',');
				}
			}
			ass.append("\n");
		}
		ass.append("--------------合并中-----------------\n");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] != null) {
					boolean bol = (boolean) (matrix[i][j].getConfig("bounder"));
					if (!bol) {
						matrix[i][j] = null;
					}
				}
			}
		}
		ass.append("--------------合并后-----------------\n");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == null) {
					ass.append('*');
				} else {
					ass.append('1');
				}
				if (j != matrix[i].length - 1) {
					ass.append(',');
				}
			}
			ass.append("\n");
		}
		ass.append("--------------合并后-----------------\n");
		System.out.println(ass.toString());
	}

	/**
	 * 
	 * @param i 行号
	 * @param j 列号
	 */
	private void findBorder(int i, int j) {
		short finnded = 0;
		try {
			Rectangle top = matrix[i - 1][j];
			if (top != null)
				finnded++;
		} catch (IndexOutOfBoundsException e) {
//			finnded++;
		}
		try {
			Rectangle bottom = matrix[i + 1][j];
			if (bottom != null)
				finnded++;
		} catch (IndexOutOfBoundsException e) {
//			finnded++;
		}
		try {
			Rectangle left = matrix[i][j - 1];
			if (left != null)
				finnded++;
		} catch (IndexOutOfBoundsException e) {
//			finnded++;
		}
		try {
			Rectangle right = matrix[i][j + 1];
			if (right != null)
				finnded++;
		} catch (IndexOutOfBoundsException e) {
//			finnded++;
		}
		if (finnded == 4) {
			matrix[i][j].setConfig("bounder", false);
		} else {
			matrix[i][j].setConfig("bounder", true);
		}
	}

	static Rectangle[] join(Rectangle[] origin, Rectangle[] input) {
		for (int i = 0; i < origin.length; i++) {
			if (origin[i] == null && input[i] != null) {
				origin[i] = input[i];
			}
		}
		return origin;
	}

	public static void main(String[] args) {
		Rectangle[][] as = new Rectangle[20][20];
		as = (Rectangle[][]) MatrixUtil.generateMatrix(as, Rectangle.class, new Rectangle());
		Process p = new Process(as);
		p.merge();
	}
}
