package com.dy.Util;

import java.lang.reflect.Array;

public class MatrixUtil {

	/**
	 * 随机生成一个矩阵
	 * 
	 * @param obj
	 * @param cla
	 * @param defalueValue
	 * @return
	 */
	public static <T> T[][] generateMatrix(T[][] obj, Class<?> cla, T defalueValue) {
		for (int i = 0; i < obj.length; i++) {
			for (int j = 0; j < obj[i].length; j++) {
				if ((int) Math.round(Math.random()) == 0) {
					obj[i][j] = null;
				} else {
					obj[i][j] = defalueValue;
				}
			}
		}
		return obj;
	}

	/**
	 * 将一维矩阵转换为二维矩阵
	 * 
	 * @param obj
	 * @param cla
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[][] reformate(T[] obj, Class<?> cla) {
		T[][] res = (T[][]) Array.newInstance(cla, new int[] { 1, obj.length });
		res[0] = obj;
		return res;
	}

	/**
	 * 将二维矩阵行列数进行任意转换
	 * 
	 * @param objs 传入的矩阵
	 * @param r    需要转换的row
	 * @param c    需要转换的column
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[][] matrixReshape(T[][] objs, Class<?> cla, int r, int c) {
		T[][] res = (T[][]) Array.newInstance(cla, new int[] { r, c });
		if (objs.length == 0 || r * c != objs.length * objs[0].length)
			return objs;
		int count = 0;
		for (int i = 0; i < objs.length; i++) {
			for (int j = 0; j < objs[0].length; j++) {
				res[count / c][count % c] = objs[i][j];
				count++;
			}
		}
		return (T[][]) res;
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
				if (objs[i][j] != null) {
					b.append(objs[i][j].toString());
				} else {
					b.append("null");
				}
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
}
