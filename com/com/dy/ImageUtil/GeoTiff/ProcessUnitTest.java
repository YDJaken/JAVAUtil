package com.dy.ImageUtil.GeoTiff;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.Stack;

import org.junit.jupiter.api.Test;

import com.dy.ImageUtil.TiffUtil;

class ProcessUnitTest {
	// 下包
	@Test
	void test1() {
		Point[] result = Point.fromArray(
				new double[] { 850, 50, 1000, 50, 1000, 75, 1125, 75, 1125, 100, 825, 100, 825, 75, 850, 75 });
		Polygon target = new Polygon(new Rectangle(850, 50, 1000, 75).toPointArray());
		Stack<Polygon> tt = new Stack<Polygon>();
		tt.push(target);
		assertEquals(Process.testStack(tt, new Rectangle(825, 75, 1125, 100)), true);
		assertEquals(result.length == target.position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(target.position[i].equals(result[i]), true);
		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		origin = ImageDrawUtil.drawPolygonOutline(origin, target, colorStr);
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testBotCase1");
	}

	// 下含
	@Test
	void test2() {
		Point[] result = Point
				.fromArray(new double[] { 850, 50, 1000, 50, 1000, 75, 900, 75, 900, 100, 875, 100, 875, 75, 850, 75 });
		Polygon target = new Polygon(new Rectangle(850, 50, 1000, 75).toPointArray());
		Stack<Polygon> tt = new Stack<Polygon>();
		tt.push(target);
		assertEquals(Process.testStack(tt, new Rectangle(875, 75, 900, 100)), true);
		assertEquals(result.length == target.position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(target.position[i].equals(result[i]), true);
		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		origin = ImageDrawUtil.drawPolygonOutline(origin, target, colorStr);
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testBotCase2");
	}

	// 下左含
	@Test
	void test3() {
		Point[] result = Point
				.fromArray(new double[] { 850, 50, 1000, 50, 1000, 75, 900, 75, 900, 100, 850, 100});
		Polygon target = new Polygon(new Rectangle(850, 50, 1000, 75).toPointArray());
		Stack<Polygon> tt = new Stack<Polygon>();
		tt.push(target);
		assertEquals(Process.testStack(tt, new Rectangle(850, 75, 900, 100)), true);
		assertEquals(result.length == target.position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(target.position[i].equals(result[i]), true);
		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		origin = ImageDrawUtil.drawPolygonOutline(origin, target, colorStr);
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testBotCase3");
	}

	// 下右含
	@Test
	void test4() {
		Point[] result = Point.fromArray(new double[] { 850, 50, 1000, 50, 1000, 100, 875, 100, 875, 75, 850, 75 });
		Polygon target = new Polygon(new Rectangle(850, 50, 1000, 75).toPointArray());
		Stack<Polygon> tt = new Stack<Polygon>();
		tt.push(target);
		assertEquals(Process.testStack(tt, new Rectangle(875, 75, 1000, 100)), true);
		assertEquals(result.length == target.position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(target.position[i].equals(result[i]), true);
		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		origin = ImageDrawUtil.drawPolygonOutline(origin, target, colorStr);
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testBotCase4");
	}

	// 下左包
	@Test
	void test5() {
		Point[] result = Point.fromArray(new double[] { 850, 50, 1000, 50, 1000, 75, 1100, 75, 1100, 100, 850, 100 });
		Polygon target = new Polygon(new Rectangle(850, 50, 1000, 75).toPointArray());
		Stack<Polygon> tt = new Stack<Polygon>();
		tt.push(target);
		assertEquals(Process.testStack(tt, new Rectangle(850, 75, 1100, 100)), true);
		assertEquals(result.length == target.position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(target.position[i].equals(result[i]), true);
		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		origin = ImageDrawUtil.drawPolygonOutline(origin, target, colorStr);
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testBotCase5");
	}

	// 下左突
	@Test
	void test6() {
		Point[] result = Point.fromArray(
				new double[] { 850, 50, 1000, 50, 1000, 75, 1100, 75, 1100, 100, 875, 100, 875, 75, 850, 75 });
		Polygon target = new Polygon(new Rectangle(850, 50, 1000, 75).toPointArray());
		Stack<Polygon> tt = new Stack<Polygon>();
		tt.push(target);
		assertEquals(Process.testStack(tt, new Rectangle(875, 75, 1100, 100)), true);
		assertEquals(result.length == target.position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(target.position[i].equals(result[i]), true);
		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		origin = ImageDrawUtil.drawPolygonOutline(origin, target, colorStr);
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testBotCase6");
	}

	// 下右包
	@Test
	void test7() {
		Point[] result = Point.fromArray(new double[] { 850, 50, 1000, 50, 1000, 100, 825, 100, 825, 75, 850, 75 });
		Polygon target = new Polygon(new Rectangle(850, 50, 1000, 75).toPointArray());
		Stack<Polygon> tt = new Stack<Polygon>();
		tt.push(target);
		assertEquals(Process.testStack(tt, new Rectangle(825, 75, 1000, 100)), true);
		assertEquals(result.length == target.position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(target.position[i].equals(result[i]), true);
		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		origin = ImageDrawUtil.drawPolygonOutline(origin, target, colorStr);
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testBotCase7");
	}

	// 下右突
	@Test
	void test8() {
		Point[] result = Point
				.fromArray(new double[] { 850, 50, 1000, 50, 1000, 75, 900, 75, 900, 100, 825, 100, 825, 75, 850, 75 });
		Polygon target = new Polygon(new Rectangle(850, 50, 1000, 75).toPointArray());
		Stack<Polygon> tt = new Stack<Polygon>();
		tt.push(target);
		assertEquals(Process.testStack(tt, new Rectangle(825, 75, 900, 100)), true);
		assertEquals(result.length == target.position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(target.position[i].equals(result[i]), true);
		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		origin = ImageDrawUtil.drawPolygonOutline(origin, target, colorStr);
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testBotCase8");
	}

	// 全包含
	@Test
	void test9() {
		Point[] result = Point.fromArray(new double[] { 850, 50, 1000, 50, 1000, 100, 850, 100 });
		Polygon target = new Polygon(new Rectangle(850, 50, 1000, 75).toPointArray());
		Stack<Polygon> tt = new Stack<Polygon>();
		tt.push(target);
		assertEquals(Process.testStack(tt, new Rectangle(850, 75, 1000, 100)), true);
		assertEquals(result.length == target.position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(target.position[i].equals(result[i]), true);
		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		origin = ImageDrawUtil.drawPolygonOutline(origin, target, colorStr);
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testBotCase9");
	}

	// 全部
	@Test
	void test10() {
		Point[] result = Point.fromArray(new double[] { 875, 25, 900, 25, 900, 50, 1000, 50, 1000, 75, 1125, 75, 1125,
				100, 1075, 100, 1075, 125, 825, 125, 825, 75, 850, 75, 850, 50, 875, 50 });
		Polygon target = new Polygon(new Rectangle(875, 25, 900, 50).toPointArray());
		Stack<Polygon> tt = new Stack<Polygon>();
		tt.push(target);
		assertEquals(Process.testStack(tt, new Rectangle(850, 50, 1000, 75)), true);
		assertEquals(Process.testStack(tt, new Rectangle(825, 75, 1125, 100)), true);
		assertEquals(Process.testStack(tt, new Rectangle(825, 100, 1075, 125)), true);
		assertEquals(result.length == target.position.length, true);
		for (int i = 0; i < result.length; i++) {
			assertEquals(target.position[i].equals(result[i]), true);
		}
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		String colorStr = Process.color[2];
		origin = ImageDrawUtil.drawPolygonOutline(origin, target, colorStr);
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testBotCase10");
	}

	
}
