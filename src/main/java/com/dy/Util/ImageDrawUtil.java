package com.dy.Util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.dy.Util.Math.TwoD.Point;
import com.dy.Util.Math.TwoD.Polygon;
import com.dy.Util.Math.TwoD.Rectangle;
public class ImageDrawUtil {

	private static final int RED = -65536;

	public static BufferedImage drawPolygonOutline(BufferedImage target, Polygon pol) {
		return ImageDrawUtil.drawPolygonOutline(target, pol, RED);
	}

	public static BufferedImage drawPolygonOutline(BufferedImage target, Polygon pol, String colorStr) {
		return ImageDrawUtil.drawPolygonOutline(target, pol, Color.decode(colorStr).getRGB());
	}

	/**
	 * 绘制多边形边框
	 * 
	 * @param target
	 * @param pol
	 * @param color
	 */
	public static BufferedImage drawPolygonOutline(BufferedImage target, Polygon pol, int color) {
		for (int i = 0; i < pol.position.length; i++) {
			if (i == pol.position.length - 1) {
				target = ImageDrawUtil.drawLine(target, pol.position[0], pol.position[i], color);
			} else {
				target = ImageDrawUtil.drawLine(target, pol.position[i], pol.position[i + 1], color);
			}
		}
		return target;
	}

	public static BufferedImage drawRectangleOutline(BufferedImage target, Rectangle rec) {
		return ImageDrawUtil.drawRectangleOutline(target, rec, RED);
	}

	public static BufferedImage drawRectangleOutline(BufferedImage target, Rectangle rec, String colorStr) {
		return ImageDrawUtil.drawRectangleOutline(target, rec, Color.decode(colorStr).getRGB());
	}

	/**
	 * 绘制矩形边框
	 * 
	 * @param target
	 * @param rec
	 * @param color
	 */
	public static BufferedImage drawRectangleOutline(BufferedImage target, Rectangle rec, int color) {
		Point[] arr = rec.toPointArray();
		target = ImageDrawUtil.drawLine(target, arr[0], arr[1], color);
		target = ImageDrawUtil.drawLine(target, arr[1], arr[2], color);
		target = ImageDrawUtil.drawLine(target, arr[2], arr[3], color);
		target = ImageDrawUtil.drawLine(target, arr[0], arr[3], color);
		return target;
	}

	public static BufferedImage drawLine(BufferedImage target, Point start, Point end) {
		return ImageDrawUtil.drawLine(target, start, end, RED);
	}

	public static BufferedImage drawLine(BufferedImage target, Point start, Point end, String colorStr) {
		return ImageDrawUtil.drawLine(target, start, end, Color.decode(colorStr).getRGB());
	}

	/**
	 * 绘制直线 (正直线,非斜线)
	 * 
	 * @param target
	 * @param start
	 * @param end
	 * @param color
	 */
	public static BufferedImage drawLine(BufferedImage target, Point start, Point end, int color) {
		int width = (int) (end.x - start.x), height = (int) (end.y - start.y), originHeight = target.getHeight(),
				originWidth = target.getWidth();
		if (width < 0) {
			start = end;
			width = Math.abs(width) + 1;
		} else {
			width += 1;
		}
		if (height < 0) {
			start = end;
			height = Math.abs(height) + 1;
		} else {
			height += 1;
		}
		int type = target.getType();
		int[] ImageArrayOne = new int[width * height];
		for (int i = 0; i < ImageArrayOne.length; i++) {
			ImageArrayOne[i] = color;
		}
		if (type != BufferedImage.TYPE_INT_ARGB) {
			BufferedImage tmp = new BufferedImage(originWidth, originHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) tmp.getGraphics();
			g.drawImage(target, 0, 0, originWidth, originHeight, null);
			tmp.setRGB((int) start.x, (int) start.y, width, height, ImageArrayOne, 0, width);
			return tmp;
		}
		target.setRGB((int) start.x, (int) start.y, width, height, ImageArrayOne, 0, width);
		return target;
	}
}
