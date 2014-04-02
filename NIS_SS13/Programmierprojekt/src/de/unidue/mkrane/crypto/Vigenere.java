package de.unidue.mkrane.crypto;

public class Vigenere {

	public static String encrypt(String text, String key) {
		return crypt(text, key, false);
	}

	public static String decrypt(String text, String key) {
		return crypt(text, key, true);
	}

	private static String crypt(String text, String key, boolean decrypt) {
		String result = "";

		text = text.toLowerCase();
		key = key.toLowerCase();

		for (int i = 0; i < text.length(); i++) {
			result += Caeser(text.charAt(i),
					key.charAt(Utils.Modulo(i, key.length())), decrypt);
		}
		return result;
	}

	private static char abc[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z' };

	private static char Caeser(char c, char v, boolean decrypt) {
		int pos = 0, a = -1, b = -1;

		for (int i = 0; i < abc.length; i++) {
			if (abc[i] == c)
				a = i;
			if (abc[i] == v)
				b = i;
			if (a != -1 && b != -1)
				break;
		}
		int k = a + b;

		if (decrypt)
			k = a - b;
		pos = Utils.Modulo(k, 26);
		return abc[pos];
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String klar = "lohnsteuerjahresausgleich";
		String key = "bootsfahrt";

		System.out.println("Klartext:  " + klar);

		String cypher = Vigenere.encrypt(klar, key);
		System.out.println("Encrypted: " + cypher);

		String klar2 = Vigenere.decrypt(cypher, key);
		System.out.println("Decrypted: " + klar2);

		System.out.println("Equals? " + klar.equals(klar2));
	}
}