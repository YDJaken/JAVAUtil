package com.dy.ImageUtil.GeoTiff.test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.Stack;

import org.junit.jupiter.api.Test;

import com.dy.ImageUtil.TiffUtil;
import com.dy.ImageUtil.GeoTiff.ImageDrawUtil;
import com.dy.ImageUtil.GeoTiff.Point;
import com.dy.ImageUtil.GeoTiff.Polygon;
import com.dy.ImageUtil.GeoTiff.Process;
import com.dy.ImageUtil.GeoTiff.Rectangle;

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
				2650.0, 1125.0, 2650.0, 1125.0, 2625.0, 1150.0, 2625.0, 1150.0, 2650.0, 1150.0, 2725.0, 1125.0, 2725.0,
				1125.0, 2700.0, 1100.0, 2700.0, 1100.0, 2675.0, 1075.0, 2675.0, 1075.0, 2650.0, 1025.0, 2650.0, 1025.0,
				2625.0, 1000.0, 2625.0, 1000.0, 2575.0, 975.0, 2575.0, 975.0, 2550.0, 975.0, 2525.0, 1000.0, 2525.0 });
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
		tmp.push(new Rectangle(3150, 525, 3175, 550));
		tmp.push(new Rectangle(3150, 550, 3200, 575));
		tmp.push(new Rectangle(3150, 575, 3200, 600));
		tmp.push(new Rectangle(3150, 600, 3225, 625));
		tmp.push(new Rectangle(3200, 625, 3225, 650));
		tmp.push(new Rectangle(3075, 650, 3100, 675));
		tmp.push(new Rectangle(3150, 650, 3250, 675));
		tmp.push(new Rectangle(3075, 675, 3200, 700));
		tmp.push(new Rectangle(3225, 675, 3250, 700));
		tmp.push(new Rectangle(3075, 700, 3100, 725));
		tmp.push(new Rectangle(3125, 700, 3150, 725));
		tmp.push(new Rectangle(3175, 700, 3200, 725));
		tmp.push(new Rectangle(3225, 700, 3275, 725));
		tmp.push(new Rectangle(3175, 725, 3225, 750));
		tmp.push(new Rectangle(3250, 725, 3300, 750));
		tmp.push(new Rectangle(3125, 750, 3200, 775));
		tmp.push(new Rectangle(3250, 750, 3325, 775));
		tmp.push(new Rectangle(3300, 775, 3325, 800));
		Stack<Polygon> ps = Process.transformPolygon(tmp);
		Point[] result = Point.fromArray(new double[] { 3075.0, 650.0, 3100.0, 650.0, 3100.0, 675.0, 3150.0, 675.0,
				3150.0, 650.0, 3200.0, 650.0, 3200.0, 625.0, 3150.0, 625.0, 3150.0, 600.0, 3150.0, 550.0, 3150.0, 525.0,
				3175.0, 525.0, 3175.0, 550.0, 3200.0, 550.0, 3200.0, 600.0, 3225.0, 600.0, 3225.0, 650.0, 3250.0, 650.0,
				3250.0, 700.0, 3275.0, 700.0, 3275.0, 725.0, 3300.0, 725.0, 3300.0, 750.0, 3325.0, 750.0, 3325.0, 800.0,
				3300.0, 800.0, 3300.0, 775.0, 3250.0, 775.0, 3250.0, 750.0, 3250.0, 725.0, 3225.0, 725.0, 3225.0, 750.0,
				3200.0, 750.0, 3200.0, 775.0, 3125.0, 775.0, 3125.0, 750.0, 3175.0, 750.0, 3175.0, 725.0, 3175.0, 700.0,
				3150.0, 700.0, 3150.0, 725.0, 3125.0, 725.0, 3125.0, 700.0, 3100.0, 700.0, 3100.0, 725.0, 3075.0, 725.0,
				3075.0, 675.0 });
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
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testAPolygon5");
	}

	// 多边形合并
	@Test
	void test4() {
		Stack<Rectangle> tmp = new Stack<>();
		tmp.push(new Rectangle(2750, 3125, 2775, 3150));
		tmp.push(new Rectangle(2750, 3150, 2800, 3175));
		tmp.push(new Rectangle(3100, 3150, 3150, 3175));
		tmp.push(new Rectangle(2700, 3175, 2800, 3200));
		tmp.push(new Rectangle(2900, 3175, 2925, 3200));
		tmp.push(new Rectangle(3025, 3175, 3050, 3200));
		tmp.push(new Rectangle(3100, 3175, 3125, 3200));
		tmp.push(new Rectangle(2675, 3200, 2725, 3225));
		tmp.push(new Rectangle(2750, 3200, 2800, 3225));
		tmp.push(new Rectangle(2900, 3200, 2950, 3225));
		tmp.push(new Rectangle(3000, 3200, 3125, 3225));
		tmp.push(new Rectangle(2675, 3225, 2800, 3250));
		tmp.push(new Rectangle(2900, 3225, 2925, 3250));
		tmp.push(new Rectangle(3000, 3225, 3125, 3250));
		tmp.push(new Rectangle(2700, 3250, 2725, 3275));
		tmp.push(new Rectangle(2750, 3250, 2800, 3275));
		tmp.push(new Rectangle(2900, 3250, 2975, 3275));
		tmp.push(new Rectangle(3000, 3250, 3125, 3275));
		tmp.push(new Rectangle(2750, 3275, 2800, 3300));
		tmp.push(new Rectangle(2875, 3275, 2975, 3300));
		tmp.push(new Rectangle(3000, 3275, 3125, 3300));
		tmp.push(new Rectangle(2700, 3300, 2800, 3325));
		tmp.push(new Rectangle(2875, 3300, 3125, 3325));
		tmp.push(new Rectangle(2650, 3325, 2725, 3350));
		tmp.push(new Rectangle(2775, 3325, 2825, 3350));
		tmp.push(new Rectangle(2850, 3325, 3125, 3350));
		tmp.push(new Rectangle(2650, 3350, 3075, 3375));
		tmp.push(new Rectangle(2625, 3375, 2675, 3400));
		tmp.push(new Rectangle(2700, 3375, 3075, 3400));
		tmp.push(new Rectangle(2625, 3400, 3100, 3425));
		tmp.push(new Rectangle(2675, 3425, 3075, 3450));
		tmp.push(new Rectangle(2750, 3450, 2800, 3475));
		tmp.push(new Rectangle(2825, 3450, 3075, 3475));
		tmp.push(new Rectangle(2900, 3475, 3050, 3500));
		tmp.push(new Rectangle(2950, 3500, 3050, 3525));
		Stack<Polygon> ps = Process.transformPolygon(tmp);
		Point[] result = Point.fromArray(new double[] { 2750.0, 3125.0, 2775.0, 3125.0, 2775.0, 3150.0, 2800.0, 3150.0,
				2800.0, 3175.0, 2800.0, 3225.0, 2800.0, 3300.0, 2800.0, 3325.0, 2825.0, 3325.0, 2825.0, 3350.0, 2850.0,
				3350.0, 2850.0, 3325.0, 2875.0, 3325.0, 2875.0, 3300.0, 2875.0, 3275.0, 2900.0, 3275.0, 2900.0, 3250.0,
				2900.0, 3200.0, 2900.0, 3175.0, 2925.0, 3175.0, 2925.0, 3200.0, 2950.0, 3200.0, 2950.0, 3225.0, 2925.0,
				3225.0, 2925.0, 3250.0, 2975.0, 3250.0, 2975.0, 3275.0, 2975.0, 3300.0, 3000.0, 3300.0, 3000.0, 3200.0,
				3025.0, 3200.0, 3025.0, 3175.0, 3050.0, 3175.0, 3050.0, 3200.0, 3100.0, 3200.0, 3100.0, 3150.0, 3150.0,
				3150.0, 3150.0, 3175.0, 3125.0, 3175.0, 3125.0, 3200.0, 3125.0, 3300.0, 3125.0, 3325.0, 3125.0, 3350.0,
				3075.0, 3350.0, 3075.0, 3400.0, 3100.0, 3400.0, 3100.0, 3425.0, 3075.0, 3425.0, 3075.0, 3475.0, 3050.0,
				3475.0, 3050.0, 3525.0, 2950.0, 3525.0, 2950.0, 3500.0, 2900.0, 3500.0, 2900.0, 3475.0, 2825.0, 3475.0,
				2825.0, 3450.0, 2800.0, 3450.0, 2800.0, 3475.0, 2750.0, 3475.0, 2750.0, 3450.0, 2675.0, 3450.0, 2675.0,
				3425.0, 2625.0, 3425.0, 2625.0, 3400.0, 2625.0, 3375.0, 2650.0, 3375.0, 2650.0, 3350.0, 2650.0, 3325.0,
				2700.0, 3325.0, 2700.0, 3300.0, 2750.0, 3300.0, 2750.0, 3250.0, 2725.0, 3250.0, 2725.0, 3275.0, 2700.0,
				3275.0, 2700.0, 3250.0, 2675.0, 3250.0, 2675.0, 3225.0, 2675.0, 3200.0, 2700.0, 3200.0, 2700.0, 3175.0,
				2750.0, 3175.0, 2750.0, 3150.0 });
		assertEquals(result.length == ps.get(0).position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(ps.get(0).position[i].equals(result[i]), true);
		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		for (int i = 0; i < ps.size(); i++) {
			origin = ImageDrawUtil.drawPolygonOutline(origin, ps.get(i), colorStr);
		}
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testAPolygon6");
	}

	// 多边形合并
	@Test
	void test5() {
		Stack<Rectangle> tmp = new Stack<>();
		tmp.push(new Rectangle(1975, 625, 2025, 650));
		tmp.push(new Rectangle(1950, 650, 2050, 675));
		tmp.push(new Rectangle(1950, 675, 2000, 700));
		tmp.push(new Rectangle(1925, 700, 2000, 725));
		tmp.push(new Rectangle(2050, 700, 2100, 725));
		tmp.push(new Rectangle(2125, 700, 2150, 725));
		tmp.push(new Rectangle(2225, 700, 2300, 725));
		tmp.push(new Rectangle(1925, 725, 2175, 750));
		tmp.push(new Rectangle(2200, 725, 2325, 750));
		tmp.push(new Rectangle(2675, 725, 2725, 750));
		tmp.push(new Rectangle(1900, 750, 2125, 775));
		tmp.push(new Rectangle(2150, 750, 2325, 775));
		tmp.push(new Rectangle(2675, 750, 2725, 775));
		tmp.push(new Rectangle(2900, 750, 2925, 775));
		tmp.push(new Rectangle(1875, 775, 1925, 800));
		tmp.push(new Rectangle(1975, 775, 2325, 800));
		tmp.push(new Rectangle(2400, 775, 2425, 800));
		tmp.push(new Rectangle(2650, 775, 2700, 800));
		tmp.push(new Rectangle(2900, 775, 2950, 800));
		tmp.push(new Rectangle(1875, 800, 1925, 825));
		tmp.push(new Rectangle(1975, 800, 2350, 825));
		tmp.push(new Rectangle(2375, 800, 2425, 825));
		tmp.push(new Rectangle(2600, 800, 2625, 825));
		tmp.push(new Rectangle(2650, 800, 2700, 825));
		tmp.push(new Rectangle(2850, 800, 2975, 825));
		tmp.push(new Rectangle(1900, 825, 1925, 850));
		tmp.push(new Rectangle(1975, 825, 2150, 850));
		tmp.push(new Rectangle(2175, 825, 2425, 850));
		tmp.push(new Rectangle(2575, 825, 2700, 850));
		tmp.push(new Rectangle(2825, 825, 2850, 850));
		tmp.push(new Rectangle(2875, 825, 3025, 850));
		tmp.push(new Rectangle(1925, 850, 2125, 875));
		tmp.push(new Rectangle(2200, 850, 2225, 875));
		tmp.push(new Rectangle(2275, 850, 2500, 875));
		tmp.push(new Rectangle(2575, 850, 2600, 875));
		tmp.push(new Rectangle(2625, 850, 2725, 875));
		tmp.push(new Rectangle(2825, 850, 3025, 875));
		tmp.push(new Rectangle(1975, 875, 2075, 900));
		tmp.push(new Rectangle(2300, 875, 2325, 900));
		tmp.push(new Rectangle(2350, 875, 2475, 900));
		tmp.push(new Rectangle(2575, 875, 2725, 900));
		tmp.push(new Rectangle(2800, 875, 3000, 900));
		tmp.push(new Rectangle(2000, 900, 2075, 925));
		tmp.push(new Rectangle(2225, 900, 2500, 925));
		tmp.push(new Rectangle(2550, 900, 2600, 925));
		tmp.push(new Rectangle(2625, 900, 2700, 925));
		tmp.push(new Rectangle(2775, 900, 2825, 925));
		tmp.push(new Rectangle(2850, 900, 3000, 925));
		tmp.push(new Rectangle(2025, 925, 2050, 950));
		tmp.push(new Rectangle(2175, 925, 2425, 950));
		tmp.push(new Rectangle(2450, 925, 2475, 950));
		tmp.push(new Rectangle(2500, 925, 2725, 950));
		tmp.push(new Rectangle(2875, 925, 2975, 950));
		tmp.push(new Rectangle(2175, 950, 2250, 975));
		tmp.push(new Rectangle(2275, 950, 2775, 975));
		tmp.push(new Rectangle(2800, 950, 2825, 975));
		tmp.push(new Rectangle(2875, 950, 2975, 975));
		tmp.push(new Rectangle(2150, 975, 2225, 1000));
		tmp.push(new Rectangle(2275, 975, 2300, 1000));
		tmp.push(new Rectangle(2350, 975, 2425, 1000));
		tmp.push(new Rectangle(2475, 975, 2650, 1000));
		tmp.push(new Rectangle(2675, 975, 2975, 1000));
		tmp.push(new Rectangle(2150, 1000, 2350, 1025));
		tmp.push(new Rectangle(2375, 1000, 2500, 1025));
		tmp.push(new Rectangle(2525, 1000, 2650, 1025));
		tmp.push(new Rectangle(2675, 1000, 2700, 1025));
		tmp.push(new Rectangle(2750, 1000, 2825, 1025));
		tmp.push(new Rectangle(2125, 1025, 2275, 1050));
		tmp.push(new Rectangle(2325, 1025, 2350, 1050));
		tmp.push(new Rectangle(2375, 1025, 2700, 1050));
		tmp.push(new Rectangle(2750, 1025, 2875, 1050));
		tmp.push(new Rectangle(2150, 1050, 2275, 1075));
		tmp.push(new Rectangle(2325, 1050, 2375, 1075));
		tmp.push(new Rectangle(2450, 1050, 2625, 1075));
		tmp.push(new Rectangle(2650, 1050, 2725, 1075));
		tmp.push(new Rectangle(2750, 1050, 2875, 1075));
		tmp.push(new Rectangle(2150, 1075, 2175, 1100));
		tmp.push(new Rectangle(2325, 1075, 2375, 1100));
		tmp.push(new Rectangle(2450, 1075, 2900, 1100));
		tmp.push(new Rectangle(2250, 1100, 2375, 1125));
		tmp.push(new Rectangle(2475, 1100, 2900, 1125));
		tmp.push(new Rectangle(2275, 1125, 2300, 1150));
		tmp.push(new Rectangle(2475, 1125, 2600, 1150));
		tmp.push(new Rectangle(2650, 1125, 2925, 1150));
		tmp.push(new Rectangle(2475, 1150, 2900, 1175));
		tmp.push(new Rectangle(2450, 1175, 2900, 1200));
		tmp.push(new Rectangle(2475, 1200, 2850, 1225));
		tmp.push(new Rectangle(2875, 1200, 3000, 1225));
		tmp.push(new Rectangle(3025, 1200, 3050, 1225));
		tmp.push(new Rectangle(2450, 1225, 2750, 1250));
		tmp.push(new Rectangle(2775, 1225, 3000, 1250));
		tmp.push(new Rectangle(3025, 1225, 3175, 1250));
		tmp.push(new Rectangle(2475, 1250, 3225, 1275));
		tmp.push(new Rectangle(2450, 1275, 2625, 1300));
		tmp.push(new Rectangle(2700, 1275, 2900, 1300));
		tmp.push(new Rectangle(2925, 1275, 2950, 1300));
		tmp.push(new Rectangle(2975, 1275, 3000, 1300));
		tmp.push(new Rectangle(3050, 1275, 3250, 1300));
		tmp.push(new Rectangle(2550, 1300, 2575, 1325));
		tmp.push(new Rectangle(2725, 1300, 2775, 1325));
		tmp.push(new Rectangle(2875, 1300, 2900, 1325));
		tmp.push(new Rectangle(2975, 1300, 3150, 1325));
		tmp.push(new Rectangle(2875, 1325, 2975, 1350));
		tmp.push(new Rectangle(3000, 1325, 3025, 1350));
		tmp.push(new Rectangle(3050, 1325, 3125, 1350));
		tmp.push(new Rectangle(2850, 1350, 3000, 1375));
		tmp.push(new Rectangle(3025, 1350, 3200, 1375));
		tmp.push(new Rectangle(2850, 1375, 3200, 1400));
		tmp.push(new Rectangle(2900, 1400, 2925, 1425));
		tmp.push(new Rectangle(2950, 1400, 3175, 1425));
		tmp.push(new Rectangle(2950, 1425, 3000, 1450));
		tmp.push(new Rectangle(3050, 1425, 3150, 1450));
		tmp.push(new Rectangle(3050, 1450, 3150, 1475));
		tmp.push(new Rectangle(3050, 1475, 3150, 1500));
		tmp.push(new Rectangle(3050, 1500, 3150, 1525));
		tmp.push(new Rectangle(3175, 1500, 3200, 1525));
		tmp.push(new Rectangle(3125, 1525, 3200, 1550));
		tmp.push(new Rectangle(3125, 1550, 3200, 1575));
		tmp.push(new Rectangle(3075, 1575, 3250, 1600));
		tmp.push(new Rectangle(3125, 1600, 3150, 1625));
		tmp.push(new Rectangle(3175, 1600, 3225, 1625));
		Stack<Polygon> ps = Process.transformPolygon(tmp);
		Point[] result = Point.fromArray(new double[] { 1975.0, 625.0, 2025.0, 625.0, 2025.0, 650.0, 2050.0, 650.0,
				2050.0, 675.0, 2000.0, 675.0, 2000.0, 700.0, 2000.0, 725.0, 2050.0, 725.0, 2050.0, 700.0, 2100.0, 700.0,
				2100.0, 725.0, 2125.0, 725.0, 2125.0, 700.0, 2150.0, 700.0, 2150.0, 725.0, 2175.0, 725.0, 2175.0, 750.0,
				2200.0, 750.0, 2200.0, 725.0, 2225.0, 725.0, 2225.0, 700.0, 2300.0, 700.0, 2300.0, 725.0, 2325.0, 725.0,
				2325.0, 750.0, 2325.0, 775.0, 2325.0, 800.0, 2350.0, 800.0, 2350.0, 825.0, 2375.0, 825.0, 2375.0, 800.0,
				2400.0, 800.0, 2400.0, 775.0, 2425.0, 775.0, 2425.0, 800.0, 2425.0, 825.0, 2425.0, 850.0, 2500.0, 850.0,
				2500.0, 875.0, 2475.0, 875.0, 2475.0, 900.0, 2500.0, 900.0, 2500.0, 925.0, 2475.0, 925.0, 2475.0, 950.0,
				2500.0, 950.0, 2500.0, 925.0, 2550.0, 925.0, 2550.0, 900.0, 2575.0, 900.0, 2575.0, 875.0, 2575.0, 825.0,
				2600.0, 825.0, 2600.0, 800.0, 2625.0, 800.0, 2625.0, 825.0, 2650.0, 825.0, 2650.0, 775.0, 2675.0, 775.0,
				2675.0, 725.0, 2725.0, 725.0, 2725.0, 775.0, 2700.0, 775.0, 2700.0, 825.0, 2700.0, 850.0, 2725.0, 850.0,
				2725.0, 875.0, 2725.0, 900.0, 2700.0, 900.0, 2700.0, 925.0, 2725.0, 925.0, 2725.0, 950.0, 2775.0, 950.0,
				2775.0, 975.0, 2800.0, 975.0, 2800.0, 950.0, 2825.0, 950.0, 2825.0, 975.0, 2875.0, 975.0, 2875.0, 925.0,
				2850.0, 925.0, 2850.0, 900.0, 2825.0, 900.0, 2825.0, 925.0, 2775.0, 925.0, 2775.0, 900.0, 2800.0, 900.0,
				2800.0, 875.0, 2825.0, 875.0, 2825.0, 850.0, 2825.0, 825.0, 2850.0, 825.0, 2850.0, 850.0, 2875.0, 850.0,
				2875.0, 825.0, 2850.0, 825.0, 2850.0, 800.0, 2900.0, 800.0, 2900.0, 775.0, 2900.0, 750.0, 2925.0, 750.0,
				2925.0, 775.0, 2950.0, 775.0, 2950.0, 800.0, 2975.0, 800.0, 2975.0, 825.0, 3025.0, 825.0, 3025.0, 850.0,
				3025.0, 875.0, 3000.0, 875.0, 3000.0, 925.0, 2975.0, 925.0, 2975.0, 975.0, 2975.0, 1000.0, 2825.0,
				1000.0, 2825.0, 1025.0, 2875.0, 1025.0, 2875.0, 1075.0, 2900.0, 1075.0, 2900.0, 1125.0, 2925.0, 1125.0,
				2925.0, 1150.0, 2900.0, 1150.0, 2900.0, 1175.0, 2900.0, 1200.0, 3000.0, 1200.0, 3000.0, 1225.0, 3000.0,
				1250.0, 3025.0, 1250.0, 3025.0, 1225.0, 3025.0, 1200.0, 3050.0, 1200.0, 3050.0, 1225.0, 3175.0, 1225.0,
				3175.0, 1250.0, 3225.0, 1250.0, 3225.0, 1275.0, 3250.0, 1275.0, 3250.0, 1300.0, 3150.0, 1300.0, 3150.0,
				1325.0, 3125.0, 1325.0, 3125.0, 1350.0, 3200.0, 1350.0, 3200.0, 1375.0, 3200.0, 1400.0, 3175.0, 1400.0,
				3175.0, 1425.0, 3150.0, 1425.0, 3150.0, 1525.0, 3175.0, 1525.0, 3175.0, 1500.0, 3200.0, 1500.0, 3200.0,
				1525.0, 3200.0, 1575.0, 3250.0, 1575.0, 3250.0, 1600.0, 3225.0, 1600.0, 3225.0, 1625.0, 3175.0, 1625.0,
				3175.0, 1600.0, 3150.0, 1600.0, 3150.0, 1625.0, 3125.0, 1625.0, 3125.0, 1600.0, 3075.0, 1600.0, 3075.0,
				1575.0, 3125.0, 1575.0, 3125.0, 1525.0, 3050.0, 1525.0, 3050.0, 1425.0, 3000.0, 1425.0, 3000.0, 1450.0,
				2950.0, 1450.0, 2950.0, 1400.0, 2925.0, 1400.0, 2925.0, 1425.0, 2900.0, 1425.0, 2900.0, 1400.0, 2850.0,
				1400.0, 2850.0, 1375.0, 2850.0, 1350.0, 2875.0, 1350.0, 2875.0, 1325.0, 2875.0, 1300.0, 2775.0, 1300.0,
				2775.0, 1325.0, 2725.0, 1325.0, 2725.0, 1300.0, 2700.0, 1300.0, 2700.0, 1275.0, 2625.0, 1275.0, 2625.0,
				1300.0, 2575.0, 1300.0, 2575.0, 1325.0, 2550.0, 1325.0, 2550.0, 1300.0, 2450.0, 1300.0, 2450.0, 1275.0,
				2475.0, 1275.0, 2475.0, 1250.0, 2450.0, 1250.0, 2450.0, 1225.0, 2475.0, 1225.0, 2475.0, 1200.0, 2450.0,
				1200.0, 2450.0, 1175.0, 2475.0, 1175.0, 2475.0, 1150.0, 2475.0, 1100.0, 2450.0, 1100.0, 2450.0, 1075.0,
				2450.0, 1050.0, 2375.0, 1050.0, 2375.0, 1100.0, 2375.0, 1125.0, 2300.0, 1125.0, 2300.0, 1150.0, 2275.0,
				1150.0, 2275.0, 1125.0, 2250.0, 1125.0, 2250.0, 1100.0, 2325.0, 1100.0, 2325.0, 1050.0, 2325.0, 1025.0,
				2275.0, 1025.0, 2275.0, 1075.0, 2175.0, 1075.0, 2175.0, 1100.0, 2150.0, 1100.0, 2150.0, 1050.0, 2125.0,
				1050.0, 2125.0, 1025.0, 2150.0, 1025.0, 2150.0, 1000.0, 2150.0, 975.0, 2175.0, 975.0, 2175.0, 925.0,
				2225.0, 925.0, 2225.0, 900.0, 2300.0, 900.0, 2300.0, 875.0, 2275.0, 875.0, 2275.0, 850.0, 2225.0, 850.0,
				2225.0, 875.0, 2200.0, 875.0, 2200.0, 850.0, 2175.0, 850.0, 2175.0, 825.0, 2150.0, 825.0, 2150.0, 850.0,
				2125.0, 850.0, 2125.0, 875.0, 2075.0, 875.0, 2075.0, 925.0, 2050.0, 925.0, 2050.0, 950.0, 2025.0, 950.0,
				2025.0, 925.0, 2000.0, 925.0, 2000.0, 900.0, 1975.0, 900.0, 1975.0, 875.0, 1925.0, 875.0, 1925.0, 850.0,
				1900.0, 850.0, 1900.0, 825.0, 1875.0, 825.0, 1875.0, 775.0, 1900.0, 775.0, 1900.0, 750.0, 1925.0, 750.0,
				1925.0, 725.0, 1925.0, 700.0, 1950.0, 700.0, 1950.0, 650.0, 1975.0, 650.0 });
		assertEquals(result.length == ps.get(0).position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(ps.get(0).position[i].equals(result[i]), true);
		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		for (int i = 0; i < ps.size(); i++) {
			origin = ImageDrawUtil.drawPolygonOutline(origin, ps.get(i), colorStr);
		}
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testAPolygon7");
	}

	// 多边形合并
	@Test
	void test6() {
		Stack<Rectangle> tmp = new Stack<>();
		tmp.push(new Rectangle(875, 25, 900, 50));
		tmp.push(new Rectangle(850, 50, 1000, 75));
		tmp.push(new Rectangle(825, 75, 1125, 100));
		tmp.push(new Rectangle(825, 100, 1075, 125));
		tmp.push(new Rectangle(1100, 100, 1225, 125));
		tmp.push(new Rectangle(800, 125, 1075, 150));
		tmp.push(new Rectangle(1100, 125, 1275, 150));
		tmp.push(new Rectangle(825, 150, 850, 175));
		tmp.push(new Rectangle(875, 150, 1150, 175));
		tmp.push(new Rectangle(1200, 150, 1275, 175));
		tmp.push(new Rectangle(850, 175, 1275, 200));
		tmp.push(new Rectangle(1400, 175, 1425, 200));
		tmp.push(new Rectangle(1475, 175, 1575, 200));
		tmp.push(new Rectangle(925, 200, 1250, 225));
		tmp.push(new Rectangle(1400, 200, 1700, 225));
		tmp.push(new Rectangle(950, 225, 975, 250));
		tmp.push(new Rectangle(1025, 225, 1375, 250));
		tmp.push(new Rectangle(1400, 225, 1800, 250));
		tmp.push(new Rectangle(1025, 250, 1425, 275));
		tmp.push(new Rectangle(1525, 250, 1850, 275));
		tmp.push(new Rectangle(1075, 275, 1425, 300));
		tmp.push(new Rectangle(1525, 275, 1625, 300));
		tmp.push(new Rectangle(1675, 275, 1925, 300));
		tmp.push(new Rectangle(1125, 300, 1175, 325));
		tmp.push(new Rectangle(1200, 300, 1475, 325));
		tmp.push(new Rectangle(1525, 300, 1650, 325));
		tmp.push(new Rectangle(1675, 300, 1950, 325));
		tmp.push(new Rectangle(1025, 325, 1075, 350));
		tmp.push(new Rectangle(1125, 325, 1500, 350));
		tmp.push(new Rectangle(1525, 325, 1975, 350));
		tmp.push(new Rectangle(2050, 325, 2075, 350));
		tmp.push(new Rectangle(2100, 325, 2125, 350));
		tmp.push(new Rectangle(1000, 350, 1075, 375));
		tmp.push(new Rectangle(1175, 350, 1475, 375));
		tmp.push(new Rectangle(1525, 350, 2125, 375));
		tmp.push(new Rectangle(875, 375, 950, 400));
		tmp.push(new Rectangle(975, 375, 1050, 400));
		tmp.push(new Rectangle(1175, 375, 1475, 400));
		tmp.push(new Rectangle(1525, 375, 2100, 400));
		tmp.push(new Rectangle(875, 400, 950, 425));
		tmp.push(new Rectangle(975, 400, 1025, 425));
		tmp.push(new Rectangle(1050, 400, 1125, 425));
		tmp.push(new Rectangle(1200, 400, 1475, 425));
		tmp.push(new Rectangle(1500, 400, 1575, 425));
		tmp.push(new Rectangle(1625, 400, 2125, 425));
		tmp.push(new Rectangle(875, 425, 975, 450));
		tmp.push(new Rectangle(1000, 425, 1100, 450));
		tmp.push(new Rectangle(1225, 425, 1300, 450));
		tmp.push(new Rectangle(1325, 425, 1375, 450));
		tmp.push(new Rectangle(1425, 425, 1575, 450));
		tmp.push(new Rectangle(1650, 425, 1975, 450));
		tmp.push(new Rectangle(2000, 425, 2150, 450));
		tmp.push(new Rectangle(900, 450, 1000, 475));
		tmp.push(new Rectangle(1025, 450, 1100, 475));
		tmp.push(new Rectangle(1125, 450, 1575, 475));
		tmp.push(new Rectangle(1650, 450, 2175, 475));
		tmp.push(new Rectangle(900, 475, 925, 500));
		tmp.push(new Rectangle(950, 475, 1225, 500));
		tmp.push(new Rectangle(1300, 475, 1725, 500));
		tmp.push(new Rectangle(1800, 475, 2200, 500));
		tmp.push(new Rectangle(950, 500, 1050, 525));
		tmp.push(new Rectangle(1100, 500, 1125, 525));
		tmp.push(new Rectangle(1300, 500, 1325, 525));
		tmp.push(new Rectangle(1350, 500, 1725, 525));
		tmp.push(new Rectangle(1825, 500, 2150, 525));
		tmp.push(new Rectangle(975, 525, 1025, 550));
		tmp.push(new Rectangle(1075, 525, 1150, 550));
		tmp.push(new Rectangle(1225, 525, 1250, 550));
		tmp.push(new Rectangle(1275, 525, 1300, 550));
		tmp.push(new Rectangle(1325, 525, 1375, 550));
		tmp.push(new Rectangle(1400, 525, 1425, 550));
		tmp.push(new Rectangle(1500, 525, 1750, 550));
		tmp.push(new Rectangle(1825, 525, 1875, 550));
		tmp.push(new Rectangle(1925, 525, 2150, 550));
		tmp.push(new Rectangle(1050, 550, 1100, 575));
		tmp.push(new Rectangle(1150, 550, 1250, 575));
		tmp.push(new Rectangle(1275, 550, 1325, 575));
		tmp.push(new Rectangle(1350, 550, 1400, 575));
		tmp.push(new Rectangle(1500, 550, 1575, 575));
		tmp.push(new Rectangle(1600, 550, 1850, 575));
		tmp.push(new Rectangle(1925, 550, 2175, 575));
		tmp.push(new Rectangle(1075, 575, 1150, 600));
		tmp.push(new Rectangle(1200, 575, 1225, 600));
		tmp.push(new Rectangle(1250, 575, 1350, 600));
		tmp.push(new Rectangle(1375, 575, 1550, 600));
		tmp.push(new Rectangle(1600, 575, 1825, 600));
		tmp.push(new Rectangle(1900, 575, 2175, 600));
		tmp.push(new Rectangle(2200, 575, 2225, 600));
		tmp.push(new Rectangle(1050, 600, 1125, 625));
		tmp.push(new Rectangle(1150, 600, 1425, 625));
		tmp.push(new Rectangle(1475, 600, 1550, 625));
		tmp.push(new Rectangle(1600, 600, 1675, 625));
		tmp.push(new Rectangle(1700, 600, 1800, 625));
		tmp.push(new Rectangle(1875, 600, 1975, 625));
		tmp.push(new Rectangle(2075, 600, 2225, 625));
		tmp.push(new Rectangle(1075, 625, 1125, 650));
		tmp.push(new Rectangle(1150, 625, 1200, 650));
		tmp.push(new Rectangle(1250, 625, 1675, 650));
		tmp.push(new Rectangle(1700, 625, 1800, 650));
		tmp.push(new Rectangle(1900, 625, 1925, 650));
		tmp.push(new Rectangle(2150, 625, 2250, 650));
		tmp.push(new Rectangle(1125, 650, 1200, 675));
		tmp.push(new Rectangle(1225, 650, 1250, 675));
		tmp.push(new Rectangle(1275, 650, 1625, 675));
		tmp.push(new Rectangle(1650, 650, 1675, 675));
		tmp.push(new Rectangle(1725, 650, 1825, 675));
		tmp.push(new Rectangle(1900, 650, 1925, 675));
		tmp.push(new Rectangle(2150, 650, 2225, 675));
		tmp.push(new Rectangle(1225, 675, 1250, 700));
		tmp.push(new Rectangle(1300, 675, 1625, 700));
		tmp.push(new Rectangle(1725, 675, 1800, 700));
		tmp.push(new Rectangle(1900, 675, 1925, 700));
		tmp.push(new Rectangle(1200, 700, 1250, 725));
		tmp.push(new Rectangle(1300, 700, 1675, 725));
		tmp.push(new Rectangle(1725, 700, 1850, 725));
		tmp.push(new Rectangle(1175, 725, 1275, 750));
		tmp.push(new Rectangle(1325, 725, 1425, 750));
		tmp.push(new Rectangle(1475, 725, 1500, 750));
		tmp.push(new Rectangle(1525, 725, 1725, 750));
		tmp.push(new Rectangle(1750, 725, 1825, 750));
		tmp.push(new Rectangle(1225, 750, 1400, 775));
		tmp.push(new Rectangle(1575, 750, 1825, 775));
		tmp.push(new Rectangle(1225, 775, 1450, 800));
		tmp.push(new Rectangle(1625, 775, 1825, 800));
		tmp.push(new Rectangle(1300, 800, 1325, 825));
		tmp.push(new Rectangle(1350, 800, 1450, 825));
		tmp.push(new Rectangle(1600, 800, 1825, 825));
		tmp.push(new Rectangle(1400, 825, 1450, 850));
		tmp.push(new Rectangle(1650, 825, 1850, 850));
		tmp.push(new Rectangle(1425, 850, 1450, 875));
		tmp.push(new Rectangle(1625, 850, 1875, 875));
		tmp.push(new Rectangle(1625, 875, 1925, 900));
		tmp.push(new Rectangle(1625, 900, 1925, 925));
		tmp.push(new Rectangle(1625, 925, 1925, 950));
		tmp.push(new Rectangle(1700, 950, 1900, 975));
		tmp.push(new Rectangle(1950, 950, 1975, 975));
		tmp.push(new Rectangle(1800, 975, 1925, 1000));
		tmp.push(new Rectangle(1950, 975, 1975, 1000));
		tmp.push(new Rectangle(1800, 1000, 1975, 1025));
		tmp.push(new Rectangle(1825, 1025, 2000, 1050));
		tmp.push(new Rectangle(1900, 1050, 2025, 1075));
		tmp.push(new Rectangle(1950, 1075, 2025, 1100));
		tmp.push(new Rectangle(1975, 1100, 2025, 1125));
		tmp.push(new Rectangle(1975, 1125, 2075, 1150));
		tmp.push(new Rectangle(2025, 1150, 2075, 1175));
		tmp.push(new Rectangle(2025, 1175, 2050, 1200));
		Stack<Polygon> ps = Process.transformPolygon(tmp);
		Point[] result = Point.fromArray(new double[] { 1225.0, 650.0, 1250.0, 650.0, 1250.0, 700.0, 1250.0, 725.0,
				1275.0, 725.0, 1275.0, 750.0, 1325.0, 750.0, 1325.0, 725.0, 1300.0, 725.0, 1300.0, 700.0, 1300.0, 675.0,
				1275.0, 675.0, 1275.0, 650.0, 1250.0, 650.0, 1250.0, 625.0, 1200.0, 625.0, 1200.0, 650.0, 1200.0, 675.0,
				1125.0, 675.0, 1125.0, 650.0, 1075.0, 650.0, 1075.0, 625.0, 1050.0, 625.0, 1050.0, 600.0, 1075.0, 600.0,
				1075.0, 575.0, 1050.0, 575.0, 1050.0, 550.0, 1075.0, 550.0, 1075.0, 525.0, 1100.0, 525.0, 1100.0, 500.0,
				1050.0, 500.0, 1050.0, 525.0, 1025.0, 525.0, 1025.0, 550.0, 975.0, 550.0, 975.0, 525.0, 950.0, 525.0,
				950.0, 475.0, 925.0, 475.0, 925.0, 500.0, 900.0, 500.0, 900.0, 450.0, 875.0, 450.0, 875.0, 425.0, 875.0,
				375.0, 950.0, 375.0, 950.0, 425.0, 975.0, 425.0, 975.0, 450.0, 1000.0, 450.0, 1000.0, 475.0, 1025.0,
				475.0, 1025.0, 450.0, 1000.0, 450.0, 1000.0, 425.0, 975.0, 425.0, 975.0, 375.0, 1000.0, 375.0, 1000.0,
				350.0, 1025.0, 350.0, 1025.0, 325.0, 1075.0, 325.0, 1075.0, 350.0, 1075.0, 375.0, 1050.0, 375.0, 1050.0,
				400.0, 1025.0, 400.0, 1025.0, 425.0, 1050.0, 425.0, 1050.0, 400.0, 1125.0, 400.0, 1125.0, 425.0, 1100.0,
				425.0, 1100.0, 475.0, 1125.0, 475.0, 1125.0, 450.0, 1225.0, 450.0, 1225.0, 425.0, 1200.0, 425.0, 1200.0,
				400.0, 1175.0, 400.0, 1175.0, 350.0, 1125.0, 350.0, 1125.0, 325.0, 1125.0, 300.0, 1075.0, 300.0, 1075.0,
				275.0, 1025.0, 275.0, 1025.0, 250.0, 1025.0, 225.0, 975.0, 225.0, 975.0, 250.0, 950.0, 250.0, 950.0,
				225.0, 925.0, 225.0, 925.0, 200.0, 850.0, 200.0, 850.0, 175.0, 825.0, 175.0, 825.0, 150.0, 800.0, 150.0,
				800.0, 125.0, 825.0, 125.0, 825.0, 75.0, 850.0, 75.0, 850.0, 50.0, 875.0, 50.0, 875.0, 25.0, 900.0,
				25.0, 900.0, 50.0, 1000.0, 50.0, 1000.0, 75.0, 1125.0, 75.0, 1125.0, 100.0, 1225.0, 100.0, 1225.0,
				125.0, 1275.0, 125.0, 1275.0, 175.0, 1275.0, 200.0, 1250.0, 200.0, 1250.0, 225.0, 1375.0, 225.0, 1375.0,
				250.0, 1400.0, 250.0, 1400.0, 225.0, 1400.0, 200.0, 1400.0, 175.0, 1425.0, 175.0, 1425.0, 200.0, 1475.0,
				200.0, 1475.0, 175.0, 1575.0, 175.0, 1575.0, 200.0, 1700.0, 200.0, 1700.0, 225.0, 1800.0, 225.0, 1800.0,
				250.0, 1850.0, 250.0, 1850.0, 275.0, 1925.0, 275.0, 1925.0, 300.0, 1950.0, 300.0, 1950.0, 325.0, 1975.0,
				325.0, 1975.0, 350.0, 2050.0, 350.0, 2050.0, 325.0, 2075.0, 325.0, 2075.0, 350.0, 2100.0, 350.0, 2100.0,
				325.0, 2125.0, 325.0, 2125.0, 350.0, 2125.0, 375.0, 2100.0, 375.0, 2100.0, 400.0, 2125.0, 400.0, 2125.0,
				425.0, 2150.0, 425.0, 2150.0, 450.0, 2175.0, 450.0, 2175.0, 475.0, 2200.0, 475.0, 2200.0, 500.0, 2150.0,
				500.0, 2150.0, 550.0, 2175.0, 550.0, 2175.0, 575.0, 2175.0, 600.0, 2200.0, 600.0, 2200.0, 575.0, 2225.0,
				575.0, 2225.0, 600.0, 2225.0, 625.0, 2250.0, 625.0, 2250.0, 650.0, 2225.0, 650.0, 2225.0, 675.0, 2150.0,
				675.0, 2150.0, 625.0, 2075.0, 625.0, 2075.0, 600.0, 1975.0, 600.0, 1975.0, 625.0, 1925.0, 625.0, 1925.0,
				700.0, 1900.0, 700.0, 1900.0, 625.0, 1875.0, 625.0, 1875.0, 600.0, 1900.0, 600.0, 1900.0, 575.0, 1925.0,
				575.0, 1925.0, 550.0, 1925.0, 525.0, 1875.0, 525.0, 1875.0, 550.0, 1850.0, 550.0, 1850.0, 575.0, 1825.0,
				575.0, 1825.0, 600.0, 1800.0, 600.0, 1800.0, 650.0, 1825.0, 650.0, 1825.0, 675.0, 1800.0, 675.0, 1800.0,
				700.0, 1850.0, 700.0, 1850.0, 725.0, 1825.0, 725.0, 1825.0, 750.0, 1825.0, 800.0, 1825.0, 825.0, 1850.0,
				825.0, 1850.0, 850.0, 1875.0, 850.0, 1875.0, 875.0, 1925.0, 875.0, 1925.0, 950.0, 1900.0, 950.0, 1900.0,
				975.0, 1925.0, 975.0, 1925.0, 1000.0, 1950.0, 1000.0, 1950.0, 950.0, 1975.0, 950.0, 1975.0, 1000.0,
				1975.0, 1025.0, 2000.0, 1025.0, 2000.0, 1050.0, 2025.0, 1050.0, 2025.0, 1125.0, 2075.0, 1125.0, 2075.0,
				1175.0, 2050.0, 1175.0, 2050.0, 1200.0, 2025.0, 1200.0, 2025.0, 1150.0, 1975.0, 1150.0, 1975.0, 1125.0,
				1975.0, 1100.0, 1950.0, 1100.0, 1950.0, 1075.0, 1900.0, 1075.0, 1900.0, 1050.0, 1825.0, 1050.0, 1825.0,
				1025.0, 1800.0, 1025.0, 1800.0, 1000.0, 1800.0, 975.0, 1700.0, 975.0, 1700.0, 950.0, 1625.0, 950.0,
				1625.0, 875.0, 1625.0, 850.0, 1650.0, 850.0, 1650.0, 825.0, 1600.0, 825.0, 1600.0, 800.0, 1625.0, 800.0,
				1625.0, 775.0, 1575.0, 775.0, 1575.0, 750.0, 1525.0, 750.0, 1525.0, 725.0, 1500.0, 725.0, 1500.0, 750.0,
				1475.0, 750.0, 1475.0, 725.0, 1425.0, 725.0, 1425.0, 750.0, 1400.0, 750.0, 1400.0, 775.0, 1450.0, 775.0,
				1450.0, 875.0, 1425.0, 875.0, 1425.0, 850.0, 1400.0, 850.0, 1400.0, 825.0, 1350.0, 825.0, 1350.0, 800.0,
				1325.0, 800.0, 1325.0, 825.0, 1300.0, 825.0, 1300.0, 800.0, 1225.0, 800.0, 1225.0, 775.0, 1225.0, 750.0,
				1175.0, 750.0, 1175.0, 725.0, 1200.0, 725.0, 1200.0, 700.0, 1225.0, 700.0 });
		assertEquals(result.length == ps.get(0).position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(ps.get(0).position[i].equals(result[i]), true);
		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		for (int i = 0; i < ps.size(); i++) {
			origin = ImageDrawUtil.drawPolygonOutline(origin, ps.get(i), colorStr);
		}
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testAPolygon8");
	}
}
