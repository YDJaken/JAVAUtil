package com.dy.Util;

import java.nio.ByteOrder;

public abstract class BitUtil {
	// 低序字节
	public static final BitUtil LITTLE = new BitUtilLittle();
	// 高序字节
	public static final BitUtil BIG = new BitUtilBig();

	public static BitUtil get(ByteOrder order) {
		if (order.equals(ByteOrder.BIG_ENDIAN))
			return BitUtil.BIG;
		else
			return BitUtil.LITTLE;
	}

	/**
	 * 将字节数组转为Double
	 * 
	 * @param bytes
	 * @return
	 */
	public final double toDouble(byte[] bytes) {
		return toDouble(bytes, 0);
	}

	public final double toDouble(byte[] bytes, int offset) {
		return Double.longBitsToDouble(toLong(bytes, offset));
	}

	/**
	 * 将double 转换为 字节数组
	 * 
	 * @param value
	 * @return
	 */
	public final byte[] fromDouble(double value) {
		byte[] bytes = new byte[8];
		fromDouble(bytes, value, 0);
		return bytes;
	}

	public final void fromDouble(byte[] bytes, double value) {
		fromDouble(bytes, value, 0);
	}

	public final void fromDouble(byte[] bytes, double value, int offset) {
		fromLong(bytes, Double.doubleToRawLongBits(value), offset);
	}

	/**
	 * 将字节数组转为Float
	 * 
	 * @param bytes
	 * @return
	 */
	public final float toFloat(byte[] bytes) {
		return toFloat(bytes, 0);
	}

	public final float toFloat(byte[] bytes, int offset) {
		return Float.intBitsToFloat(toInt(bytes, offset));
	}

	/**
	 * 将float 转换为 字节数组
	 * 
	 * @param value
	 * @return
	 */
	public final byte[] fromFloat(float value) {
		byte[] bytes = new byte[4];
		fromFloat(bytes, value, 0);
		return bytes;
	}

	public final void fromFloat(byte[] bytes, float value) {
		fromFloat(bytes, value, 0);
	}

	public final void fromFloat(byte[] bytes, float value, int offset) {
		fromInt(bytes, Float.floatToRawIntBits(value), offset);
	}

	/**
	 * 将字节数组转为Short
	 * 
	 * @param b
	 * @return
	 */
	public final short toShort(byte[] b) {
		return toShort(b, 0);
	}

	public abstract short toShort(byte[] b, int offset);

	/**
	 * 将Short转为 字节数组
	 * 
	 * @param b
	 * @return
	 */
	public final byte[] fromShort(short value) {
		byte[] bytes = new byte[4];
		fromShort(bytes, value, 0);
		return bytes;
	}

	public final void fromShort(byte[] bytes, short value) {
		fromShort(bytes, value, 0);
	}
	
	public abstract void fromShort(byte[] bytes, short value, int offset);

	/**
	 * 将字节数组转为int
	 * 
	 * @param b
	 * @return
	 */
	public final int toInt(byte[] b) {
		return toInt(b, 0);
	}

	public abstract int toInt(byte[] b, int offset);

	/**
	 * 将int转为 字节数组
	 * 
	 * @param b
	 * @return
	 */
	public final byte[] fromInt(int value) {
		byte[] bytes = new byte[4];
		fromInt(bytes, value, 0);
		return bytes;
	}

	public final void fromInt(byte[] bytes, int value) {
		fromInt(bytes, value, 0);
	}

	public abstract void fromInt(byte[] bytes, int value, int offset);

	/**
	 * 将字节数组转为long
	 * 
	 * @param b
	 * @return
	 */
	public final long toLong(byte[] b) {
		return toLong(b, 0);
	}

	public abstract long toLong(int high, int low);

	public abstract long toLong(byte[] b, int offset);

	/**
	 * 将long转为 字节数组
	 * 
	 * @param b
	 * @return
	 */
	public final byte[] fromLong(long value) {
		byte[] bytes = new byte[8];
		fromLong(bytes, value, 0);
		return bytes;
	}

	public final void fromLong(byte[] bytes, long value) {
		fromLong(bytes, value, 0);
	}

	public abstract void fromLong(byte[] bytes, long value, int offset);
	
	/**
	 * 将String的字节转换为long
	 * @param str
	 * @return
	 */
	public final long fromBitString2Long(String str) {
		if (str.length() > 64)
			throw new UnsupportedOperationException(
					"Strings needs to fit into a 'long' but length was " + str.length());

		long res = 0;
		int strLen = str.length();
		for (int charIndex = 0; charIndex < strLen; charIndex++) {
			res <<= 1;
			if (str.charAt(charIndex) != '0')
				res |= 1;
		}
		res <<= (64 - strLen);
		return res;
	}

	public abstract byte[] fromBitString(String str);

	/**
	 * 从long 转换为 BitString
	 * @param value
	 * @return
	 */
	public final String toBitString(long value) {
		return toBitString(value, 64);
	}

	/**
	 * 将 long 按照高位转换为String "01010101"
	 * @param value
	 * @param bits
	 * @return
	 */
	public String toLastBitString(long value, int bits) {
		StringBuilder sb = new StringBuilder(bits);
		long lastBit = 1L << bits - 1;
		for (int i = 0; i < bits; i++) {
			if ((value & lastBit) == 0)
				sb.append('0');
			else
				sb.append('1');

			value <<= 1;
		}
		return sb.toString();
	}

	/**
	 * 将 long 按照低位转换为String "01010101"
	 * @param value
	 * @param bits
	 * @return
	 */
	public String toBitString(long value, int bits) {
		StringBuilder sb = new StringBuilder(bits);
		long lastBit = 1L << 63;
		for (int i = 0; i < bits; i++) {
			if ((value & lastBit) == 0)
				sb.append('0');
			else
				sb.append('1');

			value <<= 1;
		}
		return sb.toString();
	}

	public abstract String toBitString(byte[] bytes);

	/**
	 * 将 long 的 [0,maxBits] 之间的顺序反转并丢弃更高位
	 * @param value
	 * @param maxBits
	 * @return
	 */
	public final long reverse(long value, int maxBits) {
		long res = 0;
		for (; maxBits > 0; value >>>= 1) {
			res <<= 1;
			res |= value & 1;
			maxBits--;
			if (value == 0) {
				res <<= maxBits;
				break;
			}
		}
		return res;
	}

	/**
	 * 获取long的低位
	 * @param longValue
	 * @return
	 */
	public final int getIntLow(long longValue) {
		return (int) (longValue & 0xFFFFFFFFL);
	}
	/**
	 * 获取long的高位
	 * @param longValue
	 * @return
	 */
	public final int getIntHigh(long longValue) {
		return (int) (longValue >> 32);
	}
	/**
	 * 生成long
	 * @param intLow 低位int
	 * @param intHigh 高位int
	 * @return
	 */
	public final long combineIntsToLong(int intLow, int intHigh) {
		return ((long) intHigh << 32) | (intLow & 0xFFFFFFFFL);
	}

	/**
	 * 将 long 的 [64,maxBits] 之间的顺序反转并丢弃更低位
	 * @param value
	 * @param maxBits
	 * @return
	 */
	public final long reverseLeft(long value, int maxBits) {
		long res = 0;
		int delta = 64 - maxBits;
		long maxBit = 1L << delta;
		for (; maxBits > 0; res <<= 1) {
			if ((value & maxBit) != 0)
				res |= 1;

			maxBit <<= 1;
			maxBits--;
			if (maxBit == 0) {
				res <<= delta;
				break;
			}
		}
		return res;
	}
}
