package com.dy.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageUtill {

	/**
	 * @param fileUrl
	 * @return 读取到的缓存图像
	 * @throws IOException
	 */
	public static BufferedImage getBufferedImage(String fileUrl) throws IOException {
		File f = new File(fileUrl);
		return ImageIO.read(f);
	}

	/**
	 * @param savedImg
	 * @param saveDir
	 * @param fileName
	 * @param format
	 * @return
	 */
	public static void saveImage(BufferedImage savedImg, String saveDir, String fileName, String format) {
		String fileUrl = saveDir + fileName;
		File file = new File(fileUrl);
		try {
			ImageIO.write(savedImg, format, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 合并两个图片
	 * 
	 * @param img1
	 * @param img2
	 * @param isHorizontal
	 * @return 返回合并后的BufferedImage对象
	 * @throws IOException
	 */
	public static BufferedImage mergeImage(BufferedImage img1, BufferedImage img2, boolean isHorizontal)
			throws IOException {
		int w1 = img1.getWidth();
		int h1 = img1.getHeight();
		int w2 = img2.getWidth();
		int h2 = img2.getHeight();

		int[] ImageArrayOne = new int[w1 * h1];
		ImageArrayOne = img1.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1);
		int[] ImageArrayTwo = new int[w2 * h2];
		ImageArrayTwo = img2.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);

		BufferedImage DestImage = null;
		if (isHorizontal) {
			DestImage = new BufferedImage(w1 + w2, h1, BufferedImage.TYPE_INT_RGB);
			DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1);
			DestImage.setRGB(w1, 0, w2, h2, ImageArrayTwo, 0, w2);
		} else {
			DestImage = new BufferedImage(w1, h1 + h2, BufferedImage.TYPE_INT_RGB);
			DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1);
			DestImage.setRGB(0, h1, w2, h2, ImageArrayTwo, 0, w2);
		}
		return DestImage;
	}

	/**
	 * 合并两个图片
	 * 
	 * @param DestImage    合成的目标图片
	 * @param origin       原图片
	 * @param startH
	 * @param startWidth
	 * @param isHorizontal
	 * @return 返回合并后的BufferedImage对象
	 * @throws IOException
	 */
	public static void mergeImage(BufferedImage DestImage, BufferedImage origin, boolean isHorizontal, int startHeight,
			int startWidth) throws IOException {
		int w2 = origin.getWidth();
		int h2 = origin.getHeight();

		int[] ImageArrayTwo = new int[w2 * h2];
		ImageArrayTwo = origin.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);

		if (isHorizontal) {
			DestImage.setRGB(startWidth, 0, w2, h2, ImageArrayTwo, 0, w2);
		} else {
			DestImage.setRGB(0, startHeight, w2, h2, ImageArrayTwo, 0, w2);
		}
	}

	/**
	 * 合并两个图片
	 * 
	 * @param DestImage    合成的目标图片
	 * @param origin       原图片
	 * @param startH
	 * @param startWidth
	 * @param isHorizontal
	 * @return 返回合并后的BufferedImage对象
	 * @throws IOException
	 */
	public static void mergeImage(BufferedImage DestImage, BufferedImage origin, int startHeight, int startWidth)
			throws IOException {
		int w2 = origin.getWidth();
		int h2 = origin.getHeight();

		int[] ImageArrayTwo = new int[w2 * h2];
		ImageArrayTwo = origin.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);

		DestImage.setRGB(startWidth, startHeight, w2, h2, ImageArrayTwo, 0, w2);
	}

	/**
	 * 合并两个图片
	 * 
	 * @param img1
	 * @param img2
	 * @param isHorizontal
	 * @return 返回合并后的BufferedImage对象
	 * @throws IOException
	 */
	public static void mergeImage(BufferedImage DestImage, BufferedImage origin, int startHeight, int startWidth,
			boolean needModify) throws IOException {
		if (needModify == false) {
			ImageUtill.mergeImage(DestImage, origin, startHeight, startWidth);
		} else {
			int w2 = origin.getWidth();
			int h2 = origin.getHeight();

			int[] ImageArrayTwo = new int[w2 * h2];
			ImageArrayTwo = origin.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);
			for (int i = 0; i < ImageArrayTwo.length; i++) {
				if (ImageArrayTwo[i] == 0) {
					ImageArrayTwo[i] = 16777215;
				}
			}

			DestImage.setRGB(startWidth, startHeight, w2, h2, ImageArrayTwo, 0, w2);
		}
	}

	/**
	 * 合并两个图片
	 * 
	 * @param img1
	 * @param img2
	 * @param isHorizontal
	 * @return 返回合并后的BufferedImage对象
	 * @throws IOException
	 */
	public static void mergeImage(BufferedImage DestImage, BufferedImage origin, boolean isHorizontal, int startHeight,
			int startWidth, boolean needModify) throws IOException {
		if (needModify == false) {
			ImageUtill.mergeImage(DestImage, origin, isHorizontal, startHeight, startWidth);
		} else {
			int w2 = origin.getWidth();
			int h2 = origin.getHeight();

			int[] ImageArrayTwo = new int[w2 * h2];
			ImageArrayTwo = origin.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);
			for (int i = 0; i < ImageArrayTwo.length; i++) {
				if (ImageArrayTwo[i] == 0) {
					ImageArrayTwo[i] = 16777215;
				}
			}

			if (isHorizontal) {
				DestImage.setRGB(startWidth, 0, w2, h2, ImageArrayTwo, 0, w2);
			} else {
				DestImage.setRGB(0, startHeight, w2, h2, ImageArrayTwo, 0, w2);
			}

		}
	}

	private static int findFlag(int[] target) {
		HashMap<Integer, Integer> targetMapping = new HashMap<Integer, Integer>();
		targetMapping.put(-1, 0);
		targetMapping.put(0, 0);
		targetMapping.put(16777215, 0);
		for (int i = 0; i < 1000; i++) {
			Integer tmp = targetMapping.get(target[i]);
			if (tmp != null) {
				tmp++;
				targetMapping.put(target[i], tmp);
			}
		}
		Integer[] test = { targetMapping.get(-1), targetMapping.get(0), targetMapping.get(16777215) };
		Integer max = 0;
		int index = 0;
		for (int i = 0; i < test.length; i++) {
			if (test[i] == null)
				continue;
			if (max < test[i]) {
				max = test[i];
				index = i;
			}
		}
		int ret = 0;
		switch (index) {
		case 0:
			ret = -1;
			break;
		case 1:
			ret = 0;
			break;
		case 2:
			ret = 16777215;
			break;
		}
		return ret;
	}

	public static void mixImage(String DestSrc, String originSrc, String format) throws IOException {
		BufferedImage DestImage = ImageUtill.getBufferedImage(DestSrc);
		BufferedImage origin = ImageUtill.getBufferedImage(originSrc);

		int w1 = DestImage.getWidth();
		int h1 = DestImage.getHeight();
		int w2 = origin.getWidth();
		int h2 = origin.getHeight();

		int[] ImageArrayOne = new int[w1 * h1];
		ImageArrayOne = DestImage.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1);
		int[] ImageArrayTwo = new int[w2 * h2];
		ImageArrayTwo = origin.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);
		origin = null;

		int testFlag = ImageUtill.findFlag(ImageArrayTwo);

		for (int i = 0; i < ImageArrayTwo.length; i++) {
			if (ImageArrayTwo[i] != testFlag) {
				ImageArrayOne[i] = ImageArrayTwo[i];
			}
		}

		DestImage.setRGB(0, 0, w2, h2, ImageArrayOne, 0, w2);

		ImageIO.write(DestImage, format, new File(DestSrc));
	}

	public static void mixImageO(String DestSrc, String originSrc, String format) throws IOException {
		BufferedImage DestImage = ImageUtill.getBufferedImage(DestSrc);
		BufferedImage origin = ImageUtill.getBufferedImage(originSrc);

		int w1 = DestImage.getWidth();
		int h1 = DestImage.getHeight();
		int w2 = origin.getWidth();
		int h2 = origin.getHeight();

		int[] ImageArrayOne = new int[w1 * h1];
		ImageArrayOne = DestImage.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1);
		int[] ImageArrayTwo = new int[w2 * h2];
		ImageArrayTwo = origin.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);
		origin = null;

		int testFlag = ImageUtill.findFlag(ImageArrayTwo);

		for (int i = 0; i < ImageArrayTwo.length; i++) {
			if (ImageArrayTwo[i] != testFlag) {
				ImageArrayOne[i] = ImageArrayTwo[i];
			}
		}

		DestImage.setRGB(0, 0, w2, h2, ImageArrayOne, 0, w2);

		ImageIO.write(DestImage, format, new File(DestSrc));
	}

	public static void main(String[] args) {
//		String imgurl = "/data/DownLoad/Map/16/";
//		String output = "/data/DownLoad/Map/";
//		BufferedImage DestImage = new BufferedImage(MoveServlet.IMAGE_SIZE * 11, MoveServlet.IMAGE_SIZE * 11,
//				BufferedImage.TYPE_INT_RGB);
//		for (int y = 10; y >= 0; y--) {
//			for (int x = 0; x < 10; x++) {
//				try {
//					BufferedImage origin = ImageUtill
//							.getBufferedImage(imgurl + (y + 24841) + File.separator + (x + 54955) + ".png");
//					ImageUtill.mergeImage(DestImage, origin, MoveServlet.IMAGE_SIZE * (10 - y),
//							MoveServlet.IMAGE_SIZE * x, false);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//
//			}
//		}
//		ImageUtill.saveImage(DestImage, output, "test", "png");
//		String imgurl = "/data/DownLoad/output.png";
//		try {
//			BufferedImage origin = ImageUtill.getBufferedImage(imgurl);
//			int w2 = origin.getWidth();
//			int h2 = origin.getHeight();
//			int[] ImageArrayTwo = new int[w2 * h2];
//			ImageArrayTwo = origin.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);
//			System.out.println(ImageUtill.findFlag(ImageArrayTwo));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		String imgurl = "/data/DownLoad/MapV/17/51958/106831.png";
//		String imgurl2 = "/data/DownLoad/MapV/17/51958/106832.png";
//		String imgurl3 = "/data/DownLoad/MapV/17/51958/106833.png";
//		String imgurl4 = "/data/DownLoad/MapV/17/51958/106834.png";
//		String imgurl5 = "/data/DownLoad/MapV/17/51958/106835.png";
//		String output = "/data/DownLoad/MapV/";
//		BufferedImage DestImage = new BufferedImage(MoveServlet.IMAGE_SIZE * 5, MoveServlet.IMAGE_SIZE,
//				BufferedImage.TYPE_INT_RGB);
//		try {
//			BufferedImage origin = ImageUtill.getBufferedImage(imgurl);
//			BufferedImage origin2 = ImageUtill.getBufferedImage(imgurl2);
//			BufferedImage origin3 = ImageUtill.getBufferedImage(imgurl3);
//			BufferedImage origin4 = ImageUtill.getBufferedImage(imgurl4);
//			BufferedImage origin5 = ImageUtill.getBufferedImage(imgurl5);
//			ImageUtill.mergeImage(DestImage, origin, true, 0, 0, true);
//			ImageUtill.mergeImage(DestImage, origin2, true, 0, MoveServlet.IMAGE_SIZE, true);
//			ImageUtill.mergeImage(DestImage, origin3, true, 0, MoveServlet.IMAGE_SIZE * 2, true);
//			ImageUtill.mergeImage(DestImage, origin4, true, 0, MoveServlet.IMAGE_SIZE * 3, true);
//			ImageUtill.mergeImage(DestImage, origin5, true, 0, MoveServlet.IMAGE_SIZE * 4, true);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		ImageUtill.saveImage(DestImage, output, "test", "png");

//		HashMap<Integer,Integer> targetMapping = new HashMap<Integer,Integer>();
//		targetMapping.put(-1, 0);
//		targetMapping.put(0, 0);
//		targetMapping.put(16777215, 0);
//		Integer a =targetMapping.get(-1);
//		a++;
//		targetMapping.put(-1,a);
//		System.out.println(targetMapping.get(-1));

//		String DestSrc = DonloadServlet.DonloadbaseB + 17 + "丨";
//		String originSrc = DonloadServlet.DonloadbaseV + 17 + "丨";
//		try {
//			ImageUtill.mixImage(DestSrc, originSrc, "png");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}
}
