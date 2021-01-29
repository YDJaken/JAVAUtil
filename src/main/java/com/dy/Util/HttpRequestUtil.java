package com.dy.Util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.dy.Util.Sup.Detect401;

/**
 * HTTP请求工具
 * 
 * @author dy
 */
public class HttpRequestUtil {
	/**
	 * 发起post请求并获取结果 支持断点续传
	 * 
	 * @param path url
	 * @return
	 */
	public static Detect401 postDownTerrain(String path, File file) {
		return HttpRequestUtil.postDownTerrain(path, "", file, 30000);
	}

	public static Detect401 postDownTerrain(String path, String token, File file) {
		return HttpRequestUtil.postDownTerrain(path, token, file, 30000);
	}

	public static Detect401 postDownTerrain(String path, File file, int readTime) {
		return HttpRequestUtil.postDownTerrain(path, "", file, 30000);
	}
	
	public static String getToken() {
		URL url = null;
		try {
			System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
			url = new URL("http://lab2.cesiumlab.com/api/user/login?user=15029066507&pass=FuCk2580");
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.addRequestProperty("User-Agent", "cpp-httplib/0.2");
			httpURLConnection.addRequestProperty("Accept", "*/*");
			httpURLConnection.setRequestProperty("Host", "lab2.cesiumlab.com:80");
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(0);
			httpURLConnection.setReadTimeout(0);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			return httpURLConnection.getHeaderField("Set-Cookie");
		} catch (Exception exc) {
			return null;
		}
	}

	public static byte[] redriect(String URl, HttpServletResponse response,String Cookie, int timeOut) {
		URL url = null;
		try {
			System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
			url = new URL(URl.toString());
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.addRequestProperty("User-Agent", "cpp-httplib/0.2");
//			httpURLConnection.addRequestProperty("Content-Type", "application/Json; charset=utf-8");
			httpURLConnection.addRequestProperty("Accept", "*/*");
			httpURLConnection.setRequestProperty("Cookie", Cookie);
//			httpURLConnection.setRequestProperty("Connection","close");
			httpURLConnection.setRequestProperty("Host", "lab2.cesiumlab.com:80");
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(timeOut);
			httpURLConnection.setReadTimeout(timeOut);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			if (response != null) {
				response.addHeader("Server", httpURLConnection.getHeaderField("Server"));
				response.addDateHeader("Date", httpURLConnection.getHeaderFieldDate("Date", timeOut));
				response.addHeader("Content-Type", httpURLConnection.getHeaderField("Content-Type"));
				response.addHeader("Content-Length", httpURLConnection.getHeaderField("Content-Length"));
				response.addHeader("Connection", httpURLConnection.getHeaderField("Connection"));
				response.addHeader("X-Suggested-Filename", httpURLConnection.getHeaderField("X-Suggested-Filename"));
				response.addHeader("Content-Disposition", httpURLConnection.getHeaderField("Content-Disposition"));
//				response.addHeader("filename", "cesiumlab2.lic");
				response.addDateHeader("Expires", httpURLConnection.getHeaderFieldDate("Expires", timeOut));
				response.addHeader("Cache-Control", httpURLConnection.getHeaderField("Cache-Control"));
			}
			InputStream is = httpURLConnection.getInputStream();
			return is.readAllBytes();
		} catch (Exception exc) {
			if (exc.getMessage().equals("Read timed out") || (exc.getMessage().equals("connect timed out"))) {
				return null;
			} else {
				System.err.println(exc.toString());
			}
		}
		return null;
	}

	public static String GET(String URl, int timeOut) {
		URL url = null;
		try {
			url = new URL(URl.toString());
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.addRequestProperty("User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
			httpURLConnection.addRequestProperty("Content-Type", "application/Json; charset=utf-8");
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(timeOut);
			httpURLConnection.setReadTimeout(timeOut);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
			return br.readLine();
		} catch (Exception exc) {
			if (exc.getMessage().equals("Read timed out") || (exc.getMessage().equals("connect timed out"))) {
				return null;
			} else {
				System.err.println(exc.toString());
			}
		}
		return null;
	}

	public static String POST(String URl, JSONObject obj, int timeOut) {
		URL url = null;
		try {
			url = new URL(URl.toString());
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.addRequestProperty("User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
			httpURLConnection.addRequestProperty("Content-Type", "application/Json; charset=utf-8");
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setConnectTimeout(timeOut);
			httpURLConnection.setReadTimeout(timeOut);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setUseCaches(false);
			OutputStream out = httpURLConnection.getOutputStream();
			out.write(obj.toString().getBytes("UTF-8"));
			out.flush();
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
			return br.readLine();
		} catch (Exception exc) {
			if (exc.getMessage().equals("Read timed out") || (exc.getMessage().equals("connect timed out"))) {
				return null;
			} else {
				System.err.println(exc.toString());
			}
		}
		return null;
	}

	public static String PUT(String URl, JSONObject obj, int timeOut) {
		URL url = null;
		try {
			url = new URL(URl.toString());
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.addRequestProperty("User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
			httpURLConnection.addRequestProperty("Content-Type", "application/Json; charset=utf-8");
			httpURLConnection.setRequestMethod("PUT");
			httpURLConnection.setConnectTimeout(timeOut);
			httpURLConnection.setReadTimeout(timeOut);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setUseCaches(false);
			OutputStream out = httpURLConnection.getOutputStream();
			out.write(obj.toString().getBytes("UTF-8"));
			out.flush();
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
			return br.readLine();
		} catch (Exception exc) {
			if (exc.getMessage().equals("Read timed out") || (exc.getMessage().equals("connect timed out"))) {
				return null;
			} else {
				System.err.println(exc.toString());
			}
		}
		return null;
	}

	public static Detect401 postDownTerrain(String path, String token, File file, int readTime) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(30000);
			httpURLConnection.setReadTimeout(readTime);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestProperty("Range", "bytes=" + file.length() + "-");
			httpURLConnection.addRequestProperty("User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
			int ResponseCode = httpURLConnection.getResponseCode();
			switch (ResponseCode) {
			case 200:
				break;
			case 404:
				return new Detect401(ResponseCode);
			case 401:
				return new Detect401(ResponseCode);
			case 503:
				return new Detect401(ResponseCode);
			case 500:
				return new Detect401(ResponseCode);
			case 300:
				httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setRequestMethod("GET");
				httpURLConnection.setConnectTimeout(30000);
				httpURLConnection.setReadTimeout(readTime);
				httpURLConnection.setDoOutput(true);
				httpURLConnection.setDoInput(true);
				httpURLConnection.addRequestProperty("User-Agent",
						"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
				httpURLConnection.addRequestProperty("accept", "*/*;" + token);
				httpURLConnection.addRequestProperty("accept-encoding", "gzip");
				break;
			case 416:
				return new Detect401(ResponseCode);
			case 429:
				List<String> code429 = httpURLConnection.getHeaderFields().get("Retry-After");
				if (code429 != null) {
					System.out.println(code429.toString());
					return new Detect401(429, code429.get(0));
				} else {
					return new Detect401(429, null);
				}
			default:
				Map<String, List<String>> tmp = httpURLConnection.getHeaderFields();
				Set<String> keys = tmp.keySet();
				Iterator<String> a = keys.iterator();
				while (a.hasNext()) {
					String key = (String) a.next();
					System.out.println(key + ":" + tmp.get(key));
				}
			}
			BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
			FileOutputStream bos = new FileOutputStream(file, true);
			int len;
			byte[] arr = new byte[1024];
			while ((len = bis.read(arr)) != -1) {
				bos.write(arr, 0, len);
				bos.flush();
			}
			bos.close();
			return new Detect401();
		} catch (Exception e) {
			if (e.getMessage().equals("Read timed out") || e.getMessage().equals("Connection reset")
					|| e.getMessage().equals("connect timed out")
					|| e.getMessage().equals("Remote host closed connection during handshake")) {
				System.err.println(e.toString());
				return new Detect401(500, "time out");
			} else if (e.getMessage().equals("assets.cesium.com")) {
				System.err.println(e.toString());
				return new Detect401(500, "assets.cesium.com");
			} else {
				System.err.println(e.toString());
				return new Detect401(500);
			}
		}
	}

	public static byte[] getAT(String path) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(2500);
			httpURLConnection.setReadTimeout(2500);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len;
			byte[] arr = new byte[1024];
			while ((len = bis.read(arr)) != -1) {
				bos.write(arr, 0, len);
				bos.flush();
			}
			bos.close();
			return bos.toByteArray();
		} catch (Exception e) {
			if (e.getMessage().equals("Read timed out") || (e.getMessage().equals("connect timed out"))) {
				return null;
			} else {
				System.err.println(e.toString());
				// e.printStackTrace();
			}
		}
		return null;
	}

	public static byte[] getRoadStatus(String path, String data) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.addRequestProperty("wayPoint", data);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setConnectTimeout(5000);
			httpURLConnection.setReadTimeout(5000);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len;
			byte[] arr = new byte[1024];
			while ((len = bis.read(arr)) != -1) {
				bos.write(arr, 0, len);
				bos.flush();
			}
			bos.close();
			return bos.toByteArray();
		} catch (Exception e) {
			if (e.getMessage().equals("Read timed out") || (e.getMessage().equals("connect timed out"))) {
				return null;
			} else {
				System.err.println(e.toString());
				// e.printStackTrace();
			}
		}
		return null;
	}

}
