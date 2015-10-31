package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	RC4: Generation Loop
//		Generieren von pseudo‐zufälligen Bytes mithilfe des State‐Tables.
//	Input: sa[0]: State‐Table (Integer‐Werte durch Unterstrich („_“) getrennt. z.B.: 2_1_3_0 
//		wären die State‐Table‐Werte an den Positionen null bis drei), sa[1]: Klartext als String
//	Output: Inhalt des State‐Tables an der Stelle t des jeweiligen Loops. Alle Werte sollen in 
//		einem String (ohne Trennzeichen) hintereinander ausgegeben werden. (z.B. „2130“)

public class Aufgabe15 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getStringArray(0), t.getStringArray(1));
	}

	public static String getSolution(String sa0, String sa1) {
		int[] key = Utils.splitStringToIntArray(sa0, "_");

		key = RC4.rndGen(key, sa1.length());

		return Utils.buildStringFromArray(key);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 15
		String sa0 = "1_0_11_9_12_6_3_8_5_10_7_4_2";
		String sa1 = "Datenschutz";

		System.out.println("1412457212947     <<<Musterlösung");
		System.out.println(Aufgabe15.getSolution(sa0, sa1));
	}
}
