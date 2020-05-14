package com.dy.Util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {

	public static boolean isSubFile(final File base, final File current) {
		if (base == null || current == null)
			return false;
		String basePath = base.getAbsolutePath();
		String currentPath = current.getAbsolutePath();
		return currentPath.indexOf(basePath) != -1;
	}

	/**
	 * 将文件转换为字节 (大文件)
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static PriorityQueue<byte[]> toBytes(final File f) throws IOException {
		long length = f.length();
		int max = Integer.MAX_VALUE;
		PriorityQueue<Integer> size = new PriorityQueue<>();
		while (length > max) {
			size.add(max);
			length -= max;
		}
		if (length < max) {
			size.add((int) length);
		}

		RandomAccessFile ra = null;
		FileChannel raf = null;
		PriorityQueue<byte[]> ret = null;
		try {
			ra = new RandomAccessFile(f, "r");
			raf = ra.getChannel();
			ret = new PriorityQueue<byte[]>();
			long startPosition = 0;
			while (!size.isEmpty()) {
				int endPosition = size.remove();
				byte[] buf = new byte[(int) (endPosition - startPosition)];
				ra.read(buf);
//				MappedByteBuffer tmp = raf.map(FileChannel.MapMode.READ_ONLY, startPosition, endPosition);
//				byte[] buf = null;
//				if(tmp.hasArray()) {
//					buf = tmp.array();
//				}else {
//					int limit = tmp.limit();
//					buf = new byte[limit];
//					tmp.get(buf);
//				}
//				tmp.clear();
				ret.add(buf);
				startPosition += max;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (raf != null) {
				if (raf.isOpen())
					raf.close();
			}
			if (ra != null) {
				ra.close();
			}
		}
		return ret;
	}

	/**
	 * 将文件转换为字节 (小文件)
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static PriorityQueue<byte[]> toBytesSafe(final File f) throws Exception {
		long length = f.length();
		int max = Integer.MAX_VALUE;
		if ((length - max) > max) {
			throw new Exception("文件过大");
		}
		int[] size = new int[2];
		if (length < max) {
			size[0] = (int) length;
		} else {
			size[0] = max;
			size[1] = (int) (length - max);
		}
		RandomAccessFile ra = null;
		PriorityQueue<byte[]> ret = null;
		try {
			ra = new RandomAccessFile(f, "r");
			ret = new PriorityQueue<byte[]>();
			int startPosition = 0;
			if (size[1] == 0) {
				byte[] b = new byte[size[0]];
				ra.read(b);
				ret.add(b);
			} else {
				byte[] b = new byte[size[0]];
				int endPosition = size[0] - 1;
				ra.readFully(b, startPosition, endPosition);
				ret.add(b);
				startPosition += max;
				endPosition = size[1];
				ra.readFully(b, startPosition, endPosition);
				ret.add(b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ra != null) {
				ra.close();
			}
		}
		return ret;
	}

	public static boolean isGzip(final File sourcedir) throws IOException {
		boolean ret = false;
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(sourcedir);
			byte[] buf = new byte[2];
			fin.read(buf);
			int b = (buf[1] & 0xFF) << 8 | buf[0];
			ret = b == GZIPInputStream.GZIP_MAGIC;
		} catch (FileNotFoundException e) {
			System.err.println(e.toString());
			// e.printStackTrace();
			return ret;
		} catch (IOException e) {
			System.err.println(e.toString());
			// e.printStackTrace();
			return ret;
		} finally {
			if (fin != null)
				fin.close();
		}
		return ret;
	}

	public static boolean unGzipFile(File sourcedir, File f) {
		boolean ret = false;
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				ret = true;
			}
			if (ret == true)
				return false;
		}
		try {
			FileInputStream fin = new FileInputStream(sourcedir);
			GZIPInputStream gzin = new GZIPInputStream(fin);
			FileOutputStream fout = new FileOutputStream(f);
			int num;
			byte[] buf = new byte[1024];
			while ((num = gzin.read(buf, 0, buf.length)) != -1) {
				fout.write(buf, 0, num);
			}
			gzin.close();
			fout.close();
			fin.close();
			return true;
		} catch (Exception e) {
			System.err.println(e.toString());
			// e.printStackTrace();
		}
		return false;
	}

	public static void unZip(File zfile) throws IOException {
		String Parent = zfile.getParent() + File.separator;
		FileInputStream fis = new FileInputStream(zfile);
		ZipInputStream zis = new ZipInputStream(fis);
		ZipEntry entry = null;
		BufferedOutputStream bos = null;
		while ((entry = zis.getNextEntry()) != null) {
			if (entry.isDirectory()) {
				File filePath = new File(Parent + entry.getName());
				if (!filePath.exists()) {
					filePath.mkdirs();
				}
			} else {
				FileOutputStream fos = new FileOutputStream(Parent + entry.getName());
				bos = new BufferedOutputStream(fos);
				byte buf[] = new byte[1024];
				int len;
				while ((len = zis.read(buf)) != -1) {
					bos.write(buf, 0, len);
				}
				zis.closeEntry();
				bos.close();
			}
		}
		zis.close();
	}

	public static int index(String path) {
		int ret = -1;
		for (int i = path.length() - 1 - 5; i > 0; i--) {
			if (path.charAt(i) == File.separatorChar) {
				return i;
			}
		}
		return ret;
	}

	public static void copyDir(final String origin, String destnation) throws IOException{
		File a = new File(origin);
		File b = new File(destnation);
		if(a.exists()) {
			if(!a.isDirectory()) {
				FileUtil.copyFile(origin, destnation);
			}else {
				if(!b.exists()) {
					b.mkdirs();
				}
				File[] subF = a.listFiles();
				for (int i = 0; i < subF.length; i++) {
					File target = subF[i];
					FileUtil.copyDir(target.getAbsolutePath(), b.getAbsolutePath() + File.separator + target.getName());
				}
			}
		}
	}
	
	public static void copyFile(final String origin, String destnation) throws IOException {
		File a = new File(origin);
		File b = new File(destnation);
		if (a.exists()) {
			if (!b.exists()) {
				try {
					b.createNewFile();
				} catch (IOException e) {
					System.err.println(e.toString());
				}
			}
			copyFile(a, b);
		}
	}
	
	private static final long FILE_COPY_BUFFER_SIZE = 1024 * 1024 * 30;
	
	public static void copyFile(final File srcFile, final File destFile) throws IOException {
		if(!destFile.exists() || !srcFile.exists()) {
			throw new IOException("目标文件或源文件不存在");
		}
		
		if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("目标文件为目录");
        }

        try (FileInputStream fis = new FileInputStream(srcFile);
             FileChannel input = fis.getChannel();
             FileOutputStream fos = new FileOutputStream(destFile);
             FileChannel output = fos.getChannel()) {
            final long size = input.size();
            long pos = 0;
            long count = 0;
            while (pos < size) {
                final long remain = size - pos;
                count = remain > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : remain;
                final long bytesCopied = output.transferFrom(input, pos, count);
                if (bytesCopied == 0) {
                    break;
                }
                pos += bytesCopied;
            }
            fis.close();
            fos.close();
        }
        
		final long srcLen = srcFile.length();
		final long dstLen = destFile.length();
		if (srcLen != dstLen) {
            throw new IOException("拷贝文件文件大小验证失败， 源文件:'" +
                    srcFile + "' 至目标文件:'" + destFile + "' 源文件大小: " + srcLen + " 目标文件大小: " + dstLen);
        }
        
        destFile.setLastModified(srcFile.lastModified());
	}
	
	public static void moveFile(final File file1,final File file2) {
		try {
			FileUtil.copyFile(file1, file2);
		} catch (IOException e) {
			System.err.println(e.toString());
			// e.printStackTrace();
		}
//		FileOutputStream fileOutputStream = null;
//		InputStream inputStream = null;
//		byte[] bytes = new byte[(int)FILE_COPY_BUFFER_SIZE];
//		int temp = 0;
//		try {
//			inputStream = new FileInputStream(file1);
//			fileOutputStream = new FileOutputStream(file2);
//			while ((temp = inputStream.read(bytes)) != -1) {
//				fileOutputStream.write(bytes, 0, temp);
//				fileOutputStream.flush();
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (inputStream != null) {
//				try {
//					inputStream.close();
//				} catch (IOException e) {
//					System.err.println(e.toString());
//					// e.printStackTrace();
//				}
//			}
//			if (fileOutputStream != null) {
//				try {
//					fileOutputStream.close();
//				} catch (IOException e) {
//					System.err.println(e.toString());
//					// e.printStackTrace();
//				}
//			}
//		}

	}

	public static void cutFile(String origin, String destnation) {
		File a = new File(origin);
		File b = new File(destnation);
		if (a.exists()) {
			if (!b.exists()) {
				try {
					b.createNewFile();
				} catch (IOException e) {
					System.err.println(e.toString());
				}
			}
			moveFile(a, b);
			a.delete();
		}
	}

	public static void moveFile(String origin, String destnation) {
		File a = new File(origin);
		File b = new File(destnation);
		if (a.exists()) {
			if (!b.exists()) {
				try {
					b.createNewFile();
				} catch (IOException e) {
					System.err.println(e.toString());
				}
			}
			moveFile(a, b);
		}
	}

	/**
	 * *向文件写入字符串
	 * 
	 * @param f
	 * @param s
	 * @return
	 */
	public static boolean writeString(File f, String s) {
//		return writeString(f, s, File.separator.equals("/") ? "UTF-8" : "GBK");
		return writeString(f, s, "UTF-8");
	}

	public static boolean writeString(File f, String s, String encoding) {
		FileOutputStream fileOutputStream = null;
		try {
			f.setWritable(true);
			fileOutputStream = new FileOutputStream(f);
			byte[] bytes = s.getBytes(encoding);
			fileOutputStream.write(bytes, 0, bytes.length);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static String readString(File f, String encoding) {
		FileInputStream fio = null;
		String ret = new String();
		try {
			f.setReadable(true);
			fio = new FileInputStream(f);
			long fileLength = f.length();
			if (fileLength > Integer.MAX_VALUE) {
				fio.close();
				throw new IOException("File size over " + Integer.MAX_VALUE + " bytes.");
			}
			byte[] buff = new byte[(int) fileLength];
			while (fio.read(buff) != -1) {
				ret = new String(buff, encoding);
			}
			fio.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static String readString(File f) {
//		return readString(f,File.separator.equals("/") ? "UTF-8" : "GBK");
		return readString(f, "UTF-8");
	}

	/**
	 * * 列出目录下的文件不包含文件夹
	 * 
	 * @param dir
	 * @return
	 */
	public static String[] loadsubFiles(File dir) {
		if (!dir.isDirectory())
			return null;
		File[] fs = dir.listFiles();
		List<String> ret = new LinkedList<>();
		for (int i = 0; i < fs.length; i++) {
			if (!fs[i].isDirectory()) {
				ret.add(fs[i].getName());
			}
		}
		return ret.toArray(new String[ret.size()]);
	}

	public static File[] loadsubFilesAsFile(File dir) {
		if (!dir.isDirectory())
			return null;
		return dir.listFiles();
	}
	
	public static boolean deleteFile(File target) {
		if(!target.exists()) {
			return true;
		}
		if(target.isFile()) {
			return target.delete();
		}
		if(target.isDirectory()) {
			File[] subFiles = target.listFiles();
			for (int i = 0; i < subFiles.length; i++) {
				boolean flag = FileUtil.deleteFile(subFiles[i]);
				if(flag == false) {
					return false;
				}
			}
			return target.delete();
		}
		
		return false;
	}

}
