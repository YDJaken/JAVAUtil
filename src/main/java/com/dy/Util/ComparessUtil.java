package com.dy.Util;

import java.util.HashMap;
import java.util.Map;

public class ComparessUtil {

	public static final Map<Byte, Character> lookUpTable = new HashMap<>();
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
		lookUpTable.put((byte) 0x52, '\\');
		lookUpTable.put((byte) 0x53, '|');
		lookUpTable.put((byte) 0x54, ';');
		lookUpTable.put((byte) 0x55, ':');
		lookUpTable.put((byte) 0x56, '\'');
		lookUpTable.put((byte) 0x57, '"');
		lookUpTable.put((byte) 0x58, ',');
		lookUpTable.put((byte) 0x59, '<');
		lookUpTable.put((byte) 0x5a, '.');
		lookUpTable.put((byte) 0x5b, '>');
		lookUpTable.put((byte) 0x5c, '/');
		lookUpTable.put((byte) 0x5d, '?');
		lookUpTable.put((byte) 0x5e, ' ');
		lookUpTable.put((byte) 0x5f, '\t');

		// 6
		lookUpTable.put((byte) 0x60, '\r');
		lookUpTable.put((byte) 0x61, '\n');
		lookUpTable.put((byte) 0x62, '\b');
		lookUpTable.put((byte) 0x63, '\f');
	}

	public static void main(String[] args) {
		System.out.println(lookUpTable.get((byte) 0x34));
	}
}
