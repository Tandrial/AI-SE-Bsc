package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	AES: SubBytes(), ShiftRows() und MixColumns()
//		Berechnung in der Reihenfolge von SubBytes(), ShiftRows() und MixColums() für einen 
//		gesamten Datenblock.
//	Input: sa[0]: HEX‐String (128Bit)
//	Output: HEX‐String(128Bit) (z.B. „21ae5….“)

public class Aufgabe13 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getStringArray(0));
	}

	public static String getSolution(String sa0) {
		String[][] tmp = AES.getStateMatrix(sa0);
		tmp = AES.subBytes(tmp, AESD.S);
		tmp = AES.shiftRows(tmp);
		tmp = AES.mixColumns(tmp);

		return AES.getStringFromMatrix(tmp);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 13
		String sa0 = "3085b849d1547370864a964a9d05998a";

		System.out
				.println("86919d40c89b620c087704694737ad4d     <<<Musterlösung");
		System.out.println(Aufgabe13.getSolution(sa0));
	}
}