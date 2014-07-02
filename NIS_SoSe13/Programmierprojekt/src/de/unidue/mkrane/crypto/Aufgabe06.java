package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	DES: Rundenschlüssel-Berechnung
//		Berechnung des Rundenschlüssels für eine gegebene Runde und gegebenen Schlüssel.
//	Input: sa[0]: Key als Binär‐String, ia[0]: geforderte Runde (1 ‐ 16) als Integer
//	Output: Roundkey als ein Binär‐String (48Bit) (z.B. „1001111011…“)

public class Aufgabe06 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getStringArray(0), t.getIntArray(0));
	}

	public static String getSolution(String sa0, int ia0) {
		String[] RundenSchlussel = DES.getRundenSchlussel(sa0);

		return RundenSchlussel[ia0 - 1];
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 06
		String sa0 = "1100000000010100010101010001000111000101010001110101011111000101";
		int ia0 = 5;

		System.out
				.println("000110110110000101000001001010101100000010101011     <<<Musterlösung");
		System.out.println(Aufgabe06.getSolution(sa0, ia0));
	}
}