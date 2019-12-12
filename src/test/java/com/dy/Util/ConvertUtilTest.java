package com.dy.Util;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import org.junit.Test;

public class ConvertUtilTest {

	@Test
	public void testSingle() {
		byte ZERO_BYTE = -128;
		int i = 0;
		while (i < 256) {
			byte input = (byte) (ZERO_BYTE + i);
			char[] compress = ConvertUtil.convert(input);
			byte output = ConvertUtil.unConvert(compress[0], compress[1]);
			assertTrue(input == output);
			i++;
		}
	}

	@Test
	public void testString() {
		String testCase = "1a2b3c4d5f6e";
		try {
			byte[] test = testCase.getBytes("UTF-8");
			String out = ConvertUtil.convertAll(test);
			byte[] ret = ConvertUtil.unConvertAll(out);
			assertArrayEquals(test, ret);
			test = testCase.getBytes("GBK");
			out = ConvertUtil.convertAll(test);
			ret = ConvertUtil.unConvertAll(out);
			assertArrayEquals(test, ret);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testInt() {
		int i = 0;
		while (i < 500) {
			int test = (int) Math.round(Math.random() * 500);
			byte[] big = BitUtil.BIG.fromInt(test);
			String out = ConvertUtil.convertAll(big);
			byte[] ret = ConvertUtil.unConvertAll(out);
			assertArrayEquals(big, ret);
			byte[] little = BitUtil.LITTLE.fromInt(test);
			out = ConvertUtil.convertAll(little);
			ret = ConvertUtil.unConvertAll(out);
			assertArrayEquals(little, ret);
			i++;
		}
	}

	@Test
	public void testDouble() {
		int i = 0;
		while (i < 500) {
			double test = Math.random() * 500;
			byte[] big = BitUtil.BIG.fromDouble(test);
			String out = ConvertUtil.convertAll(big);
			byte[] ret = ConvertUtil.unConvertAll(out);
			assertArrayEquals(big, ret);
			byte[] little = BitUtil.LITTLE.fromDouble(test);
			out = ConvertUtil.convertAll(little);
			ret = ConvertUtil.unConvertAll(out);
			assertArrayEquals(little, ret);
			i++;
		}
	}

}
