package com.dy.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAEncoder {
	public static String encodeSHA(String input) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] tmp = input.getBytes();
		md.update(tmp);
		return  ConvertUtil.convertAll(md.digest(tmp));
	}
}
