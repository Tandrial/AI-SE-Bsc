package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	RSA: Verschlüsselung
//		Verschlüsselung einer Nachricht per RSA.
//	Input: ia[0]: n, ia[1]: e (Variablenbezeichnungen aus den Vorlesungsunterlagen), sa[0]: 
//		zu verschlüsselnder Klartext
//	Output: Chiffretext als String (ASCII‐Werte der einzelnen Zeichen durch Unterstrich 
//		(„_“) getrennt)

public class Aufgabe19 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getIntArray(0), t.getIntArray(1),
				t.getStringArray(0));
	}

	public static String getSolution(int ia0, int ia1, String sa0) {
		int[] params = { ia0, ia1, 0 };
		int[] chars = new int[sa0.length()];

		for (int i = 0; i < chars.length; i++) {
			chars[i] = Utils.CharToDez(sa0.charAt(i));
		}

		int[] result = RSA.encrypt(params, chars);
		return Utils.IntArrayToString(result, "_");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 19
		int ia0 = 377;
		int ia1 = 253;
		String sa0 = "wiretapping";

		System.out
				.println("119_105_114_101_116_97_112_112_105_110_103     <<<Musterlösung");
		System.out.println(Aufgabe19.getSolution(ia0, ia1, sa0));
	}
}