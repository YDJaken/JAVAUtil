package com.dy.ImageUtil;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

import com.adobe.internal.xmp.XMPException;
import com.adobe.internal.xmp.XMPMeta;
import com.adobe.internal.xmp.XMPMetaFactory;
import com.adobe.internal.xmp.options.SerializeOptions;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.xmp.XmpDirectory;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

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

	/**
	 * 修改图片的DPI(jpeg版)
	 * 
	 * @param image
	 * @param dpi
	 * @return
	 * @throws IOException
	 */
	public static byte[] setImageDPI(File f, BufferedImage image, int dpi, float quality) throws IOException {
		Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName("jpeg");
		if (!iw.hasNext())
			return null;
		ImageWriter writer = iw.next();

		Iterator<ImageReader> ir = ImageIO.getImageReadersByFormatName("jpeg");
		if (!ir.hasNext())
			return null;
		ImageReader reader = ir.next();
		reader.setInput(ImageIO.createImageInputStream(f));

		ImageWriteParam writeParams = writer.getDefaultWriteParam();
		writeParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		// 调整图片质量
		writeParams.setCompressionQuality(quality);

		IIOMetadata data = writer.getDefaultImageMetadata(new ImageTypeSpecifier(image), writeParams);
		if (data.isReadOnly() || !data.isStandardMetadataFormatSupported()) {
			return null;
		}
		IIOMetadataNode tree = (IIOMetadataNode) data.getAsTree("javax_imageio_jpeg_image_1.0");
//		String outS = printTree(tree);
//		System.out.println(outS);
//		File file = new File("D:\\FFOutput\\test.txt");
//		PrintStream ps;
//		try {
//			ps = new PrintStream(new FileOutputStream(file));
//			ps.write(outS.getBytes());
//			ps.flush();
//			ps.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		if (tree.getElementsByTagName("app0JFIF").getLength() == 0)
			return null;
		IIOMetadataNode jfif = (IIOMetadataNode) tree.getElementsByTagName("app0JFIF").item(0);
		jfif.setAttribute("Xdensity", dpi + "");
		jfif.setAttribute("Ydensity", dpi + "");
		jfif.setAttribute("resUnits", "1");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageOutputStream stream = null;
		try {
			stream = ImageIO.createImageOutputStream(out);
			writer.setOutput(stream);
			writer.write(data, new IIOImage(image, null, null), writeParams);
		} finally {
			stream.close();
		}

		return out.toByteArray();
	}

	public static String printTree(IIOMetadataNode tree) {
		String ret = "";
		if (tree.getAttributes().getLength() > 0) {
			for (int j = 0; j < tree.getAttributes().getLength(); j++) {
				IIOMetadataNode attribute = (IIOMetadataNode) tree.getAttributes().item(j);
				ret += attribute.getNodeName() + "=" + attribute.getNodeValue() + ";\n";
			}
		}
		for (int i = 0; i < tree.getChildNodes().getLength(); i++) {
			ret += ImageUtill.printTree((IIOMetadataNode) tree.getChildNodes().item(i));
		}
		return ret;
	}

	public BufferedImage test(BufferedImage image, File file, int dpi, float quality) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(fos);
		JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(image);
		jpegEncodeParam.setDensityUnit(JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);
		jpegEncoder.setJPEGEncodeParam(jpegEncodeParam);
		jpegEncodeParam.setQuality(quality, false);
		jpegEncodeParam.setXDensity(dpi);
		jpegEncodeParam.setYDensity(dpi);
		jpegEncoder.encode(image, jpegEncodeParam);
		image.flush();
		fos.close();
		return image;
	}

	/**
	 * 更改图片分辨率并且压缩图片
	 */
	public static void resizeAndCompress() {
		int target = 1;
		String imgurl = "D:\\FFOutput\\" + target + ".jpg";
		String output = "D:\\FFOutput\\";
		BufferedImage sourceImage;
		int width = 358, height = 441;
		float q = 0.6f;
		try {
			sourceImage = ImageUtill.getBufferedImage(imgurl);
			for (int i = 1; i <= 13; i++) {
				BufferedImage tempImage = new BufferedImage(width, height, i);
				Graphics2D g2D = (Graphics2D) tempImage.getGraphics();
				g2D.drawImage(sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
				g2D.dispose();
				byte[] modified = ImageUtill.setImageDPI(new File(imgurl), tempImage, 72, q);
				if (modified == null)
					continue;
				File tmp = new File(output + target + "_" + i + ".jpg");
				if (!tmp.exists())
					tmp.createNewFile();
				FileOutputStream fio = new FileOutputStream(tmp);
				fio.write(modified);
				fio.flush();
				fio.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName("jpeg");
		if (!iw.hasNext())
			return;
		ImageWriter writer = iw.next();
//
//		Iterator<ImageReader> ir = ImageIO.getImageReadersByFormatName("jpeg");
//		if (!ir.hasNext())
//			return;
//		ImageReader reader = ir.next();
//		try {
//			reader.setInput(ImageIO.createImageInputStream());
//
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		File jpegFile = new File("C:\\Users\\hp\\Desktop\\DJI_0010.JPG");
		try {
			Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);
			Iterable<Directory> its = metadata.getDirectories();
			Iterator<Directory> it = its.iterator();
			while (it.hasNext()) {
				Directory tmp = it.next();
				XmpDirectory tar = null;
				try {
					tar = (XmpDirectory) tmp;
				} catch (Exception e) {

				}
				if (tar != null) {
					XMPMeta meta = tar.getXMPMeta();

					Map<String, String> properties = tar.getXmpProperties();
					properties.forEach((key, value) -> {
						System.out.println("		key:" + key + "		,value:" + value);
					});
					System.out.println("		-----------------------------------------------");
					try {
						System.out.println("			drone-dji:FlightPitchDegree原为: "+meta.getPropertyDouble("http://www.dji.com/drone-dji/1.0/", "drone-dji:FlightPitchDegree")+", 修改为: 35.0");
						meta.setProperty("http://www.dji.com/drone-dji/1.0/", "drone-dji:FlightPitchDegree", 35.0);
					} catch (XMPException e1) {
						e1.printStackTrace();
					}
					SerializeOptions so = new SerializeOptions().setOmitPacketWrapper(true);
					try {
						//获取元数据的buffer放入图片文件
						byte[] after = XMPMetaFactory.serializeToBuffer(meta, so);
						meta = XMPMetaFactory.parseFromBuffer(after);
						System.out.println("		-----------------------------------------------");
						System.out.println("			drone-dji:FlightPitchDegree现为: "+meta.getPropertyDouble("http://www.dji.com/drone-dji/1.0/", "drone-dji:FlightPitchDegree"));
						System.out.println("		-----------------------------------------------");
						properties = tar.getXmpProperties();
						properties.forEach((key, value) -> {
							System.out.println("		key:" + key + "		,value:" + value);
						});
					} catch (XMPException e) {
						e.printStackTrace();
					}
				} else {
					Collection<Tag> tags = tmp.getTags();
					Iterator<Tag> tag = tags.iterator();
					while (tag.hasNext()) {
						Tag tmpTag = tag.next();

						System.out.println(tmpTag.toString());
						System.out.println("Description:" + tmpTag.getDescription() + ", DirectoryName:"
								+ tmpTag.getDirectoryName() + " ,TagName:" + tmpTag.getTagName() + ",TagType:"
								+ tmpTag.getTagType());
					}
				}
//				writer.write(streamMetadata, image, param);
			}

			
		} catch (JpegProcessingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
