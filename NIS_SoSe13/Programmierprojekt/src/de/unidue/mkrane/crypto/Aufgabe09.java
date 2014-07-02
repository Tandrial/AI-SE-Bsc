package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	DES: Berechnung einer Runde
//		Durchführung einer kompletten Runde inkl. Rundenschlüssel‐Berechnung.
//	Input: sa[0]: L‐Block der vorherigen Runde (Binär‐String, 32Bit), sa[1]: R‐Block der 
//		vorherigen Runde (Binär‐String, 32Bit), sa[2]: Key (64Bit), ia[0]: geforderte Runde als 
//		Integer
//	Output: Binär‐String (64Bit, zuerst L‐Block 32Bit dann R‐Block 32Bit) (z.B. 
//		„1001111011…“)

public class Aufgabe09 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getStringArray(0), t.getStringArray(1),
				t.getStringArray(2), t.getIntArray(0));
	}

	public static String getSolution(String sa0, String sa1, String sa2, int ia0) {
		String subKey = DES.getRundenSchlussel(sa2)[ia0 - 1];

		String postFeist = DES.feistel(sa1, subKey);
		String rNeu = Utils.XOR(sa0, postFeist);

		return sa1 + rNeu;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 09
		String sa0 = "01101001000001101011100000110111";
		String sa1 = "01001111100111010100111100000011";
		String sa2 = "1110011101010001001011010010110110010110110011011110001101011010";
		int ia0 = 12;

		System.out
				.println("0100111110011101010011110000001100011001111011011110011110011001     <<<Musterlösung");
		System.out.println(Aufgabe09.getSolution(sa0, sa1, sa2, ia0));
	}
}
