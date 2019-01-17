package com.dy.ImageUtil.GeoTiff;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.dy.Util.MatrixUtil;

public class Process {

	private final Rectangle[][] matrix;

	private int currentIndex = 0;

	Process(Rectangle[][] matrix) {
		this.matrix = matrix;
	}

	Process(Rectangle[] rec, int row, int column) {
		this((Rectangle[][]) MatrixUtil.matrixReshape(MatrixUtil.reformate(rec, Rectangle.class), Rectangle.class, row,
				column));
	}

	public void merge() {

		findBorder();

		//test();

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

	/**
	 * 确定边界矩形
	 * 
	 * @param i     行号
	 * @param j     列号
	 * @param index 矩形所在的多边形的位置
	 */
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
		Process p = new Process(as);
		p.merge();
	}
}
