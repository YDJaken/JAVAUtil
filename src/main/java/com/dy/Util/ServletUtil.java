package com.dy.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtil {
	public static String getRequestPayload(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = req.getReader();) {
			if (!reader.ready()) {
				return null;
			}
			char[] buff = new char[1024];
			int len;
			while ((len = reader.read(buff)) != -1) {
				sb.append(buff, 0, len);
			}
		} catch (IOException e) {
			return null;
		}
		return sb.length() > 0 ? sb.toString() : null;
	}

	public static void returnFile(File f, HttpServletResponse res) throws Exception {
		if (FileUtil.isGzip(f)) {
			File p = f.getParentFile();
			try {
				File tmp = File.createTempFile("tmp", ".json", p);
				boolean reslut = FileUtil.unGzipFile(f, tmp);
				if (reslut == false) {
					throw new Exception("未成功解压缩gzip文件" + f.getPath() + f.getName());
				} else {
					f = tmp;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ServletOutputStream OutputStream = null;
		InputStream inputStream = null;
		byte[] bytes = new byte[1024];
		int temp = 0;
		try {
			inputStream = new FileInputStream(f);
			OutputStream = res.getOutputStream();
			while ((temp = inputStream.read(bytes)) != -1) {
				OutputStream.write(bytes, 0, temp);
				OutputStream.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					System.err.println(e.toString());
					// e.printStackTrace();
				}
			}
			if (OutputStream != null) {
				try {
					OutputStream.close();
				} catch (IOException e) {
					System.err.println(e.toString());
					// e.printStackTrace();
				}
			}
		}
	}

	public static Object getRequestParameter(HttpServletRequest request, String ID) {
		return ServletUtil.getRequestParameter(request, ID, null);
	}

	public static Object getRequestParameter(HttpServletRequest request, String ID, Object origin) {
		Object data = request.getParameter(ID);
		if (data == null) {
			data = request.getAttribute(ID);
			if (data == null) {
				data = ServletUtil.getRequestPayload(request);
			}
		}
		return data == null ? origin : data;
	}

	public static boolean isJsonString(String target) {
		return target.indexOf("{") != -1;
	}
}
