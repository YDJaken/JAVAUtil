package com.dy.Util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONObjectUtil {
	public static void convertJSON(JSONArray obj) {
		obj.forEach((value) -> {
			if (value instanceof JSONObject) {
				convertJSON((JSONObject) value);
				return;
			} else if (value instanceof JSONArray) {
				convertJSON((JSONArray) value);
				return;
			}
			String encode = getEnCode(value);
			if (encode != null) {
				int index = obj.indexOf(value);
				obj.set(index, encode);
			}
		});
	}

	private static byte[] reduceBytes(byte[] input) {
		LinkedList<Byte> ls = new LinkedList<>();
		for (int i = 0; i < input.length; i++) {
			if (input[i] != 0 || ls.size() != 0) {
				ls.add(input[i]);
			}
		}
		if (ls.size() == 0) {
			ls.add((byte) 0x00);
		}
		Byte[] rs = new Byte[ls.size()];
		ls.toArray(rs);
		byte[] ret = new byte[ls.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = rs[i];
		}
		return ret;
	}

	private static byte[] getBytes(Object input) {
		if (input instanceof Integer) {
			return reduceBytes(BitUtil.LITTLE.fromInt((int) input));
		} else if (input instanceof Float) {
			return BitUtil.LITTLE.fromFloat((float) input);
		} else if (input instanceof Double) {
			return BitUtil.LITTLE.fromDouble((double) input);
		} else if (input instanceof Long) {
			return reduceBytes(BitUtil.LITTLE.fromLong((long) input));
		} else if (input instanceof Short) {
			return reduceBytes(BitUtil.LITTLE.fromShort((short) input));
		} else if (input instanceof Byte) {
			return new byte[] { (byte) input };
		} else {
			return null;
		}
	}

	private static byte[] getBytes(BigDecimal input) {
		BigDecimal a = new BigDecimal(input.intValue());
		if (a.compareTo(input) == 0) {
			return getBytes(input.toBigInteger());
		}
		int precision = input.precision();
		BigDecimal target = new BigDecimal(input.doubleValue());
		BigDecimal target2 = new BigDecimal(input.floatValue());
		if (target2.subtract(target).abs().doubleValue() <= Math.pow(.1, precision)) {
			return BitUtil.LITTLE.fromFloat(input.floatValue());
		} else {
			return BitUtil.LITTLE.fromDouble(input.doubleValue());
		}
	}

	private static byte[] getBytes(BigInteger input) {
		try {
			byte a = input.byteValueExact();
			return new byte[] { a };
		} catch (ArithmeticException e) {
			try {
				return BitUtil.LITTLE.fromShort(input.shortValueExact());
			} catch (ArithmeticException ea) {
				try {
					return BitUtil.LITTLE.fromInt(input.intValueExact());
				} catch (ArithmeticException es) {
					return BitUtil.LITTLE.fromLong(input.longValue());
				}
			}
		}
	}

	private static String getEnCode(Object value) {
		String encode = null;
		if (value instanceof BigDecimal) {
			byte[] little = getBytes((BigDecimal) value);
			if (little != null) {
				encode = ConvertUtil.convertAll(little);

			}
		} else if (value instanceof BigInteger) {
			byte[] little = getBytes((BigInteger) value);
			if (little != null) {
				encode = ConvertUtil.convertAll(little);
			}
		} else {
			byte[] little = getBytes(value);
			if (little != null) {
				encode = ConvertUtil.convertAll(little);
			}
		}
		return encode;
	}

	public static void convertJSON(JSONObject obj) {
		obj.forEach((key, value) -> {
			if (value instanceof JSONObject) {
				convertJSON((JSONObject) value);
				return;
			} else if (value instanceof JSONArray) {
				convertJSON((JSONArray) value);
				return;
			}
			String encode = getEnCode(value);
			if (encode != null) {
				obj.put(key, encode);
			}
		});
	}

}
