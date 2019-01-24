package com.dy.ImageUtil.GeoTiff;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Stack;

import com.dy.ImageUtil.TiffUtil;

public class LocationProjection extends Config {
	private HashSet<Integer> set;

	LocationProjection() {
		set = new HashSet<Integer>();
	}

	public Integer[] getAllIndex() {
		return set.toArray(new Integer[set.size()]);
	}

	@SuppressWarnings("unchecked")
	public Stack<Rectangle> getIndex(Integer input) {
		return ((Stack<Rectangle>) getConfig(input.toString()));
	}

	public int getSize(Integer input) {
		if (set.contains(input)) {
			return (int) getConfig(input.toString() + "size");
		} else {
			return -1;
		}
	}

	@SuppressWarnings("unchecked")
	public void addRectangle(Integer input, Rectangle in) {
		Stack<Rectangle> stack;
		if (set.add(input)) {
			stack = new Stack<Rectangle>();
			stack.push(in);
			setConfig(input.toString(), stack);
			setConfig(input.toString() + "size", 1);
		} else {
			stack = (Stack<Rectangle>) getConfig(input.toString());
			Rectangle top = stack.pop();
			if (top.rightTo(in)) {
				top = Rectangle.simpleUnion(top, in);
				stack.push(top);
			} else {
				stack.push(top);
				stack.push(in);
			}
			int size = (int) getConfig(input.toString() + "size");
			setConfig(input.toString() + "size", ++size);
		}
	}

	public void removeInteger(Integer input) {
		set.remove(input);
		removeConfig(input.toString());
		removeConfig(input.toString() + "size");
	}

	public void printRectangle() {
		String imgurl = "/data/DownLoad/001.tif";
		BufferedImage origin = TiffUtil.loadTiff(imgurl)[0];
		Integer[] a = getAllIndex();
		for (int i = 0; i < a.length; i++) {
			Integer as = a[i];
			if(as == 17) {
				System.out.println();
			}
			String colorStr = Process.color[as%10];
			Stack<Rectangle> tmp = getIndex(as);
			for (int j = 0; j < tmp.size(); j++) {
				origin = ImageDrawUtil.drawRectangleOutline(origin, tmp.get(j), colorStr);
			}
		}
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testMergedRec");
	}

	public String toString() {
		return set.toString();
	}
}