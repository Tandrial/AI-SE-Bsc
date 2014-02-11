package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	AES: Schlüssel-Generierung
//		Generierung von 3 Rundenschlüsseln. Als Output sollen alle drei Schlüssel der 
//		Reihenfolge entsprechend jeweils durch einen Unterstrich („_“) getrennt und in 
//		Hexadezimal‐Darstellung an den Server gesendet werden.
//	Input: sa[0]: Key als HEX‐String (128 Bit)
//	Output: HEX‐String aller drei Rundenschlüssel (jeweils 128 Bit) jeweils durch einen 
//		Unterstrich („_“) getrennt (z.B. „a56e…_35c…_72a…“)

public class Aufgabe11 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getStringArray(0));
	}

	public static String getSolution(String sa0) {
		String[][] tmp = AES.getStateMatrix(sa0);

		String[][] keyMatrix = AES.getKeySchedule(tmp);

		return AES.getStringFromMatrix(keyMatrix, 0) + "_"
				+ AES.getStringFromMatrix(keyMatrix, 1) + "_"
				+ AES.getStringFromMatrix(keyMatrix, 2);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 11
		String sa0 = "cb628baeeeba913288654ea330413248";

		System.out
				.println("cb628baeeeba913288654ea330413248_4941d9aaa7fb48982f9e063b1fdf3473_d559566a72a21ef25d3c18c942e32cba     <<<Musterlösung");
		System.out.println(Aufgabe11.getSolution(sa0));
	}
}