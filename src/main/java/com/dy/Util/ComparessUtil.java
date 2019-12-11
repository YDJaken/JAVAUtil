package com.dy.Util;

import java.util.Map;

public class ComparessUtil {

	private static final byte ZERO_BYTE = 0x00;
	
	public static final Map<Byte, Character> lookUpTable = new DoubleSideMap<>();
	public static final Map<String, Integer> encodingTable1 = new DoubleSideMap<>();
	public static final Map<String, Integer> encodingTable2 = new DoubleSideMap<>();
	static {
		// 0
		lookUpTable.put((byte) 0x00, '0');
		lookUpTable.put((byte) 0x01, '1');
		lookUpTable.put((byte) 0x02, '2');
		lookUpTable.put((byte) 0x03, '3');
		lookUpTable.put((byte) 0x04, '4');
		lookUpTable.put((byte) 0x05, '5');
		lookUpTable.put((byte) 0x06, '6');
		lookUpTable.put((byte) 0x07, '7');
		lookUpTable.put((byte) 0x08, '8');
		lookUpTable.put((byte) 0x09, '9');
		lookUpTable.put((byte) 0x0a, 'a');
		lookUpTable.put((byte) 0x0b, 'b');
		lookUpTable.put((byte) 0x0c, 'c');
		lookUpTable.put((byte) 0x0d, 'd');
		lookUpTable.put((byte) 0x0e, 'e');
		lookUpTable.put((byte) 0x0f, 'f');

		// 1
		lookUpTable.put((byte) 0x10, 'g');
		lookUpTable.put((byte) 0x11, 'h');
		lookUpTable.put((byte) 0x12, 'i');
		lookUpTable.put((byte) 0x13, 'j');
		lookUpTable.put((byte) 0x14, 'k');
		lookUpTable.put((byte) 0x15, 'l');
		lookUpTable.put((byte) 0x16, 'm');
		lookUpTable.put((byte) 0x17, 'n');
		lookUpTable.put((byte) 0x18, 'o');
		lookUpTable.put((byte) 0x19, 'p');
		lookUpTable.put((byte) 0x1a, 'q');
		lookUpTable.put((byte) 0x1b, 'r');
		lookUpTable.put((byte) 0x1c, 's');
		lookUpTable.put((byte) 0x1d, 't');
		lookUpTable.put((byte) 0x1e, 'u');
		lookUpTable.put((byte) 0x1f, 'v');

		// 2
		lookUpTable.put((byte) 0x20, 'w');
		lookUpTable.put((byte) 0x21, 'x');
		lookUpTable.put((byte) 0x22, 'y');
		lookUpTable.put((byte) 0x23, 'z');
		lookUpTable.put((byte) 0x24, 'A');
		lookUpTable.put((byte) 0x25, 'B');
		lookUpTable.put((byte) 0x26, 'C');
		lookUpTable.put((byte) 0x27, 'D');
		lookUpTable.put((byte) 0x28, 'E');
		lookUpTable.put((byte) 0x29, 'F');
		lookUpTable.put((byte) 0x2a, 'G');
		lookUpTable.put((byte) 0x2b, 'H');
		lookUpTable.put((byte) 0x2c, 'I');
		lookUpTable.put((byte) 0x2d, 'J');
		lookUpTable.put((byte) 0x2e, 'K');
		lookUpTable.put((byte) 0x2f, 'L');

		// 3
		lookUpTable.put((byte) 0x30, 'M');
		lookUpTable.put((byte) 0x31, 'N');
		lookUpTable.put((byte) 0x32, 'O');
		lookUpTable.put((byte) 0x33, 'P');
		lookUpTable.put((byte) 0x34, 'Q');
		lookUpTable.put((byte) 0x35, 'R');
		lookUpTable.put((byte) 0x36, 'S');
		lookUpTable.put((byte) 0x37, 'T');
		lookUpTable.put((byte) 0x38, 'U');
		lookUpTable.put((byte) 0x39, 'V');
		lookUpTable.put((byte) 0x3a, 'W');
		lookUpTable.put((byte) 0x3b, 'X');
		lookUpTable.put((byte) 0x3c, 'Y');
		lookUpTable.put((byte) 0x3d, 'Z');
		lookUpTable.put((byte) 0x3e, '`');
		lookUpTable.put((byte) 0x3f, '~');

		// 4
		lookUpTable.put((byte) 0x40, '!');
		lookUpTable.put((byte) 0x41, '@');
		lookUpTable.put((byte) 0x42, '#');
		lookUpTable.put((byte) 0x43, '$');
		lookUpTable.put((byte) 0x44, '%');
		lookUpTable.put((byte) 0x45, '^');
		lookUpTable.put((byte) 0x46, '&');
		lookUpTable.put((byte) 0x47, '*');
		lookUpTable.put((byte) 0x48, '(');
		lookUpTable.put((byte) 0x49, ')');
		lookUpTable.put((byte) 0x4a, '-');
		lookUpTable.put((byte) 0x4b, '_');
		lookUpTable.put((byte) 0x4c, '=');
		lookUpTable.put((byte) 0x4d, '+');
		lookUpTable.put((byte) 0x4e, '[');
		lookUpTable.put((byte) 0x4f, '{');

		// 5
		lookUpTable.put((byte) 0x50, ']');
		lookUpTable.put((byte) 0x51, '}');
		lookUpTable.put((byte) 0x52, '/');
		lookUpTable.put((byte) 0x53, '|');
		lookUpTable.put((byte) 0x54, ';');
		lookUpTable.put((byte) 0x55, ':');
		lookUpTable.put((byte) 0x56, '\'');
		lookUpTable.put((byte) 0x57, '?');
		lookUpTable.put((byte) 0x58, ',');
		lookUpTable.put((byte) 0x59, '<');
		lookUpTable.put((byte) 0x5a, '.');
		lookUpTable.put((byte) 0x5b, '>');
		lookUpTable.put((byte) 0x5c, ' ');
//		lookUpTable.put((byte) 0x5d, '"');
//		lookUpTable.put((byte) 0x5e, '\\');
//		lookUpTable.put((byte) 0x5f, '\t');

		// 6
//		lookUpTable.put((byte) 0x60, '\r');
//		lookUpTable.put((byte) 0x61, '\n');
//		lookUpTable.put((byte) 0x62, '\b');
//		lookUpTable.put((byte) 0x63, '\f');
		
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
	
	public static String comparess(byte b) {
		int distance = b - ZERO_BYTE;
		int fourthZone = distance % 2; //Math.floorDiv(distance, 2);
		distance = distance - fourthZone; //2 * fourthZone;
		int thirdZone = Math.floorDiv(distance, 4);
		distance = distance - 4 * thirdZone;
		int secondZone = Math.floorDiv(distance, 4);
		distance = distance - 4 * secondZone;
		int firstZone = Math.floorDiv(distance, 8);
		distance = distance - 8 * firstZone;
		if(distance!= 0) {
			System.out.println("wrong");
			return "";
		}
		int table1 = encodingTable1.get(firstZone+"-"+ secondZone);
		int table2 = encodingTable2.get(thirdZone+"-"+ fourthZone);
		return lookUpTable.get((byte)(ZERO_BYTE+table1)) + lookUpTable.get((byte)(ZERO_BYTE+table2)) + "";
	}
	
//	public static byte unComparess(String str) {
//		
//	}
	
	public static void main(String[] args) {
		int i = 0;
		while(i< 256) {
			System.out.println(ComparessUtil.comparess((byte)(ZERO_BYTE + i)));
			i++;
		}
//		System.out.println(lookUpTable.toString());
//		int i = 0;
//		char a = 0x20;
//		char[] as = new char[600];
//		while(i< 600) {
//			a += 1;
//			System.out.println(a);
//			as[i] = a;
//			i++;
//		}
//		
//		File tmp = new File("D:\\test.txt");
//		FileUtil.writeString(tmp, new String(as),"GBK");
	}
}
