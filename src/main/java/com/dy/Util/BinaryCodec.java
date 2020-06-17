package com.dy.Util;

public class BinaryCodec {
	private static final short max = 255;
	
	public static byte[] encode(byte[] code) {
		int length = code.length;
		for (int i = 0; i < length; i++) {
			byte res = (byte) (max - code[i]);
			code[i] = res;
		}
		
		return code;
	}
	
	public static byte[] decode(byte[] code) {
		int length = code.length;
		for (int i = 0; i < length; i++) {
			byte res = (byte) (max - code[i]);
			code[i] = res;
		}
		return code;
	}
}
