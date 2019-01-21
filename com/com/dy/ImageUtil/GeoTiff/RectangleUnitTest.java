package com.dy.ImageUtil.GeoTiff;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RectangleUnitTest {

	int lenght = (int) Math.floor(Math.random() * 100);

	Rectangle middle = new Rectangle(0, 0, lenght, lenght);

	Rectangle top = new Rectangle(0, lenght, lenght, lenght * 2);
	Rectangle bottom = new Rectangle(0, -lenght, lenght, 0);
	Rectangle right = new Rectangle(lenght, 0, lenght * 2, lenght);
	Rectangle left = new Rectangle(-lenght, 0, 0, lenght);

	Rectangle X = new Rectangle(-lenght, 0, lenght * 2, lenght);
	Rectangle Y = new Rectangle(0, -lenght, lenght, lenght * 2);

	@Test
	void test() {
		assertEquals(middle.rightTo(middle), false);
		assertEquals(middle.leftTo(middle), false);
		assertEquals(middle.topTo(middle), false);
		assertEquals(middle.bottomTo(middle), false);
		
		assertEquals(middle.rightTo(right), true);
		assertEquals(middle.leftTo(right), false);
		assertEquals(middle.topTo(right), false);
		assertEquals(middle.bottomTo(right), false);

		assertEquals(middle.leftTo(left), true);
		assertEquals(middle.rightTo(left), false);
		assertEquals(middle.topTo(left), false);
		assertEquals(middle.bottomTo(left), false);

		assertEquals(middle.topTo(top), true);
		assertEquals(middle.rightTo(top), false);
		assertEquals(middle.leftTo(top), false);
		assertEquals(middle.bottomTo(top), false);

		assertEquals(middle.bottomTo(bottom), true);
		assertEquals(middle.rightTo(bottom), false);
		assertEquals(middle.leftTo(bottom), false);
		assertEquals(middle.topTo(bottom), false);
	}

	@Test
	void test2() {
		Rectangle mixX = Rectangle.simpleUnion(middle, left);
		mixX = Rectangle.simpleUnion(mixX, right);
		assertEquals(Rectangle.equals(mixX, X), true);
		Rectangle mixY = Rectangle.simpleUnion(middle, top);
		mixY = Rectangle.simpleUnion(mixY, bottom);
		assertEquals(Rectangle.equals(mixY, Y), true);

	}

	@Test
	void testBottomIntersection() {
		Rectangle bottomMore = new Rectangle(-lenght, lenght, lenght, lenght * 2);
		Point[] ret = middle.bottomIntersection(bottomMore);
		assertEquals(ret[0].equals(middle.southwest()), true);
		assertEquals(ret[1].equals(middle.southeast()), true);
		Rectangle bottomLess = new Rectangle(lenght / 4, lenght, lenght / 2, lenght * 2);
		ret = middle.bottomIntersection(bottomLess);
		assertEquals(ret[0].equals(bottomLess.northwest()), true);
		assertEquals(ret[1].equals(bottomLess.northeast()), true);
		Rectangle bottom1 = new Rectangle(0, lenght, lenght * 2, lenght * 2);
		ret = middle.bottomIntersection(bottom1);
		assertEquals(ret[0].equals(bottom1.northwest()), true);
		assertEquals(ret[0].equals(middle.southwest()), true);
		assertEquals(ret[1].equals(middle.southeast()), true);
		Rectangle bottom2 = new Rectangle(-lenght, lenght, lenght, lenght * 2);
		ret = middle.bottomIntersection(bottom2);
		assertEquals(ret[0].equals(middle.southwest()), true);
		assertEquals(ret[1].equals(bottom2.northeast()), true);
		assertEquals(ret[1].equals(middle.southeast()), true);
		Rectangle bottomUntouch1 = new Rectangle(-lenght * 2, lenght, -lenght, lenght * 2);
		ret = middle.bottomIntersection(bottomUntouch1);
		assertEquals(ret == null, true);
		Rectangle bottomUntouch2 = new Rectangle(lenght * 2, lenght, lenght * 3, lenght * 2);
		ret = middle.bottomIntersection(bottomUntouch2);
		assertEquals(ret == null, true);
	}

	@Test
	void testTopIntersection() {
		Rectangle topMore = new Rectangle(-lenght, -lenght, lenght, 0);
		Point[] ret = middle.topIntersection(topMore);
		assertEquals(ret[0].equals(middle.northwest()), true);
		assertEquals(ret[1].equals(middle.northeast()), true);
		Rectangle topLess = new Rectangle(lenght / 4, -lenght, lenght / 2, 0);
		ret = middle.topIntersection(topLess);
		assertEquals(ret[0].equals(topLess.southwest()), true);
		assertEquals(ret[1].equals(topLess.southeast()), true);
		Rectangle top1 = new Rectangle(0, -lenght, lenght * 2, 0);
		ret = middle.topIntersection(top1);
		assertEquals(ret[0].equals(top1.southwest()), true);
		assertEquals(ret[0].equals(middle.northwest()), true);
		assertEquals(ret[1].equals(middle.northeast()), true);
		Rectangle top2 = new Rectangle(-lenght, -lenght, lenght, 0);
		ret = middle.topIntersection(top2);
		assertEquals(ret[0].equals(middle.northwest()), true);
		assertEquals(ret[1].equals(top2.southeast()), true);
		assertEquals(ret[1].equals(middle.northeast()), true);
		Rectangle topUntouch1 = new Rectangle(-lenght * 2, -lenght, -lenght, 0);
		ret = middle.topIntersection(topUntouch1);
		assertEquals(ret == null, true);
		Rectangle topUntouch2 = new Rectangle(lenght * 2, -lenght, lenght * 3, 0);
		ret = middle.topIntersection(topUntouch2);
		assertEquals(ret == null, true);
	}

	@Test
	void testRightIntersection() {
		Rectangle rightMore = new Rectangle(lenght, -lenght, lenght * 2, lenght * 2);
		Point[] ret = middle.rightIntersection(rightMore);
		assertEquals(ret[0].equals(middle.northeast()), true);
		assertEquals(ret[1].equals(middle.southeast()), true);
		Rectangle rightLess = new Rectangle(lenght, lenght / 4, lenght * 2, lenght / 2);
		ret = middle.rightIntersection(rightLess);
		assertEquals(ret[0].equals(rightLess.northwest()), true);
		assertEquals(ret[1].equals(rightLess.southwest()), true);
		Rectangle right1 = new Rectangle(lenght, 0, lenght * 2, lenght * 2);
		ret = middle.rightIntersection(right1);
		assertEquals(ret[0].equals(middle.northeast()), true);
		assertEquals(ret[0].equals(right1.northwest()), true);
		assertEquals(ret[1].equals(middle.southeast()), true);
		Rectangle right2 = new Rectangle(lenght, -lenght, lenght * 2, lenght);
		ret = middle.rightIntersection(right2);
		assertEquals(ret[0].equals(middle.northeast()), true);
		assertEquals(ret[1].equals(right2.southwest()), true);
		assertEquals(ret[1].equals(middle.southeast()), true);
		Rectangle rightUntouch1 = new Rectangle(lenght * 2, 0, lenght * 3, lenght);
		ret = middle.rightIntersection(rightUntouch1);
		assertEquals(ret == null, true);
		Rectangle rightUntouch2 = new Rectangle(-lenght * 3, 0, -lenght * 2, lenght);
		ret = middle.rightIntersection(rightUntouch2);
		assertEquals(ret == null, true);
	}

	@Test
	void testLeftIntersection() {
		Rectangle leftMore = new Rectangle(-lenght, -lenght, 0, lenght * 2);
		Point[] ret = middle.leftIntersection(leftMore);
		assertEquals(ret[0].equals(middle.northwest()), true);
		assertEquals(ret[1].equals(middle.southwest()), true);
		Rectangle leftLess = new Rectangle(-lenght, lenght / 4, 0, lenght / 2);
		ret = middle.leftIntersection(leftLess);
		assertEquals(ret[0].equals(leftLess.northeast()), true);
		assertEquals(ret[1].equals(leftLess.southeast()), true);
		Rectangle left1 = new Rectangle(-lenght, 0, 0, lenght * 2);
		ret = middle.leftIntersection(left1);
		assertEquals(ret[0].equals(middle.northwest()), true);
		assertEquals(ret[0].equals(left1.northeast()), true);
		assertEquals(ret[1].equals(middle.southwest()), true);
		Rectangle left2 = new Rectangle(-lenght, -lenght, 0, lenght);
		ret = middle.leftIntersection(left2);
		assertEquals(ret[0].equals(middle.northwest()), true);
		assertEquals(ret[1].equals(left2.southeast()), true);
		assertEquals(ret[1].equals(middle.southwest()), true);
		Rectangle leftUntouch1 = new Rectangle(lenght * 2, 0, lenght * 3, lenght);
		ret = middle.leftIntersection(leftUntouch1);
		assertEquals(ret == null, true);
		Rectangle leftUntouch2 = new Rectangle(-lenght * 3, 0, -lenght * 2, lenght);
		ret = middle.leftIntersection(leftUntouch2);
		assertEquals(ret == null, true);
	}
}
