package com.dy.Util;

import java.io.UnsupportedEncodingException;

public class ConvertUtil {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String a = "postgresAircas@2020";
		String cover = convertAll(a.getBytes("UTF-8"));
		System.out.println(cover);
		System.out.println(new String(unConvertAll(cover),"UTF-8"));
	}

	private static final byte ZERO_BYTE = -128;

	public static final DoubleSideMap<Byte, Character> lookUpTable = new DoubleSideMap<>();
	public static final DoubleSideMap<String, Integer> encodingTable1 = new DoubleSideMap<>();
	public static final DoubleSideMap<String, Integer> encodingTable2 = new DoubleSideMap<>();
	static {
		byte tmp = ZERO_BYTE;
		// 0
		lookUpTable.put(tmp++, '0');
		lookUpTable.put(tmp++, '1');
		lookUpTable.put(tmp++, '2');
		lookUpTable.put(tmp++, '3');
		lookUpTable.put(tmp++, '4');
		lookUpTable.put(tmp++, '5');
		lookUpTable.put(tmp++, '6');
		lookUpTable.put(tmp++, '7');
		lookUpTable.put(tmp++, '8');
		lookUpTable.put(tmp++, '9');
		lookUpTable.put(tmp++, 'a');
		lookUpTable.put(tmp++, 'b');
		lookUpTable.put(tmp++, 'c');
		lookUpTable.put(tmp++, 'd');
		lookUpTable.put(tmp++, 'e');
		lookUpTable.put(tmp++, 'f');

		// 1
		lookUpTable.put(tmp++, 'g');
		lookUpTable.put(tmp++, 'h');
		lookUpTable.put(tmp++, 'i');
		lookUpTable.put(tmp++, 'j');
		lookUpTable.put(tmp++, 'k');
		lookUpTable.put(tmp++, 'l');
		lookUpTable.put(tmp++, 'm');
		lookUpTable.put(tmp++, 'n');
		lookUpTable.put(tmp++, 'o');
		lookUpTable.put(tmp++, 'p');
		lookUpTable.put(tmp++, 'q');
		lookUpTable.put(tmp++, 'r');
		lookUpTable.put(tmp++, 's');
		lookUpTable.put(tmp++, 't');
		lookUpTable.put(tmp++, 'u');
		lookUpTable.put(tmp++, 'v');

		// 2
		lookUpTable.put(tmp++, 'w');
		lookUpTable.put(tmp++, 'x');
		lookUpTable.put(tmp++, 'y');
		lookUpTable.put(tmp++, 'z');
		lookUpTable.put(tmp++, 'A');
		lookUpTable.put(tmp++, 'B');
		lookUpTable.put(tmp++, 'C');
		lookUpTable.put(tmp++, 'D');
		lookUpTable.put(tmp++, 'E');
		lookUpTable.put(tmp++, 'F');
		lookUpTable.put(tmp++, 'G');
		lookUpTable.put(tmp++, 'H');
		lookUpTable.put(tmp++, 'I');
		lookUpTable.put(tmp++, 'J');
		lookUpTable.put(tmp++, 'K');
		lookUpTable.put(tmp++, 'L');

		// 3
		lookUpTable.put(tmp++, 'M');
		lookUpTable.put(tmp++, 'N');
		lookUpTable.put(tmp++, 'O');
		lookUpTable.put(tmp++, 'P');
		lookUpTable.put(tmp++, 'Q');
		lookUpTable.put(tmp++, 'R');
		lookUpTable.put(tmp++, 'S');
		lookUpTable.put(tmp++, 'T');
		lookUpTable.put(tmp++, 'U');
		lookUpTable.put(tmp++, 'V');
		lookUpTable.put(tmp++, 'W');
		lookUpTable.put(tmp++, 'X');
		lookUpTable.put(tmp++, 'Y');
		lookUpTable.put(tmp++, 'Z');
		lookUpTable.put(tmp++, '`');
		lookUpTable.put(tmp++, '~');

		// 4
		lookUpTable.put(tmp++, '!');
		lookUpTable.put(tmp++, '@');
		lookUpTable.put(tmp++, '#');
		lookUpTable.put(tmp++, '$');
		lookUpTable.put(tmp++, '%');
		lookUpTable.put(tmp++, '^');
		lookUpTable.put(tmp++, '&');
		lookUpTable.put(tmp++, '*');
		lookUpTable.put(tmp++, '(');
		lookUpTable.put(tmp++, ')');
		lookUpTable.put(tmp++, '-');
		lookUpTable.put(tmp++, '_');
		lookUpTable.put(tmp++, '=');
		lookUpTable.put(tmp++, '+');
		lookUpTable.put(tmp++, '[');
		lookUpTable.put(tmp++, '{');

		// 5
		lookUpTable.put(tmp++, ']');
		lookUpTable.put(tmp++, '}');
		lookUpTable.put(tmp++, '/');
		lookUpTable.put(tmp++, '|');
		lookUpTable.put(tmp++, ';');
		lookUpTable.put(tmp++, ':');
		lookUpTable.put(tmp++, '\'');
		lookUpTable.put(tmp++, '?');
		lookUpTable.put(tmp++, ',');
		lookUpTable.put(tmp++, '<');
		lookUpTable.put(tmp++, '.');
		lookUpTable.put(tmp++, '>');
		lookUpTable.put(tmp++, ' ');
//		lookUpTable.put(tmp++, '"');
//		lookUpTable.put(tmp++, '\\');
//		lookUpTable.put(tmp++, '\t');

		// 6
//		lookUpTable.put(tmp++, '\r');
//		lookUpTable.put(tmp++, '\n');
//		lookUpTable.put(tmp++, '\b');
//		lookUpTable.put(tmp++, '\f');

		// 共100 个
	}

	static {
		encodingTable1.put("0-0", 0);
		encodingTable1.put("0-1", 1);
		encodingTable1.put("1-0", 2);
		encodingTable1.put("1-1", 3);
		encodingTable1.put("0-2", 4);
		encodingTable1.put("1-2", 5);
		encodingTable1.put("0-3", 6);
		encodingTable1.put("1-3", 7);
	}

	static {
		encodingTable2.put("0-0", 0);
		encodingTable2.put("0-1", 1);
		encodingTable2.put("1-0", 2);
		encodingTable2.put("0-2", 3);
		encodingTable2.put("1-1", 4);
		encodingTable2.put("2-0", 5);
		encodingTable2.put("0-3", 6);
		encodingTable2.put("1-2", 7);
		encodingTable2.put("2-1", 8);
		encodingTable2.put("3-0", 9);
		encodingTable2.put("0-4", 10);
		encodingTable2.put("1-3", 11);
		encodingTable2.put("2-2", 12);
		encodingTable2.put("3-1", 13);
		encodingTable2.put("0-5", 14);
		encodingTable2.put("1-4", 15);
		encodingTable2.put("2-3", 16);
		encodingTable2.put("3-2", 17);
		encodingTable2.put("0-6", 18);
		encodingTable2.put("1-5", 19);
		encodingTable2.put("2-4", 20);
		encodingTable2.put("3-3", 21);
		encodingTable2.put("0-7", 22);
		encodingTable2.put("1-6", 23);
		encodingTable2.put("2-5", 24);
		encodingTable2.put("3-4", 25);
		encodingTable2.put("1-7", 26);
		encodingTable2.put("2-6", 27);
		encodingTable2.put("3-5", 28);
		encodingTable2.put("2-7", 29);
		encodingTable2.put("3-6", 30);
		encodingTable2.put("3-7", 31);
	}

	public static char[] convert(byte b) {
		int distance = b - ZERO_BYTE;
		int firstZone = Math.floorDiv(distance, 32);
		distance = distance - 32 * firstZone;
		int secondZone = Math.floorDiv(distance, 8);
		distance = distance - 8 * secondZone;
		int thirdZone = Math.floorDiv(distance, 2);
		distance = distance - 2 * thirdZone;
		int fourthZone = distance % 2;
		distance = distance - fourthZone;
		if (distance != 0) {
			System.out.println("wrong");
			return null;
		}
		int table2 = encodingTable2.get(secondZone + "-" + firstZone);
		int table1 = encodingTable1.get(fourthZone + "-" + thirdZone);
		char A = lookUpTable.get((byte) (ZERO_BYTE + table1));
		char D = lookUpTable.get((byte) (ZERO_BYTE + table2));
		return new char[] { A, D };
	}

	public static void convert(byte b, StringBuilder sb) {
		sb.append(convert(b));
	}

	public static String convertAll(byte[] input) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length; i++) {
			convert(input[i], sb);
		}
		return sb.toString();
	}

	public static byte unConvert(char A, char D) {
		int table1 = lookUpTable.getByValue(A) - ZERO_BYTE;
		int table2 = lookUpTable.getByValue(D) - ZERO_BYTE;
		String tab1 = encodingTable1.getByValue(table1);
		String[] tmp = tab1.split("-");
		int fourthZone = Integer.parseInt(tmp[0]);
		int thirdZone = Integer.parseInt(tmp[1]);
		String tab2 = encodingTable2.getByValue(table2);
		tmp = tab2.split("-");
		int secondZone = Integer.parseInt(tmp[0]);
		int firstZone = Integer.parseInt(tmp[1]);
		int distance = fourthZone + 2 * thirdZone + 8 * secondZone + 32 * firstZone;
		if (distance > 255) {
			System.out.println("wrong");
			return ZERO_BYTE;
		}
		return (byte) (ZERO_BYTE + distance);
	}

	public static byte[] unConvertAll(char[] chars) {
		int length = chars.length;
		if (length % 2 != 0) {
			return null;
		}
		byte[] ret = new byte[length / 2];
		for (int i = 0; i < ret.length; i++) {
			int z = i * 2;
			ret[i] = unConvert(chars[z++], chars[z]);
		}
		return ret;
	}

	public static byte[] unConvertAll(String str) {
		return unConvertAll(str.toCharArray());
	}
}
