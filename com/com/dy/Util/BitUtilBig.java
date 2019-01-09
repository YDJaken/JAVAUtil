package com.dy.Util;

public class BitUtilBig extends BitUtil {
	// 高序字节
	BitUtilBig() {
	}

	/**
	 * 将高序字节转换为short
	 */
	@Override
	public final short toShort(byte[] b, int offset) {
		return (short) ((b[offset] & 0xFF) << 8 | (b[offset + 1] & 0xFF));
	}

	/**
	 * 将short转换为高序字节
	 */
	@Override
	public void fromShort(byte[] bytes, short value, int offset) {
		bytes[offset] = (byte) (value >> 8);
		bytes[offset + 1] = (byte) (value);
	}

	/**
	 * 将高序字节转换为int
	 */
	@Override
	public final int toInt(byte[] b, int offset) {
		return (b[offset] & 0xFF) << 24 | (b[++offset] & 0xFF) << 16 | (b[++offset] & 0xFF) << 8 | (b[++offset] & 0xFF);
	}

	/**
	 * 将int转换为高序字节
	 */
	@Override
	public final void fromInt(byte[] bytes, int value, int offset) {
		bytes[offset] = (byte) (value >> 24);
		bytes[++offset] = (byte) (value >> 16);
		bytes[++offset] = (byte) (value >> 8);
		bytes[++offset] = (byte) (value);
	}

	/**
	 * 将高序字节转换为long
	 * 
	 * @param int0 高位字节
	 * @param int1 低位字节
	 */
	@Override
	public final long toLong(int int0, int int1) {
		return ((long) int0 << 32) | (int1 & 0xFFFFFFFFL);
	}

	/**
	 * 将高序字节转换为long
	 * 
	 * @param offset 区分高低位字节的位置
	 */
	@Override
	public final long toLong(byte[] b, int offset) {
		return ((long) toInt(b, offset) << 32) | (toInt(b, offset + 4) & 0xFFFFFFFFL);
	}

	/**
	 * 将long转换为高序字节
	 */
	@Override
	public final void fromLong(byte[] bytes, long value, int offset) {
		bytes[offset] = (byte) (value >> 56);
		bytes[++offset] = (byte) (value >> 48);
		bytes[++offset] = (byte) (value >> 40);
		bytes[++offset] = (byte) (value >> 32);
		bytes[++offset] = (byte) (value >> 24);
		bytes[++offset] = (byte) (value >> 16);
		bytes[++offset] = (byte) (value >> 8);
		bytes[++offset] = (byte) (value);
	}

	/**
	 * 将 BitString 转换为高位字节数组
	 */
	@Override
	public byte[] fromBitString(String str) {
		int strLen = str.length();
		int bLen = str.length() / 8;
		if (strLen % 8 != 0)
			bLen++;

		byte[] bytes = new byte[bLen];
		int charI = 0;
		for (int b = 0; b < bLen; b++) {
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
	 * 将高序字节转换为BitString
	 */
	@Override
	public String toBitString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 8);
		byte lastBit = (byte) (1 << 7);
		for (byte b : bytes) {
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

	/**
	 * 部分反转
	 * 
	 * @param v       需要被反转的字节
	 * @param maxBits 需要被反转的字节位
	 * @return
	 */
	final long reversePart(long v, int maxBits) {
		long rest = v & (~((1L << maxBits) - 1));
		return rest | reverse(v, maxBits);
	}

	@Override
	public String toString() {
		return "big";
	}

	public static void main(String[] args) {
		/*
		 * BitUtilBig big = new BitUtilBig(); long size = big.toLong(50, 60);
		 * System.out.println(size + "");
		 */
	}
}
