package com.dy.ImageUtil.GeoTiff;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Stack;

import com.dy.ImageUtil.TiffUtil;
import com.dy.Util.MatrixUtil;

public class Process extends Config {

	final static String[] color = { "#FFB6C1", "#FFFF00", "#99FF00", "#9999FF", "#FF6600", "#990000", "#0000FF",
			"#33CCFF", "#FF9966", "#00FF33" };
	private final Rectangle[][] matrix;

	private int currentIndex = 0;

	private final Compare fatherThread;

	Process(Rectangle[][] matrix, Compare fatherThread) {
		this.matrix = matrix;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] != null) {
					if (!matrix[i][j].isImage) {
						System.out.println("${i:" + i + ",j:" + j + "}");
					}
				}
			}
		}
		this.fatherThread = fatherThread;
	}

	Process(Rectangle[] rec, int row, int column, Compare fatherThread) {
		this((Rectangle[][]) MatrixUtil.matrixReshape(MatrixUtil.reformate(rec, Rectangle.class), Rectangle.class, row,
				column), fatherThread);
	}

	public void merge() {

		findBorder();

		printBorder();

		// test();

		sortBorder();

	}

	private void printBorder() {
		BufferedImage origin = fatherThread.img1[0];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] != null) {
					int index = (int) matrix[i][j].getConfig("RegionIndex");
					int mode = index % 10;
					origin = ImageDrawUtil.drawRectangleOutline(origin, matrix[i][j], color[mode]);
				}
			}
		}
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/test");
	}

	/**
	 * 检测结果是否正确
	 */
	@SuppressWarnings("unused")
	private void test() {
		StringBuilder ass = new StringBuilder();

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == null) {
					ass.append("*    ");
				} else {
					int index = (int) matrix[i][j].getConfig("RegionIndex");
					if (index < 10) {
						ass.append(index + "    ");
					} else if (index >= 10 && index < 100) {
						ass.append((index + "   "));
					} else if (index >= 100 && index < 1000) {
						ass.append((index + "  "));
					} else if (index >= 1000 && index < 10000) {
						ass.append((index + " "));
					} else {
						ass.append((index));
					}
				}
				if (j != matrix[i].length - 1) {
					ass.append(',');
				}
			}
			ass.append("\n");
		}
		String out = ass.toString();
		File file = new File("/home/dy/Desktop/123.txt");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintStream ps;
		try {
			ps = new PrintStream(new FileOutputStream(file));
			ps.write(out.getBytes());
			ps.flush();
			ps.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] tmp1 = out.split("\n");
		String[][] tmp2 = new String[matrix.length][matrix[0].length];
		for (int i = 0; i < tmp2.length; i++) {
			String[] tmp3 = tmp1[i].split(",");
			for (int j = 0; j < tmp2[i].length; j++) {
				tmp2[i][j] = tmp3[j];
			}
		}
		L1: for (int i = 0; i < tmp2.length; i++) {
			for (int j = 0; j < tmp2[i].length; j++) {
				String current = tmp2[i][j];
				if (current.equals("*    "))
					continue;
				try {
					String top = tmp2[i - 1][j];
					if (!top.equals("*    ") && !top.equals(current)) {
						System.out.println("i: " + (i - 1) + " j: " + j + " 处有错误  Value = " + top);
						break L1;
					}
				} catch (IndexOutOfBoundsException e) {
				}
				try {
					String bottom = tmp2[i + 1][j];
					if (!bottom.equals("*    ") && !bottom.equals(current)) {
						System.out.println("i: " + (i + 1) + " j: " + j + " 处有错误  Value = " + bottom);
						break L1;
					}
				} catch (IndexOutOfBoundsException e) {
				}
				try {
					String left = tmp2[i][j - 1];
					if (!left.equals("*    ") && !left.equals(current)) {
						System.out.println("i: " + i + " j: " + (j - 1) + " 处有错误  Value = " + left);
						break L1;
					}
				} catch (IndexOutOfBoundsException e) {
				}
				try {
					String right = tmp2[i][j + 1];
					if (!right.equals("*    ") && !right.equals(current)) {
						System.out.println("i: " + i + " j: " + (j + 11) + " 处有错误  Value = " + right);
						break L1;
					}
				} catch (IndexOutOfBoundsException e) {
				}
			}
		}
	}

	private void findBorder() {
		for (int i = 0; i < matrix.length; i++) {
			L1: for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == null) {
					continue;
				} else {
					try {
						int ret = findBorder(i, j);
						if (ret != -1) {
							int wrong = (int) matrix[i][j].getConfig("RegionIndex");
							try {
								forceUpdate(i, j, ret, wrong);
							} catch (StackOverflowError e) {
								updateAgine(i, j, ret, wrong);
							}
						}
					} catch (StackOverflowError e) {
						continue L1;
					}
				}
			}
		}
	}

	private void updateAgine(int i, int j, int right, int wrong) {
		if (matrix[i][j] == null)
			return;
		for (int k = 0; k < i; k++) {
			for (int z = 0; z < j; z++) {
				if (matrix[k][z] == null)
					continue;
				Object current = matrix[k][z].getConfig("RegionIndex");
				if (current != null && (int) current == wrong) {
					matrix[k][z].setConfig("RegionIndex", right);
				}
			}
		}
	}

	private int findBorder(int i, int j) {
		return findBorder(i, j, -1);
	}

	/**
	 * 确定边界矩形
	 * 
	 * @param i     行号
	 * @param j     列号
	 * @param index 矩形所在的多边形的位置
	 */
	private int findBorder(int i, int j, int index) {
		if (matrix[i][j] == null)
			return -1;
		Object current = matrix[i][j].getConfig("RegionIndex");
		if (current != null) {
			if (index != (int) current) {
				return (int) current;
			} else {
				return -1;
			}
		}
		if (index == -1) {
			index = lookAround(i, j);
			if (index == -1) {
				index = ++currentIndex;
			}
		}

		matrix[i][j].setConfig("RegionIndex", index);
		try {
			int top = findBorder(i - 1, j, index);
			if (top != -1) {
				return top;
			}
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			int bottom = findBorder(i + 1, j, index);
			if (bottom != -1) {
				return bottom;
			}
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			int left = findBorder(i, j - 1, index);
			if (left != -1) {
				return left;
			}
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			int right = findBorder(i, j + 1, index);
			if (right != -1) {
				return right;
			}
		} catch (IndexOutOfBoundsException e) {
		}
		return -1;
	}

	private void forceUpdate(int i, int j, int right, int wrong) {
		if (matrix[i][j] == null)
			return;
		Object current = matrix[i][j].getConfig("RegionIndex");
		if ((int) current == right)
			return;

		matrix[i][j].setConfig("RegionIndex", right);
		try {
			forceUpdate(i - 1, j, right, wrong);
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			forceUpdate(i + 1, j, right, wrong);
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			forceUpdate(i, j - 1, right, wrong);
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			forceUpdate(i, j + 1, right, wrong);
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private void sortBorder() {
		LocationProjection lp = new LocationProjection();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == null) {
					continue;
				} else {
					Rectangle target = matrix[i][j];
					lp.addRectangle((int) target.getConfig("RegionIndex"), target);
				}
			}
		}
		lp.printRectangle();
		Integer[] indexs = lp.getAllIndex();
		int ignoreElement = (int) fatherThread.getConfig("ignoreElementCount");
		Stack<Polygon> all = new Stack<Polygon>();
		for (int i = 0; i < indexs.length; i++) {
			Integer index = indexs[i];
			if (lp.getSize(index) <= ignoreElement) {
				lp.removeInteger(index);
			} else {
				Stack<Polygon> back = Process.transformPolygon(lp.getIndex(index));
				if (back.size() > 0) {
					while (!back.isEmpty()) {
						all.push(back.pop());
					}
				}
			}
		}
		fatherThread.setConfig("CompareRsult", all.toArray(new Polygon[all.size()]));
		printPolygon(all, -1);
		
	}

	private void printPolygon(Stack<Polygon> target, int index) {
		BufferedImage origin = fatherThread.img1[0];
		if (index == -1) {
			while (!target.isEmpty()) {
				origin = ImageDrawUtil.drawPolygonOutline(origin, target.pop(), color[++index % 10]);
			}
			index = -1;
		} else {
			String colorStr = color[index % 10];
			while (!target.isEmpty()) {
				origin = ImageDrawUtil.drawPolygonOutline(origin, target.pop(), colorStr);
			}
		}
		TiffUtil.saveTif(origin, 0, "/data/DownLoad/001.tif", "/home/dy/Desktop/testImage/testP" + index);
	}

	public static Stack<Polygon> transformPolygon(Stack<Rectangle> input) {
		int size = input.size();
		Stack<Polygon> stack = new Stack<>();
		for (int i = 0; i < size; i++) {
			Rectangle target = input.get(i);
			if (i == 0) {
				stack.push(new Polygon(target.toPointArray()));
			} else {
				if (Process.testStack(stack, target) == false) {
					stack.push(new Polygon(target.toPointArray()));
				}
			}
		}
		return stack;
	}

	/**
	 * 对比当前的合并多边形,如果传入矩形不与任何已存在的多边形相交 返回false
	 * 
	 * @param stack
	 * @param target
	 * @return
	 */
	static boolean testStack(Stack<Polygon> stack, Rectangle target) {
		Stack<Integer> count = new Stack<>();
		L1: for (int i = 0; i < stack.size(); i++) {
			Polygon current = stack.get(i);
			int[] left = { -1, -1, -1 }, right = { -1, -1, -1 }, outter = { -1, -1 };
			Point[] points = current.position, recPoint = target.toPointArray();
			Point west = recPoint[0], east = recPoint[1];
			for (int j = points.length - 1; j >= 0; j--) {
				int next = j == 0 ? points.length - 1 : j - 1;
				if (left[2] == -1) {
					if (west.equals(points[j])) {
						left[0] = left[1] = j;
						left[2] = 0;
					} else {
						if (Process.inLine(points[j], points[next], west)) {
							if (west.equals(points[next])) {
								left[0] = left[1] = next;
								left[2] = 0;
							} else {
								left[0] = j;
								left[1] = next;
								left[2] = 0;
							}
						}
					}
				}
				if (right[2] == -1) {
					if (east.equals(points[j])) {
						right[0] = right[1] = j;
						right[2] = 0;
					} else {
						if (Process.inLine(points[j], points[next], east)) {
							if (east.equals(points[next])) {
								right[0] = right[1] = next;
								right[2] = 0;
							} else {
								right[0] = j;
								right[1] = next;
								right[2] = 0;
							}
						}
					}
				}
				if (Process.inLine(east, west, points[j])) {
					if (outter[0] == -1) {
						outter[0] = j;
					} else {
						outter[1] = j;
					}
				}
				if ((right[0] != -1 && left[0] != -1))
					break;
			}
			Point[] bigger = new Point[points.length + 4];
			if (left[0] != -1 && right[0] != -1) {
				count.push(i);
				if (left[0] == left[1] && right[0] == right[1]) {
					if (outter[0] != -1 && outter[1] != -1) {
						for (int j = 0, tureSize = 0, i_index = 0, max = Math.max(left[0], outter[0]), min = Math
								.min(right[0], outter[1]); j < bigger.length; j++) {
							if (i_index < min || i_index > max) {
								bigger[j] = points[i_index++];
							} else {
								bigger[j++] = points[min];
								bigger[j++] = recPoint[2];
								bigger[j++] = recPoint[3];
								bigger[j] = points[max];
								i_index = max + 1;
								tureSize += 3;
							}
							tureSize++;
							if (i_index == points.length) {
								current.position = new Point[tureSize];
								for (int k = 0; k < tureSize; k++) {
									current.position[k] = bigger[k];
								}
								break;
							}
						}
					} else {
						points[left[0]] = recPoint[3];
						points[right[0]] = recPoint[2];
					}
				} else if (left[0] != left[1] && right[0] != right[1]) {
					for (int j = 0, tureSize = 0, i_index = 0, max = left[0], min = right[1]; j < bigger.length; j++) {
						if (i_index < min || i_index > max) {
							bigger[j] = points[i_index++];
						} else {
							bigger[j++] = points[min];
							bigger[j++] = recPoint[1];
							bigger[j++] = recPoint[2];
							bigger[j++] = recPoint[3];
							bigger[j++] = recPoint[0];
							bigger[j] = points[max];
							i_index = max + 1;
							tureSize += 5;
						}
						tureSize++;
						if (i_index == points.length) {
							current.position = new Point[tureSize];
							for (int k = 0; k < tureSize; k++) {
								current.position[k] = bigger[k];
							}
							break;
						}
					}
				} else if (left[0] != left[1]) {
					for (int j = 0, tureSize = 0, i_index = 0, max = Math.max(left[0], right[0]), min = Math
							.min(left[1], right[1]); j < bigger.length; j++) {
						if (i_index < min || i_index > max) {
							bigger[j] = points[i_index++];
						} else {
							if (left[1] != right[1]) {
								bigger[j++] = points[min];
								tureSize++;
							}
							bigger[j++] = recPoint[2];
							bigger[j++] = recPoint[3];
							bigger[j++] = recPoint[0];
							bigger[j] = points[max];
							i_index = max + 1;
							tureSize += 3;
						}
						tureSize++;
						if (i_index == points.length) {
							current.position = new Point[tureSize];
							for (int k = 0; k < tureSize; k++) {
								current.position[k] = bigger[k];
							}
							break;
						}
					}
				} else if (right[0] != right[1]) {
					for (int j = 0, tureSize = 0, i_index = 0, max = Math.max(left[0], right[0]), min = Math
							.min(left[1], right[1]); j < bigger.length; j++) {
						if (i_index < min || i_index > max) {
							bigger[j] = points[i_index++];
						} else {
							bigger[j++] = points[min];
							bigger[j++] = recPoint[1];
							bigger[j++] = recPoint[2];
							bigger[j] = recPoint[3];
							if (left[0] != right[0]) {
								bigger[++j] = points[max];
								tureSize++;
							}
							tureSize += 3;
							i_index = max + 1;
						}
						tureSize++;
						if (i_index == points.length) {
							current.position = new Point[tureSize];
							for (int k = 0; k < tureSize; k++) {
								current.position[k] = bigger[k];
							}
							break;
						}
					}
				}
				break L1;
			} else if (left[0] != -1 && right[0] == -1) {
				if (left[0] == left[1] && outter[0] == left[1] && outter[1] == -1)
					continue;
				count.push(i);
				int max = left[0];
				int min = left[1];
				if (outter[0] != -1 && outter[1] != -1) {
					max = Math.max(max, outter[0]);
					min = Math.min(min, outter[1]);
					for (int j = 0, tureSize = 0, i_index = 0; j < bigger.length; j++) {
						if (i_index < min || i_index > max) {
							bigger[j] = points[i_index++];
						} else {
							boolean test = recPoint[0].equals(points[max]);
							bigger[j++] = points[min];
							bigger[j++] = recPoint[1];
							bigger[j++] = recPoint[2];
							bigger[j++] = recPoint[3];
							bigger[j++] = recPoint[0];
							if (!test) {
								bigger[j] = points[max];
							} else {
								j--;
							}
							i_index = max + 1;
							tureSize += test ? 4 : 5;
						}
						tureSize++;
						if (i_index == points.length) {
							current.position = new Point[tureSize];
							for (int k = 0; k < tureSize; k++) {
								current.position[k] = bigger[k];
							}
							break;
						}
					}
				} else {
					if (left[0] != left[1]) {
						for (int j = 0, tureSize = 0, i_index = 0; j < bigger.length; j++) {
							if (i_index < min || i_index > max) {
								bigger[j] = points[i_index++];
							} else {
								bigger[j++] = points[min];
								bigger[j++] = recPoint[1];
								bigger[j++] = recPoint[2];
								bigger[j++] = recPoint[3];
								bigger[j++] = recPoint[0];
								bigger[j] = points[max];
								i_index = max + 1;
								tureSize += 5;
							}
							tureSize++;
							if (i_index == points.length) {
								current.position = new Point[tureSize];
								for (int k = 0; k < tureSize; k++) {
									current.position[k] = bigger[k];
								}
								break;
							}
						}
					} else {
						for (int j = 0, tureSize = 0, i_index = 0; j < bigger.length; j++) {
							if (i_index < min || i_index > max) {
								bigger[j] = points[i_index++];
							} else {
								bigger[j++] = recPoint[1];
								bigger[j++] = recPoint[2];
								bigger[j] = recPoint[3];
								i_index = max + 1;
								tureSize += 2;
							}
							tureSize++;
							if (i_index == points.length) {
								current.position = new Point[tureSize];
								for (int k = 0; k < tureSize; k++) {
									current.position[k] = bigger[k];
								}
								break;
							}
						}
					}
				}
			} else if (left[0] == -1 && right[0] != -1) {
				if (right[0] == right[1] && outter[0] == right[1] && outter[1] == -1)
					continue;
				count.push(i);
				int max = right[0];
				int min = right[1];
				if (outter[0] != -1 && outter[1] != -1) {
					max = Math.max(max, outter[0]);
					min = Math.min(min, outter[1]);
					for (int j = 0, tureSize = 0, i_index = 0; j < bigger.length; j++) {
						if (i_index < min || i_index > max) {
							bigger[j] = points[i_index++];
						} else {
							boolean test = recPoint[1].equals(points[min]);
							if (!test)
								bigger[j++] = points[min];
							bigger[j++] = recPoint[1];
							bigger[j++] = recPoint[2];
							bigger[j++] = recPoint[3];
							bigger[j++] = recPoint[0];
							bigger[j] = points[max];
							i_index = max + 1;
							tureSize += test ? 4 : 5;
						}
						tureSize++;
						if (i_index == points.length) {
							current.position = new Point[tureSize];
							for (int k = 0; k < tureSize; k++) {
								current.position[k] = bigger[k];
							}
							break;
						}
					}
				} else {
					if (right[0] != right[1]) {
						for (int j = 0, tureSize = 0, i_index = 0; j < bigger.length; j++) {
							if (i_index < min || i_index > max) {
								bigger[j] = points[i_index++];
							} else {
								bigger[j++] = points[min];
								bigger[j++] = recPoint[1];
								bigger[j++] = recPoint[2];
								bigger[j++] = recPoint[3];
								bigger[j++] = recPoint[0];
								bigger[j] = points[max];
								i_index = max + 1;
								tureSize += 5;
							}
							tureSize++;
							if (i_index == points.length) {
								current.position = new Point[tureSize];
								for (int k = 0; k < tureSize; k++) {
									current.position[k] = bigger[k];
								}
								break;
							}
						}
					} else {
						for (int j = 0, tureSize = 0, i_index = 0; j < bigger.length; j++) {
							if (i_index < min || i_index > max) {
								bigger[j] = points[i_index++];
							} else {
								bigger[j++] = recPoint[2];
								bigger[j++] = recPoint[3];
								bigger[j] = recPoint[0];
								i_index = max + 1;
								tureSize += 2;
							}
							tureSize++;
							if (i_index == points.length) {
								current.position = new Point[tureSize];
								for (int k = 0; k < tureSize; k++) {
									current.position[k] = bigger[k];
								}
								break;
							}
						}
					}
				}
			} else if (outter[0] != -1 && outter[1] != -1) {
				count.push(i);
				for (int j = 0, tureSize = 0, i_index = 0, max = outter[0], min = outter[1]; j < bigger.length; j++) {
					if (i_index < min || i_index > max) {
						bigger[j] = points[i_index++];
					} else {
						bigger[j++] = points[min];
						bigger[j++] = recPoint[1];
						bigger[j++] = recPoint[2];
						bigger[j++] = recPoint[3];
						bigger[j++] = recPoint[0];
						bigger[j] = points[max];
						i_index = max + 1;
						tureSize += 5;
					}
					tureSize++;
					if (i_index == points.length) {
						current.position = new Point[tureSize];
						for (int k = 0; k < tureSize; k++) {
							current.position[k] = bigger[k];
						}
						break;
					}
				}
			}
		}
		if (count.size() > 1) {
			Polygon base = stack.get(count.get(0));
			for (int i = 1; i < count.size(); i++) {
				base = Process.mergePolygon(base, stack.get((int) count.get(i)), target);
			}
			while (count.size() > 1) {
				stack.remove((int) count.pop());
			}
			stack.set((int) count.get(0), base);
		}
		return count.size() > 0;
	}

	/**
	 * 合并两个多边形
	 * 
	 * @param one
	 * @param two
	 * @param rec
	 * @return
	 */
	static Polygon mergePolygon(Polygon one, Polygon two, Rectangle rec) {
		Polygon right, left;
		int[] rightIndex = Process.findIndex(one, rec), leftIndex = Process.findIndex(two, rec), tmpIndex = rightIndex;
		if (rightIndex[2] == 1 || one.position[rightIndex[0]-1].x >= one.position[rightIndex[0]].x) {
			right = two;
			left = one;
			rightIndex = leftIndex;
			leftIndex = tmpIndex;
		} else {
			right = one;
			left = two;
		}
		Stack<Point> pts = new Stack<Point>();
		int index = 0;
		int start = rightIndex[1] + 1, end = rightIndex[0] - 1;
		start = start < right.position.length ? start : 0;
		end = end >= 0 ? end : right.position.length;
		if (right.position[start].y == right.position[rightIndex[1]].y
				&& right.position[start].x > right.position[rightIndex[1]].x) {
			tmpIndex = Process.findIndex(right.position[start], right.position[end], left);
			while (index <= tmpIndex[0]) {
				pts.push(left.position[index++]);
			}
			index = start;
			while (index < right.position.length) {
				pts.push(right.position[index++]);
			}
			index = 0;
			while (index <= end) {
				pts.push(right.position[index++]);
			}
			index = tmpIndex[1];
			while (index < left.position.length) {
				pts.push(left.position[index++]);
			}
		} else {
			while (index < rightIndex[0]) {
				pts.push(right.position[index++]);
			}
			index = leftIndex[1] + 1;
			while (index < left.position.length) {
				pts.push(left.position[index++]);
			}
			index = 0;
			while (index < leftIndex[0]) {
				pts.push(left.position[index++]);
			}
			index = leftIndex[2] == 1 ? (rightIndex[0] + 1) : (rightIndex[0]);
			while (index < right.position.length) {
				pts.push(right.position[index++]);
			}
		}
		right.position = pts.toArray(new Point[pts.size()]);
		return right;
	}

	/**
	 * 找到两点在一个多边形内的某一点位置位置
	 * 
	 * @param point
	 * @param point2
	 * @param left
	 * @return
	 */
	static int[] findIndex(Point start, Point end, Polygon left) {
		int[] ret = new int[] {-1,-1};
		for (int i = 0; i < left.position.length; i++) {
			int next = -1;
			if (i == left.position.length - 1) {
				next = 0;
			} else {
				next = i + 1;
			}
			if (Process.inLine(left.position[i], left.position[next], start)) {
				ret[0] = i;
			}
			if(Process.inLine(left.position[i], left.position[next], end)) {
				ret[1] = next;
			}
			if(ret[0]!= -1 && ret[1] != -1) 
				return ret;
		}
		return ret;
	}

	/**
	 * 找到矩形在目标多边形的位置
	 * 
	 * @param pol
	 * @param rec
	 * @return
	 */
	static int[] findIndex(Polygon pol, Rectangle rec) {
		int[] ret = new int[] { -1, -1, 0 };
		Point[] position = rec.toPointArray();
		for (int i = 0; i < pol.position.length; i++) {
			if (position[0].equals(pol.position[i])) {
				ret[1] = i;
			} else if (position[1].equals(pol.position[i])) {
				ret[0] = i;
			} else if (position[2].equals(pol.position[i])) {
				if (ret[0] == -1) {
					ret[0] = i;
					ret[2] = 1;
				} else {
					ret[1] = i;
				}
			} else if (position[3].equals(pol.position[i])) {
				ret[1] = i;
			}
		}
		return ret;
	}

	/**
	 * 单纯矩形线判断点是否在线上
	 * 
	 * @param first
	 * @param second
	 * @param target
	 * @return
	 */
	static boolean inLine(Point first, Point second, Point target) {
		if (first.y == second.y && target.y == second.y) {
			return target.x >= Math.min(first.x, second.x) && target.x <= Math.max(first.x, second.x);
		}
		return false;
	}

	private int lookAround(int i, int j) {

		int ret = -1;
		try {
			Rectangle top = matrix[i - 1][j];
			if (top != null) {
				if (ret == -1) {
					Object tp = top.getConfig("RegionIndex");
					if (tp != null)
						ret = (int) tp;
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			Rectangle bottom = matrix[i + 1][j];
			if (bottom != null) {
				if (ret == -1) {
					Object tp = bottom.getConfig("RegionIndex");
					if (tp != null)
						ret = (int) tp;
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			Rectangle left = matrix[i][j - 1];
			if (left != null) {
				if (ret == -1) {
					Object tp = left.getConfig("RegionIndex");
					if (tp != null)
						ret = (int) tp;
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			Rectangle right = matrix[i][j + 1];
			if (right != null) {
				if (ret == -1) {
					Object tp = right.getConfig("RegionIndex");
					if (tp != null)
						ret = (int) tp;
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}
		return ret;
	}

	static Rectangle[] join(Rectangle[] origin, Rectangle[] input) {
		for (int i = 0; i < origin.length; i++) {
			if (origin[i] == null && input[i] != null) {
				origin[i] = input[i];
			}
		}
		return origin;
	}

	public static void main(String[] args) {
	}
}
