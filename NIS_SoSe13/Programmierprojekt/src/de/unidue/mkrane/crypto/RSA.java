package de.unidue.mkrane.crypto;

public class RSA {
	public static int[] genKeys(int p, int q) {
		int n = p * q;
		int phiN = (p - 1) * (q - 1);
		int e = 1;
		int d = 1;

		while (Utils.Modulo(e, phiN) == 0)
			e++;

		while (Utils.Modulo(e * d, phiN) != 1 || e == d)
			d++;

		return new int[] {n, e, d};
	}

	public static int[] encrypt(int[] params, int[] text) {
		return crypt(params[0], params[1], text);
	}

	public static int[] decrypt(int[] params, int[] text) {
		return crypt(params[0], params[2], text);
	}

	private static int[] crypt(int n, int power, int[] text) {
		for (int i = 0; i < text.length; i++) 
			text[i] = Utils.powerModulo(text[i], power, n);		
		return text;
	}

	public static void main(String[] args) {
		int[] primes = Utils.GetPrimes(1000);
		int p = primes[Utils.randomInt(0, primes.length - 1)];
		int q = primes[Utils.randomInt(0, primes.length - 1)];
		System.out.println(p + " " + q);

		while (p == q) {
			q = primes[Utils.randomInt(0, primes.length - 1)];
		}

		int[] keys = RSA.genKeys(p, q);

		String klar = "lohnsteuerjahresausgleich";
		int[] chars = new int[klar.length()];

		for (int i = 0; i < chars.length; i++) {
			chars[i] = Utils.CharToDez(klar.charAt(i));
		}

		int[] result = RSA.encrypt(keys, chars);

		System.out.println("Klartext:  " + klar);

		System.out.println("Encrypted: " + Utils.IntArrayToString(result, "_"));

		result = RSA.decrypt(keys, chars);

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < result.length; i++) {
			sb.append(Utils.DezToChar(result[i]));
		}
		String klar2 = sb.toString();

		System.out.println("Decrypted: " + klar2);

		System.out.println("Equals? " + klar.equals(klar2));
	}
}
