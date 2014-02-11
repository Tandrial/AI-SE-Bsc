package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	ElGamal: Verschlüsselung
//		Verschlüsselung einer Nachricht per Verfahren von ElGamal.
//	Input: ia[0]: p, ia[1]: α, ia[2]: β (jeweils als Integer; zusammen: Public Key, mit dem 
//		verschlüsselt werden soll), sa[0] zu verschlüsselnder Klartext (Variablenbezeichnungen 
//		aus den Vorlesungsunterlagen)
//	Output: Chiffretext als String (ASCII‐Werte der einzelnen Zeichen im 
//		Hexadezimalformat durch Unterstrich („_“) getrennt). Dabei wird y1 (siehe 
//		Vorlesungsunterlagen) dem Chiffretext im gleichen Format vorangestellt. (also 
//		y1_hex1_hex2_...)

public class Aufgabe21 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getIntArray(0), t.getIntArray(1),
				t.getIntArray(2), t.getStringArray(0));
	}

	public static String getSolution(int ia0, int ia1, int ia2, String sa0) {
		return ElGamal.encrypt(ia0, ia1, ia2, sa0);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 21

		int ia0 = 1931;
		int ia1 = 2;
		int ia2 = 1327;
		String sa0 = "nonrepudiation";

		System.out
				.println("658_191_24_191_368_6db_642_6ac_bd_127_504_8e_127_24_191     <<<Musterlösung");
		System.out.println(Aufgabe21.getSolution(ia0, ia1, ia2, sa0));
	}
}
