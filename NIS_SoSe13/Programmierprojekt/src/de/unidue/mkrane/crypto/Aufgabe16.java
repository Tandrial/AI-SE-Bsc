package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	RC4: Keyscheduling
//		RC4‐Schlüsselgenerierung.
//	Input: sa[0]: Key als Zahlen‐String (Integer‐Werte durch Unterstrich („_“) getrennt. z.B.: 
//		1_7_1_7 wären die Schlüssel‐Werte an den Positionen null bis drei)
//	Output: State‐Table (Integer‐Werte durch Unterstrich („_“) getrennt. z.B.: 2_1_3_0 
//		wären die State‐Table‐Werte an den Positionen null bis drei)

public class Aufgabe16 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getStringArray(0));
	}

	public static String getSolution(String sa0) {
		int[] key = Utils.splitStringToIntArray(sa0, "_");

		key = RC4.initiateSBox(key);

		return Utils.buildStringFromArray(key, "_");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 16
		String sa0 = "5_5_2_4_3_3_1";

		System.out.println("5_3_6_2_0_4_1     <<<Musterlösung");
		System.out.println(Aufgabe16.getSolution(sa0));
	}
}