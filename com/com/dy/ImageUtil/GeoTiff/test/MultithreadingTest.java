package com.dy.ImageUtil.GeoTiff.test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;

import com.dy.ImageUtil.TiffUtil;
import com.dy.ImageUtil.GeoTiff.Compare;
import com.dy.ImageUtil.GeoTiff.Polygon;

class MultithreadingTest {

	@Test
	void test() {
//		BufferedImage origin = TiffUtil.loadTiff("/data/DownLoad/001.tif")[0],
//				origin2 = TiffUtil.loadTiff("/data/DownLoad/002.tif")[0];
//		BufferedImage[] first = new BufferedImage[10], secont = new BufferedImage[10];
//		for (int i = 0; i < first.length; i++) {
//			if (i % 2 == 0) {
//				first[i] = origin2;
//				secont[i] = origin;
//			} else {
//				first[i] = origin;
//				secont[i] = origin2;
//			}
//		}
//		assertEquals(TiffUtil.saveTif(first, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/001.tif"), true);
//		assertEquals(TiffUtil.saveTif(secont, 0, "/data/DownLoad/002.tif", "/home/dy/Desktop/testImage/002.tif"), true);
		Compare a = new Compare(90.82141185464081, 45.0769349657265, 93.97137099423514, 47.032121099508096,
				"/home/dy/Desktop/testImage/001.tif", "/home/dy/Desktop/testImage/002.tif");
		a.setConfig("maxThreadPool", 5);
		try {
			Polygon[] as = a.compare();
			assertEquals(as.length,95);
			for (int i = 0; i < as.length; i++) {
				System.out.println(as[i].toString());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
