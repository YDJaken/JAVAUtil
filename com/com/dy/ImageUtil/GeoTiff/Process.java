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

	private final Rectangle[][] matrix;

	private int currentIndex = 0;

	private final Compare fatherThread;

	Process(Rectangle[][] matrix, Compare fatherThread) {
		this.matrix = matrix;
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
					int mode = index%10;
					origin = ImageDrawUtil.drawRectangleOutline(origin, matrix[i][j], "#"+mode+"f"+mode+"f"+mode+"f");
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
							forceUpdate(i, j, ret, wrong);
						}
					} catch (StackOverflowError e) {
						continue L1;
					}
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
		if (current == null || (int) current == right)
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
					Object current = target.getConfig("bounder");
					if (current == null) {
						current = testIndside(i, j);
					}
					if ((boolean) current) {
						lp.addRectangle((int) target.getConfig("RegionIndex"), target);
					}
				}
			}
		}
		Integer[] indexs = lp.getAllIndex();
		int ignoreElement = (int) fatherThread.getConfig("ignoreElementCount");
		for (int i = 0; i < indexs.length; i++) {
			Integer index = indexs[i];
			if (lp.getSize(index) <= ignoreElement) {
				lp.removeInteger(index);
			} else {
				transformPolygon(lp.getIndex(index));
			}
		}
	}

	private void transformPolygon(Stack<Rectangle> input) {
		int size = input.size();
		Stack<Polygon> stack = new Stack<>();
		for (int i = 0; i < size; i++) {
			Rectangle target = input.get(i);
			if (i == 0) {
				stack.push(new Polygon(target.toPointArray()));
			} else {
				if (!testStack(stack, target))
					stack.push(new Polygon(target.toPointArray()));
			}
		}
	}

	/**
	 * 对比当前的合并多边形,如果传入矩形不与任何已存在的多边形相交 返回false
	 * 
	 * @param stack
	 * @param target
	 * @return
	 */
	private boolean testStack(Stack<Polygon> stack, Rectangle target) {
		int count = 0;
		for (int i = 0; i < stack.size(); i++) {
			Polygon current = stack.get(i);
			int[] left = { -1, -1 }, right = { -1, -1 }, outter = { -1, -1 };
			Point[] points = current.position, recPoint = target.toPointArray();
			Point west = recPoint[0], east = recPoint[1];
			for (int j = points.length - 1; j >= 0; j--) {
				int next = j == 0 ? points.length - 1 : j - 1;
				if (right[0] != -1) {
					if (west.equals(points[j])) {
						left[0] = left[1] = j;
					} else {
						if (inLine(points[j], points[next], west)) {
							left[0] = j;
							left[1] = next;
						}
					}
				}
				if (left[0] != -1) {
					if (east.equals(points[j])) {
						right[0] = right[1] = j;
					} else {
						if (inLine(points[j], points[next], east)) {
							right[0] = j;
							right[1] = next;
						}
					}
				}
				if (inLine(east, west, points[j])) {
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
			if (outter[0] != -1 && outter[1] != -1) {
				count++;
				for (int j = 0, tureSize = 0, i_index = 0, max = outter[0], min = outter[1]; j < bigger.length; j++) {
					if (i_index <= min || i_index >= max) {
						bigger[j] = points[i_index++];
					} else {
						int pos = (j - min) + 1;
						if (pos <= 4) {
							if (pos == 4) {
								bigger[j] = recPoint[0];
								i_index = max;
							}
							bigger[j] = recPoint[pos];
						}
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
			} else if (left[0] != -1 && right[0] != -1) {
				count++;
				if (left[0] == left[1] && right[0] == right[1]) {
					points[left[0]] = recPoint[2];
					points[right[0]] = recPoint[3];
				} else if (left[0] != left[1] && right[0] != right[1]) {
					for (int j = 0, tureSize = 0, i_index = 0, max = left[1], min = right[0]; j < bigger.length; j++) {
						if (i_index <= min || i_index >= max) {
							bigger[j] = points[i_index++];
						} else {
							int pos = (j - min) + 1;
							if (pos <= 4) {
								if (pos == 4) {
									bigger[j] = recPoint[0];
									i_index = max;
								}
								bigger[j] = recPoint[pos];
							}
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
					// TODO 当右上角点与多边形的顶点相同时的逻辑
				} else if (right[0] != right[1]) {
					// TODO 当左上角点与多边形的顶点相同时的逻辑
				}
			} else if (left[0] == -1 && right[0] != -1) {
				count++;
				// TODO 当传入矩形只有左上角点在顶点或者边上时的逻辑
			} else if (left[0] != -1 && right[0] == -1) {
				count++;
				// TODO 当传入矩形只有右上角点在顶点或者边上时的逻辑
			}

		}
		if (count > 0) {
			// TODO 缺少多边形合并逻辑
		}
		return count > 0;
	}

	/**
	 * 单纯矩形线判断点是否在线上
	 * 
	 * @param first
	 * @param second
	 * @param target
	 * @return
	 */
	private boolean inLine(Point first, Point second, Point target) {
		if (first.y == second.y && target.y == second.y) {
			return target.x >= Math.min(first.x, second.x) && target.x <= Math.max(first.x, second.x);
		}
		return false;
	}

	private boolean testIndside(int i, int j) {
		short finnded = 0;
		try {
			if (matrix[i - 1][j] != null) {
				finnded++;
			}
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			if (matrix[i + 1][j] != null) {
				finnded++;
			}
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			if (matrix[i][j - 1] != null) {
				finnded++;
			}
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			if (matrix[i][j + 1] != null) {
				finnded++;
			}
		} catch (IndexOutOfBoundsException e) {
		}
		if (finnded == 4) {
			matrix[i][j].setConfig("bounder", false);
			return false;
		} else {
			matrix[i][j].setConfig("bounder", true);
			return true;
		}
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
		Rectangle[][] as = new Rectangle[70][70];
		as = (Rectangle[][]) MatrixUtil.generateMatrix(as, Rectangle.class, new Rectangle());
		Process p = new Process(as, new Compare());
		p.merge();
	}
}
