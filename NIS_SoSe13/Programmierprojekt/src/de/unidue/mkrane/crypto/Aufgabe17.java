package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	RC4: Verschlüsselung
//		Verschlüsselung unter Verwendung des Generation Loops und des Keyschedulings.
//	Input: sa[0]: Key als Zahlen‐String (Integer‐Werte durch Unterstrich („_“) getrennt. z.B.: 
//		1_7_1_7 wären die Schlüssel‐Werte an den Positionen null bis drei), sa[1]: Klartext als 
//		String
//	Output: Chiffretext als ein Binär‐String (z.B. „1000111101011…“)

public class Aufgabe17 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getStringArray(0), t.getStringArray(1));
	}

	public static String getSolution(String sa0, String sa1) {
		return RC4.encrypt(sa0, sa1);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 17
		String sa0 = "7_11_13_14_20_22_4_1_15_1_24_2_20_23_13_2_13_20_4_21_23_3_9_7";
		String sa1 = "encryption";

		System.out
				.println("01100110011000100111000001100100011110110111101001111000011001110110110001100100     <<<Musterlösung");
		System.out.println(Aufgabe17.getSolution(sa0, sa1));
	}
}