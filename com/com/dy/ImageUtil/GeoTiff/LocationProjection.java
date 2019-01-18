package com.dy.ImageUtil.GeoTiff;

import java.util.HashSet;
import java.util.Stack;

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

	public String toString() {
		return set.toString();
	}
}