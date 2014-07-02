package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	RSA: Entschlüsselung
//		Die Lösung von Teil 1 wird mit der Methode getTask(int taskId, String[] 
//		params) des Connection-Objekts an den Server geschickt. D.h. der öffentliche Schlüssel 
//		wird schon bei der Aufgabenanforderung mitgeschickt.
//		Die Lösung von Teil 2 wie gewohnt mit sendSolution.
//
//	Teil 1 (Public Key-Berechnung):
//			Client generiert zufälliges Schlüsselpaar und sendet den Public Key an den Server.
//		Output: Public Key als String-Array (String[] array =  {n, e})
//
//	Teil 2 (Entschlüsselung):
//			Zufallstext, der mit dem Public Key des Clients (aus Teil 1) verschlüsselt ist, soll
//			entschlüsselt werden.
//		Input: sa[0]: Chiffretext als String
//		Output: Klartext als String (Groß‐/Kleinschreibung wird nicht berücksichtigt.)

public class Aufgabe20 {

	public static String[] calcParams() {
		int[] primes = Utils.GetPrimes(1000);
		int p = primes[Utils.randomInt(0, primes.length - 1)];
		int q = primes[Utils.randomInt(0, primes.length - 1)];

		while (p == q) {
			q = primes[Utils.randomInt(0, primes.length - 1)];
		}

		int[] params = RSA.genKeys(p, q);

		return new String[] { params[0] + "", params[1] + "", params[2] + "" };
	}

	public static String[] sendParams(String[] params) {
		return new String[] { params[0], params[1] };
	}

	public static String getSolution(TaskObject t, String[] parameter) {
		return getSolution(t.getStringArray(0), parameter);
	}

	public static String getSolution(String sa0, String[] para) {
		int[] params = { Integer.parseInt(para[0]), Integer.parseInt(para[1]),
				Integer.parseInt(para[2]) };

		int[] chars = Utils.StringToIntArray(sa0, "_");
		int[] cryptText = RSA.decrypt(params, chars);

		StringBuilder result = new StringBuilder();

		for (int i = 0; i < cryptText.length; i++) {
			result.append(Utils.DezToChar(cryptText[i]));
		}

		return result.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 20
		String[] params = { 235 + "", 13 + "", 85 + "" };

		String sa0 = "195_31_166_168_177_205_136_168_109_177_2_179_126";

		System.out.println("235, 13		<<<Musterlösung privateKeyPair");
		System.out.println(params[0] + ", " + params[1]);

		System.out.println("steganography     <<<Musterlösung");
		System.out.println(Aufgabe20.getSolution(sa0, params));
	}
}
