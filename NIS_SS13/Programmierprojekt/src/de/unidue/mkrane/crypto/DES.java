package de.unidue.mkrane.crypto;

public class DES {

	public static String[] getRundenSchlussel(String sa0) {
		String[] subkeys = new String[16];
		sa0 = PC1(sa0);

		String l = GetL(sa0);
		String r = GetR(sa0);

		for (int i = 0; i < subkeys.length; i++) {
			switch (i) {
			case 0:
			case 1:
			case 8:
			case 15:
				l = ShiftLeft(l, 1);
				r = ShiftLeft(r, 1);
				break;
			default:
				l = ShiftLeft(l, 2);
				r = ShiftLeft(r, 2);
				break;
			}
			subkeys[i] = PC2(l + r);
		}
		return subkeys;
	}

	private static String PC1(String key) {
		return permute(key, DESD.PC1);
	}

	private static String PC2(String key) {
		return permute(key, DESD.PC2);
	}

	private static String encrypt(String klar, String key) {
		return crypt(klar, key, true);
	}

	private static String decrypt(String cypher, String key) {
		return crypt(cypher, key, false);
	}

	public static String crypt(String block, String key, boolean encrypt) {
		String[] subkeys = getRundenSchlussel(key);
		String b = IP(block);
		String l = GetL(b);
		String r = GetR(b);

		for (int i = 0; i < subkeys.length; i++) {
			String tmpR = r;
			if (encrypt)
				r = feistel(r, subkeys[i]);
			else
				r = feistel(r, subkeys[15 - i]);

			r = Utils.XOR(l, r);
			l = tmpR;
		}

		return FP(r + l);
	}

	public static String feistel(String r, String subKey) {
		String e = E(r);
		e = Utils.XOR(e, subKey);
		String[] sIn = Utils.splitIntoChunksOf(e, 6);
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < 8; i++) {
			result.append(Utils.DezToBin(S(i, sIn[i]), 4));
		}

		return P(result.toString());
	}

	private static int S(int number, String src) {
		String outer = src.charAt(0) + "" + src.charAt(src.length() - 1);
		String inner = src.substring(1, src.length() - 1);

		int r = Utils.BinToDez(outer);
		int c = Utils.BinToDez(inner);
		return DESD.S[number][r * 16 + c];
	}

	public static String IP(String key) {
		return permute(key, DESD.IP);
	}

	private static String FP(String key) {
		return permute(key, DESD.FP);
	}

	private static String E(String key) {
		return permute(key, DESD.E);
	}

	private static String P(String key) {
		return permute(key, DESD.P);
	}

	public static String GetL(String key) {
		return key.substring(0, key.length() / 2);
	}

	public static String GetR(String key) {
		return key.substring(key.length() / 2);
	}

	private static String permute(String key, byte[] permuteTable) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < permuteTable.length; i++) {
			result.append(key.charAt(permuteTable[i]));
		}
		return result.toString();
	}

	private static String ShiftLeft(String key, int amount) {
		for (int i = 0; i < amount; i++) {
			char c = key.charAt(0);
			key = key.substring(1) + c;
		}
		return key;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String klar = "1110011101010001001011010010110110010110110011011110001101011011";
		String key = "0000011100111001101001010010100111010110110110011011111001011000";

		System.out.println("Klartext:  " + klar);

		String cypher = DES.encrypt(klar, key);
		System.out.println("Encrypted: " + cypher);

		String klar2 = DES.decrypt(cypher, key);
		System.out.println("Decrypted: " + klar2);

		System.out.println("Equals? " + klar.equals(klar2));
	}
}