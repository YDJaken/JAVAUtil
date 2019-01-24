package com.dy.ImageUtil.GeoTiff;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.Stack;

import org.junit.jupiter.api.Test;

import com.dy.ImageUtil.TiffUtil;

class PolygonTester {

	// 多边形合并
	@Test
	void test() {
		Polygon p2 = new Polygon(new double[] { 1400, 175, 1425, 175, 1425, 200, 1700, 200, 1700, 225, 1400, 225 });
		Polygon p3 = new Polygon(new double[] { 1475, 175, 1575, 175, 1575, 200, 1700, 200, 1700, 225, 1400, 225, 1400,
				200, 1475, 200 });
		assertEquals(Polygon.simpleIntersection(p2, p3), true);
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0], origin2 = origin, origin3 = origin;
		String colorStr = Process.color[2];
		origin = ImageDrawUtil.drawPolygonOutline(origin, p2, colorStr);
		origin2 = ImageDrawUtil.drawPolygonOutline(origin2, p3, colorStr);
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testAPolygon1");
		TiffUtil.saveTif(origin2, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testAPolygon2");
		Polygon p1 = Process.mergePolygon(p2, p3, new Rectangle(1400, 200, 1700, 225));
		Point[] result = Point.fromArray(new double[] { 1400, 175, 1425, 175, 1425, 200, 1475, 200, 1475, 175, 1575,
				175, 1575, 200, 1700, 200, 1700, 225, 1400, 225 });
		assertEquals(result.length == p1.position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(p1.position[i].equals(result[i]), true);
		}
		origin3 = ImageDrawUtil.drawPolygonOutline(origin3, p1, colorStr);
		TiffUtil.saveTif(origin3, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testAPolygon3");
	}

	// 多边形合并2
	@Test
	void test2() {
		Stack<Rectangle> tmp = new Stack<>();
		tmp.push(new Rectangle(1000, 2500, 1100, 2525));
		tmp.push(new Rectangle(975, 2525, 1075, 2550));
		tmp.push(new Rectangle(975, 2550, 1100, 2575));
		tmp.push(new Rectangle(1000, 2575, 1100, 2600));
		tmp.push(new Rectangle(1000, 2600, 1025, 2625));
		tmp.push(new Rectangle(1050, 2600, 1125, 2625));
		tmp.push(new Rectangle(1025, 2625, 1100, 2650));
		tmp.push(new Rectangle(1125, 2625, 1150, 2650));
		tmp.push(new Rectangle(1075, 2650, 1150, 2675));
		tmp.push(new Rectangle(1100, 2675, 1150, 2700));
		tmp.push(new Rectangle(1125, 2700, 1150, 2725));
		Stack<Polygon> ps = Process.transformPolygon(tmp);
		Point[] result = Point.fromArray(new double[] { 1000.0, 2500.0, 1100.0, 2500.0, 1100.0, 2525.0, 1075.0, 2525.0,
				1075.0, 2550.0, 1100.0, 2550.0, 1100.0, 2600.0, 1125.0, 2600.0, 1125.0, 2625.0, 1100.0, 2625.0, 1100.0,
				2650.0, 1125.0, 2650.0, 1125.0, 2625.0, 1150.0, 2625.0, 1150.0, 2725.0, 1125.0, 2725.0, 1125.0, 2700.0,
				1100.0, 2700.0, 1100.0, 2675.0, 1075.0, 2675.0, 1075.0, 2650.0, 1025.0, 2650.0, 1025.0, 2625.0, 1000.0,
				2625.0, 1000.0, 2575.0, 975.0, 2575.0, 975.0, 2525.0, 1000.0, 2525.0 });
		assertEquals(result.length == ps.get(0).position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(ps.get(0).position[i].equals(result[i]), true);
		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		for (int i = 0; i < ps.size(); i++) {
			origin = ImageDrawUtil.drawPolygonOutline(origin, ps.get(0), colorStr);
		}
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testAPolygon4");
	}

	// 多边形合并3
	@Test
	void test3() {
		Stack<Rectangle> tmp = new Stack<>();
		tmp.push(new Rectangle(3150,525,3175,550));
		tmp.push(new Rectangle(3150,550,3200,575));
		tmp.push(new Rectangle(3150,575,3200,600));
		tmp.push(new Rectangle(3150,600,3225,625));
		tmp.push(new Rectangle(3200,625,3225,650));
		tmp.push(new Rectangle(3075,650,3100,675));
		tmp.push(new Rectangle(3150,650,3250,675));
		tmp.push(new Rectangle(3075,675,3200,700));
		tmp.push(new Rectangle(3225,675,3250,700));
		tmp.push(new Rectangle(3075,700,3100,725));
		tmp.push(new Rectangle(3125,700,3150,725));
		tmp.push(new Rectangle(3175,700,3200,725));
		tmp.push(new Rectangle(3225,700,3275,725));
		tmp.push(new Rectangle(3175,725,3225,750));
		tmp.push(new Rectangle(3250,725,3300,750));
		tmp.push(new Rectangle(3125,750,3200,775));
		tmp.push(new Rectangle(3250,750,3325,775));
		tmp.push(new Rectangle(3300,775,3325,800));
		Stack<Polygon> ps = Process.transformPolygon(tmp);
//		Point[] result = Point.fromArray(new double[] { 1000.0, 2500.0, 1100.0, 2500.0, 1100.0, 2525.0, 1075.0, 2525.0,
//				1075.0, 2550.0, 1100.0, 2550.0, 1100.0, 2600.0, 1125.0, 2600.0, 1125.0, 2625.0, 1100.0, 2625.0, 1100.0,
//				2650.0, 1125.0, 2650.0, 1125.0, 2625.0, 1150.0, 2625.0, 1150.0, 2725.0, 1125.0, 2725.0, 1125.0, 2700.0,
//				1100.0, 2700.0, 1100.0, 2675.0, 1075.0, 2675.0, 1075.0, 2650.0, 1025.0, 2650.0, 1025.0, 2625.0, 1000.0,
//				2625.0, 1000.0, 2575.0, 975.0, 2575.0, 975.0, 2525.0, 1000.0, 2525.0 });
//		assertEquals(result.length == ps.get(0).position.length, true);
//		for (int i = 0; i < result.length; i++) {
//			assertEquals(ps.get(0).position[i].equals(result[i]), true);
//		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		for (int i = 0; i < ps.size(); i++) {
			origin = ImageDrawUtil.drawPolygonOutline(origin, ps.get(0), colorStr);
		}
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testAPolygon5");
	}
}
