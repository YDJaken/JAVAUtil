package com.dy.Util;

import java.lang.reflect.Array;

import com.dy.ImageUtil.GeoTiff.Rectangle;

public class MatrixUtil {
	
	/**
	 * 将二维矩阵转换为一维数组或者将一维数组转换为二维矩阵
	 * 
	 * @param objs 传入的矩阵
	 * @param r    需要转换的row
	 * @param c    需要转换的column
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[][] matrixReshape(T[][] objs,Class<?> cla, int r, int c) {
		T[][] res = (T[][]) Array.newInstance(cla, new int[] {r,c});
		if (objs.length == 0 || r * c != objs.length * objs[0].length)
			return objs;
		int count = 0;
		for (int i = 0; i < objs.length; i++) {
			for (int j = 0; j < objs[0].length; j++) {	
				res[count / c][count % c] = objs[i][j];
				count++;
			}
		}
		return (T[][])res;
	}

	/**
	 * 二维矩阵转换为String
	 * 
	 * @param objs
	 * @return
	 */
	public static String toString(Object[][] objs) {
		StringBuilder b = new StringBuilder();
		b.append('[');
		for (int i = 0; i < objs.length; i++) {
			b.append('[');
			for (int j = 0; j < objs[i].length; j++) {
				b.append(objs[i][j].toString());
				if (j != objs[i].length - 1) {
					b.append(',');
				}
			}
			b.append(']');
			if (i != objs.length - 1) {
				b.append(',');
			}
		}
		b.append(']');
		return b.toString();
	}

	public static void main(String[] args) {
		Double[][] a = { { 1.0, 20.0 }, { 30.0, 40.0 },{5.0,10.0},{10.0,20.0}};
		System.out.println(MatrixUtil.toString(MatrixUtil.matrixReshape(a,Double.class, 4, 1)));
		Rectangle[][] b = {{new Rectangle(),new Rectangle(1.0,2.0,3.0,4.0)},{new Rectangle(),new Rectangle(1.0,2.0,3.0,4.0)}};
		System.out.println(MatrixUtil.toString(MatrixUtil.matrixReshape(b,Rectangle.class, 1, 4)));
	}
}
