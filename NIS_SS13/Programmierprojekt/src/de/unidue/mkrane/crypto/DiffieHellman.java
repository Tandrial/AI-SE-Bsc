package de.unidue.mkrane.crypto;

public class DiffieHellman {

	public static int genPublicKey(int g, int a, int p) {
		return Utils.powerModulo(g, a, p);
	}

	public static int genSharedSecret(double B, int a, int p) {
		return Utils.powerModulo((int) B, a, p);
	}

	public static String encrypt(String text, int secret) {
		String s = Utils.DezToBin(secret);

		StringBuilder result = new StringBuilder();

		String xor = Utils.XOR(Utils.CharToBin(text.charAt(0)), s);

		result.append(Utils.BinToDez(xor));

		for (int i = 1; i < text.length(); i++) {
			char c = text.charAt(i);
			xor = Utils.XOR(Utils.CharToBin(c), s);

			result.append("_" + Utils.BinToDez(xor));
		}

		return result.toString();
	}

	public static String decrypt(String[] text, int secret) {
		String s = Utils.DezToBin(secret);

		StringBuilder result = new StringBuilder();

		for (int i = 0; i < text.length; i++) {
			String xor = Utils
					.XOR(Utils.DezToBin(Integer.parseInt(text[i])), s);

			result.append(Utils.BinToChar(xor));
		}

		return result.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String klar = "lohnsteuerjahresausgleich";

		int p = 11;
		int a = Utils.randomInt(1, p - 2);
		int g = Utils.randomInt(1, p - 1);

		int b = Utils.randomInt(1, p - 2);
		double B = genPublicKey(g, b, p);

		int key = genSharedSecret(B, a, p);

		System.out.println("Klartext:  " + klar);

		String cypher = DiffieHellman.encrypt(klar, key);
		System.out.println("Encrypted: " + cypher);

		String klar2 = DiffieHellman.decrypt(cypher.split("_"), key);
		System.out.println("Decrypted: " + klar2);

		System.out.println("Equals? " + klar.equals(klar2));
	}
}
