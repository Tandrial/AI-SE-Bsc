package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	Vigenère
//		Entschlüsselung eines Chiffretexts per Vigenère‐Verfahren mit gegebenem Schlüssel.
//	Input: sa[0]: Chiffretext als String, sa[1]: Key als String
//	Output: Klartext als ein String (Groß‐/Kleinschreibung wird nicht berücksichtigt)

public class Aufgabe05 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getStringArray(0), t.getStringArray(1));
	}

	public static String getSolution(String sa0, String sa1) {
		return Vigenere.decrypt(sa0, sa1);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 05
		String sa0 = "EUOLWQKWRXBL";
		String sa1 = "eavesdropping";

		System.out.println("authenticity     <<<Musterlösung");
		System.out.println(Aufgabe05.getSolution(sa0, sa1));
	}
}