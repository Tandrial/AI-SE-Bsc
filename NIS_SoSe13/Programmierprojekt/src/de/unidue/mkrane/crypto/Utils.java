package de.unidue.mkrane.crypto;

import java.util.Random;

class Utils {

	public static String GF8(String a, String b) {
		String p = "";
		boolean h_bit = false;

		for (int i = 0; i < 8; i++) {
			if (b.charAt(7) == '1')
				p = Utils.XOR(a, p);

			h_bit = (a.charAt(0) == '1') ? true : false;

			a = a.substring(1) + '0';
			if (h_bit)
				a = Utils.XOR(a, "11011"); // 0x1b

			b = '0' + b.substring(0, 7);
		}
		return p;
	}

	public static String binMult(String a, String b) {
		String result = "";

		for (int i = 0; i < b.length(); i++) {
			if (b.charAt(i) == '1') {
				result = binAdd(result,
						a + Utils.paddTo("", b.length() - 1 - i));
			}
		}
		return result;
	}

	public static String binAdd(String a, String b) {
		StringBuilder result = new StringBuilder();
		boolean carry = false;
		int len = Math.max(a.length(), b.length());
		a = Utils.paddTo(a, len);
		b = Utils.paddTo(b, len);

		for (int i = len - 1; i >= 0; i--) {
			if (XOR(a.charAt(i), b.charAt(i)).equals("0")) {
				result.append(carry ? "1" : "0");
				carry = a.charAt(i) == '1' ? true : false;
			} else {
				result.append(carry ? "0" : "1");
			}
		}
		result.append(carry ? "1" : "");

		return result.reverse().toString();
	}

	private static Random r = new Random();

	public static int randomInt(int min, int max) {
		return min + r.nextInt(max - min);
	}

	private static final String[][] HexBinLookUp = {
			{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c",
					"d", "e", "f" },
			{ "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111",
					"1000", "1001", "1010", "1011", "1100", "1101", "1110",
					"1111" } };

	private static final String[][] ASCII = new String[][] {
			{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C",
					"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
					"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
					"b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
					"n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y",
					"z" },
			{ "110000", "110001", "110010", "110011", "110100", "110101",
					"110110", "110111", "111000", "111001", "1000001",
					"1000010", "1000011", "1000100", "1000101", "1000110",
					"1000111", "1001000", "1001001", "1001010", "1001011",
					"1001100", "1001101", "1001110", "1001111", "1010000",
					"1010001", "1010010", "1010011", "1010100", "1010101",
					"1010110", "1010111", "1011000", "1011111", "1100000",
					"1100001", "1100010", "1100011", "1100100", "1100101",
					"1100110", "1100111", "1101000", "1101001", "1101010",
					"1101011", "1101100", "1101101", "1101110", "1101111",
					"1110000", "1110001", "1110010", "1110011", "1110100",
					"1110101", "1110110", "1110111", "1111000", "1111001",
					"1111010" } };

	public static String convertCharToBin(char c) {
		for (int i = 0; i < ASCII[0].length; i++) {
			if (ASCII[0][i].charAt(0) == c)
				return Utils.paddTo(ASCII[1][i], 8);
		}
		return "";
	}

	public static char convertBinToChar(String c) {
		for (int i = 0; i < ASCII[0].length; i++) {
			if (ASCII[1][i].equals(c))
				return ASCII[0][i].charAt(0);
		}
		return ' ';
	}

	public static int convertCharToDez(char c) {
		return convertBinToDez(convertCharToBin(c));
	}

	public static char convertDezToChar(int c) {
		return convertBinToChar(convertDezToBin(c));
	}

	public static String convertHexToBin(String a) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < a.length(); i++) {
			for (int j = 0; j < HexBinLookUp[0].length; j++) {
				if ((a.charAt(i) + "").equals(HexBinLookUp[0][j])) {
					result.append(HexBinLookUp[1][j]);
				}
			}
		}
		return result.toString();
	}

	public static String convertBinToHex(String a) {
		String[] tmp = Utils.splitIntoChunksOf(a, 4);

		StringBuilder result = new StringBuilder();
		for (int i = 0; i < tmp.length; i++) {
			result.append(HexBinLookUp[0][convertBinToDez(tmp[i])]);
		}
		return result.toString();
	}

	public static String convertDezToBin(int a) {
		return convertDezToBin(a, 0);
	}

	public static String convertDezToBin(int a, int padding) {
		String result = "";

		while (a > 0) {
			result = (modulo(a, 2) == 0 ? "0" : "1") + result;
			a /= 2;
		}
		return paddTo(result, padding);
	}

	public static int convertBinToDez(String s) {
		int result = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '1')
				result += Math.pow(2, s.length() - 1 - i);
		}
		return result;
	}

	public static int convertHexToDez(String a) {
		return convertBinToDez(convertHexToBin(a));
	}

	public static String convertDezToHex(int a) {
		return convertBinToHex(convertDezToBin(a, 0));
	}

	public static int[] splitStringToIntArray(String text, String c) {
		String[] strings = text.split(c);

		int[] result = new int[strings.length];
		for (int i = 0; i < result.length; i++)
			result[i] = Integer.parseInt(strings[i]);

		return result;
	}

	public static String buildStringFromArray(int[] array) {
		return buildStringFromArray(array, "");
	}

	public static String buildStringFromArray(int[] array, String c) {
		StringBuilder result = new StringBuilder();
		result.append(array[0]);

		for (int i = 1; i < array.length; i++) {
			result.append(c);
			result.append(array[i]);
		}
		return result.toString();
	}

	public static String paddTo(String a, int length) {
		if (a.length() == length)
			return a;
		if (a.length() > length)
			a = removePadding(a);

		return addPadding(a, length);
	}

	private static String addPadding(String a, int padding) {
		while (a.length() < padding)
			a = "0" + a;
		return a;
	}

	public static String removePadding(String a) {
		if (a.length() <= 1)
			return a;
		while (a.length() > 0 && a.charAt(0) == '0')
			a = a.substring(1);

		return a;
	}

	public static String[] splitIntoChunksOf(String a, int size) {
		while (Utils.modulo(a.length(), size) != 0) {
			a = "0" + a;
		}

		String[] result = new String[a.length() / size];
		for (int i = 0; i < result.length; i++) {
			result[i] = a.substring(0 + i * size, size + i * size);
		}

		return result;
	}

	public static String XOR(char a, char b) {
		return XOR(a + "", b + "");
	}

	public static String XOR(String a, String b) {
		while (a.length() > b.length())
			b = "0" + b;
		while (a.length() < b.length())
			a = "0" + a;
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < a.length(); i++) {
			if (a.charAt(i) == b.charAt(i))
				result.append("0");
			else
				result.append("1");
		}
		return result.toString();
	}

	public static int modulo(int a, int b) {
		while (a < 0)
			a += b;
		while (a >= b)
			a -= b;

		return a;
	}

	public static int powerModulo(int a, int b, int c) {
		int result = 1;

		for (int i = 0; i < b; i++) {
			result *= a;
			result = modulo(result, c);
		}
		return result;
	}

	public static String modulo(String a, String b) {
		if (a.length() > b.length())
			b = addPadding(b, a.length());
		else
			a = addPadding(a, b.length());

		StringBuilder result = new StringBuilder();
		for (int i = 0; i < a.length(); i++) {
			if (a.charAt(i) == b.charAt(i))
				result.append("1");
			else
				result.append("0");
		}

		return paddTo(result.toString(), 8);
	}

	public static String faktorNumber(int a) {
		StringBuilder result = new StringBuilder();
		int max = (int) Math.sqrt(a);

		for (int i = 2; i < max; i++) {
			if (modulo(a, i) == 0) {
				a /= i;
				result.append(i + ",");
				i = 1;
			}
		}
		if (a != 1)
			result.append(a);
		return result.toString();
	}

	public static int[] getPrimes(int max) {
		boolean[] notPrime = new boolean[max + 1];
		int n = max - 1;

		for (int i = 2; i < notPrime.length; i++) {
			if (notPrime[i])
				continue;
			for (int j = i + 1; j < notPrime.length; j++) {
				if (notPrime[j])
					continue;
				if (modulo(j, i) == 0) {
					notPrime[j] = true;
					n--;
				}
			}
		}
		int[] result = new int[n];
		int pos = 0;
		for (int i = 2; i <= max; i++) {
			if (!notPrime[i]) {
				result[pos++] = i;
			}
		}
		return result;
	}
}