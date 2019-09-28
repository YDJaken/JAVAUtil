package com.dy.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

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
import com.dy.Util.Math.Point3D;
import com.dy.Util.Math.RotationMatrix;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.formats.jpeg.xmp.JpegXmpRewriter;
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

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
	 * 设置图片的经度,纬度,高度
	 * 
	 * @param jpegImageFile
	 * @param jpegMetadata
	 * @param dst
	 * @param longitude
	 * @param latitude
	 * @param height
	 * @throws IOException
	 * @throws ImageReadException
	 * @throws ImageWriteException
	 */
	public static void setExifGPSTag(final File jpegImageFile, final JpegImageMetadata jpegMetadata, final File dst,
			final double longitude, final double latitude, final double height)
			throws IOException, ImageReadException, ImageWriteException {
		try (FileOutputStream fos = new FileOutputStream(dst); OutputStream os = new BufferedOutputStream(fos)) {
			TiffOutputSet outputSet = null;

			if (jpegMetadata != null) {
				final TiffImageMetadata exif = jpegMetadata.getExif();

				if (exif != null) {
					outputSet = exif.getOutputSet();
				}
			}

			if (null == outputSet) {
				outputSet = new TiffOutputSet();
			}

			outputSet.setGPSInDegrees(longitude, latitude);

			final TiffOutputDirectory GPSDirectory = outputSet.getGPSDirectory();
			if (GPSDirectory != null) {
				GPSDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_ALTITUDE);
				GPSDirectory.add(GpsTagConstants.GPS_TAG_GPS_ALTITUDE, RationalNumber.valueOf(height));
			}

			new ExifRewriter().updateExifMetadataLossy(jpegImageFile, os, outputSet);
			os.flush();
			os.close();
			fos.flush();
			fos.close();
		}
	}

	/**
	 * 获取图片的XmpDirectory
	 * 
	 * @param metadata
	 * @return
	 */
	public static XmpDirectory loadXMPDirectory(final Metadata metadata) {
		Iterable<Directory> its = metadata.getDirectories();
		Iterator<Directory> it = its.iterator();
		XmpDirectory tar = null;
		while (it.hasNext()) {
			Directory tmp = it.next();
			try {
				tar = (XmpDirectory) tmp;
			} catch (Exception e) {
				tar = null;
			}
			if (tar != null) {
				break;
			}
		}

		return tar;
	}

	/**
	 * 保存XMP数据
	 * 
	 * @param meta
	 * @param so
	 * @param source
	 * @param des
	 * @throws XMPException
	 * @throws ImageReadException
	 * @throws ImageWriteException
	 * @throws IOException
	 */
	public static void saveXMP(final XMPMeta meta, final SerializeOptions so, final File source, final File des)
			throws XMPException, ImageReadException, ImageWriteException, IOException {
		FileOutputStream fos = new FileOutputStream(des);
		OutputStream os = new BufferedOutputStream(fos);
		String after = XMPMetaFactory.serializeToString(meta, so);
		new JpegXmpRewriter().updateXmpXml(source, os, after);
		os.flush();
		os.close();
		fos.flush();
		fos.close();
	}

	public static void printAllTags(final Metadata metadata) {
		Iterable<Directory> its = metadata.getDirectories();
		Iterator<Directory> it = its.iterator();
		while (it.hasNext()) {
			Directory tmp = it.next();
			Collection<Tag> tags = tmp.getTags();
			Iterator<Tag> tag = tags.iterator();
			while (tag.hasNext()) {
				Tag tmpTag = tag.next();
				System.out.println(tmpTag.toString());
				System.out.println(
						"Description:" + tmpTag.getDescription() + ", DirectoryName:" + tmpTag.getDirectoryName()
								+ " ,TagName:" + tmpTag.getTagName() + ",TagType:" + tmpTag.getTagType());
				System.out.println("		-----------------------------------------------");
			}
		}
	}

	public static void main(String[] args) {

//		File jpegFile = new File("C:\\Users\\hp\\Desktop\\air\\DJI_0001.JPG");
		File jpegFile = new File("C:\\Users\\hp\\Desktop\\test\\DJI_0044.JPG");
		File writeFile = new File("C:\\Users\\hp\\Desktop\\test\\DJI_0044_1.JPG");

		try {
			Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);
			printAllTags(metadata);
			SerializeOptions so = new SerializeOptions().setOmitPacketWrapper(true);
			XmpDirectory tar = loadXMPDirectory(metadata);
			if (tar != null) {
				XMPMeta meta = tar.getXMPMeta();
				Map<String, String> properties = tar.getXmpProperties();
				properties.forEach((key, value) -> {
					System.out.println("		key:" + key + ",value:" + value);
					if (key.indexOf("Degree") != -1) {
						System.out.println("Radians:" + Math.toRadians(Double.parseDouble(value)));
					}
				});

				try {
					double[] a = { 0.910096420657005, -0.414229030424482, 0.0117819973222213,
							-0.237436859891045,-0.544549227316174, -0.804418968320886,
							0.33962956684068, 0.729301343330006,-0.593945542911689 };
					Point3D hpr = RotationMatrix.computeOrientationFromMatrixDegree(a,
							new Point3D(113.558169120253, 25.0632353814444, 27.7765723755583));
					meta.setProperty("http://www.dji.com/drone-dji/1.0/", "drone-dji:GimbalYawDegree", hpr.getX());
					meta.setProperty("http://www.dji.com/drone-dji/1.0/", "drone-dji:GimbalPitchDegree",hpr.getY());
					meta.setProperty("http://www.dji.com/drone-dji/1.0/", "drone-dji:GimbalRollDegree", hpr.getZ());
					System.out.println(hpr);
					System.out.println(meta.getProperty("http://www.dji.com/drone-dji/1.0/", "drone-dji:GimbalYawDegree"));
					System.out.println(meta.getProperty("http://www.dji.com/drone-dji/1.0/", "drone-dji:GimbalPitchDegree"));
					System.out.println(meta.getProperty("http://www.dji.com/drone-dji/1.0/", "drone-dji:GimbalRollDegree"));
					double[] b = RotationMatrix.generate3DMatrix(Math.toRadians(113.558169120253), Math.toRadians(25.0632353814444), 27.7765723755583, Math.toRadians(hpr.getY()), Math.toRadians(hpr.getX()), Math.toRadians(hpr.getZ()));
					StringBuilder builder = new StringBuilder();
					builder.append("[");
					for(int i = 0; i < b.length;i++) {
						builder.append(b[i]);
						builder.append(",");
					}
					builder.append("]");
					System.out.println(builder.toString());
					saveXMP(meta, so, jpegFile, writeFile);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				System.out.println("		-----------------------------------------------");
				try {
					jpegFile = writeFile;
					writeFile = new File("C:\\Users\\hp\\Desktop\\test\\DJI_0044_2.JPG");
					final JpegImageMetadata jpegMetadata = (JpegImageMetadata) Imaging.getMetadata(jpegFile);

					setExifGPSTag(jpegFile, jpegMetadata, writeFile, 113.558169120253, 23.0632353814444,
							27.7765723755583);
					
					metadata = JpegMetadataReader.readMetadata(writeFile);
					tar = loadXMPDirectory(metadata);
					if (tar != null) {
						meta = tar.getXMPMeta();
						properties = tar.getXmpProperties();
						properties.forEach((key, value) -> {
							System.out.println("		key:" + key + ",value:" + value);
							if(key.indexOf("Degree")!= -1) {
								System.out.println("Radians:" + Math.toRadians(Double.parseDouble(value)));
								
							}
						});
					}
				} catch (ImageReadException | ImageWriteException e) {
					e.printStackTrace();
				}
			}
		} catch (JpegProcessingException |

				IOException e) {
			e.printStackTrace();
		}
	}
}
