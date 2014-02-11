package de.unidue.mkrane.crypto;

public class RC4 {

	public static int[] initiateSBox(int[] key) {
		int mod = key.length;
		int[] sBox = new int[mod];

		for (int i = 0; i < mod; i++) {
			sBox[i] = i;
		}
		int j = 0;
		for (int i = 0; i < mod; i++) {
			j = Utils.Modulo(j + sBox[i] + key[Utils.Modulo(i, mod)], mod);
			int swap = sBox[i];
			sBox[i] = sBox[j];
			sBox[j] = swap;
		}
		return sBox;
	}

	public static int[] rndGen(int[] key, int anzahl) {

		int[] result = new int[anzahl];

		int i = 0;
		int j = 0;
		int mod = key.length;

		for (int n = 0; n < anzahl; n++) {

			i = Utils.Modulo(i + 1, mod);
			j = Utils.Modulo(j + key[i], mod);

			int swap = key[i];
			key[i] = key[j];
			key[j] = swap;

			result[n] = key[Utils.Modulo(key[i] + key[j], mod)];
		}
		return result;
	}

	public static String encrypt(String key, String klar) {

		int[] sBox = Utils.StringToIntArray(key, "_");

		sBox = RC4.initiateSBox(sBox);
		sBox = RC4.rndGen(sBox, klar.length());

		StringBuilder result = new StringBuilder();

		for (int i = 0; i < klar.length(); i++) {

			String keyy = Utils.DezToBin(sBox[i]);
			String textc = Utils.CharToBin(klar.charAt(i));
			result.append(Utils.XOR(keyy, textc));
		}
		return result.toString();

	}

	public static String decrypt(String key, String cypher) {

		int[] sBox = Utils.StringToIntArray(key, "_");

		String[] text = Utils.splitIntoChunksOf(cypher, 8);

		sBox = RC4.initiateSBox(sBox);
		sBox = RC4.rndGen(sBox, text.length);

		StringBuilder result = new StringBuilder();

		for (int i = 0; i < text.length; i++) {
			String keyy = Utils.DezToBin(sBox[i]);
			String klar = Utils.XOR(keyy, text[i]);

			result.append(Utils.BinToChar(Utils.removePadding(klar)));
		}
		return result.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String klar = "lohnsteuerjahresausgleich";
		String key = "bootsfahrt";

		String keyInt = Utils.CharToDez(key.charAt(0)) + "";

		for (int i = 1; i < key.length(); i++) {
			keyInt += "_" + Utils.CharToDez(key.charAt(i));
		}

		System.out.println("Klartext:  " + klar);

		String cypher = RC4.encrypt(keyInt, klar);
		System.out.println("Encrypted: " + cypher);

		String klar2 = RC4.decrypt(keyInt, cypher);
		System.out.println("Decrypted: " + klar2);

		System.out.println("Equals? " + klar.equals(klar2));
	}
}