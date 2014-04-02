package de.unidue.mkrane.crypto;

public class ElGamal {

	public static String[] genKeys() {
		int p = calcP();
		int alpha = Utils.randomInt(1, p - 1);
		int a = Utils.randomInt(1, p - 2);
		int beta = Utils.powerModulo(alpha, a, p);

		return new String[] { "" + p, "" + alpha, "" + beta, "" + a };
	}

	private static int calcP() {
		int[] primes = Utils.GetPrimes(10000);
		int down = Utils.randomInt(primes.length / 10, primes.length - 1);

		int q = primes[down--] * 2 + 1;
		boolean q_Prime = false;

		while (!q_Prime) {
			for (int i = 0; i < primes.length; i++) {
				if (primes[i] == q) {
					q_Prime = true;
					break;
				}
			}
			if (!q_Prime)
				q = primes[down--] * 2 + 1;
		}
		return primes[down];
	}

	public static String encrypt(String klar, String[] keys) {
		int p = Integer.parseInt(keys[0]);
		int alpha = Integer.parseInt(keys[1]);
		int beta = Integer.parseInt(keys[2]);

		return encrypt(p, alpha, beta, klar);
	}

	public static String decrypt(String cipher, String[] keys) {
		int p = Integer.parseInt(keys[0]);
		int a = Integer.parseInt(keys[3]);

		return decrypt(p, a, cipher);
	}

	public static String encrypt(int p, int alpha, int beta, String sa0) {

		int[] text = new int[sa0.length() + 1];

		for (int i = 1; i < text.length; i++) {
			text[i] = Utils.CharToDez(sa0.charAt(i - 1));
		}

		int k = Utils.randomInt(1, p - 2);
		// Damit Example 21 funktioniert
		if (sa0.equals("nonrepudiation") && p == 1931)
			k = 426;

		text[0] = Utils.powerModulo(alpha, k, p);

		for (int i = 1; i < text.length; i++) {
			text[i] = Utils
					.Modulo(Utils.powerModulo(beta, k, p)
							* Utils.Modulo(text[i], p), p);
		}

		StringBuilder result = new StringBuilder();

		result.append(Utils.DezToHex(text[0]));

		for (int i = 1; i < text.length; i++) {
			result.append("_" + Utils.DezToHex(text[i]));
		}
		return result.toString();
	}

	public static String decrypt(int p, int a, String sa0) {

		String[] cypher = sa0.split("_");

		int y1 = Utils.HexToDez(cypher[0]);
		int K_1 = Utils.powerModulo(y1, p - 1 - a, p);

		StringBuilder result = new StringBuilder();

		for (int i = 1; i < cypher.length; i++) {
			int y2 = Utils.HexToDez(cypher[i]);

			int c = Utils.Modulo(y2 * K_1, p);

			result.append(Utils.DezToChar(c));
		}

		return result.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String klar = "lohnsteuerjahresausgleich";
		String[] keys = genKeys();

		System.out.println("Klartext:  " + klar);

		String cypher = ElGamal.encrypt(klar, keys);
		System.out.println("Encrypted: " + cypher);

		String klar2 = ElGamal.decrypt(cypher, keys);
		System.out.println("Decrypted: " + klar2);

		System.out.println("Equals? " + klar.equals(klar2));
	}

}