package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	DES: R-Block-Berechnung
//		Berechnung des R‐Blocks für eine gegebene Runde und gegebenen Input. Der Schlüssel 
//		wird hierbei als 0 angenommen.
//	Input: sa[0]: Binär‐String (64Bit), ia[0]: geforderte Runde (1 ‐ 16) als Integer
//	Output: R‐Block als Binär‐String (z.B. „1001111011…“)

public class Aufgabe07 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getStringArray(0), t.getIntArray(0));
	}

	public static String getSolution(String sa0, int ia0) {
		String subKey = Utils.paddTo("0", 48);

		String b = DES.IP(sa0);
		String l = DES.getL(b);
		String r = DES.getR(b);

		for (int i = 0; i < ia0; i++) {
			String tmpR = r;
			r = DES.feistel(r, subKey);
			r = Utils.XOR(l, r);
			l = tmpR;
		}

		return r;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 07
		String sa0 = "1001001001011111110001001010011110000010110100011000111011100100";
		int ia0 = 4;

		System.out
				.println("11100001011111101110110101111010     <<<Musterlösung");
		System.out.println(Aufgabe07.getSolution(sa0, ia0));
	}
}