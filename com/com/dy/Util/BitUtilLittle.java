package com.dy.Util;

public class BitUtilLittle extends BitUtil {
	// 低序字节
	BitUtilLittle() {
	}

	/**
	 * 将低序字节转换为short
	 */
	@Override
	public final short toShort(byte[] b, int offset) {
		return (short) ((b[offset + 1] & 0xFF) << 8 | (b[offset] & 0xFF));
	}

	/**
	 * 将short转换为低序字节
	 */
	@Override
	public void fromShort(byte[] bytes, short value, int offset) {
		bytes[offset + 1] = (byte) (value >>> 8);
		bytes[offset] = (byte) (value);
	}

	/**
	 * 将低序字节转换为int
	 */
	@Override
	public final int toInt(byte[] b, int offset) {
		return (b[offset + 3] & 0xFF) << 24 | (b[offset + 2] & 0xFF) << 16 | (b[offset + 1] & 0xFF) << 8
				| (b[offset] & 0xFF);
	}

	/**
	 * 将int转换为低序字节
	 */
	@Override
	public final void fromInt(byte[] bytes, int value, int offset) {
		bytes[offset + 3] = (byte) (value >>> 24);
		bytes[offset + 2] = (byte) (value >>> 16);
		bytes[offset + 1] = (byte) (value >>> 8);
		bytes[offset] = (byte) (value);
	}

	/**
	 * 将低序字节转换为long
	 * 
	 * @param int1 高位字节
	 * @param int0 低位字节
	 */
	@Override
	public final long toLong(int int0, int int1) {
		return ((long) int1 << 32) | (int0 & 0xFFFFFFFFL);
	}

	/**
	 * 将低序字节转换为long
	 * 
	 * @param offset 区分高低位字节的位置
	 */
	@Override
	public final long toLong(byte[] b, int offset) {
		return ((long) toInt(b, offset + 4) << 32) | (toInt(b, offset) & 0xFFFFFFFFL);
	}

	/**
	 * 将long转换为低序字节
	 */
	@Override
	public final void fromLong(byte[] bytes, long value, int offset) {
		bytes[offset + 7] = (byte) (value >> 56);
		bytes[offset + 6] = (byte) (value >> 48);
		bytes[offset + 5] = (byte) (value >> 40);
		bytes[offset + 4] = (byte) (value >> 32);
		bytes[offset + 3] = (byte) (value >> 24);
		bytes[offset + 2] = (byte) (value >> 16);
		bytes[offset + 1] = (byte) (value >> 8);
		bytes[offset] = (byte) (value);
	}

	/**
	 * 将 BitString 转换为低位字节数组
	 */
	@Override
	public byte[] fromBitString(String str) {
		// no need for performance or memory tuning ...
		int strLen = str.length();
		int bLen = str.length() / 8;
		if (strLen % 8 != 0)
			bLen++;

		byte[] bytes = new byte[bLen];
		int charI = 0;
		for (int b = bLen - 1; b >= 0; b--) {
			byte res = 0;
			for (int i = 0; i < 8; i++) {
				res <<= 1;
				if (charI < strLen && str.charAt(charI) != '0')
					res |= 1;

				charI++;
			}
			bytes[b] = res;
		}
		return bytes;
	}

	/**
	 * 将低序字节转换为BitString
	 */
	@Override
	public String toBitString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 8);
		byte lastBit = (byte) (1 << 7);
		for (int bIndex = bytes.length - 1; bIndex >= 0; bIndex--) {
			byte b = bytes[bIndex];
			for (int i = 0; i < 8; i++) {
				if ((b & lastBit) == 0)
					sb.append('0');
				else
					sb.append('1');

				b <<= 1;
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return "little";
	}
}
