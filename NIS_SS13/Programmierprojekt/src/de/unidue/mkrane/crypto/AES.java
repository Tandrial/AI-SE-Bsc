package de.unidue.mkrane.crypto;

public class AES {

	public static String[][] getKeySchedule(String[][] key) {
		String[][] keyschedule = new String[4][4 * 11];
		int rcon = 0;

		for (int i = 0; i < key.length; i++) {
			System.arraycopy(key[i], 0, keyschedule[i], 0, key[i].length);
		}

		for (int i = 4; i < keyschedule[0].length; i++) {
			if (Utils.Modulo(i, 4) == 0) {
				String[] w_i = getCol(keyschedule, i - 1);
				w_i = rotWord(w_i);
				w_i = subByte(w_i, AESD.S);

				w_i[0] = Utils.XOR(w_i[0], AESD.Rcon[rcon++]);

				setCol(keyschedule, i,
						ColumnXOR(w_i, getCol(keyschedule, i - 4)));

			} else {
				setCol(keyschedule,
						i,
						ColumnXOR(getCol(keyschedule, i - 4),
								getCol(keyschedule, i - 1)));
			}
		}
		return keyschedule;
	}

	public static String[] ColumnXOR(String[] a, String[] b) {
		String[] result = new String[a.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = Utils.XOR(a[i], b[i]);
		}
		return result;
	}

	public static String[] getCol(String[][] a, int col) {
		String[] result = new String[a.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = a[i][col];
		}
		return result;
	}

	public static void setCol(String[][] a, int col, String[] column) {
		String[] result = new String[a.length];
		for (int i = 0; i < result.length; i++) {
			a[i][col] = column[i];
		}
		return;
	}

	public static String[] rotWord(String[] a) {
		String tmp = a[0];
		a[0] = a[1];
		a[1] = a[2];
		a[2] = a[3];
		a[3] = tmp;

		return a;
	}

	public static String[][] encrypt(String[][] sMatrix, String[][] keys) {
		sMatrix = AES.addRoundKey(sMatrix, keys, 0);

		for (int i = 1; i < (keys[0].length / 4) - 1; i++) {
			sMatrix = AES.subBytes(sMatrix, AESD.S);
			sMatrix = AES.shiftRows(sMatrix);
			sMatrix = AES.mixColumns(sMatrix);
			sMatrix = AES.addRoundKey(sMatrix, keys, i);
		}

		sMatrix = AES.subBytes(sMatrix, AESD.S);
		sMatrix = AES.shiftRows(sMatrix);
		sMatrix = AES.addRoundKey(sMatrix, keys, (keys[0].length / 4) - 1);

		return sMatrix;
	}

	public static String[][] decrypt(String[][] sMatrix, String[][] keys) {
		sMatrix = AES.addRoundKey(sMatrix, keys, (keys[0].length / 4) - 1);

		for (int i = (keys[0].length / 4) - 2; i > 0; i--) {

			sMatrix = AES.InvShiftRows(sMatrix);
			sMatrix = AES.subBytes(sMatrix, AESD.S_inv);
			sMatrix = AES.addRoundKey(sMatrix, keys, i);
			sMatrix = AES.InvMixColumns(sMatrix);
		}

		sMatrix = AES.InvShiftRows(sMatrix);
		sMatrix = AES.subBytes(sMatrix, AESD.S_inv);
		sMatrix = AES.addRoundKey(sMatrix, keys, 0);

		return sMatrix;
	}

	public static String[][] addRoundKey(String[][] sMatrix, String[][] key,
			int round) {
		for (int i = 0; i < key.length; i++) {
			for (int j = 0; j < key.length; j++) {
				sMatrix[i][j] = Utils.XOR(sMatrix[i][j], key[i][j + 4 * round]);
			}
		}
		return sMatrix;
	}

	public static String[][] subBytes(String[][] stataMatrix, String[] LookUp) {
		for (int i = 0; i < stataMatrix.length; i++)
			stataMatrix[i] = subByte(stataMatrix[i], LookUp);
		return stataMatrix;
	}

	public static String[] subByte(String[] a, String[] LookUp) {
		for (int i = 0; i < a.length; i++)
			a[i] = subByte(a[i], LookUp);
		return a;
	}

	public static String subByte(String a, String[] LookUp) {
		int col = Utils.BinToDez(Utils.splitIntoChunksOf(a, 4)[1]);
		int row = Utils.BinToDez(Utils.splitIntoChunksOf(a, 4)[0]);

		return Utils.HexToBin(LookUp[row * 16 + col]);
	}

	public static String[][] shiftRows(String[][] sMatrix) {
		return ShiftRows(sMatrix, false);
	}

	public static String[][] InvShiftRows(String[][] sMatrix) {
		return ShiftRows(sMatrix, true);
	}

	public static String[][] ShiftRows(String[][] a, boolean inv) {
		for (int i = 1; i < 4; i++) {
			if (inv)
				a[i] = rotWord(a[i], 4 - i);
			else
				a[i] = rotWord(a[i], i);
		}
		return a;
	}

	public static String[] rotWord(String[] a, int times) {
		for (int i = 0; i < times; i++) {
			a = rotWord(a);
		}
		return a;
	}

	public static String[][] mixColumns(String[][] sMatrix) {
		return MixColumns(sMatrix, AESD.Mix);
	}

	public static String[][] InvMixColumns(String[][] sMatrix) {
		return MixColumns(sMatrix, AESD.InvMix);
	}

	private static String[][] MixColumns(String[][] sMatrix,
			String[][] MixMatrix) {
		for (int i = 0; i < sMatrix.length; i++) {

			String[] s = getCol(sMatrix, i);

			for (int j = 0; j < 4; j++) {
				sMatrix[j][i] = GF8Col(s, MixMatrix[j]);
			}
		}
		return sMatrix;
	}

	private static String GF8Col(String[] s, String[] mix) {
		String result = "";
		for (int i = 0; i < 4; i++) {
			result = Utils.XOR(result, GF8(s[i], mix[i]));
		}
		return result;
	}

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

	public static String[][] getStateMatrix(String a) {
		a = Utils.HexToBin(a);
		String[] tmp = Utils.splitIntoChunksOf(a, 8);

		String[][] sMatrix = new String[4][4];
		int pos = 0;
		for (int i = 0; i < sMatrix.length; i++)
			for (int j = 0; j < sMatrix[i].length; j++)
				sMatrix[j][i] = tmp[pos++];

		return sMatrix;
	}

	public static String getStringFromMatrix(String[][] sMatrix) {
		return getStringFromMatrix(sMatrix, 0);
	}

	public static String getStringFromMatrix(String[][] sMatrix, int round) {
		StringBuilder result = new StringBuilder();

		for (int i = round * 4; i < (round * 4) + 4; i++)
			for (int j = 0; j < sMatrix.length; j++)
				result.append(Utils.BinToHex(sMatrix[j][i]));

		return result.toString();
	}

	public static void main(String[] args) {
		String klar = "00112233445566778899aabbccddeeff";
		String key = "000102030405060708090a0b0c0d0e0f";

		String[][] keys = AES.getKeySchedule(AES.getStateMatrix(key));
		String[][] textblock = AES.getStateMatrix(klar);

		System.out.println("Klartext:  " + klar);

		String cypher = AES.getStringFromMatrix(AES.encrypt(textblock, keys));
		System.out.println("Encrypted: " + cypher);

		String klar2 = AES.getStringFromMatrix(AES.decrypt(
				AES.getStateMatrix(cypher), keys));
		System.out.println("Decrypted: " + klar2);

		System.out.println("Equals? " + klar.equals(klar2));

		textblock = AES.decrypt(textblock, keys);

	}
}