package com.dy.ImageUtil.GeoTiff;

import com.dy.Util.MatrixUtil;

public class Process {
	
	private final Rectangle[][] matrix;
	
	Process(Rectangle[][] matrix){
		this.matrix = matrix;
	}
	
	Process(Rectangle[] rec,int row,int column){
		this((Rectangle[][])MatrixUtil.matrixReshape(MatrixUtil.reformate(rec, Rectangle.class), Rectangle.class, row , column));
	}
	
	private void merge() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
					if(matrix[i][j] == null || matrix[i][j].getConfig("bounder") == null) {
						continue;
					}else {
						
					}
				}
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
		as = p.matrix;
		StringBuilder ass = new StringBuilder();
		ass.append("--------------合并前-----------------\n");
		for (int i = 0; i < as.length; i++) {
			for (int j = 0; j < as[i].length; j++) {
				if (as[i][j] == null) {
					ass.append('*');
				} else {
					ass.append('1');
				}
				if(j != as[i].length-1) {
					ass.append(',');
				}
			}
			ass.append("\n");
		}
		ass.append("--------------合并前-----------------\n");
		System.out.println(ass.toString());

	}
}
